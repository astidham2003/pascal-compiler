package wci.intermediate;

/**
* <h1>SymTabEntry</h1>
*
* <p>The interface for a symbol table entry.</p>
*/
public interface SymTabEntry
{
	/**
	* @return the name of the entry
	*/
	public String getName();

	/**
	* @return the symbol table that contains this entry
	*/
	public SymTab getSymTab();

	/**
	* Append a source line number to the entry.
	*
	* @param lineNumber the lineNumber to append
	*/
	public void appendLineNumber(int lineNumber);

	/**
	* @return the list of source line numbers
	*/
	public java.util.ArrayList<Integer> getLineNumbers();

	/**
	* Get the value of an attribute key.
	*
	* @param key the attribute key
	* @return the attribute value
	*/
	public Object getAttribute(SymTabKey key);

	/**
	* Set an attribute of the entry.
	*
	* @param key the attribute key
	* @param value the attribute value
	*/
	public void setAttribute(SymTabKey key,Object value);

	/**
	* Setter.
	*
	* @param definition the definition to set
	*/
	public void setDefinition(Definition definition);

	/**
	* Getter.
	*
	* @return the definition
	*/
	public Definition getDefinition();

	/**
	* Setter.
	*
	* @param typeSpec the type specification to set
	*/
	public void setTypeSpec(TypeSpec typeSpec);

	/**
	* Getter.
	*
	* @return the type specification
	*/
	public TypeSpec getTypeSpec();
}

