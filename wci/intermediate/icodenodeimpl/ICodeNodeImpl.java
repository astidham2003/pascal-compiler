package wci.intermediate.icodeimpl;

import wci.intermediate.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
* <h1>ICodeNodeImpl</h1>
*
* <p>An implementation of a node of the intermediate code.</p>
*/
public class ICodeNodeImpl
	extends HashMap<ICodeKey,Object>
	implements ICodeNode
{

	private final ICodeNodeType type;
	private final ArrayList<ICodeNode> children;

	private ICodeNode parent;

	/**
	* Constructor.
	*
	* @param type the node type whose name will be the name of this
	* node
	*/
	public ICodeNodeImpl(ICodeNodeType type)
	{
		this.parent = null;
		this.type = type;
		this.children = new ArrayList<ICodeNode>();
	}

	// ----------------------------------------------------------------
	// ICodeNode methods

	/**
	* @return the node type
	*/
	@Override
	public ICodeNodeType getType()
	{
		return type;
	}

	/**
	* @return the parent node
	*/
	@Override
	public ICodeNode getParent()
	{
		return parent;
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
		if (node != null) {

			children.add(node);

			// XXX Does assuming the node is a ICodeNodeImpl
			// XXX violate the 'program to an interface' guideline?
			// XXX After all, we are passing an interface and then
			// XXX demanding that it be an implementation.
			((ICodeNodeImpl) node).parent = this;
		}

		return node;
	}

	/**
	* @return an array list of this node's children
	*/
	@Override
	public ArrayList<ICodeNode> getChildren()
	{
		return children;
	}

	/**
	* Set a node attribute.
	*
	* @param key the attribute key
	* @param value the attribute value
	*/
	@Override
	@SuppressWarning("unchecked")
	public void setAttribute(ICodeKey key,Object value)
	{
		put(key,value);
	}

	/**
	* Get a node attribute.
	*
	* @param key the attribute key
	* @return value the attribute value
	*/
	@Override
	@SuppressWarning("unchecked")
	public Object getAttribute(ICodeKey key)
	{
		return get(key);
	}

	/**
	* Make a copy of this node.
	*
	* @return the copy
	*/
	@Override
	public ICodeNode copy()
	{
		ICodeNodeImpl copy =
			(ICodeNodeImpl) ICodeFactory.createICodeNode(type);
		Set<Map.Entry<ICodeKey,Object>> attributes = entrySet();

		for(Map.Entry<ICodeKey,Object> attribute : attributes) {
			copy.put(attribute.getKey(),attribute.getValue())
		}

		return copy;
	}

	@Override
	public String toString()
	{
		return type.toString();
	}

}

