<%! public static boolean isLoggedIn(HttpSession session)
{
	boolean isLoggedIn = false;
	String luserName = (String) session.getAttribute("sysusername");
	if (luserName != null && luserName.trim().length() > 0) {
		isLoggedIn = true;
	}
	return isLoggedIn;
}
public static String getUserName(HttpSession session)
{
	return (String) session.getAttribute(org.sgrp.singer.AccessionConstants.SYSUSERNAME);
}

public static String getUserID(HttpSession session)
{
	return (String) session.getAttribute(org.sgrp.singer.AccessionConstants.SYSUSERID);
}
public static boolean isAllowed(HttpSession session)
{
	return true;
}
%>
