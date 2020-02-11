package org.ekayukta.test.ui.framework.filereader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.ekayukta.test.ui.framework.helper.ResourceHelper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

public class ExcelFileReader {	
		
	public HashMap<String, String> LoadTestData(Path ptTestDataFilePath,String TCID) 
	{		
		HashMap<String, String> hashMapTestData = new HashMap<String, String>();
			 
		try{              

			System.out.println("		Started capturing the Test Data for the Test Case ID - "+ TCID);
			File objTestDataFile = new File(ptTestDataFilePath.toString());
			
			FileInputStream file = new FileInputStream(objTestDataFile);	 
					
			Workbook workbook = null;
			if(ptTestDataFilePath.toString().toLowerCase().endsWith("xlsx")){
				//workbook = new XSSFWorkbook(file);
			}else if(ptTestDataFilePath.toString().toLowerCase().endsWith("xls")){
				workbook = new HSSFWorkbook(file);
			}
			Sheet sheet = workbook.getSheet("Sheet1");
			Iterator<Row> rowIterator = sheet.iterator();
			System.out.println("		No of rows - "+sheet.getLastRowNum());

			for (int i = 0; i<= sheet.getLastRowNum();i++)
			{
				Row row = rowIterator.next();

				if (row.getCell(0).toString().equalsIgnoreCase(TCID.toLowerCase()))
				{
					System.out.println("		No of columns - " +row.getLastCellNum());
					for (int j=0; j < row.getLastCellNum();j++)
					{			
						String strCellValue = getCellValue(row.getCell(j)).toString().trim();
						if(strCellValue.contentEquals(""))
						{
							hashMapTestData.put( sheet.getRow(0).getCell(j).getRichStringCellValue().toString().trim(), null );
							System.out.println("			"+sheet.getRow(0).getCell(j).getRichStringCellValue().toString() +":" + strCellValue);
						}else
						{														
							//hashMapTestData.put( sheet.getRow(0).getCell(j).getRichStringCellValue().toString(), row.getCell(j).toString().trim());
							hashMapTestData.put( sheet.getRow(0).getCell(j).getRichStringCellValue().toString(), strCellValue);
							System.out.println("			"+sheet.getRow(0).getCell(j).getRichStringCellValue().toString() +":" + strCellValue);
						}
					}
					break;
				}
			}
			System.out.println("		Completed capturing the Test Data for the Test Case ID -"+ TCID);
		}  
		catch (Exception Ex) {
			System.out.println("Exception in LoadTestData - " + Ex.toString());  
		}
		return hashMapTestData;
	}

	
	public String getCellValue(Cell rowCell) {

		String strreturn = null;
		try {

			if ("FORMULA".contentEquals(rowCell.getCellType().name())) {
				strreturn = rowCell.getStringCellValue();
			} else {
				strreturn = rowCell.toString();
			}

		} catch (Exception EX) {
			System.out.println("Exception in getCellValue. Exception -  " + EX.toString());
		}
		return strreturn;
	}
	
	public List<String>  CaptureTestSuite(Path ptTestPlanFilePath) 
	{
		List<String> lstTestSuiteRepo = new ArrayList<String>();
		try{      
			
			System.out.println("Started capturing the Test Suite from Test Plan.");
			
			File objTestPlanFile = new File(ptTestPlanFilePath.toString());
			
			FileInputStream file = new FileInputStream(objTestPlanFile);	 
			
			Workbook workbook = null;
			if(ptTestPlanFilePath.toString().toLowerCase().endsWith("xlsx")){
				//workbook = new XSSFWorkbook(file);
			}else if(ptTestPlanFilePath.toString().toLowerCase().endsWith("xls")){
				workbook = new HSSFWorkbook(file);
			}
			Sheet sheet = workbook.getSheet("TestSet");			
			Iterator<Row> rowIterator = sheet.iterator();
			for (int i = 0; i<= sheet.getLastRowNum();i++)
			{
				Row row = rowIterator.next();

				if (row.getCell(3).toString().equalsIgnoreCase("yes"))
				{					
					if(row.getCell(1).toString()!= null && row.getCell(2).toString()!= null  )
					{
						lstTestSuiteRepo.add( row.getCell(1).toString()+";"+ row.getCell(2).toString() ); 
						System.out.println("   "+row.getCell(1).toString() +":" + row.getCell(2).toString());
					}
				}
			}
			System.out.println("Completed capturing the Test Suite from Test Plan");
		}  
		catch (Exception Ex) {
			System.out.println("Exception in CaptureTestSuite - " + Ex);  
		}
		return lstTestSuiteRepo;
	}

	public List<String> CaptureTestCases(Path ptTestPlanFilePath,String strTSID ) 
	{
		List<String> lstTestCasesRepo = new ArrayList<String>();
		try{            
			System.out.println("Started capturing the Test Cases details for the Test Set : " + strTSID.toString());
			File objTestPlanFile = new File(ptTestPlanFilePath.toString());
			
			FileInputStream file = new FileInputStream(objTestPlanFile);	 

			Workbook workbook = null;
			if(ptTestPlanFilePath.toString().toLowerCase().endsWith("xlsx")){
				//workbook = new XSSFWorkbook(file);
			}else if(ptTestPlanFilePath.toString().toLowerCase().endsWith("xls")){
				workbook = new HSSFWorkbook(file);
			}
			Sheet sheet = workbook.getSheet(strTSID.toString());
			Iterator<Row> rowIterator = sheet.iterator();
			for (int i = 0; i<= sheet.getLastRowNum();i++)
			{
				Row row = rowIterator.next();

				if (row.getCell(1).toString().equalsIgnoreCase(strTSID.toString()) && row.getCell(5).toString().equalsIgnoreCase("yes"))
				{
					if(row.getCell(1).toString()!= null && row.getCell(2).toString()!= null  )
					{
						lstTestCasesRepo.add( row.getCell(2).toString()+";"+ row.getCell(3).toString()+";"+ row.getCell(4).toString()); 
						System.out.println("   "+row.getCell(2).toString() +":" + row.getCell(3).toString()+";"+ row.getCell(4).toString());
					}
				}
			}
			System.out.println("Completed capturing the Test Cases details for execution of Test Set: " + strTSID.toString());
		}  
		catch (Exception Ex) {
			System.out.println("Exception in CaptureTestCases - " + Ex);  
		}
		return lstTestCasesRepo;
	}
	public void update_testdata(HashMap<String, String> hashMapTestData )
	{

		try{
			String  ptTestDataFilePath = ResourceHelper.getResourcePath("exceldata/TestData.xls");

			String TCID = hashMapTestData.get("TC_ID");
			//System.out.println("		Started capturing the Test Data for the Test Case ID - "+ TCID);
			File objTestDataFile = new File(ptTestDataFilePath.toString());

			FileInputStream file = new FileInputStream(objTestDataFile);

			Workbook workbook = null;
			workbook = WorkbookFactory.create(file);
			Sheet sheet = workbook.getSheetAt(0);
			int rowCount = sheet.getLastRowNum();

			Iterator<Row> rowIterator = sheet.iterator();
			System.out.println("		No of rows - "+sheet.getLastRowNum());

			for (int i = 0; i<= sheet.getLastRowNum();i++)
			{
				Row row = rowIterator.next();

				if (row.getCell(0).toString().equalsIgnoreCase(TCID.toLowerCase()))
				{
					System.out.println("		No of columns - " +row.getLastCellNum());
					int col = row.getLastCellNum()-1;
					row.getCell(col).setCellValue(hashMapTestData.get("Comments"));

					break;
				}
			}


			file.close();

			FileOutputStream outputStream = new FileOutputStream(ptTestDataFilePath.toString());
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();


			System.out.println("		Completed capturing the Test Data for the Test Case ID -"+ TCID);
		}
		catch (Exception Ex) {
			System.out.println("Exception in LoadTestData - " + Ex.toString());
		}

	}

	public HashMap<String, String> FetchElementRepo(Path ptElementRepoFilePath)
	{

		HashMap<String, String> objectRepo = new HashMap<String, String>();
		try{
			System.out.println("Started Learning the Objects form Object Repository");
			System.out.println(ptElementRepoFilePath.toAbsolutePath().toString());

			FileInputStream file = new FileInputStream(new File(ptElementRepoFilePath.toString()));

			Workbook workbook = null;
			if(ptElementRepoFilePath.toString().toLowerCase().endsWith("xlsx")){
				//workbook = new XSSFWorkbook(file);
			}else if(ptElementRepoFilePath.toString().toLowerCase().endsWith("xls")){
				workbook = new HSSFWorkbook(file);
			}

			Sheet sheet = workbook.getSheetAt(0);

			int rowCount =0;
			Iterator<Row> rowIterator = sheet.iterator();
			System.out.println("No of rows"+sheet.getLastRowNum());

			for (int i = 0; i<= sheet.getLastRowNum();i++)
			{
				//Row row = rowIterator.next();

				Cell strObjectID = sheet.getRow(i).getCell(1);
				Cell strProperty = sheet.getRow(i).getCell(2);
				Cell strPropertyValue = sheet.getRow(i).getCell(3);
				objectRepo.put(strObjectID.toString() ,strProperty.toString() +"#~"+ strPropertyValue.toString() );
				rowCount ++;
			}
			System.out.println("Completed Learning Objects '" + rowCount + "' form Object Repository and Loaded ");

		}
		catch (Exception Ex) {
			System.out.println(Ex);
		}
		return objectRepo;
	}



}
