package wci.intermediate;

import wci.intermediate.icodeimpl.ICodeImpl;
import wci.intermediate.icodeimpl.ICodeNodeImpl;

/**
* <h1>ICodeFactory</h1>
*
* <p>A factory for creating objects that implement the intermediate
* code.</p>
*/
// XXX As with the other factories (SymTabFactory, FrontendFactory),
// XXX this factory allows us to hide the actual implementation
// XXX types behind interfaces.  Without a factory to provide an
// XXX interface, the client would instantiate the implementation and
// XXX would be able to see all of the methods that it
// XXX inherited from its parent class (if it has one).
public final class ICodeFactory
{

	private ICodeFactory()
	{
	}

	/**
	* Create and return an intermediate code implementation.
	*
	* @return the intermediate code implementation
	*/
	public static ICode createICode()
	{
		return new ICodeImpl();
	}

	/**
	* Create and return a node implementation.
	*
	* @param type the node type
	* @return an intermediate code node implementation
	*/
	public static ICodeNode createICodeNode(ICodeNodeType type)
	{
		return new ICodeNodeImpl(type);
	}

}

