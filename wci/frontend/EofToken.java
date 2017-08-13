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
	* @param source the source from where to fetch
	* subsequent characters.
	* @throws Exception
	*/
	public EofToken(Source source) throws Exception
	{
		super(source);
	}

	/**
	* Do nothing.
	* @param source the source from where to fetch the token's
	* characters.
	* @throws Exception
	*/
	protected void extract(Source source)
	{
	}

}

