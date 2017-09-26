package wci.intermediate.icodeimpl;

import wci.intermediate.ICode;
import wci.intermediate.ICodeNode;

/**
* <h1>ICodeImpl</h1>
*/
public class ICodeImpl implements ICode
{

	private ICodeNode root;

	/**
	* Constuctor.
	*/
	public ICodeImpl()
	{
	}

	// ----------------------------------------------------------------
	// ICode methods

	/**
	* Set the root node.
	*
	* @param node the root node
	* @return the root node
	*/
	@Override
	public ICodeNode setRoot(ICodeNode node)
	{
		root = node;
		return node;
	}

	/**
	* @return the root node
	*/
	@Override
	public ICodeNode getRoot()
	{
		return root;
	}

}

