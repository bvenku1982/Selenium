package org.ekayukta.test.ui.framework.helper;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.ekayukta.test.ui.framework.filereader.ExcelFileReader;
import org.ekayukta.test.ui.framework.filereader.PropertyFileReader;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class RepotrsHelper {


	Date dtBatchStartDate,dtTestCaseStartDate;
	String strTestSuiteStartDate,strTestCaseStartDate,strTestCaseEndDate;
	String strDateStamp;
	JSONObject objTestSuiteDetails;
	public String strCurrentReleaseJSONFilePath;
	public String strDashboardPath;
	public Path ptDashboardPath;
	String strExecutionDetailsJSonFile;
	String strTotalTimeForTC;
	public static AtomicInteger passcount;
	public static AtomicInteger failcount;
	public static AtomicInteger warningcount;
	public static AtomicInteger stepNo;


	public static String strCurrentTestCaseId;
	public RepotrsHelper()
	{
		objTestSuiteDetails = new JSONObject();

	}

	public String FormatCurrentDateTime()
	{
		Date dtnow = new Date();
		return dtnow.toString();
	}



	public String DateTimeDiff(Date strPastDate)
	{
		Date dtnow = new Date();


		long diff = dtnow.getTime() - strPastDate.getTime();//as given
		long seconds = diff / 1000 % 60;
		long minutes = diff / (60 * 1000) % 60;
		long Hours = diff / (60 * 60 * 1000) % 24;

		String strHours = null;
		String strMinutes = null;
		String strSeconds = null;

		if (Hours >= 0 & Hours <=9 )
		{
			strHours = "0" + Hours;
		}else
		{
			strHours = ""+Hours;
		}
		if (minutes >= 0 &  minutes <= 9)
		{
			strMinutes = "0" + minutes;
		}else
		{
			strMinutes = "" + minutes;
		}
		if (seconds >= 0 & seconds <=9)
		{
			strSeconds =  "0"+seconds;
		}else
		{
			strSeconds =  ""+seconds;
		}

		return strHours + " : "+ strMinutes +" : "+ strSeconds;
	}

	public void CreateMainHTMLHeader_01()
	{

		strCurrentReleaseJSONFilePath = PropertyFileReader.GetCurrentReleaseJson();
		//prop = PropertyFileReader.prop;
		try {

			String browserType = PropertyFileReader.getBrowser().toString();
			String strEnviroment = PropertyFileReader.getEnvironment();
			String strRegion = PropertyFileReader.getRegion();
			String strProjectRelease = PropertyFileReader.getRelease();
			String strProjectName = PropertyFileReader.getPropertyValue("Project");
			String strMachineName = PropertyFileReader.getPropertyValue("ComputerName");
			String strExecutionUserName = System.getProperty("user.name");
			//boolean blCaptureScreenshots = objPropertyFileReader.getCaptureScreenshots();
			//int intImplicitly_Wait = objPropertyFileReader.getImplicitWait();
			//int intThinkTime_Short = objPropertyFileReader.getExplicitShortWait();
			//int intThinkTime_Long = objPropertyFileReader.getExplicitLongWait();
			//int intThinkTime_VeryLong = objPropertyFileReader.getExplicitVeryLongWait();

			DateFormat strDateConverot = new SimpleDateFormat("dd/MM/yyyy");
			Date dtnow = new Date();

			//stepTCNo = 0;

			dtBatchStartDate = dtnow;
			String strOnlyDate = strDateConverot.format(dtnow);
			strTestSuiteStartDate = dtnow.toString();
			String TempDate = strTestSuiteStartDate;
			strDateStamp = strTestSuiteStartDate.replace("/", "_");
			strDateStamp = strDateStamp.replace(" ", "_");
			strDateStamp = strDateStamp.replace(":", "_");

			//TCsFailcount = 0;
			//TCsPasscount = 0;

			//String strFeatureFileName = "Scenarios Execution";
			//String strFeatureFileDescription = "Execution of Selenium scripts";


			//File srcDir = new File(ReportsPath + "/BaseReports");
			//File ReportsDestDir = new File(srcDir + strDateStamp);

			//ReportsDestinationLocation = ReportsDestDir.getAbsolutePath();
			//FileUtils.copyDirectory(srcDir, ReportsDestDir);


			//##########################################################################
			strDashboardPath = strCurrentReleaseJSONFilePath.replace("CurrentRelease.json", strEnviroment + "_" + strProjectName + "_" + strProjectRelease +  File.separator + strDateStamp);
			ptDashboardPath = Paths.get(strDashboardPath);
			strDashboardPath = Paths.get(strDashboardPath).toString();
			FileUtils.forceMkdir(new File(strDashboardPath));
			PropertyFileReader.prop.setProperty("DashboardPath",strDashboardPath);
			//##########################################################################
			//FileWriter ExecutionDetailsFileWritter1 = new FileWriter(DriverScript.strExecutionDetailsJSonFilePath);
			//JSONArray arrExecutionDetails1 = new JSONArray();
			//JSONObject ExecutionDetails1 = new JSONObject();

			strExecutionDetailsJSonFile = PropertyFileReader.GetExecutionDetailsJson();
			File fsExecutionDetailsJSonFile1 = new File(strExecutionDetailsJSonFile);
			FileWriter fwExecutionDetails1 = null;

			JSONParser parser1 = new JSONParser();
			Reader reader1 = new FileReader(strExecutionDetailsJSonFile);
			Object jsonObj1 = parser1.parse(reader1);
			JSONObject jsonObject1 = (JSONObject) jsonObj1;

			JSONArray ExecutionDetails1 = (JSONArray) jsonObject1.get("CurrentExecution");
			JSONObject jsonCurrentExecutionDetailsObject1 = (JSONObject) ExecutionDetails1.get(0);
			JSONArray PreviousExecutionDetails1 = (JSONArray) jsonObject1.get("PreviousExecutionDetails");
			PreviousExecutionDetails1.add(0, jsonCurrentExecutionDetailsObject1);
			//PreviousExecutionDetails1.add(jsonCurrentExecutionDetailsObject1);
			fwExecutionDetails1 = new FileWriter(fsExecutionDetailsJSonFile1);
			fwExecutionDetails1.write(jsonObj1.toString());
			fwExecutionDetails1.close();
			updateExecutionDetailsJsonToJs(true);

			File fsExecutionDetailsJSonFile = new File(strExecutionDetailsJSonFile);
			FileWriter fwExecutionDetails;

			JSONParser parser = new JSONParser();
			Reader reader = new FileReader(strExecutionDetailsJSonFile);
			Object jsonObj = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) jsonObj;

			JSONArray ExecutionDetails = (JSONArray) jsonObject.get("CurrentExecution");
			JSONObject jsonCurrentExecutionDetailsObject = (JSONObject) ExecutionDetails.get(0);
			//JSONObject jsonSubObject = (JSONObject) ExecutionDetails.get(0);
			//if(jsonSubObject.get("TestExecutionID").toString().equalsIgnoreCase(DriverScript.strTSExecutionID))
			//{
			String strReleaseFromJson = jsonCurrentExecutionDetailsObject.get("Release").toString();
			jsonCurrentExecutionDetailsObject.remove("Release", strReleaseFromJson);
			jsonCurrentExecutionDetailsObject.put("Release", strProjectRelease);

			String strExecutionDate = jsonCurrentExecutionDetailsObject.get("ExecutionDate").toString();
			jsonCurrentExecutionDetailsObject.remove("ExecutionDate", strExecutionDate);
			jsonCurrentExecutionDetailsObject.put("ExecutionDate", TempDate);

			String strRun = jsonCurrentExecutionDetailsObject.get("Run").toString();
			jsonCurrentExecutionDetailsObject.remove("Run", strRun);

			SimpleDateFormat DateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			Date ParsedDate1 = DateFormat.parse(strExecutionDate);
			Date ParsedDate2 = DateFormat.parse(TempDate);
			String strParsedDate1 = new SimpleDateFormat("dd/MM/yyyy").format(ParsedDate1);
			String strParsedDate2 = new SimpleDateFormat("dd/MM/yyyy").format(ParsedDate2);

			if (strReleaseFromJson.contentEquals(strProjectRelease) && strParsedDate1.contentEquals(strParsedDate2)) {
				int intRun = Integer.parseInt(strRun.split(" ")[1]);
				intRun = intRun + 1;

				String strTempRun = "Run " + intRun;
				jsonCurrentExecutionDetailsObject.put("Run", strTempRun);
				strRun = strTempRun;
			} else {
				jsonCurrentExecutionDetailsObject.put("Run", "Run 1");
				strRun = "Run 1";
			}

			String strExecutionPath = jsonCurrentExecutionDetailsObject.get("ExecutionPath").toString();
			jsonCurrentExecutionDetailsObject.remove("ExecutionPath", strExecutionPath);
			jsonCurrentExecutionDetailsObject.put("ExecutionPath", "Releases"+ File.separator + strEnviroment + "_" + strProjectName + "_" + strProjectRelease + File.separator + strDateStamp + File.separator + "CurrentRelease.js");

			String strScreenshotsPath = jsonCurrentExecutionDetailsObject.get("ScreenshotsPath").toString();
			jsonCurrentExecutionDetailsObject.remove("ScreenshotsPath", strExecutionPath);
			jsonCurrentExecutionDetailsObject.put("ScreenshotsPath", "Releases"+ File.separator + strEnviroment + "_" + strProjectName + "_" + strProjectRelease + File.separator + strDateStamp);


			String strTargetted = jsonCurrentExecutionDetailsObject.get("Targetted").toString();
			jsonCurrentExecutionDetailsObject.remove("Targetted", strTargetted);
			jsonCurrentExecutionDetailsObject.put("Targetted", "N/A");

			String strExecuted = jsonCurrentExecutionDetailsObject.get("Executed").toString();
			jsonCurrentExecutionDetailsObject.remove("Executed", strExecuted);
			jsonCurrentExecutionDetailsObject.put("Executed", "N/A");

			String strPassed = jsonCurrentExecutionDetailsObject.get("Passed").toString();
			jsonCurrentExecutionDetailsObject.remove("Passed", strPassed);
			jsonCurrentExecutionDetailsObject.put("Passed", "N/A");

			String strFailed = jsonCurrentExecutionDetailsObject.get("Failed").toString();
			jsonCurrentExecutionDetailsObject.remove("Failed", strFailed);
			jsonCurrentExecutionDetailsObject.put("Failed", "N/A");

			String strSkipped = jsonCurrentExecutionDetailsObject.get("Skipped").toString();
			jsonCurrentExecutionDetailsObject.remove("Skipped", strSkipped);
			jsonCurrentExecutionDetailsObject.put("Skipped", "N/A");

			String strInProgress = jsonCurrentExecutionDetailsObject.get("InProgress").toString();
			jsonCurrentExecutionDetailsObject.remove("InProgress", strInProgress);
			jsonCurrentExecutionDetailsObject.put("InProgress", "N/A");

			//System.out.println(jsonSubObject.get("Status"));
			//	break;
			//}
			//}
				/*
				JSONObject objPreviousExecutionDetails = new JSONObject();
				objPreviousExecutionDetails.put("Release", strRelease);
				objPreviousExecutionDetails.put("ExecutionDate", strExecutionDate);
				objPreviousExecutionDetails.put("Run", strRun);
				objPreviousExecutionDetails.put("ExecutionPath", strExecutionPath);
				objPreviousExecutionDetails.put("ScreenshotsPath", strScreenshotsPath);
				objPreviousExecutionDetails.put("Targetted", "N/A");
				objPreviousExecutionDetails.put("Executed", "N/A");
				objPreviousExecutionDetails.put("Passed", "N/A");
				objPreviousExecutionDetails.put("Failed", "N/A");
				objPreviousExecutionDetails.put("Skipped", "N/A");
				objPreviousExecutionDetails.put("InProgress", "N/A");
				PreviousExecutionDetails.add(objPreviousExecutionDetails);*/

			fwExecutionDetails = new FileWriter(fsExecutionDetailsJSonFile);
			fwExecutionDetails.write(jsonObj.toString());
			fwExecutionDetails.close();
			updateExecutionDetailsJsonToJs(true);

			//##########################################################################
			FileWriter filewritter = new FileWriter(strCurrentReleaseJSONFilePath);
			JSONArray arrTestSuiteDetails = new JSONArray();
			JSONObject TestSuiteExecutionDetails = new JSONObject();
			TestSuiteExecutionDetails.put("BrowserType", browserType);
			TestSuiteExecutionDetails.put("Environment", strEnviroment);
			TestSuiteExecutionDetails.put("ExecutedBy", strExecutionUserName);
			TestSuiteExecutionDetails.put("ComputerName", strMachineName);
			TestSuiteExecutionDetails.put("ProjectName", strProjectName);
			TestSuiteExecutionDetails.put("SystemDate", strOnlyDate);
			TestSuiteExecutionDetails.put("Release", strProjectRelease);
			TestSuiteExecutionDetails.put("Run", strRun);
			TestSuiteExecutionDetails.put("RunStarted", dtBatchStartDate.toString());
			TestSuiteExecutionDetails.put("RunEnded", "N/A");
			TestSuiteExecutionDetails.put("TotalTime", "N/A");
			arrTestSuiteDetails.add(TestSuiteExecutionDetails);
			objTestSuiteDetails.put("TestSuiteExecutionDetails", arrTestSuiteDetails);
			filewritter.write(objTestSuiteDetails.toJSONString());
			filewritter.close();
			//##########################################################################
			//String strFirstLetter = strCompanyName.substring(0, 1);
			//int len = strCompanyName.length();
			//String strStepName = strCompanyName.substring(1, len);
		}
		catch (Exception ex)
		{

		}
	}

	public void InitailizeHtmlRepot_02(Path ptTestPlanFilePath)
	{
		FileWriter filewritter;
		ExcelFileReader objExcelFileReader = new ExcelFileReader();
		try {
			List<String> lstTestSuite = objExcelFileReader.CaptureTestSuite(ptTestPlanFilePath);
			JSONParser parser = new JSONParser();
			Reader reader = new FileReader(strCurrentReleaseJSONFilePath);
			Object jsonObj = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) jsonObj;
			InetAddress ipAddr = InetAddress.getLocalHost();
			String strSystemIP = ipAddr.getHostAddress();
			JSONArray TestCaseDetails = new JSONArray();

			for (String strTestSuiteLoop : lstTestSuite) {
				String strTestSuiteId = strTestSuiteLoop.split(";")[0];
				String strTSName = strTestSuiteLoop.split(";")[1];
				List<String> lstTestCases = objExcelFileReader.CaptureTestCases(ptTestPlanFilePath, strTestSuiteId);

				//int x = 1;
				int arraySize = lstTestCases.size();
				for (String strTestCaseLoop : lstTestCases) {
					//x = x + 1;

					String TestCaseId = strTestCaseLoop.split(";")[0];
					String strTCName = strTestCaseLoop.split(";")[1];
					//String strTSExecutionID = strTestCaseLoop.split(";")[2];
					JSONObject objTestSuiteSubDetails = new JSONObject();

					//objTestSuiteSubDetails.put("TestExecutionID", strTSExecutionID);
					objTestSuiteSubDetails.put("TestSuiteID", strTestSuiteId);
					objTestSuiteSubDetails.put("strTSName", strTSName);
					objTestSuiteSubDetails.put("TestCaseID", TestCaseId);
					objTestSuiteSubDetails.put("SystemIP", strSystemIP);
					objTestSuiteSubDetails.put("TestTitle", strTCName);
					objTestSuiteSubDetails.put("Duration", "00:00:00");
					objTestSuiteSubDetails.put("Status", "In Progress");
					objTestSuiteSubDetails.put("DetailResults", "N/A");

					TestCaseDetails.add(objTestSuiteSubDetails);
				}
				lstTestCases.clear();
			}
			lstTestSuite.clear();

			jsonObject.put("ExecutionDetails", TestCaseDetails);
			File file = new File(strCurrentReleaseJSONFilePath);
			filewritter = new FileWriter(file);
			filewritter.write(jsonObject.toString());
			filewritter.close();
			updateJsonToJs(true);
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());

		}
	}


	public void createHTMLHeader_TC_03(String TestCaseId)
	{
		passcount = new AtomicInteger(0);
		failcount = new AtomicInteger(0);
		warningcount= new AtomicInteger(0);
		stepNo= new AtomicInteger(0);
		strCurrentTestCaseId = TestCaseId;
		try {
			//ScriptRunner objScriptRunner = new ScriptRunner("");

			FileWriter filewritter = null;
			JSONParser parser = new JSONParser();
			Reader reader = new FileReader(PropertyFileReader.GetCurrentReleaseJson());
			Object jsonObj = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) jsonObj;

			JSONObject objTestSuiteSubDetails1 = new JSONObject();
			JSONArray TestCaseDetails1 = new JSONArray();

			JSONArray TestCaseDetails2 = new JSONArray();
			JSONObject objTestSuiteSubDetails2 = new JSONObject();
			strTestCaseStartDate = FormatCurrentDateTime();
			dtTestCaseStartDate = new Date();
			objTestSuiteSubDetails2.put("ExecutionStarted", strTestCaseStartDate);
			objTestSuiteSubDetails2.put("ExecutionEnded", "N/A");
			objTestSuiteSubDetails2.put("TotalTime", "N/A");
			objTestSuiteSubDetails2.put("NoOfStepsPassed", "N/A");
			objTestSuiteSubDetails2.put("NoOfStepsFailed", "N/A");
			objTestSuiteSubDetails2.put("NoOfStepsCompletedWithWarnings", "N/A");
			TestCaseDetails2.add(objTestSuiteSubDetails2);
			objTestSuiteSubDetails1.put("TestCaseExecutionDetails", TestCaseDetails2);
			TestCaseDetails1.add(objTestSuiteSubDetails1);

			jsonObject.put(TestCaseId, TestCaseDetails1);
			// jsonObject.put(DriverScript.TestCaseId, objTestSuiteSubDetails);
			File file = new File(PropertyFileReader.GetCurrentReleaseJson());
			filewritter = new FileWriter(file);
			filewritter.write(jsonObject.toString());
			filewritter.close();
		} catch (Exception EX) {
			System.out.println("Exception in CreateHTMLHeader_TC. Exception -  " + EX.toString());
		}

	}

	public void createHTMLFooter_TC_04(String TestCaseId)
	{

		try {
			// ###################################################################################################
			//ScriptRunner objScriptRunner = new ScriptRunner("");
			Path strCurrentReleaseJSONFilePath = Paths.get(PropertyFileReader.GetCurrentReleaseJson());
			File file = new File(strCurrentReleaseJSONFilePath.toString());
			FileWriter filewritter = null;

			JSONParser parser = new JSONParser();
			Reader reader = new FileReader(strCurrentReleaseJSONFilePath.toString());
			Object jsonObj = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) jsonObj;
			JSONArray jsonArray = (JSONArray) jsonObject.get(TestCaseId);
			JSONObject jsonNestedObject = (JSONObject) jsonArray.get(0);
			JSONArray ExecutionDetails = (JSONArray) jsonNestedObject.get("TestCaseExecutionDetails");
			for (int i = 0; i < ExecutionDetails.size(); i++) {
				JSONObject jsonSubObject = (JSONObject) ExecutionDetails.get(i);
				jsonSubObject.remove("ExecutionEnded", "N/A");
				jsonSubObject.remove("TotalTime", "N/A");
				jsonSubObject.remove("NoOfStepsPassed", "N/A");
				jsonSubObject.remove("NoOfStepsFailed", "N/A");
				jsonSubObject.remove("NoOfStepsCompletedWithWarnings", "N/A");

				// jsonSubObject.put("ExecutionEnded",strTotalTimeForModule);
				strTestCaseEndDate = FormatCurrentDateTime();
				jsonSubObject.put("ExecutionEnded", strTestCaseEndDate);
				strTotalTimeForTC = DateTimeDiff(dtTestCaseStartDate);
				jsonSubObject.put("TotalTime", strTotalTimeForTC);
				jsonSubObject.put("NoOfStepsPassed", passcount);
				jsonSubObject.put("NoOfStepsFailed", failcount);
				jsonSubObject.put("NoOfStepsCompletedWithWarnings", warningcount);
				// return;
			}

			filewritter = new FileWriter(file);
			filewritter.write(jsonObj.toString());
			filewritter.close();
			updateJsonToJs(true);

			// ###################################################################################################

		} catch (Exception EX) {
			System.out.println("Exception in CreateHTMLFooter_TC. Exception -  " + EX.toString());
		}
	}

	public void createMainHTMLFooter_05()
	{
		try {
			String strEndDate = FormatCurrentDateTime();
			String strTotalTimeForModule= DateTimeDiff(dtBatchStartDate);
			//###################################################################################################
			File file = new File(strCurrentReleaseJSONFilePath);
			FileWriter filewritter;
			JSONParser parser = new JSONParser();
			Reader reader = new FileReader(strCurrentReleaseJSONFilePath);
			Object jsonObj = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) jsonObj;
			JSONArray ExecutionDetails = (JSONArray) jsonObject.get("TestSuiteExecutionDetails");
			for (int i = 0; i < ExecutionDetails.size(); i++) {
				JSONObject jsonSubObject = (JSONObject) ExecutionDetails.get(i);
				jsonSubObject.remove("RunEnded", "N/A");
				jsonSubObject.remove("TotalTime", "N/A");
				jsonSubObject.put("RunEnded", strEndDate);
				jsonSubObject.put("TotalTime", strTotalTimeForModule);
			}
			filewritter = new FileWriter(file);
			filewritter.write(jsonObj.toString());
			filewritter.close();
			updateJsonToJs(true);
			FileUtils.copyFile(new File(PropertyFileReader.GetCurrentReleaseJs()), new File(PropertyFileReader.prop.getProperty("DashboardPath") + File.separator + "CurrentRelease.js"));

			//###########################################################################################################################

			JSONParser CRParser = new JSONParser();
			Reader CRReader = new FileReader(strCurrentReleaseJSONFilePath);
			Object CRJsonObj = CRParser.parse(CRReader);
			JSONObject CRJsonObject = (JSONObject) CRJsonObj;
			JSONArray CRExecutionDetails = (JSONArray) CRJsonObject.get("ExecutionDetails");

			int intPass = 0;
			int intFail = 0;
			int intTotal = 0;
			int intSkipped = 0;
			int intInProgress = 0;
			int intExecuted = 0;

			for (int i = 0; i < CRExecutionDetails.size(); i++) {
				JSONObject CRJsonSubObject = (JSONObject) CRExecutionDetails.get(i);
				String strStatus = CRJsonSubObject.get("Status").toString();
				if (strStatus.equalsIgnoreCase("pass")) {
					intPass = intPass + 1;

				} else if (strStatus.equalsIgnoreCase("fail")) {
					intFail = intFail + 1;
				}

				intTotal = intTotal + 1;

			}
			intExecuted = intPass + intFail;
			intInProgress = intTotal - intExecuted;
			File CRFile = new File(strExecutionDetailsJSonFile);
			FileWriter CRFileWritter;
			Reader EDReader = new FileReader(strExecutionDetailsJSonFile);
			Object EDJsonObj = CRParser.parse(EDReader);
			JSONObject EDJsonObject = (JSONObject) EDJsonObj;
			JSONArray EDExecutionDetails = (JSONArray) EDJsonObject.get("CurrentExecution");
			JSONObject EDJsonSubObject = (JSONObject) EDExecutionDetails.get(0);

			EDJsonSubObject.remove("Targetted", "N/A");
			EDJsonSubObject.put("Targetted", intTotal);
			EDJsonSubObject.remove("Executed", "N/A");
			EDJsonSubObject.put("Executed", intExecuted);
			EDJsonSubObject.remove("Passed", "N/A");
			EDJsonSubObject.put("Passed", intPass);
			EDJsonSubObject.remove("Failed", "N/A");
			EDJsonSubObject.put("Failed", intFail);
			EDJsonSubObject.remove("Skipped", "N/A");
			EDJsonSubObject.put("Skipped", 0);
			EDJsonSubObject.remove("InProgress", "N/A");
			EDJsonSubObject.put("InProgress", intInProgress);

			CRFileWritter = new FileWriter(CRFile);
			CRFileWritter.write(EDJsonObj.toString());
			CRFileWritter.close();
			updateExecutionDetailsJsonToJs(true);
		}
		catch (Exception ex)
		{

		}
	}

	public void updateExecutionDetailsJsonToJs(Boolean strNewFile) {
		try {
			//ScriptRunner objScriptRunner = new ScriptRunner();
			FileWriter FileWritterNew = null;
			File file = new File(PropertyFileReader.GetExecutionDetailsJs());

			if (strNewFile) {
				FileWritterNew = new FileWriter(file, false);
			} else {
				FileWritterNew = new FileWriter(file, true);
			}

			JSONParser ParserFinal = new JSONParser();
			Reader ReaderFinal = new FileReader(PropertyFileReader.GetExecutionDetailsJson());
			Object JsonObjFinal = ParserFinal.parse(ReaderFinal);
			JSONObject JsonObjectFinal = (JSONObject) JsonObjFinal;
			for (Object key : JsonObjectFinal.keySet()) {
				String keyStr = (String) key;
				Object keyvalue = JsonObjectFinal.get(keyStr);
				FileWritterNew.write("var " + keyStr + "=" + keyvalue.toString());
				FileWritterNew.write(String.format("%n"));
			}
			FileWritterNew.close();
		} catch (Exception EX) {
			System.out.println("Exception in UpdateExecutionDetailsJsonToJs. Exception -  " + EX.toString());
		}
	}

	public void updateJsonToJs(Boolean strNewFile) throws IOException, ParseException {
		try {

			FileWriter FileWritterNew = null;
			File file = new File(PropertyFileReader.GetCurrentReleaseJs());
			if (strNewFile) {
				FileWritterNew = new FileWriter(file, false);
			} else {
				FileWritterNew = new FileWriter(file, true);
			}
			JSONParser ParserFinal = new JSONParser();
			Reader ReaderFinal = new FileReader(PropertyFileReader.GetCurrentReleaseJson());
			Object JsonObjFinal = ParserFinal.parse(ReaderFinal);
			JSONObject JsonObjectFinal = (JSONObject) JsonObjFinal;
			for (Object key : JsonObjectFinal.keySet()) {
				String keyStr = (String) key;
				Object keyvalue = JsonObjectFinal.get(keyStr);
				FileWritterNew.write("var " + keyStr + "=" + keyvalue.toString());
				FileWritterNew.write(String.format("%n"));
			}
			FileWritterNew.close();
		} catch (Exception EX) {

		}
	}

	public void WriteMainTCResults(String TestCaseId,String TestSuiteId, int intTCStatus,String strTCFoundStatus)
	{
		try {
			File file = new File(strCurrentReleaseJSONFilePath);
			FileWriter filewritter;

/*
			stepTCNo = stepTCNo + 1;


			String stepNumber = "";
			if (stepTCNo >= 1 & stepTCNo <= 9) {
				stepNumber = "00" + stepTCNo;
			}
			if (stepTCNo >= 10 & stepTCNo <= 99) {
				stepNumber = "0" + stepTCNo;
			}
			if (stepTCNo > 99) {
				stepNumber = "" + stepTCNo;
			}

			stepNumber = TestCaseId;
*/

			String strTCStatus = null;

			if (intTCStatus == 0) {
				strTCStatus = "Pass";

			} else if (intTCStatus >= 0) {
				strTCStatus = "Fail";
			}

			//###################################################################################################



			JSONParser parser = new JSONParser();
			Reader reader = new FileReader(strCurrentReleaseJSONFilePath);
			Object jsonObj = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) jsonObj;

			JSONArray ExecutionDetails = (JSONArray) jsonObject.get("ExecutionDetails");
			for (int i = 0; i < ExecutionDetails.size(); i++) {
				JSONObject jsonSubObject = (JSONObject) ExecutionDetails.get(i);


				if (jsonSubObject.get("TestCaseID").toString().equalsIgnoreCase(TestCaseId) &&  jsonSubObject.get("TestSuiteID").toString().equalsIgnoreCase(TestSuiteId) ) {
					//if (jsonSubObject.get("TestExecutionID").toString().equalsIgnoreCase(TestCaseId)) {
					jsonSubObject.remove("Status", "In Progress");
					jsonSubObject.remove("Duration", "00:00:00");
					jsonSubObject.put("Status", strTCStatus);
					jsonSubObject.put("Duration",strTotalTimeForTC );
					break;
				}
			}
			filewritter = new FileWriter(file);
			filewritter.write(jsonObj.toString());
			filewritter.close();
			updateJsonToJs(true);
			//###################################################################################################
		}
		catch (Exception Ex)
		{

		}

	}

	public void WriteResults(String strstatus,String strExpected,String strActual,String strObject, String strAction)
	{
		strCurrentReleaseJSONFilePath = PropertyFileReader.GetCurrentReleaseJson();
		strDashboardPath = PropertyFileReader.prop.getProperty("DashboardPath");
		//String strStatus = null;
		String stepNumber = "";
		//String snapFileName = null;
		String snapshotRelativepath = null;
        Path ptsnapshotFileName=null;
		try
		{
			strstatus = strstatus.replace(strstatus.substring(0, 1),strstatus.substring(0, 1).toUpperCase());
				stepNo.getAndIncrement();

				if (stepNo.get() >= 1 & stepNo.get() <=9 )
				{
					stepNumber = "00" + stepNo.get();
				}
				if (stepNo.get() >= 10 &  stepNo.get() <= 99)
				{
					stepNumber = "0" + stepNo.get();
				}
				if (stepNo.get() > 99)
				{
					stepNumber =  ""+stepNo.get();
				}

				switch (strstatus)
				{
					case "Pass":
						passcount.getAndIncrement();
						break;
					case "Fail":
						failcount.getAndIncrement();
						break;
					case "Warning":
						warningcount.getAndIncrement();
						break;
				}



				//if (strstatus.toLowerCase() == "fail" || strstatus.toLowerCase() == "pass" || strstatus.toLowerCase() == "warning")
				if (PropertyFileReader.getPropertyValue("CaptureScreenshots").equalsIgnoreCase(strstatus)|| PropertyFileReader.getPropertyValue("CaptureScreenshots").equalsIgnoreCase("ALL"))
				{
					Date dtScreenNow = new Date();

					DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

					String dtSystemDate = dateFormat.format(dtScreenNow);

					dtSystemDate = dtSystemDate.replace("/", "_");
					dtSystemDate = dtSystemDate.replace(" ", "_");
					dtSystemDate = dtSystemDate.replace(":", "_");

					//snapFileName = "SnapShot" + dtSystemDate + ".JPG";

					try {
						InitializeWebDrive objInitializeWebDrive =  new InitializeWebDrive();
						GenericHelper objGenericHelper= new GenericHelper(objInitializeWebDrive.driver);
						//Thread.sleep(DriverScript.ThinkTime_Short);

                        ptsnapshotFileName = Paths.get(strDashboardPath + File.separator + "SnapShot" + dtSystemDate + ".JPG");
                        //snapshotFileName = ptsnapshotFileName.toString();
                        objGenericHelper.takeFullScreenShot(ptsnapshotFileName.toString());
						//CaptureScreenshot(ReportsDestinationLocation + "/TestCaseDetailReports/SnapShots/" + snapFileName);
						//snapshotFileName = DriverScript.strDashboardPath +"/"+ snapFileName;
						//FileUtils.copyFile(new File (ReportsDestinationLocation + "/TestCaseDetailReports/SnapShots/" + snapFileName), new File (snapshotFileName));

						for (int i=0;i<=ptsnapshotFileName.getNameCount()-1;i++)
						{
							if((ptsnapshotFileName.getName(i).toString()).equalsIgnoreCase("Releases"))
							{
								snapshotRelativepath = ptsnapshotFileName.subpath(i+1,ptsnapshotFileName.getNameCount()).toString();
								break;
							}
						}

					} catch (Exception ex) {
						System.out.println("Exception occured while Capturing the Screenshot - " + ex.toString());
					}


				}





			//###################################################################################################

			File file = new File(strCurrentReleaseJSONFilePath);
			FileWriter filewritter = null;

			JSONParser parser = new JSONParser();
			Reader reader = new FileReader(strCurrentReleaseJSONFilePath);
			Object jsonObj = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) jsonObj;
			JSONArray ExecutionDetails = (JSONArray) jsonObject.get(strCurrentTestCaseId);

			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			//JSONArray TestCaseDetails1 = new JSONArray();
			//for (int i=0; i < ExecutionDetails.size(); i++)
			//{
			JSONArray TestCaseDetails2 = new JSONArray();
			JSONObject jsonSubObject2 = new JSONObject();
			jsonSubObject2.put("StepNo", stepNumber);
			jsonSubObject2.put("StepDescription", strExpected);
			jsonSubObject2.put("ObjectName",  strObject);
			jsonSubObject2.put("ActionOperation",  strAction.toUpperCase());
			jsonSubObject2.put("ActualResult",  strActual);
			jsonSubObject2.put("Duration",  "N/A");
			jsonSubObject2.put("Status", strstatus);
            ;
			if (snapshotRelativepath!=null){
			jsonSubObject2.put("SnapShot", snapshotRelativepath);
			}
			//snapshotFileName = "";

			TestCaseDetails2.add(jsonSubObject2);

			JSONObject jsonNestedObject = new JSONObject();
			//System.out.println(ExecutionDetails.size());

			if(ExecutionDetails.size()>1)
			{
				jsonNestedObject = (JSONObject) ExecutionDetails.get(1);
			}

			if (jsonNestedObject.containsKey("TestSteps")) {
				JSONArray TestStepsArray = (JSONArray) jsonNestedObject.get("TestSteps");
				TestStepsArray.add(jsonSubObject2);

			}else {
				JSONObject objTestSuiteSubDetails1 = new JSONObject();
				objTestSuiteSubDetails1.put("TestSteps",TestCaseDetails2);
				ExecutionDetails.add(objTestSuiteSubDetails1);
			}

			//return;
			//}
			jsonObject.put(strCurrentTestCaseId, ExecutionDetails);
			filewritter = new FileWriter(file);
			filewritter.write(jsonObject.toString());
			filewritter.close();
			updateJsonToJs(true);
			//###################################################################################################
		} catch (Exception e) {
			System.out.println("Exception in WriteResults - "+e);
		}
	}

}