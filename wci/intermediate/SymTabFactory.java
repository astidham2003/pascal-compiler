package wci.intermediate;

import wci.intermediate.symtabimpl.*;

/**
* <h1>SymTabFactory</h1>
*
* <p>A factory for creating objects that implement the symbol table.
* </p>
*/
// XXX A nice benefit of hiding the implementations by returning only
// interfaces is that it allows the implementations to extend
// java.util classes like ArrayList and HashMap without the client
// being able to 'see' all of the superclass methods.
public final class SymTabFactory
{
	/**
	* Constructor
	*/
	private SymTabFactory()
	{
	}

	/**
	* Create and return a symbol table stack implementation.
	*
	* @return the symbol table implementation.
	*/
	public static SymTabStack createSymTabStack()
	{
		return new SymTabStackImpl();
	}

	/**
	* Create and return a symbol table implementation.
	*
	* @param nestingLevel the nesting level
	* @return the symbol table implementation
	*/
	public static SymTab createSymTab(int nestingLevel)
	{
		return new SymTabImpl(nestingLevel);
	}

	/**
	* Create and return a symbol table entry implementation.
	*
	* @param name the identifier
	* @param symTab the symbol table that contains this entry
	* @return the symbol table entry implementation
	*/
	public static SymTabEntry createSymTabEntry(
		String name,SymTab symTab)
	{
		return new SymTabEntryImpl(name,symTab);
	}

}

