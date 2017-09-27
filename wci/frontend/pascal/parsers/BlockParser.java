package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.TokenType;

import wci.frontend.pascal.PascalParserTD;

import wci.intermediate.ICodeNode;
import wci.intermediate.ICodeFactory;
import wci.intermediate.SymTabEntry;

import static wci.frontend.pascal.PascalErrorCode.MISSING_END;
import static wci.frontend.pascal.PascalErrorCode.MISSING_BEGIN;

import static wci.frontend.pascal.PascalTokenType.BEGIN;
import static wci.frontend.pascal.PascalTokenType.END;

import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.COMPOUND;

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
		DeclarationsParser declarationsParser =
			new DeclarationsParser(this);
		StatementParser statementParser =
			new StatementParser(this);

		// Parse any declarations.
		declarationsParser.parse(token);

		token = synchronize(StatementParser.STMT_START_SET);
		TokenType tokenType = token.getType();
		ICodeNode rootNode = null;

		// Look for the BEGIN token to parse a compound statement.
		if (tokenType == BEGIN) {
			rootNode = statementParser.parse(token);
		}

		// Missing BEGIN: Attempt to parse anyway if possible.
		else {
			errorHandler.flag(token,MISSING_BEGIN,this);

			if(StatementParser.STMT_START_SET.contains(tokenType)) {
				rootNode = ICodeFactory.createICodeNode(COMPOUND);
				statementParser.parseList(
					token,rootNode,END,MISSING_END);
			}
		}

		return rootNode;
	}
}
