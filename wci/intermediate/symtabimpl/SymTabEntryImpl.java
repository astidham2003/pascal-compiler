package wci.intermediate.symtabimpl;

import java.util.ArrayList;
import java.util.HashMap;

import wci.intermediate.*;

/**
* <h1>SymTabEntryImpl</h1>
*
* <p>An entry in the symbol table.</p>
*/
public class SymTabEntryImpl
	extends HashMap
	implements SymTabEntry
{
	private Definition definition; // How the identifier is defined.
	private TypeSpec typeSpec; // Type specification.

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
	* Setter.
	*
	* @param definition the definition to set
	*/
	@Override
	public void setDefinition(Definition definition)
	{
		this.definition = definition;
	}

	/**
	* Getter.
	*
	* @return the definition
	*/
	@Override
	public Definition getDefinition()
	{
		return definition;
	}

	/**
	* Getter.
	*
	* @return the type specification
	*/
	@Override
	public TypeSpec getTypeSpec()
	{
		return typeSpec;
	}

	/**
	* Setter.
	*
	* @param typeSpec the typeSpec to set
	*/
	@Override
	public void setTypeSpec(TypeSpec typeSpec)
	{
		this.typeSpec = typeSpec;
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

