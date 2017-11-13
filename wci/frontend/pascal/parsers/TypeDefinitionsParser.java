package wci.frontend.pascal.parsers;

import java.util.EnumSet;

import wci.frontend.Token;
import wci.frontend.TokenType;

import wci.frontend.pascal.PascalTokenType;
import wci.frontend.pascal.PascalParserTD;

import wci.intermediate.SymTabEntry;
import wci.intermediate.TypeSpec;

import static wci.frontend.pascal.PascalTokenType.IDENTIFIER;
import static wci.frontend.pascal.PascalTokenType.EQUALS;
import static wci.frontend.pascal.PascalTokenType.SEMICOLON;

import static wci.frontend.pascal.PascalErrorCode.IDENTIFIER_REDEFINED;
import static wci.frontend.pascal.PascalErrorCode.MISSING_EQUALS;
import static wci.frontend.pascal.PascalErrorCode.MISSING_SEMICOLON;

import static wci.intermediate.symtabimpl.DefinitionImpl.TYPE;

/**
* <h1>TypeDefinitionsParser</h1>
*
* <p>The type definitions parser.</p>
*/
public class TypeDefinitionsParser extends PascalParserTD
{

	// Synchronization set for a type identifier.
	private static final EnumSet<PascalTokenType> IDENTIFIER_SET;
	// Synchronization set for the = token.
	private static final EnumSet<PascalTokenType> EQUALS_SET;
	// Synchronization set for what follows a definition or
	// declaration.
	private static final EnumSet<PascalTokenType> FOLLOW_SET;
	// Synchonization set for the start of the next definition or
	// declaration.
	private static final EnumSet<PascalTokenType> NEXT_START_SET;

	static
	{
		IDENTIFIER_SET = DeclarationsParser.VAR_START_SET.clone();
		// VAR,PROCEDURE,FUNCTION,BEGIN,IDENTIFIER
		IDENTIFIER_SET.add(IDENTIFIER);

		EQUALS_SET =
			ConstantDefinitionsParser.CONSTANT_START_SET.clone();
		EQUALS_SET.add(EQUALS);
		// IDENTIFIER,INTEGER,REAL,PLUS,MINUS,STRING,EQUALS,SEMICOLON
		// XXX
		//SEMICOLON already belongs to the CONSTANT_START_SET, why add
		// it a second time.
		// In fact, why are starting from the CONSTANT_START_SET
		// when we could just use the ConstantDefinitionsParser
		// EQUALS_SET, that is congruent to this EQUALS_SET?
		// XXX
		EQUALS_SET.add(SEMICOLON);

		FOLLOW_SET = EnumSet.of(SEMICOLON);

		NEXT_START_SET = DeclarationsParser.VAR_START_SET.clone();
		NEXT_START_SET.add(SEMICOLON);
		// VAR,PROCEDURE,FUNCTION,BEGIN,SEMICOLON,IDENTIFIER
		NEXT_START_SET.add(IDENTIFIER);
	}

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public TypeDefinitionsParser(PascalParserTD parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------

	/**
	* Parser the type definition.
	*
	* @param token the initial token
	* @throws Exception
	*/
	public void parse(Token token) throws Exception
	{
		token = synchronize(IDENTIFIER_SET);

		// Loop to parse a sequence of type definitions separated by
		// semicolons.
		while (token.getType() == IDENTIFIER) {
			String name = token.getText().toLowerCase();
			SymTabEntry typeId = symTabStack.lookupLocal(name);

			// Enter the new identifier into the symbol table but
			// don't set how it's defined yet.
			if (typeId == null) {
				typeId = symTabStack.enterLocal(name);
				typeId.appendLineNumber(token.getLineNumber());
			}
			else {
				errorHandler.flag(token,IDENTIFIER_REDEFINED,this);
				typeId = null;
			}

			token = nextToken(); // Consume the identifier token.

			// Synchronize on the - token.
			token = synchronize(EQUALS_SET);
			if (token.getType() == EQUALS) {
				token = nextToken();
			}
			else {
				errorHandler.flag(token,MISSING_EQUALS,this);
			}

			// Parse the type specification.
			TypeSpecificationParser typeSpecificationParser =
				new TypeSpecificationParser(this);
			TypeSpec type = typeSpecificationParser.parse(token);

			// Set identifier to be a type and set its type
			// specification.
			if (typeId != null) {
				typeId.setDefinition(TYPE);
			}

			// Cross-link the type identifier and the type
			// specification.
			if (type != null && typeId != null) {
				if (type.getIdentifier() == null) {
					type.setIdentifier(typeId);
				}
				typeId.setTypeSpec(type);
			}
			else {
				token = synchronize(FOLLOW_SET);
			}

			token = currentToken();
			TokenType tokenType = token.getType();

			// Look for one or more semicolons after a definition.
			if (tokenType == SEMICOLON) {
				while (token.getType() == SEMICOLON) {
					token = nextToken(); // Consume the ; token(s).
				}
			}
			// If at the start of the next definition or declaration
			// then missing a semicolon.
			else if (NEXT_START_SET.contains(tokenType)) {
				errorHandler.flag(token,MISSING_SEMICOLON,this);
			}

			token = synchronize(IDENTIFIER_SET);
		}
	}
}

