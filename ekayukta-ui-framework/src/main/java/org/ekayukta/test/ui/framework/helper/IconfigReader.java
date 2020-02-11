package org.ekayukta.test.ui.framework.helper;

import org.ekayukta.test.ui.framework.browserconfig.BrowserType;

public interface IconfigReader {
	 static String getUserName()
	{return "username";}
	 static String getPassword()
	{return "username";}
	 static String getWebsite()
	{return "username";}
	 static String getEnvironment()
	{return "username";}
	 static int getPageLoadTimeOut()
	{return 10;}
	 static int getImplicitWait()
	{return 10;}
	 static int getExplicitWait()
	{return 10;}
	 static BrowserType getBrowser()
	{return BrowserType.Chrome;}
	 static String getPropertyValue()
	{return "value";}
	static String GetCurrentReleaseJson()
	{return "value";}
	static String GetCurrentReleaseJs()
	{return "value";}
	static String GetExecutionDetailsJson()
	{return "value";}
	static String GetExecutionDetailsJs()
	{return "value";}

}
