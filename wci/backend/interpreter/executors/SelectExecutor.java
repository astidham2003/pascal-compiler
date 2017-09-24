package wci.backend.interpreter.executors;

import wci.intermediate.ICodeNode;

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
		return null;
	}
}

