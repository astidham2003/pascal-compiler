package wci.message;

public interface MessageProducer
{

	/**
	* Add a listener to the listener list.
	* @param listener the listener to add
	*/
	public void addMessageListener(MessageListener listener);

	/**
	* Remove a listener from the listener list.
	* @param listener the listener to remove
	*/
	public void removeMessageListener(MessageListener listener);

	/**
	* Notify listners after sending a message.
	* @param msg the message to set
	*/
	public void sendMessage(Message msg);

}

