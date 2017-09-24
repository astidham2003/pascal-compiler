package wci.backend.interpreter.executors;

import java.util.List;

import wci.intermediate.ICodeNode;

/**
* <h1>IfExecutor</h1>
*
* <p>Executes a IF node.</p>
*/
public class IfExecutor extends StatementExecutor
{

	/**
	* Constructor.
	*
	* @param parent the parent executor
	*/
	public IfExecutor(StatementExecutor parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------

	/**
	* Execute an IF statement.
	*
	* @param node the root node of the statement
	* @return null
	*/
	@Override
	public Object execute(ICodeNode node)
	{

		// Get the IF node's children.
		List<ICodeNode> children = node.getChildren();
		ICodeNode expr = children.get(0);
		ICodeNode thenStmtNode = children.get(1);
		ICodeNode elseStmtNode =
			children.size() > 2 ? children.get(2) : null;

		ExpressionExecutor expressionExecutor =
			new ExpressionExecutor(this);
		StatementExecutor statementExecutor =
			new StatementExecutor(this);

		// Evaluate the expression to determine which statement to
		// execute.
		boolean b = (Boolean) expressionExecutor.execute(expr);
		if (b) {
			statementExecutor.execute(thenStmtNode);
		}
		else if (elseStmtNode != null) {
			statementExecutor.execute(elseStmtNode);
		}

		++executionCount;
		return null;
	}
}

