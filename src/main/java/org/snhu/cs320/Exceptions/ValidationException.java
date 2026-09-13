package org.snhu.cs320.Exceptions;
//brings in the base java validations
final public class ValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4626660215334437295L;

	
	public ValidationException(String message) {
		super(message);
	}

	
}
