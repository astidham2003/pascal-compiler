package wci.frontend.pascal.parsers;

import wci.frontend.Token;

import wci.frontend.pascal.PascalParserTD;

/**
* <h1>ConstantDefinitionsParser</h1>
*
* <p>The constant definition parser.</p>
*/
public class ConstantDefinitionsParser extends PascalParserTD
{

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public ConstantDefinitionsParser(PascalParserTD parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------

	/**
	* Parse the constant definition.
	*
	* @param token the initial token
	* @throws Exception
	*/
	public void parse(Token token) throws Exception
	{
		throw new Exception("Not implemented");
	}
}

