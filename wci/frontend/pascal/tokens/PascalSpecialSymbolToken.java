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
			type = SPECIAL_SYMBOLS.get(text);
		}
		// : or :=
		else if (currentChar == ':') {
			currentChar = nextChar();

			if (currentChar == '=') {
				text += currentChar;
				nextChar();
			}
			type = SPECIAL_SYMBOLS.get(text);
		}
		// < or <= or <>
		else if (currentChar == '<') {
			currentChar = nextChar();

			if (currentChar == '=') {
				text += currentChar;
				nextChar();
			}

			else if (currentChar == '>') {
				text += currentChar;
				nextChar();
			}
			type = SPECIAL_SYMBOLS.get(text);
		}
		// > or >=
		else if (currentChar == '>') {
			currentChar = nextChar();

			if (currentChar == '=') {
				text += currentChar;
				nextChar();
			}
			type = SPECIAL_SYMBOLS.get(text);
		}
		// . or ..
		else if (currentChar == '.') {
			currentChar = nextChar();

			if (currentChar == '.') {
				text += currentChar;
				nextChar();
			}
			type = SPECIAL_SYMBOLS.get(text);
		}

		else {
			nextChar(); // Consume bad character.
			type = ERROR;
			value = INVALID_CHARACTER;
		}

	}

}

