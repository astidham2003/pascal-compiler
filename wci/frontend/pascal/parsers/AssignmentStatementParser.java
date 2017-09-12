package wci.frontend.pascal.parsers;

import wci.intermediate.ICodeNode;

import wci.frontend.Token;
import wci.frontend.pascal.PascalParserTD;

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
		ICodeNode assignmentStatement = null;
		return assignmentStatement;
	}
}

