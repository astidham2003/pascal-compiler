package wci.intermediate.typeimpl;

import java.util.HashMap;

import wci.intermediate.*;

import wci.intermediate.symtabimpl.Predefined;

import static wci.intermediate.typeimpl.TypeFormImpl.ARRAY;
import static wci.intermediate.typeimpl.TypeFormImpl.SUBRANGE;
import static wci.intermediate.typeimpl.TypeKeyImpl.*;

/**
* <h1>TypeSpecImpl<h1>
*
* <p>A pascal type specification implementation.</p>
*/
public class TypeSpecImpl
	extends HashMap<TypeKey,Object>
	implements TypeSpec
{
	private TypeForm form;
	private SymTabEntry identifier;

	/**
	* Constructor.
	*
	* @param form the type form.
	*/
	public TypeSpecImpl(TypeForm form)
	{
		this.form = form;
		this.identifier = null;
	}

	/**
	* Create a Pascal string with no characters whose length is equal
	* to the value argument's length.
	*
	* @param value a string value
	*/
	public TypeSpecImpl(String value)
	{
		this.form = ARRAY;

		TypeSpec indexType = new TypeSpecImpl(SUBRANGE);

		indexType.setAttribute(
			SUBRANGE_BASE_TYPE,Predefined.integerType);
		indexType.setAttribute(SUBRANGE_MIN_VALUE,1);
		indexType.setAttribute(SUBRANGE_MAX_VALUE,value.length());

		setAttribute(ARRAY_INDEX_TYPE,indexType);
		setAttribute(ARRAY_ELEMENT_TYPE,Predefined.charType);
		setAttribute(ARRAY_ELEMENT_COUNT,value.length());
	}

	// ----------------------------------------------------------------


	/**
	* Getter
	*
	* @return the type form
	*/
	@Override
	public TypeForm getForm()
	{
		return form;
	}

	/**
	* Setter
	*
	* @param identifier the type identifier (symbol table entry)
	*/
	@Override
	public void setIdentifier(SymTabEntry identifier)
	{
		this.identifier = identifier;
	}

	/**
	* Getter
	*
	* @return the type identifier (symbol table entry)
	*/
	@Override
	public SymTabEntry getIdentifier()
	{
		return identifier;
	}


	/**
	* Set an attribute of the specification.
	*
	* @param key the attribute key
	* @param value the attibute value
	*/
	@Override
	public Object setAttribute(TypeKey key,Object value)
	{
		this.put(key,value);
		return value;
	}

	/**
	* Get the value of an attribute of the specification.
	*
	* @param key the attribute key
	* @return the attribute value
	*/
	@Override
	public Object getAttribute(TypeKey key)
	{
		return this.get(key);
	}

	/**
	* @return true if this is a Pascal string type.
	*/
	@Override
	public boolean isPascalString()
	{
		// Pascal string is an array of char.
		if (form == ARRAY){
			TypeSpec elmtType =
				(TypeSpec) getAttribute(ARRAY_ELEMENT_TYPE);
			TypeSpec indexType =
				(TypeSpec) getAttribute(ARRAY_INDEX_TYPE);

			return (elmtType.baseType() == Predefined.charType &&
				indexType.baseType() == Predefined.integerType);
		}
		else {
			return false;
		}
	}

	/**
	* @return the base type of this type
	*/
	@Override
	public TypeSpec baseType()
	{
		return (TypeFormImpl) form == SUBRANGE ?
			(TypeSpec) getAttribute(SUBRANGE_BASE_TYPE) :
			this;
	}
}

