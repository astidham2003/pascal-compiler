package wci.backend.interpreter.executors;

import wci.backend.interpreter.Executor;
import static wci.backend.interpreter.RuntimeErrorCode.
	UNIMPLEMENTED_FEATURE;

import wci.intermediate.ICodeNode;

import wci.intermediate.icodeimpl.ICodeNodeTypeImpl;
import wci.intermediate.icodeimpl.ICodeKeyImpl;

import wci.message.Message;

import static wci.message.MessageType.SOURCE_LINE;

import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.ASSIGN;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.COMPOUND;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.NO_OP;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.LINE;

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
		ICodeNodeTypeImpl nodeType =
			(ICodeNodeTypeImpl) node.getType();

		// Send a message about the current source line.
		sendSourceLineMessage(node);

		if (nodeType == COMPOUND) {
			CompoundExecutor compoundExecutor =
				new CompoundExecutor(this);
			return compoundExecutor.execute(node);
		}

		else if (nodeType == ASSIGN) {
			AssignmentExecutor assignmentExecutor =
				new AssignmentExecutor(this);
			return assignmentExecutor.execute(node);
		}

		else if (nodeType == NO_OP) {
			return null;
		}

		else {
			errorHandler.flag(node,UNIMPLEMENTED_FEATURE,this);
			return null;
		}
	}

	/**
	* Send a message about the current source line.
	*
	* @param node the statement node
	*/
	private void sendSourceLineMessage(ICodeNode node)
	{
		Object lineNumber = node.getAttribute(LINE);

		// Send the SOURCE_LINE message.
		if (lineNumber != null) {
			sendMessage(
				new Message(
					SOURCE_LINE,
					lineNumber));
		}
	}

}

