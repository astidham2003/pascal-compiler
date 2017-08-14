import java.io.BufferedReader;
import java.io.FileReader;

import wci.frontend.*;
import wci.intermediate.*;
import wci.backend.*;
import wci.message.*;

import static wci.message.MessageType.*;
import static wci.frontend.pascal.PascalTokenType.STRING;

/**
* <h1>Pascal</h1>
*
* <p>Compile or interpret a Pascal source program.</p>
*/
public class Pascal
{
	private Parser parser; // Language independant parser.
	// XXX Should this be a Scanner?  In Chapter 2 it is a Source,
	// XXX but that doesn't make sense because source is creatd
	// XXX AND closed in the constructor.  So, why would we hold
	// XXX a reference to it?
	private Source source; // Language independant scanner.
	private ICode iCode;
	private SymTab symTab;
	private Backend backend;

	private static final String FLAGS = "[-ix]";
	private static final String USAGE =
		String.format(
			"Usage: Pascal execute|compile '%s' <source file path>",
			FLAGS);

	/**
	* Constructor.
	*
	* @param operation either BackendFactory.Operation.EXECUTE or
	* BackendFactory.Operation.COMPILE.
	*
	* @param filePath the source file path XXX Absolute or relative?
	* @param flags the command line flags
	*/
	public Pascal(String operation,String filePath,String flags)
	{
		// XXX Seems a little odd for all of this in the
		// XXX constructor.  Sortof defies the adage of trying
		// XXX not to throw exceptions in constructors.
		// XXX Changed source to implement AutoCloseable so it
		// XXX could be used this way.
		// XXX Keep in mind that this is now a different source
		// XXX than what this class holds a reference.
		try (Source source = new Source(
					new BufferedReader(
						new FileReader(filePath)))) {
			// XXX Not used?
			boolean intermediate = flags.indexOf('i') > -1;
			// XXX Not used?
			boolean xref = flags.indexOf('x') > -1;

			source.addMessageListener(new SourceMessageListener());

			parser = FrontendFactory.createParser(
				FrontendFactory.Language.PASCAL,
				FrontendFactory.Type.TOP_DOWN,
				source);
			parser.addMessageListener(new ParserMessageListener());

			backend = BackendFactory.createBackend(operation);
			backend.addMessageListener(new BackendMessageListener());

			parser.parse();

			iCode = parser.getICode();
			symTab = parser.getSymTab();

			backend.process(iCode,symTab);
		}
		catch (Exception e) {
			System.out.println(
				"***** Internal translator error. *****");
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		try {
			String operation = args[0];
			if (!operation.equalsIgnoreCase(
					BackendFactory.Operation.COMPILE) &&
				!operation.equalsIgnoreCase(
					BackendFactory.Operation.EXECUTE))
				throw new Exception();

			int arg_pos = 1;
			String flags = "";

			while (arg_pos < args.length &&
					args[arg_pos].charAt(0) == '-') {
				flags += args[arg_pos].substring(1);
				++arg_pos;
			}

			if (arg_pos < args.length) {
				String path = args[arg_pos];
				new Pascal(operation,path,flags);
			}
			else {
				throw new Exception();
			}
		}
		catch (Exception e) {
			System.out.println(USAGE);
		}
	}

	/**
	* Listener for source messages.
	*/
	private class SourceMessageListener implements MessageListener
	{
		static final String SOURCE_LINE_FORMAT = "%03d %s";

		/**
		* Called by the source whenever it produces a message.
		*
		* @param msg the message
		*/
		@Override
		public void messageReceived(Message msg)
		{
			if (msg.getType() == SOURCE_LINE) {
				Object[] body = (Object[]) msg.getBody();
				int lineNum = (Integer) body[0];
				String lineTxt = (String) body[1];

				System.out.println(
					String.format(
						SOURCE_LINE_FORMAT,
						lineNum,lineTxt));
			}
		}
	}

	private class ParserMessageListener implements MessageListener
	{
		static final String PARSER_SUMMARY_FORMAT =
			"\n%,20d source lines." +
			"\n%,20d syntax errors." +
			"\n%,20.2f seconds total parsing time.\n";
		static final String TOKEN_FORMAT =
			">>> %-15s line=%03d, pos=%2d, text=\"%s\"";
		static final String VALUE_FORMAT =
			">>>			value=%s";
		static final int PREFIX_WIDTH = 5;

		/**
		* Called by the parser whenever it produces a message.
		*
		* @param msg the message
		*/
		@Override
		public void messageReceived(Message msg)
		{
			MessageType msgType = msg.getType();

			if (msgType == PARSER_SUMMARY) {
				Object[] body = (Object[]) msg.getBody();
				int statementCount = (Integer) body[0];
				int syntaxErrors = (Integer) body[1];
				float elapsed = (Float) body[2];

				System.out.printf(
					PARSER_SUMMARY_FORMAT,
					statementCount,syntaxErrors,elapsed);
			}

			else if (msgType == TOKEN) {
				Object[] body = (Object[]) msg.getBody();
				int line = (Integer) body[0];
				int position = (Integer) body[1];
				TokenType tokenType = (TokenType) body[2];
				String tokenTxt = (String) body[3];
				Object tokenValue = body[4];

				System.out.println(
					String.format(
						TOKEN_FORMAT,
						tokenType,
						line,
						position,
						tokenTxt));

				if (tokenValue != null) {
					if (tokenType == STRING) {
						tokenValue =
							String.format("\"%s\"",tokenValue);
					}

					System.out.println(
						String.format(
							VALUE_FORMAT,tokenValue));
				}
			}

			else if (msgType == SYNTAX_ERROR) {
				Object[] body = (Object[]) msg.getBody();
				int lineNumber = (Integer) body[0];
				int position = (Integer) body[1];
				String tokenText = (String) body[2];
				String errorMsg = (String) body[3];

				int spaceCount = PREFIX_WIDTH + position;
				StringBuilder flagBuffer = new StringBuilder();

				// Spaces up to the error position.
				for (int i = 1; i < spaceCount; ++i) {
					flagBuffer.append(' ');
				}

				// A pointer to the error followed by the error
				// message.
				flagBuffer.
					append("^\n*** ").
					append(errorMsg);

				// Text, if any, of the bad token.
				if (tokenText != null) {
					flagBuffer.
						append(" [at \"").
						append(tokenText).
						append("\"]");
				}

				System.out.println(flagBuffer.toString());
			}

		}
	}

	/**
	* Listener for back end messages.
	*/
	private class BackendMessageListener implements MessageListener
	{
		static final String INTERPRETER_SUMMARY_FORMAT =
			"\n%,20d statements executed." +
			"\n%,20dd runtime erros." +
			"\n%,20.2f seconds total execution time.\n";

		static final String COMPILER_SUMMARY_FORMAT =
			"\n%,20d instructions generated." +
			"\n%,20.2f seconds total code generation time.\n";

		/**
		* Called by the back end whenever it produces a message.
		*
		* @param msg the message.
		*/
		@Override
		public void messageReceived(Message msg)
		{
			if (msg.getType() == INTERPRETER_SUMMARY) {
				Number[] body = (Number[]) msg.getBody();
				int executionCount = (Integer) body[0];
				int runtimeErrors = (Integer) body[1];
				float elapsed = (Float) body[2];

				System.out.printf(
					INTERPRETER_SUMMARY_FORMAT,
					executionCount,runtimeErrors,elapsed);
			}

			else if(msg.getType() == COMPILER_SUMMARY) {
				Number[] body = (Number[]) msg.getBody();
				int instructionCount = (Integer) body[0];
				float elapsed = (Float) body[1];

				System.out.printf(
					COMPILER_SUMMARY_FORMAT,
					instructionCount,elapsed);
			}
		}

	}

}

