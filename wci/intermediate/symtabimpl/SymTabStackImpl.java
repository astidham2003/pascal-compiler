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

	/**
	* Constructor.
	*/
	public SymTabStackImpl()
	{
		currentNestingLevel = 0;
		add(SymTabFactory.createSymTab(currentNestingLevel));
	}

	// ----------------------------------------------------------------
	// SymTabStack methods

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

	@Override
	public SymTabEntry lookup(String name)
	{
		return lookupLocal(name);
	}

}

