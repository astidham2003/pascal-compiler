package wci.frontend.pascal.parsers;

import wci.intermediate.ICodeNode;
import wci.intermediate.ICodeFactory;

import wci.frontend.Token;

import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.LOOP;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.TEST;

import static wci.frontend.pascal.PascalTokenType.UNTIL;

import static wci.frontend.pascal.PascalErrorCode.MISSING_UNTIL;

/**
* <h1>RepeatStatemetnParser</h1>
*
* <p>Root parser for REPEAT statements.</p>
*/
public class RepeatStatementParser extends StatementParser
{

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public RepeatStatementParser(StatementParser parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------
	// StatementParser methods

	/**
	* Parser a REPEAT statement.
	*
	* @param token the initial token.
	* @return the root node of the generated parse tree
	* @throws Exception
	*/
	@Override
	public ICodeNode parse(Token token) throws Exception
	{
		token = nextToken(); // Consume the REPEAT token.

		// Create the LOOP and TEST nodes.
		ICodeNode loopNode = ICodeFactory.createICodeNode(LOOP);
		ICodeNode testNode = ICodeFactory.createICodeNode(TEST);

		// Parse the statement list terminated by the UNTIL token.
		// The LOOP node is the parent of the statement subtrees.
		StatementParser statementParser = new StatementParser(this);
		statementParser.parseList(token,loopNode,UNTIL,MISSING_UNTIL);
		token = currentToken();

		// Parse the expression.
		// The TEST node adopts the expression subtree as its only
		// child.
		// The LOOP node adopts the TEST node.
		ExpressionParser expressionParser =
			new ExpressionParser(this);
		testNode.addChild(expressionParser.parse(token));
		loopNode.addChild(testNode);

		return loopNode;
	}
}

