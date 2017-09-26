package wci.frontend.pascal;

import java.util.EnumSet;

import wci.frontend.*;

import wci.frontend.pascal.parsers.StatementParser;
import wci.frontend.pascal.parsers.BlockParser;

import wci.intermediate.ICodeFactory;
import wci.intermediate.ICodeNode;
import wci.intermediate.SymTabEntry;

import wci.intermediate.symtabimpl.Predefined;
import wci.intermediate.symtabimpl.DefinitionImpl;


import wci.message.Message;

import static wci.frontend.pascal.PascalTokenType.BEGIN;
import static wci.frontend.pascal.PascalTokenType.DOT;
import static wci.frontend.pascal.PascalTokenType.ERROR;
import static wci.frontend.pascal.PascalTokenType.IDENTIFIER;
import static wci.frontend.pascal.PascalErrorCode.IO_ERROR;
import static wci.frontend.pascal.PascalErrorCode.MISSING_PERIOD;
import static wci.frontend.pascal.PascalErrorCode.UNEXPECTED_TOKEN;

import static wci.intermediate.symtabimpl.SymTabKeyImpl.ROUTINE_ICODE;
import static wci.intermediate.symtabimpl.SymTabKeyImpl.ROUTINE_SYMTAB;

import static wci.message.MessageType.PARSER_SUMMARY;
import static wci.message.MessageType.TOKEN;

/**
* <h1>PascalParserTD</h1>
*
* <p>The top-down Pascal parser.</p>
*/
public class PascalParserTD extends Parser
{

	private SymTabEntry routineId; // Name of the routine being parsed.

	protected static PascalErrorHandler errorHandler =
		new PascalErrorHandler();

	/**
	* Constructor.
	*
	* @param scanner the source scanner
	*/
	public PascalParserTD(Scanner scanner)
	{
		super(scanner);
	}

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public PascalParserTD(PascalParserTD parent)
	{
		super(parent.getScanner());
	}

	// ----------------------------------------------------------------

	/**
	* Synchronize the parser.
	* 
	* @param syncSet the set of token types for synchronizing the
	* parser
	* @return the token where the parser has synchronized
	* @throws Exception if an error occurred
	*/
	public Token synchronize(EnumSet syncSet) throws Exception
	{
		Token token = currentToken();

		// If the current token is not in the sync set, then it is
		// unexpected and the parser must recover.
		if (!syncSet.contains(token.getType())) {
			// Flag the unexpected token.
			errorHandler.flag(token,UNEXPECTED_TOKEN,this);

			// Recover by skipping tokens that are not in the
			// sync set.
			do {
				token = nextToken();
			} while(!(token instanceof EofToken) &&
				!syncSet.contains(token.getType()));
		}

		return token;
	}

	// ----------------------------------------------------------------

	/**
	* Parse the source and generate the symbol table and
	* intermediate code.
	*
	* @throws Exception
	*/
	@Override
	public void parse() throws Exception
	{
		long t0 = System.currentTimeMillis();
		iCode = ICodeFactory.createICode();
		Predefined.initialize(symTabStack);

		// Create a dummy program idenifier symbol table entry.
		routineId =
			symTabStack.enterLocal("DummyProgramName".toLowerCase());
		routineId.setDefinition(DefinitionImpl.PROGRAM);
		symTabStack.setProgramId(routineId);

		// Push a new symbol table onto the symbol table stack and set
		// the routine's symbol table and intermediate code.
		routineId.setAttribute(ROUTINE_SYMTAB,symTabStack.push());
		routineId.setAttribute(ROUTINE_ICODE,iCode);

		BlockParser blockParser = new BlockParser(this);

		try{
			Token token = nextToken();

			// Parse a block.
			ICodeNode rootNode = blockParser.parse(token,routineId);
			iCode.setRoot(rootNode);
			symTabStack.pop();

			// Look for the final period.
			token = currentToken();
			if (token.getType() != DOT) {
				errorHandler.flag(token,MISSING_PERIOD,this);
			}
			token = currentToken();

			// Send the parser summary message.
			float elapsedTime =
				(System.currentTimeMillis() - t0) / 1000f;
			sendMessage(
				new Message(
					PARSER_SUMMARY,
					new Number[] {
						token.getLineNumber(),
						getErrorCount(),
						elapsedTime}));
		}
		catch (java.io.IOException e) {
			errorHandler.abortTranslation(IO_ERROR,this);
		}
	}

	/**
	* The number of errors found by this parser.
	*
	* @return the number of syntax errors found by the parser
	*/
	@Override
	public int getErrorCount()
	{
		return errorHandler.getErrorCount();
	}

}

