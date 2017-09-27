package wci.frontend.pascal.parsers;

import wci.frontend.Token;

import wci.frontend.pascal.PascalParserTD;

import wci.intermediate.ICodeNode;

/**
* <h1>DeclarationsParser</h1>
*
* <p>The declarations parser.</p>
*/
public class DeclarationsParser extends PascalParserTD
{

	/**
	* Constructor
	*
	* @param parent the parent parser
	*/
	public DeclarationsParser(PascalParserTD parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------

	/**
	* Parse declarations.
	*
	* @param token the initial token
	* @return the root node of the parse tree
	*/
	public ICodeNode parse(Token token)
	{
		return null;
	}
}

