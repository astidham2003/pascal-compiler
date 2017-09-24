package wci.backend.interpreter.executors;

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
		return null;
	}
}

