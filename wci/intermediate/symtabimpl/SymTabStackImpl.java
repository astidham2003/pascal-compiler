package wci.intermediate.symtabimpl;

import wci.intermediate.*;

import java.util.ArrayList;

/**
* <h1>SymTabStackImpl</h1>
*/
public class SymTabStackImpl
	extends ArrayList<SymTab>
	implements SymTabStack
{
	// XXX Don't worry about this until chapter 9.
	private int currentNestingLevel; // Current scope nesting level.
	private SymTabEntry programId;	// Entry for the main program id.

	/**
	* Constructor.
	*/
	public SymTabStackImpl()
	{
		currentNestingLevel = 0;
		add(SymTabFactory.createSymTab(currentNestingLevel));
	}

	// ----------------------------------------------------------------

	/**
	* Getter.
	*
	* @return the program identifier
	*/
	@Override
	public SymTabEntry getProgramId()
	{
		return programId;
	}

	/**
	* Setter.
	*
	* @param programId the main program id
	*/
	@Override
	public void setProgramId(SymTabEntry programId)
	{
		this.programId = programId;
	}

	@Override
	public SymTab getLocalSymTab()
	{
		return get(currentNestingLevel);
	}

	@Override
	public int getCurrentNestingLevel()
	{
		return currentNestingLevel;
	}

	@Override
	public SymTabEntry enterLocal(String name)
	{
		return get(currentNestingLevel).enter(name);
	}

	@Override
	public SymTabEntry lookupLocal(String name)
	{
		return get(currentNestingLevel).lookup(name);
	}

	/**
	* Look up an existing symbol table entry throughout the stack.
	*
	* @param name the name of the entry
	* @return the entry, or null if it does not exist
	*/
	@Override
	public SymTabEntry lookup(String name)
	{
		SymTabEntry foundEntry = null;

		// Search the current and enclosing scopes.
		for (int i = currentNestingLevel;
				i >= 0 && foundEntry == null; --i) {

			foundEntry = get(i).lookup(name);
		}

		return foundEntry;
	}

	/**
	* Push a new symbol table onto the symbol table stack.
	*
	* @return the pushed symbol table
	*/
	public SymTab push()
	{
		SymTab symTab =
			SymTabFactory.createSymTab(++currentNestingLevel);
		add(symTab);

		return symTab;
	}


	/**
	* Push a symbol table onto the symbol table stack.
	*
	* @return the pushed symbol table.
	*/
	@Override
	public SymTab push(SymTab symTab)
	{
		++currentNestingLevel;
		add(symTab);

		return symTab;
	}

	/**
	* Pop a symbol table off the symbol table stack.
	*
	* @return the popped symbol table
	*/
	@Override
	public SymTab pop()
	{
	SymTab symTab = get(currentNestingLevel);
	remove(currentNestingLevel--);

	return symTab;
	}
}

