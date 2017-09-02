package wci.backend;

import wci.backend.compiler.CodeGenerator;
import wci.backend.interpreter.Executor;

/**
* <h1>BackendFactory</h1>
*
* <p>A factory class that creates compiler and interpreter
* components. </p>
*/
public final class BackendFactory
{
	/**
	* Constructor
	*/
	private BackendFactory()
	{
	}

	/**
	* Create a compiler or an interpreter back end component.
	*
	* @param operation either "compile" or "execute"
	* @return a compiler or an interpreter backend component
	* @throws Exception
	*/
	public static Backend createBackend(String operation)
		throws Exception
	{
		if (operation.equalsIgnoreCase(
				Operation.COMPILE))
			return new CodeGenerator();
		else if (operation.equalsIgnoreCase(
				Operation.EXECUTE))
			return new Executor();
		else
			throw new Exception(
				String.format(
					"Backend factory: Invalid operation '%s'",
					operation));
	}

	// ----------------------------------------------------------------

	// XXX Not part of book listing.  Just thought this might be
	// an alternative to having to remember what string options
	// were available to pass the createBackend() method.
	public static class Operation
	{
		public static final String COMPILE = "compile";
		public static final String EXECUTE = "execute";
	}
}
