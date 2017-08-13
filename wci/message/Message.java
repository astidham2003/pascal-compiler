package wci.message;

/**
* <h1>A message to be passed on to listeners.</h1>
*/
public class Message
{

	private Object body;
	private MessageType type;

	/**
	* Constructor.
	* @param type the message type
	* @param body the message body
	*/
	public Message(MessageType type,Object body)
	{
		this.body = body;
		this.type = type;
	}

	public Object getBody()
	{
		return body;
	}

	public MessageType getType()
	{
		return type;
	}

}

