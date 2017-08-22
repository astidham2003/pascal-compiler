package wci.intermediate.symtabimpl;

import wci.intermediate.*;

import java.util.Stack;

/**
* <h1>SymTabStackImpl</h1>
*/
// XXX Just taking a stab at things, here.
// Will have to change a lot, I'm sure.
public class SymTabStackImpl implements SymTabStack
{
	private int currentNestingLevel;
	private Stack<SymTab> symTabStack;

	/**
	* Constructor.
	*/
	public SymTabStackImpl()
	{
		symTabStack = new Stack<SymTab>();
	}

	@Override
	public SymTab getLocalSymTab()
	{
		// XXX Or should this be pop?
		return symTabStack.peek();
	}

	@Override
	public int getCurrentNestingLevel()
	{
		return currentNestingLevel;
	}

	@Override
	public SymTabEntry enterLocal(String name)
	{
		return symTabStack.peek().enter(
				new SymTabEntryImpl(
						name,
						symTabStack.peek()));
	}

	@Override
	public SymTabEntry lookupLocal(String name)
	{
		return symTabStack.peek().lookup(name);
	}

	@Override
	public SymTabEntry lookup(String name)
	{
		Iterator iter = symTabStack.iterator();
		SymTabEntry entry = null;
		while (iter.hasNext() && entry == null)
		{
			entry = iter.next().lookup(name);
		}

		return entry;
	}

}

