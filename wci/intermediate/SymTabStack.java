package wci.intermediate;

public interface SymTabStack
{
	public int getCurrentNestingLevel();
	public SymTab getLocalSymTab();
	public SymTabEntry enterLocal();
	public SymTabEntry lookupLocal();
	public SymTabEntry lookup();
}

