package wci.intermediate;

import java.util.ArrayList;

/**
* <h1>ICodeNode</h1>
*
* <p>The interface for a node of the intermediate code.</p>
*/
public interface ICodeNode
{
	/**
	* @return the node type
	*/
	public ICodeType getType();

	/**
	* @return the parent node
	*/
	public ICodeNode getParent();

	/**
	* Add a child node.
	*
	* @param node the child node
	* @return the child node
	*/
	public ICodeNode addChild(ICodeNode node);

	/**
	* @return an array list of this node's children
	*/
	public ArrayList<ICodeNode> getChildren();

	/**
	* Set a node attribute.
	*
	* @param key the attribute key
	* @param value the attribute value
	*/
	public void setAttribute(ICodeKey key,Object value);

	/**
	* Get the value of a node attribute.
	*
	* @param key the attribute key
	* @return the attribute value
	*/
	public Object getAttribute(ICodeKey key);

	/**
	* Make a copy of this node.
	*
	* @return the copy
	*/
	public ICodeNode copy();

}

