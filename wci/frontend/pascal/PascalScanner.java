package wci.frontend.pascal;

import wci.frontend.*;
import static wci.frontend.Source.EOF;

/**
* <h1>PascalScanner</h1>
* 
* <p>The Pascal scanner.</p>
*/
public class PascalScanner extends Scanner
{

	/**
	* Constructor.
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
	*/
	@Override
	protected Token extractToken() throws Exception
	{
		Token token;
		char currentChar = currentChar();

		// Construct the next token.
		// The current character determines the token type.
		if (currentChar == EOF) {
			token = new EofToken(source);
		}
		else {
			// Constructing a token advances the source position
			// by 1.
			token = new Token(source);
		}

		return token;
	}

}

