package wci.backend.interpreter.executors;

import java.util.List;

import wci.intermediate.ICodeNode;

import wci.intermediate.icodeimpl.ICodeNodeTypeImpl;

import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.TEST;

/**
* <h1>LoopExecutor</h1>
*
* <p>Executes a LOOP node.</p>
*/
public class LoopExecutor extends StatementExecutor
{

	/**
	* Constructor.
	*
	* @param parent the parent executor
	*/
	public LoopExecutor(StatementExecutor parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------

	/**
	* Execute a LOOP node.
	*
	* @param node the root node of the statement
	* @return null
	*/
	public Object execute(ICodeNode node)
	{
		boolean exitLoop = false;
		ICodeNode exprNode = null;
		List<ICodeNode> loopChildren = node.getChildren();

		ExpressionExecutor expressionExecutor =
			new ExpressionExecutor(this);
		StatementExecutor statementExecutor =
			new StatementExecutor(this);

		// Loop until the TEST expression value is true.
		while (!exitLoop) {
			++executionCount;	// Count the loop statement itself.

			// Execute the children of the LOOP node.
			for (ICodeNode child : loopChildren) {
				ICodeNodeTypeImpl childType =
					(ICodeNodeTypeImpl) child.getType();

				// Test node?
				if (childType == TEST) {
					if (exprNode == null) {
						exprNode = child.getChildren().get(0);
					}

					exitLoop =
						(Boolean) expressionExecutor.execute(exprNode);
				}

				// Statement node.
				else {
					statementExecutor.execute(child);
				}

				// Exit if the TEST expression value is true.
				if (exitLoop) {
					break;
				}
			}
		}

		return null;
	}

}

