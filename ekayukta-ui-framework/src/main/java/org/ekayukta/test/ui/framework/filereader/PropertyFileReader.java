package org.ekayukta.test.ui.framework.filereader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.log4j.Level;

import org.ekayukta.test.ui.framework.browserconfig.BrowserType;
import org.ekayukta.test.ui.framework.helper.IconfigReader;
import org.ekayukta.test.ui.framework.helper.ResourceHelper;


public class PropertyFileReader implements IconfigReader {

	public static Properties prop = null;

	public static void load() {
		prop = new Properties();
		try {
			prop.load(ResourceHelper
					.getResourcePathInputStream("/configfile/"
							+ "config.properties"));
			String strOperatingSystem = System.getProperty("os.name");
			InetAddress ipAddr = InetAddress.getLocalHost();
			String strSystemIP = ipAddr.getHostAddress();
			String strMachineName = InetAddress.getLocalHost().getHostName();
			prop.setProperty("SystemIP",strSystemIP);
			prop.setProperty("OS",strOperatingSystem);
			prop.setProperty("ComputerName",strMachineName);

			prop.setProperty("TestData",getPropertyValue("TestData_UAT"));
			prop.setProperty("Website",getPropertyValue("Website_UAT"));

			if (getEnvironment().equalsIgnoreCase("dev"))
			{
				prop.setProperty("TestData",getPropertyValue("TestData_DEV"));
				prop.setProperty("Website",getPropertyValue("Website_DEV"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public PropertyFileReader(String fileName) {

		prop = new Properties();
		try {
			prop.load(ResourceHelper
					.getResourcePathInputStream("configfile/"
							+ fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public static String getPropertyValue(String Property) {
		return prop.getProperty(Property);
	}

	public static String GetCurrentReleaseJson()
	{return prop.getProperty("CurrentReleaseJson");}
	public static String GetCurrentReleaseJs()
	{return prop.getProperty("CurrentReleaseJs");}
	public static String GetExecutionDetailsJson()
	{return prop.getProperty("ExecutionDetailsJson");}
	public static String GetExecutionDetailsJs()
	{return prop.getProperty("ExecutionDetailsJs");}

	public static String getUserName()
	{
		return prop.getProperty("Username");
	}

	public static String getPassword() {
		return prop.getProperty("Password");
	}

	public static String getWebsite() {
		return prop.getProperty("Website");
	}


	
	public static String getEnvironment() {
		return prop.getProperty("Environment");
	}
	public static String getRegion() {
		return prop.getProperty("Region");
	}
	public static String getRelease() {
		return prop.getProperty("Release");
	}
	
	public static boolean getCaptureScreenshots() {
		return Boolean.parseBoolean(prop.getProperty("CaptureScreenshots"));
	}
	
	public static String getTestDataFilePath() {
		return prop.getProperty("TestDataFilePath");
	}

	public static int getPageLoadTimeOut() {
		return Integer.parseInt(prop.getProperty("PageLoadTimeOut"));
	}

	public static int getImplicitWait() {
		return Integer.parseInt(prop.getProperty("ImplicitWait"));
	}

	public static int getExplicitShortWait() {
		return Integer.parseInt(prop.getProperty("ExplicitWait_Short"));
	}

	
	public static int getExplicitLongWait() {
		return Integer.parseInt(prop.getProperty("ExplicitWait_Long"));
	}
	
	
	public static int getExplicitVeryLongWait() {
		return Integer.parseInt(prop.getProperty("ExplicitWait_veryLong"));
	}
	
	
	public static int getExplicitWait() {
		return Integer.parseInt(prop.getProperty("ExplicitWait"));
	}
	
	
	
	public String getDbType() {
		return prop.getProperty("DataBase.Type");
	}

	public String getDbConnStr() {
		return prop.getProperty("DtaBase.ConnectionStr");
	}

	public static BrowserType getBrowser() {
		return BrowserType.valueOf(prop.getProperty("Browser"));
	}
	
	public Level getLoggerLevel() {
		
		switch (prop.getProperty("Logger.Level")) {
		
		case "DEBUG":
			return Level.DEBUG;
		case "INFO":
			return Level.INFO;
		case "WARN":
			return Level.WARN;
		case "ERROR":
			return Level.ERROR;
		case "FATAL":
			return Level.FATAL;
		}
		return Level.ALL;
	}

	public void ClearRepository(Path ptFileLocation)  {

		Properties prop = new Properties();
		FileOutputStream objOutPutFile;
		File objFileLocation = new File(ptFileLocation.toString());
				
		try {
			objOutPutFile = new FileOutputStream(objFileLocation);
			ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
			prop.store(arrayOut, null);
			String string = new String(arrayOut.toByteArray(), "8859_1");
			String sep = System.getProperty("line.separator");
			String content = string.substring(string.indexOf(sep) + sep.length());
			objOutPutFile.write(content.getBytes("8859_1"));
			objOutPutFile.close();
		}catch (Exception Ex) {
			System.out.println(Ex);  
		}
	}
}
