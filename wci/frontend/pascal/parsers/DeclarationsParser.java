package wci.frontend.pascal.parsers;

import java.util.EnumSet;

import wci.frontend.Token;

import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;

import wci.intermediate.ICodeNode;

import static wci.frontend.pascal.PascalTokenType.*;

/**
* <h1>DeclarationsParser</h1>
*
* <p>The declarations parser.</p>
*/
public class DeclarationsParser extends PascalParserTD
{

	static final EnumSet<PascalTokenType> DECLARATION_START_SET =
		EnumSet.of(CONST,TYPE,VAR,PROCEDURE,FUNCTION,BEGIN);

	static final EnumSet<PascalTokenType> TYPE_START_SET =
		DECLARATION_START_SET.clone();

	static final EnumSet<PascalTokenType> VAR_START_SET =
		TYPE_START_SET.clone();

	static {
		TYPE_START_SET.remove(CONST);
		VAR_START_SET.remove(TYPE);
	}

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

