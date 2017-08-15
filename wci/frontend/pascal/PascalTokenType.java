package wci.frontend.pascal;

import java.util.Hashtable;
import java.util.HashSet;

import wci.frontend.TokenType;

/**
* <h1>PascalTokenType</h1>
*
* <p>Pascal token types.</p>
*/
public enum PascalTokenType implements TokenType
{
	// Reserved words.
	AND, ARRAY, BEGIN, CASE, CONST, DIV, DO, DOWNTO, ELSE, END,
	FILE, FOR, FUNCTION, GOTO, IF, IN, LABEL, MOD, NIL, NOT,
	OF, OR, PACKED, PROCEDURE, PROGRAM, RECORD, REPEAT, SET,
	THEN, TO, TYPE, UNTIL, VAR, WHILE, WITH,

	// Special symbols.
	PLUS("+"), MINUS("-"), STAR("*"), SLASH("/"),
	COLON_EQUALS(":="), DOT("."), COMMA(","), SEMICOLON(";"),
	COLON(":"), QUOTE("'"), EQUALS("="), NOT_EQUALS("<>"),
	LESS_THAN("<"), LESS_EQUALS("<="), GREATER_EQUALS(">="),
	GREATER_THAN(">"), LEFT_PAREN("("), RIGHT_PAREN(")"),
	LEFT_BRACKET("["), RIGHT_BRACKET("]"), LEFT_BRACE("{"),
	RIGHT_BRACE("}"), UP_ARROW("^"), DOT_DOT(".."),

	IDENTIFIER, INTEGER, REAL, STRING, ERROR, END_OF_FILE;

	private String text;

	private static final int FIRST_RESERVED_INDEX = AND.ordinal();
	private static final int LAST_RESERVED_INDEX = WITH.ordinal();
	private static final int FIRST_SPECIAL_INDEX = PLUS.ordinal();
	private static final int LAST_SPECIAL_INDEX = DOT_DOT.ordinal();

	// Set of lowercase Pascal reserved word text strings.
	public static HashSet<String> RESERVED_WORDS;
	// Hashtable of Pascal special symbols.  Each symbol's text
	// is the key to its Pascal token type.
	// XXX Hashtable vs HashMap?  Hashtable is synchronized and
	// XXX doesn't allow nulls.
	public static Hashtable<String,PascalTokenType> SPECIAL_SYMBOLS;

	static {
		RESERVED_WORDS = new HashSet<String>();
		PascalTokenType[] values = PascalTokenType.values();
		for (int i = FIRST_RESERVED_INDEX;
				i <= LAST_RESERVED_INDEX; ++i) {
			RESERVED_WORDS.add(values[i].getText().toLowerCase());
		}

		SPECIAL_SYMBOLS = new Hashtable<String,PascalTokenType>();
		for (int i = FIRST_SPECIAL_INDEX;
				i <= LAST_SPECIAL_INDEX; ++i) {
			SPECIAL_SYMBOLS.put(values[i].getText(),values[i]);
		}
	}

	/**
	* Constructor.
	*/
	PascalTokenType()
	{
		text = toString().toLowerCase();
	}

	/**
	* Constructor.
	*
	* @param text the token text
	*/
	PascalTokenType(String text)
	{
		this.text = text;
	}

	/**
	* Getter.
	*
	* @return the token text
	*/
	public String getText()
	{
		return text;
	}

}

