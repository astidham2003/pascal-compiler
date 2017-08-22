package wci.intermediate;

public interface SymTabEntry
{
	public String getName();
	public SymTab getSymTab();
	public void appendedLineNumber();
	public java.util.ArrayList getLineNumbers();
	public Object getAttribute();
}

