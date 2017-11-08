package wci.frontend.pascal.parsers;

import java.util.EnumSet;

import wci.frontend.Token;
import wci.frontend.TokenType;

import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;

import wci.intermediate.TypeSpec;
import wci.intermediate.SymTabEntry;
import wci.intermediate.Definition;
import wci.intermediate.TypeFactory;

import wci.intermediate.symtabimpl.Predefined;

import static wci.frontend.pascal.PascalTokenType.*;

import static wci.frontend.pascal.PascalErrorCode.MISSING_EQUALS;
import static wci.frontend.pascal.PascalErrorCode.MISSING_SEMICOLON;
import static wci.frontend.pascal.PascalErrorCode.IDENTIFIER_REDEFINED;
import static wci.frontend.pascal.PascalErrorCode.IDENTIFIER_UNDEFINED;
import static wci.frontend.pascal.PascalErrorCode.INVALID_CONSTANT;
import static wci.frontend.pascal.PascalErrorCode.
	NOT_CONSTANT_IDENTIFIER;

import static wci.intermediate.symtabimpl.DefinitionImpl.CONSTANT;
import static wci.intermediate.symtabimpl.DefinitionImpl.
	ENUMERATION_CONSTANT;

import static wci.intermediate.symtabimpl.SymTabKeyImpl.CONSTANT_VALUE;

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
		token = synchronize(IDENTIFIER_SET);

		// Loop to parse a sequence of constant definitions
		// separated by sewmicolons.
		while (token.getType() == IDENTIFIER) {
			String name = token.getText().toLowerCase();
			SymTabEntry constantId = symTabStack.lookupLocal(name);

			// Enter the new identifier into the symbol table
			// but don't set how it's defined, yet.
			if (constantId == null) {
				constantId = symTabStack.enterLocal(name);
				constantId.appendLineNumber(token.getLineNumber());
			}
			else {
				errorHandler.flag(token,IDENTIFIER_REDEFINED,this);
				constantId = null;
			}

			token = nextToken();  // Consume the identifier token.

			// Synchronize on the = token.
			token = synchronize(EQUALS_SET);
			if (token.getType() == EQUALS) {
				token = nextToken(); // Consume the =.
			}
			else {
				errorHandler.flag(token,MISSING_EQUALS,this);
			}

			// Parse the constant value.
			Token constantToken = token;
			Object value = parseConstant(token);

			// Set identifier to be a constant and set its value.
			if (constantId != null) {
				constantId.setDefinition(CONSTANT);
				constantId.setAttribute(CONSTANT_VALUE,value);

				// Set the constant's type.
				TypeSpec constantType =
					constantToken.getType() == IDENTIFIER ?
						getConstantType(constantToken) :
						getConstantType(value);
				constantId.setTypeSpec(constantType);
			}

			token = currentToken();
			TokenType tokenType = token.getType();

			// Look for one or more semicolons after a definition.
			if (tokenType == SEMICOLON) {
				while (tokenType == SEMICOLON) {
					token = nextToken(); // Consume the ;.
				}
			}
			// If at the start of the next definition of declaration,
			// then missing a semicolon.
			else {
				errorHandler.flag(token,MISSING_SEMICOLON,this);
			}

			token = synchronize(IDENTIFIER_SET);
		}
	}

	/**
	* Parse a constant value.
	*
	* @param token the current token
	* @return the constant value
	* @throws Exception
	*/
	protected Object parseConstant(Token token) throws Exception
	{
		TokenType sign = null;

		// Synchronize at the start of a constant.
		token = synchronize(CONSTANT_START_SET);
		TokenType tokenType = token.getType();

		// Plus or minus sign?
		if ((tokenType == PLUS) || (tokenType == MINUS)) {
			sign = tokenType;
			token = nextToken(); // Consume the sign.
		}

		// Parse the constant.
		if (token.getType() == IDENTIFIER) {
			return parseIdentifierConstant(token,sign);
		}

		else if (token.getType() == INTEGER) {
			Integer value = (Integer) token.getValue();
			nextToken();  // Consume the number.
			return sign == MINUS ? -value : value;
		}

		else if (token.getType() == REAL) {
			Float value = (Float) token.getValue();
			nextToken(); // Consume the number.
			return sign == MINUS ? -value : value;
		}

		else if (token.getType() == STRING) {
			if (sign != null) {
				errorHandler.flag(token,INVALID_CONSTANT,this);
			}

			nextToken(); // Consume the string.
			return (String) token.getValue();
		}

		else {
			errorHandler.flag(token,INVALID_CONSTANT,this);
			return null;
		}
	}

	/**
	* Parse an identifier constant.
	*
	* @param token the current token
	* @param sign the sign, if any
	* @return Object the constant value
	* @throws Exception
	*/
	protected Object parseIdentifierConstant(
		Token token,TokenType sign) throws Exception
	{
		String name = token.getText();
		SymTabEntry id = symTabStack.lookup(name);

		nextToken(); // Consume the identifier.

		// The identifier must have already been defined
		// as a constant identifier.
		if (id == null) {
			errorHandler.flag(token,IDENTIFIER_UNDEFINED,this);
			return null;
		}

		Definition definition = id.getDefinition();

		if (definition == CONSTANT) {
			Object value = id.getAttribute(CONSTANT_VALUE);
			id.appendLineNumber(token.getLineNumber());

			if (value instanceof Integer) {
				return sign == MINUS ? -((Integer)value) : value;
			}
			else if (value instanceof Float) {
				return sign == MINUS ? -((Float)value) : value;
			}
			else if (value instanceof String) {
				if (sign != null) {
					errorHandler.flag(token,INVALID_CONSTANT,this);
				}

				return value;
			}
			else {
				return null;
			}
		}
		else if (definition == ENUMERATION_CONSTANT) {
			Object value = id.getAttribute(CONSTANT_VALUE);
			id.appendLineNumber(token.getLineNumber());

			if (sign != null) {
				errorHandler.flag(token,INVALID_CONSTANT,this);
			}

			return value;
		}
		else if (definition == null) {
			errorHandler.flag(token,NOT_CONSTANT_IDENTIFIER,this);
			return null;
		}
		else {
			errorHandler.flag(token,INVALID_CONSTANT,this);
			return null;
		}
	}

	/**
	* Return the type of a constant given its value.
	*
	* @param value the constant's value
	* @return the type specification
	*/
	protected TypeSpec getConstantType(Object value)
	{
		TypeSpec constantType = null;

		if (value instanceof Integer) {
			constantType = Predefined.integerType;
		}
		else if (value instanceof Float) {
			constantType = Predefined.realType;
		}
		else if (value instanceof String) {
			if (((String) value).length() == 1) {
				constantType = Predefined.charType;
			}
			else {
				constantType = TypeFactory.
					createStringType((String) value);
			}
		}

		return constantType;
	}

	/**
	* Return the type of a constant given its identifier.
	*
	* @param identifier the constant's identifier
	* @return the type specification
	*/
	protected TypeSpec getConstantType(Token identifier)
	{
		SymTabEntry id = symTabStack.lookup(identifier.getText());

		if (id == null) {
			return null;
		}

		Definition definition = id.getDefinition();

		if (definition == CONSTANT ||
				definition == ENUMERATION_CONSTANT) {
			return id.getTypeSpec();
		}
		else {
			return null;
		}
	}
}

