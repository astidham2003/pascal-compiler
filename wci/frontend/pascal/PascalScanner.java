package wci.frontend.pascal;

import wci.frontend.*;
import wci.frontend.pascal.tokens.*;

import static wci.frontend.Source.EOF;
import static wci.frontend.pascal.PascalErrorCode.INVALID_CHARACTER;
import static wci.frontend.pascal.PascalTokenType.*;

/**
* <h1>PascalScanner</h1>
* 
* <p>The Pascal scanner.</p>
*/
public class PascalScanner extends Scanner
{

	/**
	* Constructor.
	*
	* @param source the source to be used with this scanner
	*/
	public PascalScanner(Source source)
	{
		super(source);
	}

	// ----------------------------------------------------------------
	// Scanner method

	/**
	* Extract and return the next token.
	*
	* @return the next token
	* @throws Exception
	*/
	@Override
	protected Token extractToken() throws Exception
	{
		skipWhiteSpace();

		Token token;
		char currentChar = currentChar();

		// Construct the next token.
		// The current character determines the token type.
		if (currentChar == EOF) {
			token = new EofToken(source,END_OF_FILE);
		}

		else if (Character.isLetter(currentChar)) {
			token = new PascalWordToken(source);
		}

		else if (Character.isDigit(currentChar)) {
			token = new PascalNumberToken(source);
		}

		else if (currentChar == '\'') {
			token = new PascalStringToken(source);
		}

		else if (PascalTokenType.SPECIAL_SYMBOLS
				.containsKey(Character.toString(currentChar))) {
			token = new PascalSpecialSymbolToken(source);
		}

		else {
			token = new PascalErrorToken(
				source,
				INVALID_CHARACTER,
				Character.toString(currentChar));
			nextChar();
		}

		return token;
	}

	/**
	* Skip whitespace characters by consuming them.  A comment
	* is whitespace.
	*
	* @throws Exception
	*/
	private void skipWhiteSpace() throws Exception
	{
		char currentChar = currentChar();

		while (Character.isWhitespace(currentChar) ||
			currentChar == '{') {

			// Start of a comment?
			if (currentChar == '{') {
				do {
					currentChar = nextChar();
				} while (currentChar != '}' && currentChar != EOF);

				// Found closing '}'?
				if (currentChar == '}') {
					currentChar = nextChar();
				}
			}

			else {
				currentChar = nextChar();
			}
		}
	}

}

