package wci.util;

import java.util.ArrayList;

import wci.intermediate.*;

import static wci.intermediate.symtabimpl.SymTabKeyImpl.*;

/**
* <h1>CrossReferencer</h1>
*
* <p>Generate a cross-reference listing</p>
*
* <p>XXX The below copyright was in the book, so I'm including it.</p>
* <p>Copyright(c) 2008 by Ronald Mak</p>
* <p>For instructional purposes only.  No warranties</p>
*/
public class CrossReferencer
{
	private static final int NAME_WIDTH = 16;

	private static final String NAME_FORMAT = "%-" + NAME_WIDTH + "s";
	private static final String NUMBERS_LABEL = " Line numbers    ";
	private static final String NUMBERS_UNDERLINE =
		" ------------    ";
	private static final String NUMBER_FORMAT = " %03d";

	private static final int LABEL_WIDTH = NUMBERS_LABEL.length();
	private static final int INDENT_WIDTH = NAME_WIDTH + LABEL_WIDTH;

	private static final StringBuilder INDENT =
		new StringBuilder(INDENT_WIDTH);
	static
	{
		for (int i = 0;i < INDENT_WIDTH; ++i)
		{
			INDENT.append(" ");
		}
	}

	/**
	* Print the cross-reference table.
	*
	* @param symTabStack the symbol table stack
	*/
	public void print(SymTabStack symTabStack)
	{
		System.out.println("\n==== CROSS-REFERENCE TABLE ====");
		printColumnHeadings();

		printSymTab(symTabStack.getLocalSymTab());
	}

	/**
	* Print the column headings.
	*/
	public void printColumnHeadings()
	{
		System.out.println();
		System.out.println(
			String.format(NAME_FORMAT,"Identifier") +
			NUMBERS_LABEL);
		System.out.println(
			String.format(NAME_FORMAT,"----------") +
			NUMBERS_UNDERLINE);
	}

	/**
	* Print the entries in a symbol table.
	*
	* @param SymTab the symbol table.
	*/
	private void printSymTab(SymTab symTab)
	{
		// Loop over the sorted list of symbol table entries.
		ArrayList<SymTabEntry> sorted = symTab.sortedEntries();
		for (SymTabEntry e : sorted) {
			System.out.print(String.format(NAME_FORMAT,e.getName()));

			ArrayList<Integer> lineNumbers = e.getLineNumbers();
			if (lineNumbers != null) {
				for (Integer lineNumber : lineNumbers) {
					System.out.print(
						String.format(NUMBER_FORMAT,lineNumber));
				}
			}

			System.out.println();
		}
	}

}

