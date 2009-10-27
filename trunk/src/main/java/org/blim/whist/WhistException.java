package org.blim.whist;

public class WhistException extends Exception {
	private String message;

	public WhistException()
	{
		message = "unknown";
	}
	  
	public WhistException(String message)
	{
		this.message = message;  // save message
	}
	  
	public String getMessage()
	{
		return message;
	}
}
