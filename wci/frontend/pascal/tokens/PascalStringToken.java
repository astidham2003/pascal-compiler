package wci.frontend.pascal.tokens;

import wci.frontend.pascal.*;
import wci.frontend.*;

import static wci.frontend.Source.EOF;
import static wci.frontend.pascal.PascalErrorCode.UNEXPECTED_EOF;
import static wci.frontend.pascal.PascalTokenType.*;

public class PascalStringToken extends PascalToken
{

	public PascalStringToken(Source source) throws Exception
	{
		super(source);
	}

	// ----------------------------------------------------------------
	// Token methods

	/**
	* Extract a Pascal string token from the source.
	*
	* @throws Exception
	*/
	@Override
	protected void extract() throws Exception
	{
		StringBuilder textBuffer = new StringBuilder();
		StringBuilder valueBuffer = new StringBuilder();


		char currentChar = nextChar(); // Consume initial quote
		textBuffer.append('\'');

		do {
			// Replace any whitespace character with a blank
			if (Character.isWhitespace(currentChar)) {
				currentChar = ' ';
			}

			if (currentChar != '\'' && currentChar != EOF) {
				textBuffer.append(currentChar);
				valueBuffer.append(currentChar);
				currentChar = nextChar();
			}

			// Quote?  Each pair of adjacent quotes represents a
			// single-quote.
			if (currentChar == '\'') {
				while (currentChar == '\'' && peekChar() == '\'') {
					textBuffer.append("''");
					valueBuffer.append(currentChar);
					currentChar = nextChar();
					// currentChar is now char after last '.
					currentChar = nextChar();
				}
			}
		} while (currentChar != '\'' && currentChar != EOF);

		if (currentChar == '\'') {
			nextChar();
			textBuffer.append('\'');

			type = STRING;
			value = valueBuffer.toString();
		}

		else {
			type = ERROR;
			value = UNEXPECTED_EOF;
		}

		text = textBuffer.toString();
	}

}

