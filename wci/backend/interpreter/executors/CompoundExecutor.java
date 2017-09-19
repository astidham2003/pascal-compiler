package wci.backend.interpreter.executors;

import wci.backend.interpreter.executors.StatementExecutor;

import wci.intermediate.ICodeNode;

import java.util.ArrayList;

/**
* <h1>CompoundExecutor</h1>
*
* <p>Executor a compound statement.</p>
*/
public class CompoundExecutor extends StatementExecutor
{

	/**
	* Constructor.
	*
	* @param parent the parent executor.
	*/
	public CompoundExecutor(StatementExecutor parent)
	{
		super(parent);
	}

	/**
	* Execute a compound statement.
	*
	* @param node the root node of the statement
	* @return null
	*/
	@Override
	public Object execute(ICodeNode node)
	{
		// Loop over the children of the COMPOUND node and execute
		// each child.
		StatementExecutor statementExecutor =
			new StatementExecutor(this);
		ArrayList<ICodeNode> children = node.getChildren();
		for (ICodeNode child : children) {
			statementExecutor.execute(child);
		}

		return null;
	}
}

