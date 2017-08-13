package wci.frontend.pascal;

import wci.frontend.*;
import wci.message.Message;

import static wci.message.MessageType.PARSER_SUMMARY;

/**
* <h1>PascalParserTD</h1>
*
* <p>The top-down Pascal parser.</p>
*/
public class PascalParserTD extends Parser
{

	/**
	* Constructor.
	* @param scanner the scanner to be used with this parser
	*/
	public PascalParserTD(Scanner scanner)
	{
		super(scanner);
	}

	// ----------------------------------------------------------------
	// Parser methods

	/**
	* Parse the source and generate the symbol table and
	* intermediate code.
	*/
	@Override
	public void parse() throws Exception
	{
		Token token;
		long t0 = System.currentTimeMillis();

		while (!((token = nextToken()) instanceof EofToken));

		// Send the parser summary message.
		float elapsedTime = (System.currentTimeMillis() - t0)/1000f;
		sendMessage(
			new Message(PARSER_SUMMARY,
						new Number[]
							{
							token.getLineNumber(),
							getErrorCount(),
							elapsedTime
							}));
	}

	/**
	* The number of errors found by this parser.
	* @return the number of syntax errors found by the parser
	*/
	@Override
	public int getErrorCount()
	{
		return 0;
	}

}

