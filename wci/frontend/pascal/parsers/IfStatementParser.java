package wci.frontend.pascal.parsers;

import java.util.EnumSet;

import wci.intermediate.ICodeNode;
import wci.intermediate.ICodeFactory;
import wci.intermediate.icodeimpl.ICodeNodeTypeImpl;

import wci.frontend.Token;

import wci.frontend.pascal.PascalTokenType;

import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.IF;

import static wci.frontend.pascal.PascalTokenType.THEN;
import static wci.frontend.pascal.PascalTokenType.ELSE;

import static wci.frontend.pascal.PascalErrorCode.MISSING_THEN;

public class IfStatementParser extends StatementParser
{

	// Synchronization set for THEN.
	private static final EnumSet<PascalTokenType> THEN_SET =
		StatementParser.STMT_START_SET.clone();

	static
	{
		THEN_SET.add(THEN);
		THEN_SET.addAll(StatementParser.STMT_FOLLOW_SET);
	}

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public IfStatementParser(StatementParser parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------
	// StatementParser methods

	/**
	* Parse an IF statement.
	*
	* @param token the initial IF token
	* @return the root of the generated parse subtree
	*/
	@Override
	public ICodeNode parse(Token token) throws Exception
	{
		token = nextToken();  // Consume the IF token.

		// Create an If node.
		ICodeNode ifNode = ICodeFactory.createICodeNode(
			ICodeNodeTypeImpl.IF);

		// Parse the expression.
		// The if node adopts the expression subtree as its first
		// child.
		ExpressionParser expressionParser = new ExpressionParser(this);
		ifNode.addChild(expressionParser.parse(token));

		// Synchronize the token on THEN.
		token = synchronize(THEN_SET);
		if (token.getType() == THEN) {
			token = nextToken(); // Consume the then.
		}
		else {
			errorHandler.flag(token,MISSING_THEN,this);
		}

		// Parse the then statement.
		// The IF node adopts the statement subtree as its second
		// child.
		StatementParser statementParser = new StatementParser(this);
		ifNode.addChild(statementParser.parse(token));
		token = currentToken();

		// Look for an ELSE.
		if (token.getType() == ELSE) {
			token = nextToken(); // Consume the ELSE.

			// Parse the ELSE statement.
			// The IF node adopts the statement subtree as its third
			// child.
			ifNode.addChild(statementParser.parse(token));
		}

		return ifNode;
	}
}

