package wci.frontend.pascal.parsers;

import wci.frontend.Token;

import wci.frontend.pascal.PascalParserTD;

import wci.intermediate.TypeSpec;

/**
* <h1>TypeSpecificationParser</h1>
*
* <p>The type specification parser.</p>
*/
public class TypeSpecificationParser extends PascalParserTD
{

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public TypeSpecificationParser(PascalParserTD parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------

	/**
	* Parse the type specification.
	*
	* @param token the initial token
	* @throws Exception
	*/
	public TypeSpec parse(Token token) throws Exception
	{
		throw new Exception("Not implemented.");
	}
}

