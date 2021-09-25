package de.ervk.rcon.exceptions;

import java.io.IOException;

public class MalformedPacketException extends IOException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MalformedPacketException(String message) {
		super(message);
	}
	
}
