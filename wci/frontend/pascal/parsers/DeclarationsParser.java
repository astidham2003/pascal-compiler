package wci.frontend.pascal.parsers;

import java.util.EnumSet;

import wci.frontend.Token;

import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;

import wci.intermediate.ICodeNode;

import static wci.frontend.pascal.PascalTokenType.*;

import static wci.intermediate.symtabimpl.DefinitionImpl.VARIABLE;

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

	static final EnumSet<PascalTokenType> ROUTINE_START_SET =
		VAR_START_SET.clone();

	static {
		TYPE_START_SET.remove(CONST);
		VAR_START_SET.remove(TYPE);
		ROUTINE_START_SET.remove(VAR);
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
	* To be overridden by the specialized declarations parser
	* subclasses.
	*
	* @param token the initial token
	* @return the root node of the parse tree
	*/
	public void parse(Token token) throws Exception
	{
		token = synchronize(DECLARATION_START_SET);

		if (token.getType() == CONST) {
			token = nextToken(); // Consume the CONST token.

			ConstantDefinitionsParser constantDefinitionsParser =
				new ConstantDefinitionsParser(this);
			constantDefinitionsParser.parse(token);
		}

		token = synchronize(TYPE_START_SET);

		if (token.getType() == TYPE) {
			token = nextToken();  // Consume the TYPE token.

			TypeDefinitionsParser typeDefinitionsParser =
				new TypeDefinitionsParser(this);
			typeDefinitionsParser.parse(token);
		}

		token = synchronize(VAR_START_SET);

		if (token.getType() == VAR) {
			token = nextToken(); // Consume the VAR token.

			VariableDeclarationsParser variableDeclarationsParser =
				new VariableDeclarationsParser(this);
			variableDeclarationsParser.setDefinition(VARIABLE);
			variableDeclarationsParser.parse(token);
		}

		token = synchronize(ROUTINE_START_SET);
	}
}

