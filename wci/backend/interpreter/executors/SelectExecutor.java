package wci.backend.interpreter.executors;

import java.util.ArrayList;

import wci.intermediate.ICodeNode;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.VALUE;

/**
* <h1>SelectExecutor</h1>
*
* <p>Executes a SELECT node.</p>
*/
public class SelectExecutor extends StatementExecutor
{

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
		// Get the SELECT node's children.
		ArrayList<ICodeNode> selectChildren = node.getChildren();
		ICodeNode exprNode = selectChildren.get(0);


		// Evaluate the SELECT expression.
		ExpressionExecutor expressionExecutor =
			new ExpressionExecutor(this);
		Object selectValue = expressionExecutor.execute(exprNode);

		// Attempt to select a SELECT_BRANCH.
		ICodeNode selectedBranchNode =
			searchBranches(selectValue,selectChildren);

		// If there was a selection, then execute the SELECT_BRANCH's
		// statement.
		if (selectedBranchNode != null) {
			ICodeNode stmtNode =
				selectedBranchNode.getChildren().get(1);
			StatementExecutor statementExecutor =
				new StatementExecutor(this);
			statementExecutor.execute(stmtNode);
		}

		++executionCount;
		return null;
	}

	/**
	* Search the SELECT_BRANCHs to find a match.
	*
	* @param selectValue the value to match
	* @param selectChildren the children of the SELECT node
	* @return the matching SELECT_BRANCH node if any, else null
	*/
	private ICodeNode searchBranches(
		Object selectValue,ArrayList<ICodeNode> selectChildren)
	{

		// Loop over the SELECT_BRANCHs to find a match.
		for (int i = 1; i < selectChildren.size(); ++i) {
			ICodeNode branchNode = selectChildren.get(i);

			if (searchConstants(selectValue,branchNode)) {
				return branchNode;
			}
		}

		return null;
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

