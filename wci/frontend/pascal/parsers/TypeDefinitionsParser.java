package wci.frontend.pascal.parsers;

import wci.frontend.Token;

import wci.frontend.pascal.PascalParserTD;

/**
* <h1>TypeDefinitionsParser</h1>
*
* <p>The type definitions parser.</p>
*/
public class TypeDefinitionsParser extends PascalParserTD
{

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public TypeDefinitionsParser(PascalParserTD parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------

	/**
	* Parser the type definition.
	*
	* @param token the initial token
	* @throws Exception
	*/
	public void parse(Token token) throws Exception
	{
		throw new Exception("Not implemented");
	}
}

