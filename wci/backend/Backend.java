package wci.backend;

import wci.message.*;
import wci.intermediate.SymTab;
import wci.intermediate.ICode;

/**
* <h1>Backend</h1>
*
* <p>The framework class that represents the back end component.</p>
*/
public abstract class Backend implements MessageProducer
{

	protected static MessageHandler messageHandler;
	protected ICode iCode;
	protected SymTab symTab;

	static
	{
		messageHandler = new MessageHandler();
	}

	/**
	* Process the intermediate code and the symbol table generated
	* by the parser.  To be implemented by a compiler or an
	* interpreter subclass.
	* @param iCode the intermediate code
	* @param symTab the symbol table
	* @throws Exception
	*/
	public abstract void process(
		ICode iCode,SymTab symTab) throws Exception;

	// ----------------------------------------------------------------
	// MessageProducer methods

	@Override
	public void addMessageListener(MessageListener listener)
	{
		messageHandler.addListener(listener);
	}

	@Override
	public void removeMessageListener(MessageListener listener)
	{
		messageHandler.removeListener(listener);
	}

	@Override
	public void sendMessage(Message message)
	{
		messageHandler.sendMessage(message);
	}

}

