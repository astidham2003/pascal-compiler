package wci.backend.compiler;

import wci.message.*;
import wci.backend.Backend;
import wci.intermediate.SymTab;
import wci.intermediate.ICode;

import static wci.message.MessageType.*;

/**
* <h1>CodeGenerator</h1>
*
* <p>The class that generates the Jasmin assembly language.</p>
*/
public class CodeGenerator extends Backend
{

	// ----------------------------------------------------------------
	// Backend method

	/**
	* Process the intermediate code and the symbol table generated
	* by the parser to generate machine level instructions.
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
		int instructionCount = 0;

		sendMessage(
			new Message(
				COMPILER_SUMMARY,
				new Number[] {
					instructionCount,
					elapsed
					}));
	}

}

