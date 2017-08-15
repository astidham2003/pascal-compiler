package wci.frontend;

/**
* <h1>EofToken</h1>
* 
* <p>The generic,language independent, end-of-file token.</p>
*/
public class EofToken extends Token
{

	/**
	* Constructor.
	*
	* @param source the source from where to fetch
	* subsequent characters
	* @throws Exception
	*/
	public EofToken(Source source) throws Exception
	{
		super(source);
	}

	/**
	* Constructor.
	*
	* @param source the source from where to fetch the token's
	* subsequent characters
	* @param the token type
	* @throws Exception
	*/
	public EofToken(Source source,TokenType type) throws Exception
	{
		this(source);
		this.type = type;
	}

	// ----------------------------------------------------------------
	// Token methods

	/**
	* Do nothing.
	* @param source the source from where to fetch the token's
	* characters.
	* @throws Exception
	*/
	@Override
	protected void extract() throws Exception
	{
	}

}

