package wci.intermediate.symtabimpl;

/**
* <h1>SymTabKeyImpl</h1>
*
* <p>Attribute keys for a symbol table entry.</p>
*/
public enum SymTabKeyImpl implements wci.intermediate.SymTabKey
{
	// Constant
	CONSTANT_VALUE,

	// Procedure or function
	ROUTINE_CODE, ROUTINE_SYMTAB, ROUTINE_ICODE,
	ROUTINE_PARMS, ROUTINE_ROUTINES,

	// Variable or record field value
	DATA_VALUE

}

