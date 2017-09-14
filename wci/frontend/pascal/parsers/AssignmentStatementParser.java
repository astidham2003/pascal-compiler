package wci.frontend.pascal.parsers;

import wci.intermediate.ICodeNode;
import wci.intermediate.ICodeFactory;
import wci.intermediate.SymTabEntry;

import wci.frontend.Token;
import wci.frontend.pascal.PascalParserTD;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.ID;

import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.ASSIGN;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.VARIABLE;

import static wci.frontend.pascal.PascalTokenType.COLON_EQUALS;
import static wci.frontend.pascal.PascalErrorCode.MISSING_COLON_EQUALS;

/**
* <h1>AssignmentStatementParser</h1>
*
* <p>Root parser for assignment statements.</p>
*/
public class AssignmentStatementParser
	extends StatementParser
{
	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public AssignmentStatementParser(PascalParserTD parent)
	{
		super(parent);
	}

	/**
	* Parse an assignment statement.
	*
	* @param token the initial token
	* @return the root node of the generated parse tree
	* @throws Exception
	*/
	public ICodeNode parse(Token token) throws Exception
	{
		ICodeNode assignNode = ICodeFactory.createICodeNode(ASSIGN);

		// Look up the target identifier in the symbol table stack.
		// Enter the identifier into the table if it's not found.
		String targetName = token.getText().toLowerCase();
		SymTabEntry targetId = symTabStack.lookup(targetName);
		if (targetId == null) {
			// XXX For now, consider a variable declared the first
			// XXX time it appears.
			targetId = symTabStack.enterLocal(targetName);
		}
		targetId.appendLineNumber(token.getLineNumber());

		token = nextToken(); // Consume the identifier token.

		// Create the variable node and set its name attribute.
		ICodeNode variableNode =
			ICodeFactory.createICodeNode(VARIABLE);
		// XXX For now, every variable is an identifier.
		variableNode.setAttribute(ID,targetId);

		// The ASSIGN node adopts the variable node as its first child.
		assignNode.addChild(variableNode);

		// Look for the := token.
		if (token.getType() == COLON_EQUALS) {
			token = nextToken(); // Consume the :=.
		}
		else {
			errorHandler.flag(token,MISSING_COLON_EQUALS,this);
		}

		// Parse the expression.  The ASSIGN node adopts the
		// expression's node as its second child.
		ExpressionParser expressionParser = new ExpressionParser(this);
		assignNode.addChild(expressionParser.parse(token));

		return assignNode;
	}
}

