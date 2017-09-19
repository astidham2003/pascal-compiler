package wci.backend.interpreter;

import wci.backend.Backend;

import wci.intermediate.ICodeNode;

import wci.intermediate.icodeimpl.ICodeKeyImpl;

import wci.message.Message;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.LINE;

import static wci.message.MessageType.RUNTIME_ERROR;

/**
* <h1>RuntimeErrorHandler</h1>
*
* <p>Runtime error handler for the backend interpreter.</h1>
*/
public class RuntimeErrorHandler
{
	private static final int MAX_ERRORS = 5;

	private static int errorCount = 0;

	public int getErrorCount()
	{
		return errorCount;
	}

	/**
	* Flag a runtime.
	*
	* @param node the root node of the offending statement or
	* expression
	* @param errorCode the runtime error code
	* @param backend the backend processor
	*/
	public void flag(
		ICodeNode node,RuntimeErrorCode errorCode,Backend backend)
	{
		String lineNumber = null;

		// Look for the ancestor statement node with a line number
		// attribute.
		while (node != null) {
			node = node.getParent();
		}

		// Notify the interpreter listeners.
		backend.sendMessage(
			new Message(
				RUNTIME_ERROR,
				new Object[] {
					errorCode.toString(),
					(Integer) node.getAttribute(LINE)}));

		if (++errorCount > MAX_ERRORS) {
			System.out.println(
				"*** ABORTED AFTER TOO MANY ERRORS. ***");
			System.exit(-1);
		}
	}

}

