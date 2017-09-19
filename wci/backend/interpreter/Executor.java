package wci.backend.interpreter;

import wci.backend.Backend;

import wci.backend.interpreter.executors.StatementExecutor;

import wci.intermediate.SymTabStack;
import wci.intermediate.ICode;
import wci.intermediate.ICodeNode;

import wci.message.*;

import static wci.message.MessageType.*;

/**
* <h1>Executor</h1>
*
* <p>The class for interpreting the Pascal language.</p>
*/
public class Executor extends Backend
{

	protected static int executionCount;
	protected static RuntimeErrorHandler errorHandler;

	static {
		executionCount = 0;
		errorHandler = new RuntimeErrorHandler();
	}

	/**
	* Constructor.
	*
	* @param the parent executor
	*/
	public Executor()
	{
		super();
	}

	/**
	* Constructor.
	*
	* @param the parent executor
	*/
	public Executor(Executor parent)
	{
		super();
	}

	// ----------------------------------------------------------------
	// Backend method

	/**
	* Process the intermediate code and the symbol table generated
	* by the parser to execute the source program.
	*
	* @param iCode the intermediate code
	* @param symTabStack the symbol table stack
	* @throws Exception
	*/
	@Override
	public void process(
		ICode iCode,SymTabStack symTabStack) throws Exception
	{
		this.symTabStack = symTabStack;
		this.iCode = iCode;

		long t0 = System.currentTimeMillis();

		// Get the root node of the intermediate code and execute.
		ICodeNode rootNode = iCode.getRoot();
		StatementExecutor statementExecutor =
			new StatementExecutor(this);
		statementExecutor.execute(rootNode);

		float elapsed = (System.currentTimeMillis() - t0)/1000f;
		int runtimeErrors = errorHandler.getErrorCount();

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

