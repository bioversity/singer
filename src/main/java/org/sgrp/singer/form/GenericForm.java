package org.sgrp.singer.form;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.validator.ValidatorForm;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.Utils;
import org.sgrp.singer.db.UserManager;

@SuppressWarnings("serial")
public class GenericForm extends ValidatorForm {
	public static final String	defaultDateFormat	= "dd/MM/yyyy";
	public static final String EMAIL_REGEX="^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
	public static final String URL_REGEX="^(ftp|http|https):(//(([a-zA-Z0-9\\-_\\.!~\\*'\\(\\)%;:&\\+\\$,]*@)?(([a-zA-Z0-9][a-zA-Z0-9-]*\\.)*[a-zA-Z][a-zA-Z0-9]*|[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)(:[0-9]+)?)(/[a-zA-Z0-9\\-_\\.!~\\*'\\(\\)%:@&=\\+\\$,]*(;[a-zA-Z0-9\\-_\\.!~\\*'\\(\\)%:@&=\\+\\$,]*)*(/[a-zA-Z0-9\\-_\\.!~\\*'\\(\\)%:@&=\\+\\$,]*(;[a-zA-Z0-9\\-_\\.!~\\*'\\(\\)%:@&=\\+\\$,]*)*)*)?)(\\?[;/\\?:@&=\\+\\$,a-zA-Z0-9\\-_\\.!~\\*'\\(\\)%]+)?$";
	public static String getCleanString(String value) {
		StringBuffer sb = new StringBuffer();
		char arr[] = value.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			char c = arr[i];
			if (Character.isISOControl(c) && !Character.isWhitespace(c)) {
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static Date getDate(String value) {
		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(defaultDateFormat);
		try {
			date = dateFormat.parse(value);
		} catch (Exception e) {
			date = null;
			e.printStackTrace();
		}
		return date;
	}

	public static String getlongAsDate(long value, String format) {
		String dateStr = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			Date date = new Date(value);
			dateStr = dateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateStr;
	}

	public static String getlongAsDate(String value) {
		return getlongAsDate(value, defaultDateFormat);
	}

	public static String getlongAsDate(String value, String format) {
		if (value != null) {
			return getlongAsDate(Long.parseLong(value), format);
		} else {
			return null;
		}

	}

	public String	action;

	public String	member;

	public String	nextPage;

	public String	status;

	public GenericForm() {
		super();
	}

	public String getAction() {
		return action;
	}

	public String getMember() {
		return member;
	}

	public String getNextPage() {
		return nextPage;
	}

	public String getStatus() {
		return status;
	}

	public String[] getStringArrayfromString(String str) {

		StringTokenizer st = new StringTokenizer(str, " ", false);
		String strArr[] = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreElements()) {
			String token = (String) st.nextElement();
			strArr[i] = token;
			i++;
		}
		return strArr;
	}

	public String getStringfromStringArray(String strArr[]) {
		StringBuffer sb = new StringBuffer();
		if ((strArr != null) && (strArr.length > 0)) {
			for (String element : strArr) {
				sb.append(element + " ");
			}
		}

		return sb.toString().trim();
	}

	/*
	 * public static String getlongAsDate(long value) { return getlongAsDate(value, defaultDateFormat); }
	 */

	public boolean isNull(String value) {
		return AccessionConstants.isNull(value);
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static boolean doLogin(HttpServletRequest request, String userid, String password)
	{
		boolean loggedIn = false;
		try {
		String name = userid;
		HttpSession session = request.getSession(true);
		session.setAttribute(AccessionConstants.AUTHORIZATION, "Basic " + Utils.b64encode(userid+ ":" + password));
			PIDMemberForm memberForm = UserManager.getInstance().getUser(request.getSession());
			name = memberForm.getNfname() + " " + memberForm.getNlname();
		// System.out.println("User logged with " + getUserid() + " and name " + name);
		//request.getSession().setAttribute(AccessionConstants.SYSUSERNAME, name);
		//request.getSession().setAttribute(AccessionConstants.SYSUSERID, userid);
		loggedIn = true;
		} catch (Exception e) {
			loggedIn = false;
		}
		return loggedIn;
		
	}
}
