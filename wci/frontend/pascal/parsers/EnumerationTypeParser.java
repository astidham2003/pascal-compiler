package wci.frontend.pascal.parsers;

import java.util.EnumSet;
import java.util.ArrayList;

import wci.frontend.Token;
import wci.frontend.TokenType;

import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;

import wci.intermediate.TypeSpec;
import wci.intermediate.TypeFactory;
import wci.intermediate.SymTabEntry;

import static wci.frontend.pascal.PascalTokenType.IDENTIFIER;
import static wci.frontend.pascal.PascalTokenType.COMMA;
import static wci.frontend.pascal.PascalTokenType.RIGHT_PAREN;
import static wci.frontend.pascal.PascalTokenType.SEMICOLON;

import static wci.frontend.pascal.PascalErrorCode.MISSING_IDENTIFIER;
import static wci.frontend.pascal.PascalErrorCode.MISSING_COMMA;
import static wci.frontend.pascal.PascalErrorCode.MISSING_RIGHT_PAREN;
import static wci.frontend.pascal.PascalErrorCode.IDENTIFIER_REDEFINED;

import static wci.intermediate.symtabimpl.SymTabKeyImpl.CONSTANT_VALUE;
import static wci.intermediate.symtabimpl.DefinitionImpl.
	ENUMERATION_CONSTANT;

import static wci.intermediate.typeimpl.TypeFormImpl.ENUMERATION;
import static wci.intermediate.typeimpl.TypeKeyImpl.
	ENUMERATION_CONSTANTS;

/**
* <h1>EnumerationTypeParser</h1>
*
* <p>The enumeration type specification parser.</p>
*/
public class EnumerationTypeParser extends PascalParserTD
{

	// Synchronization set to start an enumeration constant.
	private static final EnumSet<PascalTokenType>
		ENUM_CONSTANT_START_SET;
	// Synchronization set to follow an enumeration definition.
	private static final EnumSet<PascalTokenType>
		ENUM_DEFINITION_FOLLOW_SET;

	static
	{
		ENUM_CONSTANT_START_SET = EnumSet.of(IDENTIFIER,COMMA);
		ENUM_DEFINITION_FOLLOW_SET =
			EnumSet.of(RIGHT_PAREN,SEMICOLON);
		// VAR, PROCEDURE, FUNCTION, BEGIN, RIGHT_PAREN, SEMICOLON
		ENUM_DEFINITION_FOLLOW_SET.addAll(
			DeclarationsParser.VAR_START_SET);
	}

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public EnumerationTypeParser(PascalParserTD parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------

	/**
	* Parse a Pascal enumeration type specification.
	*
	* @param token the current token
	* @return the enumeration type specification
	* @throws Exception
	*/
	public TypeSpec parse(Token token) throws Exception
	{
		TypeSpec enumerationType = TypeFactory.createType(ENUMERATION);
		int value = -1;
		ArrayList<SymTabEntry> constants =
			new ArrayList<SymTabEntry>();

		token = nextToken(); // Consume the opening (.

		do {
			token = synchronize(ENUM_CONSTANT_START_SET);
			parseEnumerationIdentifier(
				token, ++value, enumerationType, constants);

			token = currentToken();
			TokenType tokenType = token.getType();

			// Look for the comma.
			if (tokenType == COMMA) {
				token = nextToken(); // Consume the comma.

				if (ENUM_DEFINITION_FOLLOW_SET.contains(
						token.getType())) {
					errorHandler.flag(token, MISSING_IDENTIFIER, this);
				}
			}
			else if (ENUM_CONSTANT_START_SET.contains(tokenType)) {
				errorHandler.flag(token, MISSING_COMMA, this);
			}
		} while (
			!ENUM_DEFINITION_FOLLOW_SET.contains(token.getType()));

		// Look for the closing ).
		if (token.getType() == RIGHT_PAREN) {
			token = nextToken(); // Consume the );
		}
		else {
			errorHandler.flag(token, MISSING_RIGHT_PAREN, this);
		}

		enumerationType.setAttribute(ENUMERATION_CONSTANTS, constants);
		return enumerationType;
	}

	/**
	* Parse an enumeration identifier.
	*
	* @param token the current token.
	* @param value the identifier's integer value (sequence number).
	* @param enumerationType the enumeration type specification.
	* @param constants the array of symbol table entries for the
	* enumeratin constants.
	* @throws Exception
	*/
	private void parseEnumerationIdentifier(
		Token token, int value, TypeSpec enumerationType,
		ArrayList<SymTabEntry> constants) throws Exception
	{
		TokenType tokenType = token.getType();

		if (tokenType == IDENTIFIER) {
			String name = token.getText().toLowerCase();
			SymTabEntry constantId = symTabStack.lookupLocal(name);

			if (constantId != null) {
				errorHandler.flag(token, IDENTIFIER_REDEFINED, this);
			}
			else {
				constantId = symTabStack.enterLocal(token.getText());
				constantId.setDefinition(ENUMERATION_CONSTANT);
				constantId.setTypeSpec(enumerationType);
				constantId.setAttribute(CONSTANT_VALUE, value);
				constantId.appendLineNumber(token.getLineNumber());
				constants.add(constantId);
			}

			token = nextToken(); // Consume the identifier.
		}
		else {
			errorHandler.flag(token, MISSING_IDENTIFIER, this);
		}
	}
}


