package wci.frontend.pascal.parsers;

import wci.frontend.Token;

import wci.frontend.pascal.PascalParserTD;

import wci.intermediate.symtabimpl.DefinitionImpl;

/**
* <h1>VariableDeclarationsParser</h1>
*
* <p>The variable declarations parser.</p>
*/
public class VariableDeclarationsParser extends PascalParserTD
{

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public VariableDeclarationsParser(PascalParserTD parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------

	public void setDefinition(DefinitionImpl definition)
	{
		// XXX
	}

	// ----------------------------------------------------------------

	/**
	* Parse the variable definition.
	*
	* @param token the initial token
	* @throws Exception
	*/
	public void parse(Token token) throws Exception
	{
		// XXX
	}
}

