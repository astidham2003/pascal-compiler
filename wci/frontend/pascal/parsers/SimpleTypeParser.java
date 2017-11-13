package wci.frontend.pascal.parsers;

import java.util.EnumSet;

import wci.frontend.Token;

import wci.frontend.pascal.PascalTokenType;

import wci.frontend.pascal.PascalParserTD;

import wci.intermediate.TypeSpec;

import static wci.frontend.pascal.PascalTokenType.LEFT_PAREN;
import static wci.frontend.pascal.PascalTokenType.COMMA;
import static wci.frontend.pascal.PascalTokenType.SEMICOLON;

/**
* <h1>SimpleTypeParser</h1>
*
* <p>The type specification parser.</p>
*/
public class SimpleTypeParser extends PascalParserTD
{

	// Synchronization set for starting a simple type specification.
	static final EnumSet<PascalTokenType> SIMPLE_TYPE_START_SET;

	static
	{
		SIMPLE_TYPE_START_SET =
			ConstantDefinitionsParser.CONSTANT_START_SET.clone();
		SIMPLE_TYPE_START_SET.add(LEFT_PAREN);
		SIMPLE_TYPE_START_SET.add(COMMA);
		// IDENTIFIER, REAL, INTEGER, PLUS, MINUS, STRING, LEFT_PAREN,
		// COMMA, SEMICOLON
		SIMPLE_TYPE_START_SET.add(SEMICOLON);
	}

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public SimpleTypeParser(PascalParserTD parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------

	/**
	* Parse a Pascal simple type specification.
	*
	* @param token the current token
	* @return the type specification
	* @throws Exception
	*/
	public TypeSpec parse(Token token) throws Exception
	{
		throw new Exception("Not implemented.");
	}
}

