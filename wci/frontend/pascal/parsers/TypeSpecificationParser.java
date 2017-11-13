package wci.frontend.pascal.parsers;

import java.util.EnumSet;

import wci.frontend.Token;

import wci.frontend.pascal.PascalTokenType;

import wci.frontend.pascal.PascalParserTD;

import wci.intermediate.TypeSpec;

import static wci.frontend.pascal.PascalTokenType.ARRAY;
import static wci.frontend.pascal.PascalTokenType.RECORD;
import static wci.frontend.pascal.PascalTokenType.SEMICOLON;

/**
* <h1>TypeSpecificationParser</h1>
*
* <p>The type specification parser.</p>
*/
public class TypeSpecificationParser extends PascalParserTD
{

	// Synchronization set for starting a type specification.
	static final EnumSet<PascalTokenType> TYPE_START_SET;

	static
	{
		TYPE_START_SET =
			SimpleTypeParser.SIMPLE_TYPE_START_SET.clone();
		TYPE_START_SET.add(PascalTokenType.ARRAY);
		TYPE_START_SET.add(PascalTokenType.RECORD);
		// IDENTIFIER, REAL, INTEGER, PLUS, MINUS, STRING, ARRAY,
		// RECORD, SEMICOLON
		TYPE_START_SET.add(SEMICOLON);
	}

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
	* Parse a Pascal type specification.
	*
	* @param token the current token
	* @return the type specification
	* @throws Exception
	*/
	public TypeSpec parse(Token token) throws Exception
	{
		// Synchronize at the start of a type specification.
		token = synchronize(TYPE_START_SET);

		if (token.getType() == ARRAY) {
			ArrayTypeParser arrayTypeParser =
				new ArrayTypeParser(this);
			return arrayTypeParser.parse(token);
		}
		else if (token.getType() == RECORD) {
			RecordTypeParser recordTypeParser =
				new RecordTypeParser(this);
			return recordTypeParser.parse(token);
		}
		else {
			SimpleTypeParser simpleTypeParser =
				new SimpleTypeParser(this);
			return simpleTypeParser.parse(token);
		}
	}
}

