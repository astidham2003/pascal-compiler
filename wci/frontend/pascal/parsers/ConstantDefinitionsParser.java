package wci.frontend.pascal.parsers;

import java.util.EnumSet;

import wci.frontend.Token;

import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;

import static wci.frontend.pascal.PascalTokenType.*;

/**
* <h1>ConstantDefinitionsParser</h1>
*
* <p>The constant definition parser.</p>
*/
public class ConstantDefinitionsParser extends PascalParserTD
{
	// Synchronization set for a constant identifier.
	private static final EnumSet<PascalTokenType> IDENTIFIER_SET;
	// Synchronization set for starting a constant.
	private static final EnumSet<PascalTokenType> CONSTANT_START_SET;
	private static final EnumSet<PascalTokenType> EQUALS_SET;
	private static final EnumSet<PascalTokenType> NEXT_START_SET;

	static
	{
		IDENTIFIER_SET = DeclarationsParser.TYPE_START_SET.clone();
		// TYPE,VAR,PROCEDURE,FUNCTION,BEGIN,IDENTIFIER
		IDENTIFIER_SET.add(IDENTIFIER);

		CONSTANT_START_SET =
			EnumSet.of(
				IDENTIFIER,INTEGER,REAL,PLUS,MINUS,STRING,SEMICOLON);

		EQUALS_SET = CONSTANT_START_SET.clone();
		EQUALS_SET.add(EQUALS);
		// IDENTIFIER,INTEGER,REAL,PLUS,MINUS,STRING,SEMICOLON,EQUALS,
		// SEMICOLON
		EQUALS_SET.add(SEMICOLON);

		NEXT_START_SET = DeclarationsParser.TYPE_START_SET.clone();
		NEXT_START_SET.add(IDENTIFIER);
		// TYPE,VAR,PROCEDURE,FUNCTION,BEGIN,IDENTIFIER,SEMICOLON
		NEXT_START_SET.add(SEMICOLON);
	}

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

