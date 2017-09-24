package wci.frontend.pascal.parsers;

import java.util.EnumSet;

import wci.intermediate.ICodeNode;
import wci.intermediate.ICodeFactory;

import wci.intermediate.icodeimpl.ICodeNodeTypeImpl;

import wci.frontend.Token;

import wci.frontend.pascal.PascalTokenType;

import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.LOOP;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.TEST;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.NOT;

import static wci.frontend.pascal.PascalTokenType.DO;

import static wci.frontend.pascal.PascalErrorCode.MISSING_DO;

public class WhileStatementParser extends StatementParser
{

	// Synchronization set for DO.
	private static final EnumSet<PascalTokenType> DO_SET =
		StatementParser.STMT_START_SET.clone();

	static
	{
		DO_SET.add(DO);
		// Contains UNTIL, among other tokens.
		DO_SET.addAll(StatementParser.STMT_FOLLOW_SET);
	}

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public WhileStatementParser(StatementParser parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------
	// StatementParser methods

	/**
	* Parse a WHILE statement.
	*
	* @param token the initial token.
	* @return the root node of the generated parser tree
	* @throws Exception
	*/
	@Override
	public ICodeNode parse(Token token) throws Exception
	{
		// Consume the while.
		token = nextToken();

		// Create LOOP,TEST, and NOT nodes.
		ICodeNode loopNode = ICodeFactory.createICodeNode(LOOP);
		ICodeNode breakNode = ICodeFactory.createICodeNode(TEST);
		ICodeNode notNode = ICodeFactory.
			createICodeNode(ICodeNodeTypeImpl.NOT);

		// The LOOP node adopts the TEST node as its first child.
		loopNode.addChild(breakNode);
		breakNode.addChild(notNode);

		// Parse the expression.
		// The NOT node adopts the expression subtree as its only
		// child.
		ExpressionParser expressionParser =
			new ExpressionParser(this);
		notNode.addChild(expressionParser.parse(token));

		// Synchronize at the DO.
		token = synchronize(DO_SET);
		if (token.getType() == DO) {
			token = nextToken();  // Consume the DO.
		}
		else {
			errorHandler.flag(token,MISSING_DO,this);
		}

		// Parse the statement.
		// The LOOP node adopts the statement subtree as its second
		// child.
		StatementParser statementParser = new StatementParser(this);
		loopNode.addChild(statementParser.parse(token));

		return loopNode;
	}
}

