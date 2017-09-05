package wci.intermediate.icodeimpl;

import wci.intermediate.ICodeKey;
import wci.intermediate.ICodeNode;
import wci.intermediate.ICodeNodeType;

import util.ArrayList;

/**
* <h1>ICodeNodeImpl</h1>
*/
public class ICodeNodeImpl implements ICodeNode
{

	/**
	* Constructor.
	*
	* @param type the node type
	*/
	public ICodeNodeImpl(ICodeNodeType type)
	{
	}

	// ----------------------------------------------------------------
	// ICodeNode methods

	/**
	* @return the node type
	*/
	@Override
	public ICodeNodeType getType()
	{
	}

	/**
	* @return the parent node
	*/
	@Override
	public ICodeNode getParent()
	{
	}

	/**
	* Add a child node.
	*
	* @param node the child node
	* @return the child node
	*/
	@Override
	public ICodeNode addChild(ICodeNode node)
	{
	}

	/**
	* @return an array list of this node's children
	*/
	@Override
	public ArrayList<ICodeNode> getChildren()
	{
	}

	/**
	* Set a node attribute.
	*
	* @param key the attribute key
	* @param value the attribute value
	*/
	@Override
	public void setAttribute(ICodeKey key,Object value)
	{
	}

	/**
	* Get a node attribute.
	*
	* @param key the attribute key
	* @return value the attribute value
	*/
	@Override
	public void getAttribute(ICodeKey key)
	{
	}

	/**
	* Make a copy of this node.
	*
	* @return the copy
	*/
	@Override
	public ICodeNode copy()
	{
	}

}

