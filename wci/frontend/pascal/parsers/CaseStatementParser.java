package wci.frontend.pascal.parsers;

import java.util.EnumSet;
import java.util.HashSet;

import wci.intermediate.ICodeNode;
import wci.intermediate.ICodeFactory;

import wci.frontend.Token;
import wci.frontend.TokenType;
import wci.frontend.EofToken;

import wci.frontend.pascal.PascalTokenType;

import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.SELECT;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.
	SELECT_BRANCH;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.
	SELECT_CONSTANTS;

import static wci.frontend.pascal.PascalTokenType.IDENTIFIER;
import static wci.frontend.pascal.PascalTokenType.INTEGER;
import static wci.frontend.pascal.PascalTokenType.PLUS;
import static wci.frontend.pascal.PascalTokenType.MINUS;
import static wci.frontend.pascal.PascalTokenType.STRING;
import static wci.frontend.pascal.PascalTokenType.OF;
import static wci.frontend.pascal.PascalTokenType.END;
import static wci.frontend.pascal.PascalTokenType.SEMICOLON;
import static wci.frontend.pascal.PascalTokenType.COLON;

import static wci.frontend.pascal.PascalErrorCode.MISSING_OF;
import static wci.frontend.pascal.PascalErrorCode.MISSING_SEMICOLON;
import static wci.frontend.pascal.PascalErrorCode.MISSING_END;
import static wci.frontend.pascal.PascalErrorCode.MISSING_COLON;

/**
* <h1>CaseStatementParser</p>
*
* <p>Root parser for case statements.</p>
*/
public class CaseStatementParser extends StatementParser
{

	// Synchronization set for stating CASE option statement.
	private static final EnumSet<PascalTokenType> CONSTANT_START_SET =
		EnumSet.of(IDENTIFIER,INTEGER,PLUS,MINUS,STRING);

	// Synchonization set for OF.
	private static final EnumSet<PascalTokenType> OF_SET =
		CONSTANT_START_SET.clone();

	static
	{
		OF_SET.add(OF);
		OF_SET.addAll(StatementParser.STMT_FOLLOW_SET);
	}

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public CaseStatementParser(StatementParser parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------
	// StatementParser methods

	/**
	* Parse a CASE statement.
	*
	* @param token the initial token.
	* @return the root node of the generated parse tree.
	* @throws Exception
	*/
	@Override
	public ICodeNode parse(Token token) throws Exception
	{
		token = nextToken(); // Consume the CASE.

		// Create a SELECT node.
		ICodeNode selectNode =
			ICodeFactory.createICodeNode(SELECT);

		// Parse the CASE expression.
		ExpressionParser expressionParser = new ExpressionParser(this);
		selectNode.addChild(expressionParser.parse(token));

		// Synchronize on the OF.
		token = synchronize(OF_SET);
		if (token.getType() == OF) {
			token = nextToken(); // Consume the OF.
		}
		else {
			errorHandler.flag(token,MISSING_OF,this);
		}

		// Set of CASE branch constants.
		HashSet<Object> constantSet = new HashSet<Object>();

		// Loop to parse each CASE branch until the END token
		// or the end of the source file.
		while (!(token instanceof EofToken) &&
				(token.getType() != END)) {

			// The SELECT node adopts the CASE branch subtree.
			selectNode.addChild(parseBranch(token,constantSet));

			token = currentToken();
			TokenType tokenType = token.getType();

			// Look for the semicolon between CASE branches.
			if (tokenType == SEMICOLON) {
				token = nextToken(); // Consume the ;.
			}
			// If at the start of the next constant, then missing
			// semicolon.
			else if (CONSTANT_START_SET.contains(tokenType)) {
				errorHandler.flag(token,MISSING_SEMICOLON,this);
			}
		}

		// Look for the END token.
		if (token.getType() == END) {
			token = nextToken(); // Consume the END.
		}
		else {
			errorHandler.flag(token,MISSING_END,this);
		}

		return selectNode;
	}

	/**
	* Parse a CASE branch.
	*
	* @param token the current token
	* @param constantSet the set of CASE branch constants
	* @return the root SELECT_BRANCH node of the subtree
	* @throws Exception
	*/
	private ICodeNode parseBranch(
		Token token,HashSet<Object> constantSet) throws Exception
	{
		// Create a SELECT_BRANCH node and a SELECT_CONSTANTS node.
		// The SELECT_BRANCH node adopts the SELECT_CONSTANTS node
		// as its first child.
		ICodeNode branchNode =
			ICodeFactory.createICodeNode(SELECT_BRANCH);
		ICodeNode constantsNode =
			ICodeFactory.createICodeNode(SELECT_CONSTANTS);
		branchNode.addChild(constantsNode);

		// Parse the list of CASE branch constants.
		// The SELECT_CONSTANTS node adopts each constant.
		parseConstantList(token,constantsNode,constantSet);

		// Look for the : token.
		token = currentToken();
		if (token.getType() == COLON) {
			token = nextToken(); // Consume the colon.
		}
		else {
			errorHandler.flag(token,MISSING_COLON,this);
		}

		// Parse the CASE branch statement.  The SELECT_BRANCH node
		// adopts the statement subtree as its second child.
		StatementParser statementParser = new StatementParser(this);
		branchNode.addChild(statementParser.parse(token));

		return branchNode;
	}

	/**
	* Parse a list of constants.
	*
	* @param token the initial token
	* @param constantsNode the root node of the list
	* @param constantSet // XXX
	*/
	private void parseConstantList(
		Token token,ICodeNode constantsNode,
		HashSet<Object> constantSet)
	{
		// XXX
	}
}

