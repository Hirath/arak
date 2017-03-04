package com.hirath.arak;

import static java.lang.String.format;

public class NoResponseForMethodException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	static final String MESSAGE_FORMAT = "No Supplier found when calling method : \"%s\""; 
	public NoResponseForMethodException(String method) {
		super(format(MESSAGE_FORMAT, method));
	}
}
