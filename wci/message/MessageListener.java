package wci.message;

public interface MessageListener
{

	/**
	* Called to receive a message sent by a message producer.
	* @param msg the message that was sent
	*/
	public void messageReceived(Message msg);

}

