package org.sgrp.singer;

import org.apache.struts.action.ActionMessage;

public class ErrorConstants {

	public static final String	ERROR_INVALID_LOGIN	= "errors.invalid.login";

	public static final String	ERROR_PASSWORD_NULL	= "errors.password.null";

	public static final String	ERROR_USERID_NULL	= "errors.userid.null";
	
	public static final String	ERROR_OLD_SINGER_LOGIN = "errors.old.singer.login";
	
	public static final String ERROR_NOT_A_NUMBER ="errors.field.mustbenumeric";
	
	public static final String ERROR_NOT_AN_INTEGER="errors.field.mustbeinteger";
	
	public static final String ERROR_INVERSED_DATES="errors.inversed.dates";

	public static ActionMessage getActionMessage(String errorName) {
		return new ActionMessage(errorName);
	}
	
	public static ActionMessage getActionMessage(String errorName, Object replacement)
	{
		return new ActionMessage(errorName, replacement);
	}
}
