package wci.frontend.pascal.parsers;

import java.util.EnumSet;

import wci.frontend.Token;

import wci.frontend.pascal.PascalTokenType;

import wci.frontend.pascal.PascalParserTD;

import wci.intermediate.TypeSpec;
import wci.intermediate.Definition;
import wci.intermediate.SymTabEntry;

import wci.intermediate.symtabimpl.DefinitionImpl;

import static wci.frontend.pascal.PascalTokenType.LEFT_PAREN;
import static wci.frontend.pascal.PascalTokenType.COMMA;
import static wci.frontend.pascal.PascalTokenType.SEMICOLON;
import static wci.frontend.pascal.PascalTokenType.IDENTIFIER;

import static wci.frontend.pascal.PascalErrorCode.NOT_TYPE_IDENTIFIER;
import static wci.frontend.pascal.PascalErrorCode.IDENTIFIER_UNDEFINED;
import static wci.frontend.pascal.PascalErrorCode.INVALID_TYPE;

import static wci.intermediate.symtabimpl.DefinitionImpl.CONSTANT;
import static wci.intermediate.symtabimpl.DefinitionImpl.
	ENUMERATION_CONSTANT;

/**
* <h1>SimpleTypeParser</h1>
*
* <p>The simple type specification parser.</p>
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
		// Synchronize at the start of a simple type specification.
		token = synchronize(SIMPLE_TYPE_START_SET);

		if (token.getType() == IDENTIFIER) {
			String name = token.getText().toLowerCase();
			SymTabEntry id = symTabStack.lookup(name);

			if (id != null) {
				Definition definition = id.getDefinition();

				// It's either a type identifier
				// or the start of a subrange type.
				if (definition == DefinitionImpl.TYPE) {
					id.appendLineNumber(token.getLineNumber());
					token = nextToken(); // Consume the identifier.

					// Return the type of the referent type.
					return id.getTypeSpec();
				}
				else if (definition != CONSTANT &&
						definition != ENUMERATION_CONSTANT) {
					errorHandler.flag(token,NOT_TYPE_IDENTIFIER,this);
					token = nextToken(); // Consume the identifier.
					return null;
				}
				else {
					SubrangeTypeParser subrangeTypeParser =
						new SubrangeTypeParser(this);
					return subrangeTypeParser.parse(token);
				}
			}
			else {
				errorHandler.flag(token, IDENTIFIER_UNDEFINED, this);
				token = nextToken(); // Consume the identifier.
				return null;
			}
		}

		else if (token.getType() == LEFT_PAREN) {
			EnumerationTypeParser enumerationTypeParser =
				new EnumerationTypeParser(this);
			return enumerationTypeParser.parse(token);
		}

		else if (token.getType() == COMMA ||
				token.getType() == SEMICOLON) {
			errorHandler.flag(token, INVALID_TYPE, this);
			return null;
		}

		else {
			SubrangeTypeParser subrangeTypeParser =
				new SubrangeTypeParser(this);
			return subrangeTypeParser.parse(token);
		}
	}
}

