package wci.frontend.pascal.parsers;

import wci.frontend.Token;

import wci.frontend.pascal.PascalParserTD;

import wci.intermediate.TypeSpec;

/**
* <h1>EnumerationTypeParser</h1>
*
* <p>The enumeration type specification parser.</p>
*/
public class EnumerationTypeParser extends PascalParserTD
{

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public EnumerationTypeParser(PascalParserTD parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------

	/**
	* Parse a Pascal enumeration type specification.
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


