package wci.frontend;

import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalScanner;

/**
* <h1>FrontendFactory</h1>
*
* <p>A factory class that creates parsers for specific source
* languages.</p>
*/
public class FrontendFactory
{

	/**
	* Create a parser.
	* @param language the name of the source language,e.g.
	* "Pascal"
	* @param type the type of parser, e.g. "top-down"
	* @param source the source object
	* @return the parser
	* @throws Exception
	*/
	public static Parser createParser(
		String language,String type,Source source) throws Exception
	{
		if (language.equalsIgnoreCase(Language.PASCAL) &&
				type.equalsIgnoreCase(Type.TOP_DOWN)) {
			Scanner s = new PascalScanner(source);
			return new PascalParserTD(s);
		}
		else if (!language.equalsIgnoreCase(Language.PASCAL)) {
			throw new Exception(
				String.format(
					"Parser factory: Invalid language '%s'",
					language));
		}
		else {
			throw new Exception(
				String.format(
					"Parser factory: Invalid type '%s'",
					type));
		}
	}

	// ----------------------------------------------------------------
	// XXX Not part of the book source listing.  Added so that I
	// wouldn't have to remember what strings were valid in the
	// createParser() method.

	public static class Language
	{
		public static final String PASCAL = "pascal";
	}

	public static class Type
	{
		public static final String TOP_DOWN = "top-down";
	}

}

