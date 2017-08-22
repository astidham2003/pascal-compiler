package wci.frontend.pascal;

import wci.frontend.*;
import wci.message.Message;

import static wci.frontend.pascal.PascalTokenType.*;
import static wci.frontend.pascal.PascalErrorCode.*;
import static wci.message.MessageType.SYNTAX_ERROR;

/**
* <h1>PascalErrorHandler</h1>
*
* <p>Error handler for Pascal syntax errors.</p>
*/
public class PascalErrorHandler
{
	private static final int MAX_ERRORS = 25;

	private static int errorCount = 0;

	/**
	* Flag an error in the source line.
	*
	* @param token the bad token
	* @param errorCode the error code
	* @param parser the parser
	*/
	public void flag(
		Token token,PascalErrorCode errorCode,Parser parser)
	{
		// Notify the parser's listeners.
		parser.sendMessage(
			new Message(
				SYNTAX_ERROR,
				new Object[] {
					token.getLineNumber(),
					token.getPosition(),
					token.getText(),
					errorCode.toString()
				}));

		if (++errorCount > MAX_ERRORS) {
			abortTranslation(TOO_MANY_ERRORS,parser);
		}
	}

	/**
	* Abort the translation.
	*
	* @param errorCode the error code
	* @param parser the parser
	*/
	public void abortTranslation(
		PascalErrorCode errorCode,Parser parser)
	{
		// Notify the parser's listners and then abort.
		String fatalTxt =
			String.format(
				"FATAL ERROR: %s",errorCode.toString());

		parser.sendMessage(
			new Message(
				SYNTAX_ERROR,
				new Object[] {
					0,
					0,
					"",
					fatalTxt
				}));

		System.exit(errorCode.getStatus());
	}

	public int getErrorCount()
	{
		return errorCount;
	}

}
