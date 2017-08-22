package wci.intermediate.symtabimpl;

import wci.intermediate.*;
import java.util.*;

/**
* <h1>SymTabImpl</h1>
*/
public class SymTabImpl implements SymTab
{
	private int nestingLevel;
	private HashTable<String,SymTabEntry> entries;

	/**
	* Constructor.
	*/
	public SymTabImpl()
	{
		entries = new HashTable<String,SymTabEntry>();
	}

	@Override
	public int getNestingLevel()
	{
		return nestingLevel;
	}

	@Override
	public SymTabEntry enter(String name)
	{
		// XXX How to document this exception?
		if (entries.containsKey(name)) {
			throw new IllegalArgumentException(
				String.format(
					"Symbol table already contains entry: '%s'",
					name));
		}

		entries.put(name, new SymTabEntryImpl(name,this));
	}

	@Override
	public ArrayList<SymTabEntry> sortedEntries()
	{
		return new ArrayList<SymTabEntry>(entries.values()).
			sorted(Comparator.comparing(SymTabEntry::getName));
	}

}

