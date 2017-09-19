package wci.backend.interpreter.executors;

import wci.backend.interpreter.executors.AssignmentExecutor;

import wci.intermediate.ICodeNode;

// XXX Bare min needed to compile.
public class ExpressionExecutor extends AssignmentExecutor
{

	/**
	* Constructor.
	*
	* @param parent the parent executor
	*/
	public ExpressionExecutor(AssignmentExecutor parent)
	{
		super(parent);
	}

	@Override
	public Object execute(ICodeNode node)
	{
		return null;
	}

}

