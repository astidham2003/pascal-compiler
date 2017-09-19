package wci.frontend;

import java.io.BufferedReader;
import java.io.IOException;

import wci.message.*;

import static wci.message.MessageType.*;

/**
* <h1>Source</h1>
*
* <p>The framework class that represents the source program.</p>
*/
public class Source implements MessageProducer,AutoCloseable
{

	public static final char EOL = '\n';	// End-of-line character.
	public static final char EOF = (char) 0;// End-of-file character.

	protected static MessageHandler messageHandler;

	private BufferedReader reader;	// Reader for the source program.
	private String line; 			// One line of source code.
	private int lineNum;			// Current line number.
	private int currentPos;			// Current line position.

	static
	{
		messageHandler = new MessageHandler();
	}

	/**
	* Constructor.
	*
	* @param reader the reader for the source program.
	* @throws IOException if an I/O error ocurred.
	*/
	public Source(BufferedReader reader) throws IOException
	{
		// XXX How can an IOException occur here?
		this.lineNum = 0;
		// Set to -2 to read the first line.
		this.currentPos = -2;
		this.reader = reader;
	}

	public int getLine()
	{
		return lineNum;
	}

	public int getPosition()
	{
		return currentPos;
	}

	/**
	* Return the source character at the current position.
	* @return the source character at the current position.
	* @throws Exception if an error occurred.
	*/
	public char currentChar() throws Exception
	{
		// First time?
		if (currentPos == -2) {
			// Reads a line and set currentPos to -1.
			readLine();
			// Increments currentPos, so it's now 0, 
			// and returns currentChar().
			return nextChar();
		}
		// At end of file?
		else if (line == null) {
			return EOF;
		}
		// At end of line?
		// XXX How does currentPos become -1?
		else if ((currentPos == -1) ||
				(currentPos == line.length())) {
			return EOL;
		}
		// Need to read the next line?
		else if (currentPos > line.length()) {
			// Reads a line and set currentPos to -1.
			readLine();
			// Increments currentPos, so it's now 0, 
			// and returns currentChar().
			return nextChar();
		}
		// Return the character at the current position.
		else {
			return line.charAt(currentPos);
		}
	}

	/**
	* Consume the current source character and return the next
	* character.
	*
	* @return the next source character.
	* @throws Exception if an error occurred.
	*/
	public char nextChar() throws Exception
	{
		++currentPos;
		return currentChar();
	}

	/**
	* Return the source character following the current character
	* without consuming the current character.
	*
	* @return the following character.
	* @throws Exception if an error occurred.
	*/
	public char peekChar() throws Exception
	{
		currentChar();
		if (line == null) {
			return EOF;
		}

		int nextPos = currentPos + 1;
		return nextPos < line.length() ? line.charAt(nextPos) : EOL;
	}

	/**
	* Read the next source line.
	*
	* @throws IOException if an I/O error occurred.
	*/
	private void readLine() throws IOException
	{
		// Will be null when at the end of the source.
		line = reader.readLine();
		currentPos = -1;

		if (line != null) {
			++lineNum;
		}

		// XXX Not sure why this is its own if statement.
		if (line != null) {
			sendMessage(
				new Message(
					SOURCE_LINE,
					new Object[] {
						lineNum,
						line}));
		}
	}

	// ----------------------------------------------------------------
	// AutoCloseable method

	/**
	* Close the source.
	*
	* @throws Exception if an error occurred.
	*/
	@Override
	public void close() throws Exception
	{
		if (reader != null) {
			try {
				reader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

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

