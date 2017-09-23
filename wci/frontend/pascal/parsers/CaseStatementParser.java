package wci.frontend.pascal.parsers;

import wci.intermediate.ICodeNode;

import wci.frontend.Token;

public class CaseStatementParser extends StatementParser
{

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

	@Override
	public ICodeNode parse(Token token) throws Exception
	{
		return null;
	}
}

