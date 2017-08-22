package wci.intermediate;

public interface SymTab
{
	public int getNestingLevel();
	public SymTabEntry enter();
	public SymTabEntry lookup();
	public java.util.ArrayList sortedEntries();
}

