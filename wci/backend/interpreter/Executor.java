package wci.backend.interpreter;

import wci.message.*;
import wci.backend.Backend;
import wci.intermediate.SymTab;
import wci.intermediate.ICode;

import static wci.message.MessageType.*;

/**
* <h1>Executor</h1>
*
* <p>The class for interpreting the Pascal language.</p>
*/
public class Executor extends Backend
{

	// ----------------------------------------------------------------
	// Backend method

	/**
	* Process the intermediate code and the symbol table generated
	* by the parser to execute the source program.
	*
	* @param iCode the intermediate code
	* @param symTab the symbol table
	* @throws Exception
	*/
	@Override
	public void process(
		ICode iCode,SymTab symTab) throws Exception
	{
		long t0 = System.currentTimeMillis();
		float elapsed = (System.currentTimeMillis() - t0)/1000f;
		int executionCount = 0;
		int runtimeErrors = 0;

		sendMessage(
			new Message(
				INTERPRETER_SUMMARY,
				new Number[] {
					executionCount,
					runtimeErrors,
					elapsed
					}));
	}

}

