package wci.frontend.pascal.parsers;

import wci.intermediate.ICodeNode;
import wci.intermediate.ICodeFactory;

import wci.frontend.Token;
import wci.frontend.TokenType;
import wci.frontend.EofToken;

import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalErrorCode;
import wci.frontend.pascal.PascalTokenType;

import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.NO_OP;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.LINE;

import static wci.frontend.pascal.PascalTokenType.*;

import static wci.frontend.pascal.PascalErrorCode.UNEXPECTED_TOKEN;
import static wci.frontend.pascal.PascalErrorCode.MISSING_SEMICOLON;



/**
* <h1>StatementParser</h1>
*
* <p>Root parser for statements.</p>
*/
public class StatementParser extends PascalParserTD
{
	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public StatementParser(PascalParserTD parent)
	{
		super(parent);
	}

	/**
	* Parse a statement.
	* To be overridden by the specialized statement parser subclasses.
	*
	* @param token the initial token
	* @return the root node of the generated parse tree
	* @throws Exception
	*/
	public ICodeNode parse(Token token) throws Exception
	{
		ICodeNode statementNode = null;
		PascalTokenType tokenType = (PascalTokenType) token.getType();

		if (tokenType == BEGIN) {
			CompoundStatementParser compoundParser =
				new CompoundStatementParser(this);
			statementNode = compoundParser.parse(token);
		}

		// An assignment statement begins with a variables identifier.
		else if (tokenType == IDENTIFIER) {
			AssignmentStatementParser assignmentParser =
				new AssignmentStatementParser(this);
			statementNode = assignmentParser.parse(token);
		}

		else {
			statementNode = ICodeFactory.createICodeNode(NO_OP);
		}

		// Set the current line number as an attribute.
		setLineNumber(statementNode,token);

		return statementNode;
	}

	/**
	* Parse a statement list.
	*
	* @param token the current token
	* @param parentNode the parent node of the statement list
	* @param terminator the token type of the node that terminates
	* the list
	* @param errorCode the error code if the terminator token is
	* missing
	* @throws Exception
	*/
	protected void parseList(
			Token token,ICodeNode parentNode,
			PascalTokenType terminator,PascalErrorCode errorCode)
		throws Exception
	{
		// Loop to parse each statement until the END token
		// or the end of the source file.
		while (!(token instanceof EofToken) &&
				(token.getType() != terminator)) {
			// Parse a statement.  The parent node adopts the statement
			// node.
			ICodeNode statementNode = parse(token);
			parentNode.addChild(statementNode);

			token = currentToken();
			TokenType tokenType = token.getType();

			if (tokenType == SEMICOLON) {
				token = nextToken(); // Consume the ;.
			}

			// If start of the next assignment statement without seeing
			// a ;, then missing a semi-colon.
			else if (tokenType == IDENTIFIER) {
				errorHandler.flag(token,MISSING_SEMICOLON,this);
			}

			// Unexpected token.
			else if (tokenType != terminator) {
				errorHandler.flag(token,UNEXPECTED_TOKEN,this);
				token = nextToken(); // Consume the unexpected token.
			}

		}

		if (token.getType() == terminator) {
			token = nextToken(); // Consume the terminator.
		}
		else
		{
			errorHandler.flag(token,errorCode,this);
		}

	}

	/**
	* Set the current line number as a statement node attribute.
	*
	* @param node ICodeNode
	* @param token Token
	*/
	protected void setLineNumber(ICodeNode node,Token token)
	{
		if (node != null) {
			node.setAttribute(LINE,token.getLineNumber());
		}
	}

}

