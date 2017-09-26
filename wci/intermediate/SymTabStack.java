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
	public SymTabEntry enterLocal(String name);

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
	* @param name the name of the entry
	* @return the entry, or null if it doesn't exist
	*/
	public SymTabEntry lookup(String name);

	/**
	* Setter.
	*
	* @param entry the symbol table entry for the main program
	* identifier
	*/
	public void setProgramId(SymTabEntry entry);

	/**
	* Getter.
	*
	* @retrun the symbol table entry for the main program identifier
	*/
	public SymTabEntry getProgramId();

	/**
	* Push a new symbol table onto the stack.
	*
	* @return the pushed symbol table
	*/
	public SymTab push();

	/**
	* Push a symbol table onto the stack.
	*
	* @param symTab the symbol table to push
	* @return the pushed symbol table
	*/
	public SymTab push(SymTab symTab);

	/**
	* Pop a symbol table off the stack.
	*
	* @return the popped symbol table
	*/
	public SymTab pop();
}

