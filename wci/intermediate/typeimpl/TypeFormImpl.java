package wci.intermediate.typeimpl;

import wci.intermediate.TypeForm;

/**
* <h1>TypeForm</h1>
*
* <p>Type forms for a Pascal type specification.</p>
*/
public enum TypeFormImpl implements TypeForm
{
	SCALAR, ENUMERATION, SUBRANGE, ARRAY, RECORD;

	public String toString()
	{
		return super.toString().toLowerCase();
	}
}

