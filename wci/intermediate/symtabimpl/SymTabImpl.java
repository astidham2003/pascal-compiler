package wci.intermediate.symtabimpl;

import wci.intermediate.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

/**
* <h1>SymTabImpl</h1>
*
* <p>An implementation of the symbol table.</p>
*/
public class SymTabImpl
	extends TreeMap<String,SymTabEntry>
	implements SymTab
{
	// XXX Don't worry about this until chapter 9.
	private int nestingLevel;

	/**
	* Constructor.
	*/
	public SymTabImpl(int nestingLevel)
	{
		this.nestingLevel = nestingLevel;
	}

	// ----------------------------------------------------------------
	// SymTab methods

	@Override
	public int getNestingLevel()
	{
		return nestingLevel;
	}

	/**
	* Create and enter a new entry into the symbol table.
	*
	* @param name the name of the entry
	* @return the new entry
	*/
	@Override
	@SuppressWarnings("unchecked")
	public SymTabEntry enter(String name)
	{
		SymTabEntry e = SymTabFactory.createSymTabEntry(name,this);
		put(name,e);
		return e;
	}


	/**
	* Look up an existing symbol table entry.
	*
	* @param name the name of the entry
	* @return the entry, or null if it does not exist
	*/
	@Override
	public SymTabEntry lookup(String name)
	{
		return get(name);
	}

	/**
	* @return a list of symbol table entries sorted by name
	*/
	@Override
	public ArrayList<SymTabEntry> sortedEntries()
	{
		// XXX Seems a little convoluted to build the list this
		// way.  Why not return new ArrayList(values())?
		Collection<SymTabEntry> vs = values();
		Iterator<SymTabEntry> iter = vs.iterator();
		ArrayList<SymTabEntry> l = new ArrayList<SymTabEntry>(size());
		while (iter.hasNext()) {
			l.add(iter.next());
		}

		return l;
	}

}

