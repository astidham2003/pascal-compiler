package wci.frontend.pascal.tokens;

import wci.frontend.pascal.*;
import wci.frontend.*;

import static wci.frontend.pascal.PascalTokenType.SPECIAL_SYMBOLS;
import static wci.frontend.pascal.PascalTokenType.ERROR;
import static wci.frontend.pascal.PascalErrorCode.INVALID_CHARACTER;

public class PascalSpecialSymbolToken extends PascalToken
{

	public PascalSpecialSymbolToken(Source source) throws Exception
	{
		super(source);
	}

	// ----------------------------------------------------------------
	// Token methods

	/**
	* Extract the special symbol token.
	*
	* @throws Exception
	*/
	@Override
	protected void extract() throws Exception
	{
		char currentChar = currentChar();

		text = Character.toString(currentChar);
		type = null;

		if (currentChar == '+' || currentChar == '-' ||
				currentChar == '*' || currentChar == '/' ||
				currentChar == ',' || currentChar == ';' ||
				currentChar == '\'' || currentChar == '=' ||
				currentChar == '(' || currentChar == ')' ||
				currentChar == '[' || currentChar == ']' ||
				currentChar == '{' || currentChar == '}' ||
				currentChar == '^') {
			nextChar();
		}
		// : or :=
		else if (currentChar == ':') {
			nextChar();

			if (currentChar == '=') {
				text += currentChar;
				nextChar();
			}
		}
		// < or <= or <>
		else if (currentChar == '<') {
			currentChar = nextChar();

			// XXX Seems like we could || these in one
			// if-statement.  Maybe they need to separate for later?
			if (currentChar == '=') {
				text += currentChar;
				nextChar();
			}

			else if (currentChar == '>') {
				text += currentChar;
				nextChar();
			}
		}
		// > or >=
		else if (currentChar == '>') {
			currentChar = nextChar();

			if (currentChar == '=') {
				text += currentChar;
				nextChar();
			}
		}
		// . or ..
		else if (currentChar == '.') {
			currentChar = nextChar();

			if (currentChar == '.') {
				text += currentChar;
				nextChar();
			}
		}

		else {
			nextChar(); // Consume bad character.
			type = ERROR;
			value = INVALID_CHARACTER;
		}

		if (type == null) {
			type = SPECIAL_SYMBOLS.get(text);
		}

	}

}

