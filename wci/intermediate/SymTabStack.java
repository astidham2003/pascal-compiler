package wci.intermediate;

/**
* <h1>SymTabStack</h1>
*
* <p>The interface for the symbol table.</p>
*/
public interface SymTabStack
{
	/**
	* @return the current nesting level
	*/
	public int getCurrentNestingLevel();

	/**
	* @return the local symbol table from the top of the symbol stack
	*/
	public SymTab getLocalSymTab();

	/**
	* Create and enter a new entry into the local symbol table.
	*
	* @return the new entry
	*/
	public SymTabEntry enterLocal();

	/**
	* Look up an existing symbol table entry in the local symbol table.
	*
	* @param name the name of the entry
	* @return the entry, or null if it does not exist
	*/
	public SymTabEntry lookupLocal(String name);

	/**
	* Look up an existing symbol table entry throughout the stack.
	*
	* @param name the nmae of the entry
	* @return the entry, or null if it doesn't exist
	*/
	public SymTabEntry lookup(String name);
}

