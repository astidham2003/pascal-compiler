package wci.frontend;

import wci.intermediate.ICode;
import wci.intermediate.SymTab;
import wci.intermediate.SymTabStack;
import wci.intermediate.SymTabFactory;

import wci.message.*;

/**
* <h1>Parser</h1>
*
* <p>A language-independent framework class.  This abstract parser
* class will be implemented by language-specific subclasses.</p>
*/
public abstract class Parser implements MessageProducer
{

	protected static SymTab symTab;	// Generated symbol table.
	protected static SymTabStack symTabStack;
	protected static MessageHandler messageHandler; // Delegate
	protected Scanner scanner;	// Scanner used with this parser.

	static
	{
		symTab = null;
		symTabStack = SymTabFactory.createSymTabStack();
		messageHandler = new MessageHandler();
	}

	/**
	* Constructor.
	*
	* @param scanner the scanner to be used with this parser
	*/
	protected Parser(Scanner scanner)
	{
		this.scanner = scanner;
	}

	// ----------------------------------------------------------------

	/**
	* Getter.
	*
	* @return the generated intermediate code
	*/
	public abstract ICode getICode();

	/**
	* Parse a source program and generate the intermediate code
	* and the symbol table.  To be implemented by a language-specific
	* parser subclass.
	*
	* @ throws Exception if an error occurred
	*/
	public abstract void parse() throws Exception;

	/**
	* Return the number of syntax errors found by the parser.
	* To be implemented by a language-specific parser subclass.
	*
	* @return the error count
	*/
	public abstract int getErrorCount();

	// ----------------------------------------------------------------

	public SymTabStack getSymTabStack()
	{
		return symTabStack;
	}


	public Scanner getScanner()
	{
		return scanner;
	}

	/**
	* Call the scanner's currentToken() method.
	* @return the current token.
	*/
	public Token currentToken()
	{
		return scanner.currentToken();
	}

	/**
	* Call the scanner's nextToken() method.
	*
	* @return the next token.
	* @throws Exception if an error occured.
	*/
	public Token nextToken() throws Exception
	{
		return scanner.nextToken();
	}

	// ----------------------------------------------------------------

	/**
	* Add a parser message listener.
	*
	* @param listener the message listener to add
	*/
	@Override
	public void addMessageListener(MessageListener listener)
	{
		messageHandler.addListener(listener);
	}


	/**
	* Remove a parser message listener.
	*
	* @param listener the message listener to remove
	*/
	@Override
	public void removeMessageListener(MessageListener listener)
	{
		messageHandler.removeListener(listener);
	}

	/**
	* Notify listeners after setting the message.
	*
	* @param msg the message to set
	*/
	@Override
	public void sendMessage(Message msg)
	{
		messageHandler.sendMessage(msg);
	}

}


