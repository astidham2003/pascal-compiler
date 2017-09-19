package wci.backend.interpreter.executors;

import wci.backend.interpreter.executors.StatementExecutor;

import wci.intermediate.ICodeNode;
import wci.intermediate.SymTabEntry;

import wci.message.Message;

import java.util.ArrayList;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.ID;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.LINE;

import static wci.intermediate.symtabimpl.SymTabKeyImpl.DATA_VALUE;

import static wci.message.MessageType.ASSIGN;

/**
* <h1>AssignmentExecutor</h1>
*
* <p>Executes an assignment.</p>
*/
public class AssignmentExecutor extends StatementExecutor
{

	/**
	* Constructor.
	*
	* @param parent the parent executor
	*/
	public AssignmentExecutor(StatementExecutor parent)
	{
		super(parent);
	}

	/**
	* Execute an assignment statement.
	*
	* @param node the node to execute
	* @return null
	*/
	@Override
	public Object execute(ICodeNode node)
	{
		// The assign node's children are the target variable and the
		// expression.
		ArrayList<ICodeNode> children = node.getChildren();
		ICodeNode variableNode = children.get(0);
		ICodeNode expressionNode = children.get(1);

		// Execute the expression and get its value.
		ExpressionExecutor expressionExecutor =
			new ExpressionExecutor(this);
		Object value = expressionExecutor.execute(expressionNode);

		// Set the value as an attribute of the variable's symbol table
		// entry.
		SymTabEntry variableId =
			(SymTabEntry) variableNode.getAttribute(ID);
		variableId.setAttribute(DATA_VALUE,value);

		sendMessage(node,variableId.getName(),value);

		++executionCount;
		return null;
	}

	/**
	* Send a message about the assignment operation.
	*
	* @param node the ASSIGN node
	* @param variableName the name of the target variable
	* @param value the value of the expression
	*/
	private void sendMessage(
		ICodeNode node,String variableName,Object value)
	{
		Object lineNumber = node.getAttribute(LINE);

		// Send an ASSINGN message.
		if (lineNumber != null) {
			sendMessage(
				new Message(
					ASSIGN,
					new Object[] {
						lineNumber,
						variableName,
						value}));
		}
	}


}

