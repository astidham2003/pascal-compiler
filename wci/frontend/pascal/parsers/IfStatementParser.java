package wci.frontend.pascal.parsers;

import wci.intermediate.ICodeNode;

import wci.frontend.Token;

import wci.frontend.pascal.parsers.StatementParser;

public class IfStatementParser extends StatementParser
{

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

	@Override
	public ICodeNode parse(Token token) throws Exception
	{
		return null;
	}
}

