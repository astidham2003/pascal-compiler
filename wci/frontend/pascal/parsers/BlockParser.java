package wci.frontend.pascal.parsers;

import wci.frontend.Token;

import wci.frontend.pascal.PascalParserTD;

import wci.intermediate.ICodeNode;
import wci.intermediate.SymTabEntry;

/**
* <h1>BlockParser</h1>
*
* <p>The block parser.</p>
*/
public class BlockParser extends PascalParserTD
{

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public BlockParser(PascalParserTD parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------

	/**
	* Parse a block.
	*
	* @param token the initial token
	* @param routineId the symbol table entry of the routine name
	* @return the root node of the parse tree
	* @throws Exception
	*/
	public ICodeNode parse(Token token,SymTabEntry routineId)
		throws Exception
	{
		return null;
	}
}
