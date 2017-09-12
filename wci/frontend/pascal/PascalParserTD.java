package wci.frontend.pascal;

import wci.message.Message;

import wci.intermediate.SymTabEntry;
import wci.intermediate.ICodeFactory;
import wci.intermediate.ICodeNode;

import wci.frontend.*;

import wci.frontend.pascal.parsers.StatementParser;

import static wci.frontend.pascal.PascalErrorCode.IO_ERROR;
import static wci.frontend.pascal.PascalErrorCode.MISSING_PERIOD;
import static wci.frontend.pascal.PascalErrorCode.UNEXPECTED_TOKEN;

import static wci.frontend.pascal.PascalTokenType.ERROR;
import static wci.frontend.pascal.PascalTokenType.IDENTIFIER;
import static wci.frontend.pascal.PascalTokenType.DOT;
import static wci.frontend.pascal.PascalTokenType.BEGIN;


import static wci.message.MessageType.PARSER_SUMMARY;
import static wci.message.MessageType.TOKEN;


/**
* <h1>PascalParserTD</h1>
*
* <p>The top-down Pascal parser.</p>
*/
public class PascalParserTD extends Parser
{

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
	// Parser methods

	/**
	* Parse the source and generate the symbol table and
	* intermediate code.
	*/
	@Override
	public void parse() throws Exception
	{
		long t0 = System.currentTimeMillis();
		iCode = ICodeFactory.createICode();

		try{
			Token token = nextToken();
			ICodeNode rootNode = null;

			// Look for the BEGIN token to parse a compound statement.
			if (token.getType() == BEGIN) {
				StatementParser statementParser =
					new StatementParser(this);
				rootNode = statementParser.parse(token);
				token = currentToken();
			}
			else {
				errorHandler.flag(token,UNEXPECTED_TOKEN,this);
			}

			// Look for the final period.
			if (token.getType() != DOT) {
				errorHandler.flag(token,MISSING_PERIOD,this);
			}
			token = currentToken();

			// Set the parse tree root node.
			if (rootNode != null) {
				iCode.setRoot(rootNode);
			}

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

