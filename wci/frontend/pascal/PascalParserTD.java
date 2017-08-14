package wci.frontend.pascal;

import wci.frontend.*;
import wci.message.Message;

import static wci.frontend.pascal.PascalErrorCode.IO_ERROR;
import static wci.message.MessageType.PARSER_SUMMARY;
import static wci.message.MessageType.TOKEN;
import static wci.frontend.pascal.PascalTokenType.ERROR;


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
				if (tokenType != ERROR) {
					// Format each token.
					sendMessage(
						new Message(
							TOKEN,
							new Object[] {
								token.getLineNumber(),
								token.getPosition(),
								tokenType,
								token.getText(),
								token.getValue()
							}));
				}

				else {
					errorHandler.flag(
						token,
						(PascalErrorCode) token.getValue(),
						this);
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

