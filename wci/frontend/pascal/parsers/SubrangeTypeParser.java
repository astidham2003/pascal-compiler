package wci.frontend.pascal.parsers;

import wci.frontend.Token;

import wci.frontend.pascal.PascalParserTD;

import wci.intermediate.TypeSpec;

/**
* <h1>SubrangeTypeParser</h1>
*
* <p>The subrange type specification parser.</p>
*/
public class SubrangeTypeParser extends PascalParserTD
{

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public SubrangeTypeParser(PascalParserTD parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------

	/**
	* Parse a Pascal subrange type specification.
	*
	* @param token the current token
	* @return the type specification
	* @throws Exception
	*/
	public TypeSpec parse(Token token) throws Exception
	{
		throw new Exception("Not implemented");
	}
}


