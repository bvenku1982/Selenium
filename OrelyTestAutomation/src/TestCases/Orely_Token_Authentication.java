package TestCases;



import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.ekayukta.test.ui.framework.filereader.PropertyFileReader;
import org.ekayukta.test.ui.framework.helper.InitializeWebDrive;
import org.ekayukta.test.ui.framework.helper.RepotrsHelper;
import org.ekayukta.test.ui.framework.helper.SeleniumHelper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class Orely_Token_Authentication {
	
	public String strServiceProvider = null;
	public String strDestination = null;
	public String strOrelyEnvironmnet = null;
	public String appOrelyURL = null;
	public String strOTPGenerated = "Null";
	public String strOTPDpxFilePath = null;
	public String strVirtualToken = null;
	public String strDpxKey = null;
	public String strSPIEnabled = null;
	public String strBrowser = null;
	InitializeWebDrive initializeWebDrive = null;
	SeleniumHelper objselHelper = null;
	RepotrsHelper objRepotrsHelper;
	WebDriver WebDriver = null;
	WebElement objWebElement = null;

	public void OrelyAuthentication(HashMap<String, String> hashMapTestData) throws Exception {
		try {
			//Boolean s = linkExists("https://orely.dev.luxtrust.net/demo/startParametrized.jsp".toString());
			initializeWebDrive = new InitializeWebDrive();
			WebDriver = initializeWebDrive.browserInit();
			objRepotrsHelper = new RepotrsHelper();
			objselHelper = new SeleniumHelper(WebDriver);

			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
			LocalDateTime startTime = LocalDateTime.now();
			String strPath = Paths.get("").toAbsolutePath().toString() + File.separator + "Resources" + File.separator;
			String jxlTestData = strPath + "TestData.xls";
			strOTPDpxFilePath = strPath + "OTPGeneratorVacman" + File.separator
					+ "2015-10-06_10000_first_9000_it_dev.dpx";
			jxlTestData = new File(jxlTestData).toString();


			strServiceProvider = PropertyFileReader.getPropertyValue("ServiceProvider");
			strDestination = PropertyFileReader.getPropertyValue("Destination");
			strOrelyEnvironmnet = PropertyFileReader.getPropertyValue("Environment");
			appOrelyURL = PropertyFileReader.getPropertyValue("Website");
			strSPIEnabled = PropertyFileReader.getPropertyValue("SPIEnabled");
			strDpxKey = PropertyFileReader.getPropertyValue("DpxKey");
			
			strBrowser = PropertyFileReader.getPropertyValue("Browser");

			String strTempTime = startTime.format(myFormatObj);
			strTempTime = strTempTime.toString().replace("-", "").replace(":", "-");

			Path OutPutPath = Paths.get(strPath + "OutPut" + File.separator + strOrelyEnvironmnet + strTempTime);
			Files.createDirectories(OutPutPath);
			Files.createDirectories(Paths.get(OutPutPath + File.separator + "log"));
			String jxlTestExecutionReports = OutPutPath + File.separator + "log" + File.separator + "OutPut_"
					+ strTempTime + ".xls";

			System.out.println("	jxlTestData - " + jxlTestData);
			System.out.println("	logTextFile - " + OutPutPath + File.separator + "log" + File.separator + "OutPut_"
					+ strTempTime + ".txt");
			System.out.println("	jxlTestExecutionReports - " + jxlTestExecutionReports);
			
			try {

				orelyAuthenticationSubmit();
				orelyAuthenticationDeviceSelection();
				orelyAuthenticationValidation();

				LocalDateTime endTime = LocalDateTime.now();
				long minutes = startTime.until(endTime, ChronoUnit.MINUTES);
				long seconds = startTime.until(endTime, ChronoUnit.SECONDS);
				System.out.println("Test Execution Ended at : " + endTime.format(myFormatObj));
				System.out.println("Total Test Execution time : " + minutes + " Minutes " + seconds + " Seconds");
			} catch (Exception Ex) {
				System.out.println("Exception in OrelyAuthentication - " + Ex);

			}

		} catch (Exception ex) {
			System.out.println("Failed - ");
		}
		initializeWebDrive.tearDownDriver();

	}
	public void orelyAuthenticationValidation() throws Exception {

		String strExecutionStatus = "pass";
		String strTEST_STATUS =  objselHelper.Get_Data("DT_ExpectedResult");
		String strMinQAAExpected = objselHelper.Get_Data("DT_Expected_MINQAA");
		String ActualResult;

		//Thread.sleep(2000);
		objselHelper.performOperation("eleResponseInformation","checktext","Response Information");
		/*objWebElement = objselHelper.waitForElement("eleResponseInformation");
		if (objWebElement != null) {
			objRepotrsHelper.WriteResults("fail", "Validate Authentication", "Authentication page not displayed", "Authentication Page","Validation");
		return;
		}
*/
		objselHelper.performOperation("eleResponseID","writevisibletext","");
			/*objWebElement = objselHelper.waitForElement("eleResponseID");

			if (objWebElement != null) {
				System.out.println("                     ID                         :" + objWebElement.getText());
			}*/
			objWebElement = objselHelper.waitForElement("eleResponseStatus");
			if (objWebElement != null) {
				System.out.println("                     RESPONSE STATUS                   :" + objWebElement.getText());

				if (objWebElement.getText().contains("status:Success") && "SUCCESS".contentEquals(strTEST_STATUS.toUpperCase())) {
					ActualResult = "                     Authentication Successfull";
				} else if (objWebElement.getText().contains("status:AuthnInterrupt") && "CANCEL".contentEquals(strTEST_STATUS.toUpperCase())) {
					ActualResult = "                     Authentication Successfull";
				} else {
					ActualResult = "                     Authentication Fail";

					strExecutionStatus = "fail";
				}
			}
			else
			{
				ActualResult = "                     Authentication Fail";
				strExecutionStatus = "fail";
			}

			System.out.println(ActualResult);
			objRepotrsHelper.WriteResults(strExecutionStatus, "Validate Authentication", ActualResult, "Authentication Page","Validation");


			objselHelper.performOperation("eleResponseMessage","writevisibletext","");
			/*objWebElement = objselHelper.waitForElement("eleResponseMessage");

			if (objWebElement != null) {
				System.out.println("                     RESPONSE MESSAGE           :" + objWebElement.getText());
			}
*/
			objselHelper.performOperation("eleResponseDetails","writevisibletext","");
			/*
			objWebElement = objselHelper.waitForElement("eleResponseDetails");
			if (objWebElement != null) {
				System.out.println("                     RESPONSE DETAILS           :" + objWebElement.getText());
			}
*/
			objselHelper.performOperation("eleResponseIssueInstant","writevisibletext","");
			/*
			objWebElement = objselHelper.waitForElement("eleResponseIssueInstant");
			if (objWebElement != null) {
				System.out.println("                     ISSUE INSTANT              :" + objWebElement.getText());
			}
*/
			objselHelper.performOperation("eleResponseDistinguishedName","writevisibletext","");


/*
			objWebElement = objselHelper.waitForElement("eleResponseDistinguishedName");
			if (objWebElement != null) {
				System.out.println("                     DISTINGUISHED NAME         :" + objWebElement.getText());
			}
*/

			objselHelper.performOperation("eleResponseDistinguishedName","writevisibletext","");


			objWebElement = objselHelper.waitForElement("eleResponseTSPMode");
			ActualResult ="                    MinQAA Validation Fail";
			strExecutionStatus = "fail";
			if (objWebElement != null) {
				System.out.println("                     TSP-MODE                   :" + objWebElement.getText());
				if (!"NULL".equals(strMinQAAExpected.toUpperCase())) {

					if (objWebElement.getText().equals(strMinQAAExpected.toUpperCase())) {
						ActualResult ="                    MinQAA Validation Pass";
						//System.out.println("                    MinQAA Validation Pass");
						//objSeleniumHelper.writeXLResults(jxlTestExecutionReports, intXLResultsRowNum, orelyTCNumber,strTEST_STATUS, "PASS");
						strExecutionStatus = "pass";
					} /*else {
						//System.out.println("                    MinQAA Validation Fail");
						//objSeleniumHelper.writeXLResults(jxlTestExecutionReports, intXLResultsRowNum, orelyTCNumber,strTEST_STATUS, "FAIL");
						strExecutionStatus = "FAIL";
					}*/
					System.out.println(ActualResult);
					objRepotrsHelper.WriteResults(strExecutionStatus, "Validate MinQAA", ActualResult, "Authentication Page","Validation");

				}

			}

		objselHelper.performOperation("eleResponseTSPID","writevisibletext","");
			/*
			objWebElement = objselHelper.waitForElement("eleResponseTSPID");
			if (objWebElement != null) {
				System.out.println("                     TSP-ID                     :" + objWebElement.getText());
			}
*/



	}


	public boolean linkExists(String URLName) {
		try {
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con =
					(HttpURLConnection) new URL(URLName).openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void orelyAuthenticationSubmit() throws Exception {

		objselHelper.performOperation("eleRequestType##DT_REQUEST_TYPE", "click", "");
		objselHelper.performOperation("eleCredentialToUse##ENV_ServiceProvider", "click", "");
		objselHelper.performOperation("eleDestination##ENV_Destination", "click", "");

		String strMinQAA = objselHelper.Get_Data("DT_MINQAA");
		if (!"NULL".equals(strMinQAA.toUpperCase())) {

			if ("NOVALUE".equals(strMinQAA.toUpperCase())) {
				objselHelper.performOperation("eleMinQAA", "clear", "");
			} else {
				objselHelper.performOperation("eleMinQAA", "clearandset", "DT_MINQAA");
			}
		}

		objselHelper.performOperation("eleTspId", "setnotnull", "DT_TSP_ID");

		String strTSP_MODE = objselHelper.Get_Data("DT_TSP_MODE");
		if (!"NULL".equals(strTSP_MODE.toUpperCase())) {
			objselHelper.performOperation("eleTspMode##DT_TSP_MODE", "click", "");
		}

		objselHelper.performOperation("eleSubjectID", "setnotnull", "DT_SUBJECT_ID");

		
		String strCERTIFICATE_NEEDED = objselHelper.Get_Data("DT_CERTIFICATE_NEEDED");
		if (!"NULL".equals(strCERTIFICATE_NEEDED.toUpperCase())) {
			objselHelper.performOperation("eleReqFullCertificate##DT_CERTIFICATE_NEEDED", "click", "");
		}

		String strSPI_CHANGE = objselHelper.Get_Data("DT_SPI_CHANGE");
		if (!"NULL".equals(strSPI_CHANGE.toUpperCase())) {
			objselHelper.performOperation("eleChangeSpi##DT_SPI_CHANGE", "click", "");
		}

		objselHelper.performOperation("eleSubmit", "click", "");

	}

	public void orelyAuthenticationDeviceSelection() throws Exception {
		
		Generic ObjGeneric = new Generic();

		String strDEVICE_TYPE = null;

		String strTSP_ID = null;
		String strTSP_MODE = null;
		String strSUBJECT_ID = null;
		String strSPI_CHANGE = null;
		String strPASS_AUTHENTICATION = null;
		String strOTP_AUTHENTICATION = null;

		strDEVICE_TYPE = objselHelper.Get_Data("DT_DEVICE_TYPE");
		if ("GO6".equals(strDEVICE_TYPE.toUpperCase())) {

			String strOTPGenerated = ObjGeneric.generateOTP(objselHelper.Get_Data("DT_VIRTUAL_TOKEN"));

			strTSP_ID = objselHelper.Get_Data("DT_TSP_ID");
			if ("NULL".equals(strTSP_ID.toUpperCase())) {
				objselHelper.performOperation("eleTokenSelection", "click", "");
			}

			strTSP_MODE = objselHelper.Get_Data("DT_TSP_MODE");
			strSUBJECT_ID = objselHelper.Get_Data("DT_SUBJECT_ID");
			if ("LTAS".equals(strTSP_MODE.toUpperCase())) {
				if ("NULL".equals(strSUBJECT_ID.toUpperCase())) {
					// User name , Password and OTP					
					objselHelper.performOperation("eleUserName", "set", "DT_USER_NAME");
					objselHelper.performOperation("elePassword", "set", "DT_PASSWORD");
				} else {
					// Only OTP screen
				}

			} else if ("FULL".equals(strTSP_MODE.toUpperCase())) {

				if ("NULL".equals(strSUBJECT_ID.toUpperCase())) {
					// User name , Password and OTP
					objselHelper.performOperation("eleUserName", "set", "DT_USER_NAME");
					objselHelper.performOperation("elePassword", "set", "DT_PASSWORD");
				} else {
					// Password and OTP
					objselHelper.performOperation("elePassword", "set", "DT_PASSWORD");
				}
			} else if ("NULL".equals(strTSP_MODE.toUpperCase())) {
				if ("NULL".equals(strSUBJECT_ID.toUpperCase())) {
					// User name , Password and OTP
					objselHelper.performOperation("eleUserName", "set", "DT_USER_NAME");
					objselHelper.performOperation("elePassword", "set", "DT_PASSWORD");
				} else {
					// Password and OTP
					objselHelper.performOperation("elePassword", "set", "DT_PASSWORD");
				}

			}

			

			if ("YES".equals(strSPIEnabled.toUpperCase())) {
				// SPI is enabled
				objselHelper.performOperation("eleOTPAuthenticate", "click", "");
			}
			strSPI_CHANGE = objselHelper.Get_Data("DT_SPI_CHANGE");
			if ("YES".equals(strSPI_CHANGE.toUpperCase())) {
				objselHelper.performOperation("eleChangeSpiImage", "click", "");
				objselHelper.performOperation("eleSpiImageSet", "click", "");
			}
			strPASS_AUTHENTICATION = objselHelper.Get_Data("DT_PASS_AUTHENTICATION");
			if (!"YES".equals(strPASS_AUTHENTICATION.toUpperCase())) {
				objselHelper.performOperation("eleAuthenticateCancel", "click", "");
				// Cancel the Authentication
			}

			strOTP_AUTHENTICATION = objselHelper.Get_Data("DT_OTP_AUTHENTICATION");
			if ("YES".equals(strOTP_AUTHENTICATION.toUpperCase())) {
				// Cancel the Authentication
				objselHelper.performOperation("eleOTP", "clearandset", strOTPGenerated);
				objWebElement = objselHelper.waitForElement("eleOTP");
				if (!strOTPGenerated.contentEquals(objWebElement.getAttribute("value"))) {
					objselHelper.performOperation("eleOTP", "clearandset", strOTPGenerated);
				}

				objselHelper.performOperation("eleOTPAuthenticate", "click", "");

			} else if ("NO".equals(strOTP_AUTHENTICATION.toUpperCase())) {

				objselHelper.performOperation("eleAuthenticateCancel", "click", "");
			}

		}

	}


}
