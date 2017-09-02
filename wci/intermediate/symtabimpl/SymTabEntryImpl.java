package wci.intermediate.symtabimpl;

import wci.intermediate.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
* <h1>SymTabEntryImpl</h1>
*
* <p>An entry in the symbol table.</p>
*/
public class SymTabEntryImpl
	extends HashMap
	implements SymTabEntry
{
	private final String name; // Entry name.
	private final SymTab symTab; // Parent symbol table.
	private final ArrayList<Integer> lineNumbers; // Source line nums.

	/**
	* Constructor.
	*
	* @param name the name of the entry.
	* @param symTab the symbol table holding this entry.
	*/
	public SymTabEntryImpl(String name,SymTab symTab)
	{
		lineNumbers = new ArrayList<Integer>();
		this.name = name;
		this.symTab = symTab;
	}

	// ----------------------------------------------------------------
	// SymTabEntry methods

	/**
	* @return the name of the entry
	*/
	@Override
	public String getName()
	{
		return name;
	}

	/**
	* @return the symbol table that contains this entry
	*/
	@Override
	public SymTab getSymTab()
	{
		return symTab;
	}

	/**
	* Set the value of an attribute key.
	*
	* @param key the attribute key
	* @param value the attribute value
	* @return the attribute value
	*/
	@Override
	@SuppressWarnings("unchecked")
	public void setAttribute(SymTabKey key,Object value)
	{
		put(key,value);
	}

	/**
	* Get the value of an attribute key.
	*
	* @param key the attribute key
	* @return the attribute value
	*/
	@Override
	public Object getAttribute(SymTabKey key)
	{
		return get(key);
	}

	/**
	* Append a source line number to the entry.
	*
	* @param lineNumber the lineNumber to append
	*/
	@Override
	public void appendLineNumber(int lineNumber)
	{
		lineNumbers.add(lineNumber);
	}

	/**
	* @return the list of source line numbers
	*/
	@Override
	public ArrayList<Integer> getLineNumbers()
	{
		// XXX Should we return a copy of the line number list instead?
		return lineNumbers;
	}

}

