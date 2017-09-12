package wci.frontend.pascal.parsers;

import wci.intermediate.ICodeNode;
import wci.intermediate.ICodeFactory;

import wci.frontend.Token;

import wci.frontend.pascal.PascalParserTD;

import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.NO_OP;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.COMPOUND;

import static wci.frontend.pascal.PascalTokenType.*;

import static wci.frontend.pascal.PascalErrorCode.MISSING_END;

/**
* <h1>CompoundStatementParser</h1>
*
* <p>Root parser for compound statements.</p>
*/
public class CompoundStatementParser extends StatementParser
{
	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public CompoundStatementParser(PascalParserTD parent)
	{
		super(parent);
	}

	/**
	* Parse a compound statement.
	*
	* @param token the initial token
	* @return the root node of the generated parse tree
	* @throws Exception
	*/
	@Override
	public ICodeNode parse(Token token) throws Exception
	{
		token = nextToken(); // Consume the BEGIN.

		// Create the compound node.
		ICodeNode compoundNode =
			ICodeFactory.createICodeNode(COMPOUND);

		// Parse the statement list terminated by the END token.
		StatementParser statementParser = new StatementParser(this);
		statementParser.parseList(token,compoundNode,END,MISSING_END);

		return compoundNode;
	}


}

