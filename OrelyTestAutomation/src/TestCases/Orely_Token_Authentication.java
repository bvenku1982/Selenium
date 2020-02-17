package TestCases;

import org.openqa.selenium.WebElement;
import org.ekayukta.test.ui.framework.filereader.PropertyFileReader;
import org.ekayukta.test.ui.framework.helper.InitializeWebDrive;
import org.ekayukta.test.ui.framework.helper.RepotrsHelper;
import org.ekayukta.test.ui.framework.helper.SeleniumHelper;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Orely_Token_Authentication {
	
	public String strServiceProvider = null;
	public String strDestination = null;
	public String strOTPGenerated = "Null";
	public String strSPIEnabled = null;
	InitializeWebDrive initializeWebDrive = null;
	SeleniumHelper objselHelper = null;
	RepotrsHelper objRepotrsHelper;
	//WebElement objWebElement = null;

	public void OrelyAuthentication(HashMap<String, String> hashMapTestData) throws Exception {
		try {
			initializeWebDrive = new InitializeWebDrive();
			objRepotrsHelper = new RepotrsHelper();
			objselHelper = new SeleniumHelper(initializeWebDrive.browserInit());
			Load_Properties_Env();
			try {
				orelyAuthenticationSubmit();
				orelyAuthenticationDeviceSelection();
				orelyAuthenticationValidation();
			} catch (Exception Ex) {
				System.out.println("Exception in OrelyAuthentication - " + Ex);}
		} catch (Exception ex) {
			System.out.println("Failed - ");}
		initializeWebDrive.tearDownDriver();
	}

	public void Load_Properties_Env()
	{
		PropertyFileReader.prop.setProperty("ServiceProvider",PropertyFileReader.getPropertyValue("ServiceProvider_UAT"));
		PropertyFileReader.prop.setProperty("Destination",PropertyFileReader.getPropertyValue("Destination_UAT"));

		if (PropertyFileReader.getEnvironment().equalsIgnoreCase("dev"))
		{PropertyFileReader.prop.setProperty("ServiceProvider",PropertyFileReader.getPropertyValue("ServiceProvider_DEV"));
		 PropertyFileReader.prop.setProperty("Destination",PropertyFileReader.getPropertyValue("Destination_DEV"));}

		strServiceProvider = PropertyFileReader.getPropertyValue("ServiceProvider");
		strDestination = PropertyFileReader.getPropertyValue("Destination");
	}

	public void orelyAuthenticationValidation() throws Exception {

		String strExecutionStatus = "pass";
		String strTEST_STATUS =  objselHelper.Get_Data("DT_ExpectedResult");
		String strMinQAAExpected = objselHelper.Get_Data("DT_Expected_MINQAA");
		String ActualResult;
		//Thread.sleep(2000);
		objselHelper.performOperation("eleResponseInformation","checktext","Response Information");
		objselHelper.performOperation("eleResponseID","writevisibletext","");
		objselHelper.ObjReuseWebElement = objselHelper.waitForElement("eleResponseStatus");
		if (objselHelper.ObjReuseWebElement != null) {
			System.out.println("                     RESPONSE STATUS                   :" + objselHelper.ObjReuseWebElement.getText());

			if (objselHelper.ObjReuseWebElement.getText().contains("status:Success") && "SUCCESS".contentEquals(strTEST_STATUS.toUpperCase())) {
				ActualResult = "                     Authentication Successfull";
			} else if (objselHelper.ObjReuseWebElement.getText().contains("status:AuthnInterrupt") && "CANCEL".contentEquals(strTEST_STATUS.toUpperCase())) {
				ActualResult = "                     Authentication Successfull";
			} else {
				ActualResult = "                     Authentication Fail";
				strExecutionStatus = "fail";
			}
		}
		else
		{ActualResult = "                     Authentication Fail";
			strExecutionStatus = "fail";}

		System.out.println(ActualResult);
		objRepotrsHelper.WriteResults(strExecutionStatus, "Validate Authentication", ActualResult, "Authentication Page","Validation");


		objselHelper.performOperation("eleResponseMessage","writevisibletext","");
		objselHelper.performOperation("eleResponseDetails","writevisibletext","");
		objselHelper.performOperation("eleResponseIssueInstant","writevisibletext","");
		objselHelper.performOperation("eleResponseDistinguishedName","writevisibletext","");
		objselHelper.performOperation("eleResponseDistinguishedName","writevisibletext","");
		objselHelper.ObjReuseWebElement = objselHelper.waitForElement("eleResponseTSPMode");
			ActualResult ="                    MinQAA Validation Fail";
			strExecutionStatus = "fail";
			if (objselHelper.ObjReuseWebElement != null) {
				System.out.println("                     TSP-MODE                   :" + objselHelper.ObjReuseWebElement.getText());
				if (!"NULL".equals(strMinQAAExpected.toUpperCase())) {

					if (objselHelper.ObjReuseWebElement.getText().equals(strMinQAAExpected.toUpperCase())) {
						ActualResult ="                    MinQAA Validation Pass";
						strExecutionStatus = "pass";
					}
					System.out.println(ActualResult);
					objRepotrsHelper.WriteResults(strExecutionStatus, "Validate MinQAA", ActualResult, "Authentication Page","Validation");
				}
			}
		objselHelper.performOperation("eleResponseTSPID","writevisibletext","");
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

			strOTPGenerated = ObjGeneric.generateOTP(objselHelper.Get_Data("DT_VIRTUAL_TOKEN"));

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
				objselHelper.ObjReuseWebElement = objselHelper.waitForElement("eleOTP");
				if (!strOTPGenerated.contentEquals(objselHelper.ObjReuseWebElement.getAttribute("value"))) {
					objselHelper.performOperation("eleOTP", "clearandset", strOTPGenerated);}
				objselHelper.performOperation("eleOTPAuthenticate", "click", "");

			} else if ("NO".equals(strOTP_AUTHENTICATION.toUpperCase())) {

				objselHelper.performOperation("eleAuthenticateCancel", "click", "");
			}

		}

	}


}
