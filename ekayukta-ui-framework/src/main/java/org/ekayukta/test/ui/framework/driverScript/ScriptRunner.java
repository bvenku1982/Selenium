package org.ekayukta.test.ui.framework.driverScript;

import java.io.File;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import org.ekayukta.test.ui.framework.filereader.ExcelFileReader;
import org.ekayukta.test.ui.framework.filereader.PropertyFileReader;
import org.ekayukta.test.ui.framework.helper.RepotrsHelper;
import org.ekayukta.test.ui.framework.helper.ResourceHelper;

public class ScriptRunner {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap<String, String> hashMapRepo;
	public static HashMap<String, String> hashMapTestData;
	public Path ptJSONFile;
	public Path ptJSFile;
	public Path ptExecutionDetailsJSonFile;
	public Path ptExecutionDetailsJSFile;
	String strReportsPath;

	public ScriptRunner()
	{
		//objPropertyFileReader = new PropertyFileReader();

		//strResourcesPath = Paths.get("src/resources").toAbsolutePath().toString();
		PropertyFileReader.load();
		strReportsPath = PropertyFileReader.getPropertyValue("TestReportPath").toString();

		if (strReportsPath.indexOf("Releases")>1){
			if (strReportsPath.indexOf("CurrentRelease.json")  <1){
				if (strReportsPath.indexOf(File.separator, strReportsPath.length() - 1) <0)
				{strReportsPath = strReportsPath + File.separator + "CurrentRelease.json";}
				strReportsPath = strReportsPath + "CurrentRelease.json";
			}
		}
		else{
		if (strReportsPath.indexOf(File.separator, strReportsPath.length() - 1) <0)
		{strReportsPath = strReportsPath + File.separator + "Releases" + File.separator + "CurrentRelease.json";}
		else
            {strReportsPath = strReportsPath + "Releases" + File.separator + "CurrentRelease.json";
            }
		}
		//strResourcesPath = Paths.get("src/resources").toAbsolutePath().toString();
		ptJSONFile = Paths.get(strReportsPath);
		ptJSFile = Paths.get(ptJSONFile.toString().replace("CurrentRelease.json", "CurrentRelease.js"));
		ptExecutionDetailsJSonFile = Paths.get(ptJSONFile.toString().replace("CurrentRelease.json", "ExecutionDetails.json"));
		ptExecutionDetailsJSFile = Paths.get(ptExecutionDetailsJSonFile.toString().replace("ExecutionDetails.json", "ExecutionDetails.js"));

		PropertyFileReader.prop.setProperty("CurrentReleaseJson",ptJSONFile.toAbsolutePath().toString());
		PropertyFileReader.prop.setProperty("CurrentReleaseJs",ptJSFile.toAbsolutePath().toString());
		PropertyFileReader.prop.setProperty("ExecutionDetailsJson",ptExecutionDetailsJSonFile.toAbsolutePath().toString());
		PropertyFileReader.prop.setProperty("ExecutionDetailsJs",ptExecutionDetailsJSFile.toAbsolutePath().toString());
	}

	public void Run(){
		try {
			System.out.println("Regression Started");
			//Path ptRelativePath = Paths.get(strResourcesPath);
			//String strOperatingSystem = System.getProperty("os.name");
			InetAddress ipAddr = InetAddress.getLocalHost();
			//String strSystemIP = ipAddr.getHostAddress();
			//Path ptRunTimePropFilePath = ResourceHelper.getSpecificResourcePath("configfile" + File.separator +"runtime.properties");
			Path ptTestPlanFilePath = ResourceHelper.getSpecificResourcePath("testplan" + File.separator +"TestPlan.xls");
			Path ptElementRepoFilePath = ResourceHelper.getSpecificResourcePath("objectrepository" + File.separator +"ObjectRepository.xls");
			Path ptTestDataFilePath = ResourceHelper.getSpecificResourcePath("exceldata" + File.separator + PropertyFileReader.getPropertyValue("TestData"));
			//####################################################################################################################################################
			//Reporting
			//####################################################################################################################################################
			ExcelFileReader objExcelFileReader = new ExcelFileReader();
			RepotrsHelper objReporter = new RepotrsHelper();
			objReporter.CreateMainHTMLHeader_01();
			objReporter.InitailizeHtmlRepot_02(ptTestPlanFilePath);
			hashMapRepo = objExcelFileReader.FetchElementRepo(ptElementRepoFilePath);
			List<String> lstTestSuite = objExcelFileReader.CaptureTestSuite(ptTestPlanFilePath);
			System.out.println("");
			for (String strTestSet : lstTestSuite) {
				String strTestSuiteId = strTestSet.split(";")[0];
				// String strTSName = strTestSet.split(";")[1];
				List<String> lstTestCases = objExcelFileReader.CaptureTestCases(ptTestPlanFilePath, strTestSuiteId);
				//Make sure to load all test suites and test cases
				System.out.println("");
				System.out.println("Execution Started for the Test Set - " + strTestSuiteId);
				for (String strTestCase : lstTestCases) {
					String strTCId = strTestCase.split(";")[0];
					String strTCModuleName = strTestCase.split(";")[1];
					String strTCName = strTestCase.split(";")[2];
					System.out.println("	Scenario Started	: " + strTCName);
					//objPropertyFileReader.ClearRepository(ptRunTimePropFilePath);
					try {
						hashMapTestData = objExcelFileReader.LoadTestData(ptTestDataFilePath,
								strTCId);
						Class<?> objTestClass = Class.forName("TestCases." + strTCModuleName);
						Object objTestClassInstance = objTestClass.newInstance();
						Method objTestMethod = objTestClass.getDeclaredMethod(strTCName, HashMap.class);
						objReporter.createHTMLHeader_TC_03(strTCId);
						objTestMethod.invoke(objTestClassInstance, hashMapTestData);
						objReporter.createHTMLFooter_TC_04(strTCId);
						if (objReporter.failcount.get() > 0)
						{
							objReporter.WriteMainTCResults(strTCId,strTestSuiteId, 1,"Found");
						}else
						{
							objReporter.WriteMainTCResults(strTCId,strTestSuiteId, 0,"Found");
						}
					} catch (Exception EX) {
						EX.printStackTrace();
						System.out.println("Test Case Not Found Exception -" + EX.toString());
						objReporter.WriteMainTCResults(strTCId,strTCName, 1,"Test Case Not Found" );
					}
					//objPropertyFileReader.ClearRepository(ptRunTimePropFilePath);
					System.out.println("	Scenario Ended		: " + strTCName);
					System.out.println("");
				}
				System.out.println("Execution Ended for the Test Set - " + strTestSuiteId);
				System.out.println("");
			}
//####################################################################################################################################################		
			objReporter.createMainHTMLFooter_05();
			System.out.println("Regression Ended");
		} catch (Exception Ex) {
			System.out.println("Exception in DriverScript: " + Ex);
		}
	}


}
