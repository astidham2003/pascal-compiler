package wci.backend.interpreter.executors;

import java.util.ArrayList;
import java.util.HashMap;

import wci.intermediate.ICodeNode;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.VALUE;

/**
* <h1>SelectExecutor</h1>
*
* <p>Executes a SELECT node.</p>
*/
public class SelectExecutor extends StatementExecutor
{

	// Jump table cache: entry key is a SELECT node.
	//                   entry  value is the jump table.
	// Jump table: entry key is a selection value,
	//             entry value is the branch statement.
	private static HashMap<ICodeNode,HashMap<Object,ICodeNode>>
		jumpCache = new HashMap<ICodeNode,HashMap<Object,ICodeNode>>();

	/**
	* Constructor.
	*
	* @param parent the parent Executor
	*/
	public SelectExecutor(StatementExecutor parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------

	/**
	* Exectute a SELECT node.
	*
	* @param node the root node of the statement
	* @return null
	*/
	@Override
	public Object execute(ICodeNode node)
	{
		// Is there an entry for this SELECT node in the jump table
		// cache?  If not, then create a jump table cache entry.
		HashMap<Object,ICodeNode> jumpTable = jumpCache.get(node);
		if (jumpTable == null) {
			jumpTable = createJumpTable(node);
			jumpCache.put(node,jumpTable);
		}

		// Get the SELECT node's children.
		ArrayList<ICodeNode> selectChildren = node.getChildren();
		ICodeNode exprNode = selectChildren.get(0);


		// Evaluate the SELECT expression.
		ExpressionExecutor expressionExecutor =
			new ExpressionExecutor(this);
		Object selectValue = expressionExecutor.execute(exprNode);

		// If there is a selection, then execute the SELECT_BRANCH's,
		// statement.
		ICodeNode statementNode = jumpTable.get(selectValue);
		if (statementNode != null) {
			StatementExecutor statementExecutor =
				new StatementExecutor(this);
			statementExecutor.execute(statementNode);
		}

		++executionCount; // Count the SELECT statement itself.
		return null;
	}

	/**
	* Create a jump table for a SELECT node.
	*
	* @param node the SELECT node
	* @return the jump table
	*/
	private HashMap<Object,ICodeNode> createJumpTable(ICodeNode node)
	{
		HashMap<Object,ICodeNode> jumpTable =
			new HashMap<Object,ICodeNode>();

		// Loop over children that are SELECT_BRANCH nodes.
		ArrayList<ICodeNode> selectChildren = node.getChildren();
		for (int i = 1; i < selectChildren.size(); ++i) {
			ICodeNode branchNode = selectChildren.get(i);
			ICodeNode constantsNode = branchNode.getChildren().get(0);
			ICodeNode statementNode = branchNode.getChildren().get(1);

			// Loop over the constants children of the branch's
			// CONSTANTS_NODE.
			ArrayList<ICodeNode> constantsList =
				constantsNode.getChildren();
			for (ICodeNode constantNode : constantsList) {

				// Create a jump table entry.
				Object value = constantNode.getAttribute(VALUE);
				jumpTable.put(value,statementNode);
			}
		}

		return jumpTable;
	}

	/**
	* Search the constants of a SELECT_BRANCH for a matching value.
	*
	* @param selectValue the value to match
	* @param branchNode the SELECT_BRANCH node
	* return true if a match is found, else false
	*/
	private boolean searchConstants(
		Object selectValue,ICodeNode branchNode)
	{
		// Are the values integer or string?
		boolean integerMode = selectValue instanceof Integer;

		// Get the list of SELECT_CONSTANT values.
		ICodeNode constantsNode = branchNode.getChildren().get(0);
		ArrayList<ICodeNode> constantsList =
			constantsNode.getChildren();

		// Search the list of constants.
		if (selectValue instanceof Integer) {
			for (ICodeNode constantNode : constantsList) {
				int constant =
					(Integer) constantNode.getAttribute(VALUE);

				if ((Integer) selectValue == constant) {
					return true;
				}
			}
		}
		else {
			for (ICodeNode constantNode : constantsList) {
				String constant =
					(String) constantNode.getAttribute(VALUE);

				if (((String) selectValue).equals(constant)) {
					return true;
				}
			}
		}

		return false; // No match found.
	}
}

