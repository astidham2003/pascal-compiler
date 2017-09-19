package wci.backend.interpreter.executors;

import wci.backend.interpreter.Executor;

import wci.intermediate.ICodeNode;

/**
* <h1>StatementExecutor</h1>
*
* <p>Executes a statement.</p>
*/
public class StatementExecutor extends Executor
{

	/**
	* Constructor.
	*
	* @param parent the parent executor
	*/
	public StatementExecutor(Executor parent)
	{
		super(parent);
	}

	/**
	* Execute a statement.
	* To be overridden by the specialized statement executor
	* subclasses.
	*
	* @param node the root node of the statement
	* @return null
	*/
	public Object execute(ICodeNode node)
	{
		return null;
	}

}

