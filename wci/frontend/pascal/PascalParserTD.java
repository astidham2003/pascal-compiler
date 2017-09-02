package wci.frontend.pascal;

import wci.frontend.*;
import wci.message.Message;
import wci.intermediate.SymTabEntry;

import static wci.frontend.pascal.PascalErrorCode.IO_ERROR;
import static wci.message.MessageType.PARSER_SUMMARY;
import static wci.message.MessageType.TOKEN;
import static wci.frontend.pascal.PascalTokenType.ERROR;
import static wci.frontend.pascal.PascalTokenType.IDENTIFIER;


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
	* @param scanner the scanner to be used with this parser
	*/
	public PascalParserTD(Scanner scanner)
	{
		super(scanner);
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
		Token token;
		long t0 = System.currentTimeMillis();

		try{
			while (!((token = nextToken()) instanceof EofToken)) {
				TokenType tokenType = token.getType();

				// Cross reference only the identifiers.
				if (tokenType == IDENTIFIER) {
					String name = token.getText().toLowerCase();

					// If it's not already in the symbol table,
					// create and enter a new identifier.
					SymTabEntry entry = symTabStack.lookup(name);
					if (entry == null) {
						entry = symTabStack.enterLocal(name);
					}

					// Append the current line number to the entry.
					entry.appendLineNumber(token.getLineNumber());
				}

				else if (tokenType == ERROR) {
					errorHandler.flag(
						token,(PascalErrorCode)token.getValue(),this);
				}
			}

			// Send the parser summary message.
			float elapsedTime = (System.currentTimeMillis() - t0)/1000f;
			sendMessage(
				new Message(
					PARSER_SUMMARY,
					new Number[]
						{
						token.getLineNumber(),
						getErrorCount(),
						elapsedTime
						}));
		}
		catch (java.io.IOException e) {
			errorHandler.abortTranslation(IO_ERROR,this);
		}
	}

	/**
	* The number of errors found by this parser.
	* @return the number of syntax errors found by the parser
	*/
	@Override
	public int getErrorCount()
	{
		return errorHandler.getErrorCount();
	}

}

