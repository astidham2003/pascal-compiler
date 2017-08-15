package wci.frontend.pascal.tokens;

import wci.frontend.pascal.*;
import wci.frontend.*;

import static wci.frontend.pascal.PascalTokenType.*;

public class PascalWordToken extends PascalToken
{

	/**
	* Constructor.
	*
	* @param source the source from where to fetch this token's
	* characters.
	*/
	public PascalWordToken(Source source) throws Exception
	{
		super(source);
	}

	// ----------------------------------------------------------------
	// Token methods

	/**
	* Extract a Pascal word token from the source.
	*
	* @throws Exception
	*/
	@Override
	protected void extract() throws Exception
	{
		StringBuilder sb = new StringBuilder();
		char currentChar = currentChar();

		// Get the word characters (letter or digit).  The
		// scanner has already determined that the first
		// character is a letter.
		while (Character.isLetterOrDigit(currentChar)) {
			sb.append(currentChar);
			currentChar = nextChar();
		}

		text = sb.toString();

		// Is it a reserved word or an identifier?
		type = (RESERVED_WORDS.contains(text.toLowerCase())) ?
			PascalTokenType.valueOf(text.toUpperCase()) :
			IDENTIFIER;
	}

}

