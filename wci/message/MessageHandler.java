package wci.message;

import java.util.ArrayList;

/**
* <h1>MessageHandler</h1>
* 
* <p>A helper class to which message producer classes delegate
* the task of maintaining and notifying listeners.</p>
*/
public class MessageHandler
{
	private Message message;
	private ArrayList<MessageListener> listeners;

	/**
	* Constructor.
	*/
	public MessageHandler()
	{
		this.listeners = new ArrayList<MessageListener>();
	}

	/**
	* Add a listener to the listener list.
	* @param listener the listener to add
	*/
	public void addListener(MessageListener listener)
	{
		listeners.add(listener);
	}

	/**
	* Remove a listener from the listener list.
	* @param listener the listener to remove
	*/
	public void removeListener(MessageListener listener)
	{
		listeners.remove(listener);
	}

	/**
	* Notify listeners after setting message.
	* @param msg the message to set
	*/
	public void sendMessage(Message msg)
	{
		this.message = msg;
		notifyListeners();
	}

	/**
	* Notify each listener in the listener list.
	*/
	private void notifyListeners()
	{
		for(MessageListener listener : listeners) {
			listener.messageReceived(message);
		}
	}

}

