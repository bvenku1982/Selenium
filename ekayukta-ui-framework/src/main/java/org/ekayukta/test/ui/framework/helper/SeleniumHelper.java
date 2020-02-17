package org.ekayukta.test.ui.framework.helper;

import java.util.LinkedList;
import java.util.List;

import org.ekayukta.test.ui.framework.driverScript.ScriptRunner;
import org.ekayukta.test.ui.framework.filereader.PropertyFileReader;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;


public class SeleniumHelper implements IwebComponent{
	
	private WebDriver driver;
	private WebElement element;
	public static WebElement ObjReuseWebElement;
	private String strElementId ;
	RepotrsHelper objRepotrsHelper;
	private Logger oLog = LoggerHelper.getLogger(SeleniumHelper.class);

	public SeleniumHelper(WebDriver driver) {
		this.driver = driver;
		objRepotrsHelper = new RepotrsHelper();
		oLog.debug("AlertHelper : " + this.driver.hashCode());
	}
	public  WebElement FindElement (String strObjectProp , String strElement)
	{
		element = null;
		try
		{
			if (strObjectProp.length() > 0 && strElement.length() > 0)
			{
				switch (strObjectProp.toLowerCase())
				{
					case "id":
						element = driver.findElement(By.id(strElement));
						break;
					case "name":
						element = driver.findElement(By.name(strElement));
						break;
					case "xpath":
						element = driver.findElement(By.xpath(strElement));
						break;
					case "link":
						element = driver.findElement(By.linkText(strElement));
						break;
					case "css":
						element = driver.findElement(By.cssSelector(strElement));
						break;
					default:
						element = null;
						break;
				}
			}
		}
		catch (Exception Ex)
		{
			System.out.println("Failed in FindElement and Failed to Identify the Element - " + strElement + ". Exception - " +Ex);
			return element;
		}
		return element;
	}

	public  WebElement waitForElement(String objWebElement)
	{
		element = null;
		int intElementIteration = 0;
		int intSecondsToWait = 20;
		if (ScriptRunner.hashMapRepo.containsKey(objWebElement)) {
			objWebElement = ScriptRunner.hashMapRepo.get(objWebElement);
		}
		if(objWebElement.contains("##") )
		{
			String dynamicDataValue = Get_Data(objWebElement.split("##")[1]);
			objWebElement = objWebElement.split("##")[0];
			if (ScriptRunner.hashMapRepo.containsKey(objWebElement)) {
				objWebElement = ScriptRunner.hashMapRepo.get(objWebElement);
			}
			objWebElement= objWebElement.replace("@@@",dynamicDataValue);

		}
		strElementId = objWebElement;
		String strObjectProp = objWebElement.split("#~")[0];
		String strObjPropVal = objWebElement.split("#~")[1];
		do
		{
			try
			{
				element = FindElement(strObjectProp , strObjPropVal);
				if (element != null){
					break;
				}
				Thread.sleep(1000);
				intElementIteration = intElementIteration + 1;

			}catch (Exception Ex) {
				System.out.println("Failed in WaitForElement. Exception - " +Ex);
			}
		}while(intElementIteration != intSecondsToWait);
		return element;
	}

	public  WebElement waitForElementAndGettext(String objWebElement)
	{
		element = null;
		int intElementIteration = 0;
		int intSecondsToWait = 20;
		if (ScriptRunner.hashMapRepo.containsKey(objWebElement)) {
			objWebElement = ScriptRunner.hashMapRepo.get(objWebElement);
		}
		if(objWebElement.contains("##") )
		{
			String dynamicDataValue = Get_Data(objWebElement.split("##")[1]);
			objWebElement = objWebElement.split("##")[0];
			if (ScriptRunner.hashMapRepo.containsKey(objWebElement)) {
				objWebElement = ScriptRunner.hashMapRepo.get(objWebElement);
			}
			objWebElement= objWebElement.replace("@@@",dynamicDataValue);

		}
		strElementId = objWebElement;
		String strObjectProp = objWebElement.split("#~")[0];
		String strObjPropVal = objWebElement.split("#~")[1];
		do
		{
			try
			{
				element = FindElement(strObjectProp , strObjPropVal);
				if (element != null){
					break;
				}
				Thread.sleep(1000);
				intElementIteration = intElementIteration + 1;

			}catch (Exception Ex) {
				System.out.println("Failed in WaitForElement. Exception - " +Ex);
			}
		}while(intElementIteration != intSecondsToWait);
		return element;
	}

	public void SelectItemByText(String strOption)
	{
		List<WebElement> options = element.findElements(By.tagName("option"));
		for (WebElement option :options)
		{
			if (strOption.equals(option.getText().trim()))
			{
				option.click();
				break;
			}
		}
	}

	public void SelectItemByPartialText(String strOption)
	{

		List<WebElement> options = element.findElements(By.tagName("option"));
		for (WebElement option :options)
		{

			if (option.getText().trim().toLowerCase().contains(strOption.toLowerCase()))
			{
				option.click();
				break;
			}
		}
	}

	public void performOperation(String objWebElement, String strOperation, String strValue) throws Exception
	{
		try
		{
				element = waitForElement(objWebElement);

				if (element == null){

					//If object found return the value else return null
					if (strOperation.equalsIgnoreCase("writevisibletext"))
					{
						objRepotrsHelper.WriteResults("pass", "Get the visible text", "Text visible : ", "WebElement ID - " + strElementId, strOperation.toUpperCase());
						return;
					}

					//Assert.assertEquals("Element Not Found", "Fail to Identity the Element");
					if (PropertyFileReader.getPropertyValue("SkipTestCaseifStepFail").toString().equalsIgnoreCase("true"))
					{
						throw new Exception("Element Not Found - Fail to Identity the Element");
					}
					objRepotrsHelper.WriteResults("fail", "Perform Operation", "Failed to identity the WebElement.", "WebElement : " + objWebElement + " ", strOperation.toUpperCase());
					return;
				}



					System.out.println("Element Displayed - " + element.isDisplayed());
					switch (strOperation.toLowerCase())
					{
						case "writevisibletext":
							strValue = element.getText();
							objRepotrsHelper.WriteResults("pass", "Get the visible text", "Text visible : " + strValue, "WebElement ID - " + strElementId, strOperation.toUpperCase());
							break;
						case "click":
							element.click();
							objRepotrsHelper.WriteResults("pass", "Click on the WebElement", "Clicked on the WebElement", "WebElement ID - " + strElementId, strOperation.toUpperCase());
							break;
						case "clear":
							element.clear();
							objRepotrsHelper.WriteResults("pass", "Clear Text on the WebElement", "Cleared the Text on the WebElement", "WebElement ID - " + strElementId, strOperation.toUpperCase());
							break;
						case "clearandset":
							strValue = Get_Data(strValue);
							element.clear();
							element.sendKeys(strValue);
							objRepotrsHelper.WriteResults("pass", "Clear the existing value and enter the '" + strValue + "' text ", "Successfully cleared the existing value and entered the '" + strValue + "' text ", "WebElement ID - " + strElementId, strOperation.toUpperCase());
							break;
						case "set":
							strValue = Get_Data(strValue);
							element.sendKeys(strValue);
							objRepotrsHelper.WriteResults("pass", "Enter the '" + strValue + "' text ", "Entered the text: "+ strValue, "WebElement ID - " + strElementId, strOperation.toUpperCase());
							//objRepotrsHelper.WriteResults("fail", "Enter the '" + strValue + "' text ", "Entered the text: "+ strValue, "WebElement ID - " + strElementId, strOperation.toUpperCase());
							//objRepotrsHelper.WriteResults("warning", "Enter the '" + strValue + "' text ", "Entered the text: "+ strValue, "WebElement ID - " + strElementId, strOperation.toUpperCase());
							break;
						case "setnotnull":
							strValue = Get_Data(strValue);
							if(!"NULL".equals(strValue.toUpperCase())) {
								element.clear();
								element.sendKeys(strValue);
								objRepotrsHelper.WriteResults("pass", "Enter the '" + strValue + "' text ", "Entered the text: " + strValue, "WebElement ID - " + strElementId, strOperation.toUpperCase());
							}
							//objRepotrsHelper.WriteResults("fail", "Enter the '" + strValue + "' text ", "Entered the text: "+ strValue, "WebElement ID - " + strElementId, strOperation.toUpperCase());
							//objRepotrsHelper.WriteResults("warning", "Enter the '" + strValue + "' text ", "Entered the text: "+ strValue, "WebElement ID - " + strElementId, strOperation.toUpperCase());
							break;
						case "submit":
							element.submit();
							objRepotrsHelper.WriteResults("pass", "Submit the WebElement", "Submitted the WebElement", "WebElement ID - " + strElementId, strOperation.toUpperCase());
							break;
						case "selectitembytext":
							strValue = Get_Data(strValue);
							SelectItemByText(strValue);

							objRepotrsHelper.WriteResults("pass", "Select the List '" + strValue + "' item ", "List item is selected", "WebElement ID - " + strElementId, strOperation.toUpperCase());
							break;
						case "selectitembypartialtext":

							strValue = Get_Data(strValue);
							SelectItemByPartialText(strValue);
							objRepotrsHelper.WriteResults("pass", "Select the partial List '" + strValue + "' item ", "Partial list item is selected", "WebElement ID - " + strElementId, strOperation.toUpperCase());
							break;
						case "exist":
							if (element != null && element.isDisplayed())
							{
								objRepotrsHelper.WriteResults("pass", "Verify the Existence of the WebElement", "WebElement '" + element.getText()+ "' is Exist and Displayed", "WebElement ID - " + strElementId, strOperation.toUpperCase());
							}
							break;

						case "checktext":
							strValue = Get_Data(strValue).trim();
							if (element.getText().equalsIgnoreCase(strValue ))
							{
								objRepotrsHelper.WriteResults("pass", "Verify the WebElement text present", "Successfully verified : " + strValue, "WebElement ID - " + strElementId, strOperation.toUpperCase());
							}else
							{
								objRepotrsHelper.WriteResults("fail", "Verify the WebElement text present", "Verification failed and actual text is : " + element.getText(), "WebElement ID - " + strElementId, strOperation.toUpperCase());;
							}
							break;


						case "compare":
							String strString1 = strValue.split(":")[0];
							String strString2 = strValue.split(":")[1];

							strString1 = Get_Data(strString1).trim();
							strString2 = Get_Data(strString2).trim();
							if (strString1.equalsIgnoreCase(strString2))
							{
								objRepotrsHelper.WriteResults("pass", "Verify both values", "Successfully verified : " + strString1, " with " + strString2, strOperation.toUpperCase());
							}else
							{
								objRepotrsHelper.WriteResults("fail", "Verify both values","Verification failed and values are : " + strString1 +" :: "+strString2 , "WebElement ID - " + strElementId, strOperation.toUpperCase());;
							}
							break;
						case "comparepartialtext":
							String strStringVal1 = strValue.split(":")[0];
							String strStringval2 = strValue.split(":")[1];

							strStringVal1 = Get_Data(strStringVal1).trim();
							strStringval2 = Get_Data(strStringval2).trim();
							if (strStringVal1.contains(strStringval2))
							{
								objRepotrsHelper.WriteResults("pass", "Verify both values", "Successfully verified : " + strStringVal1, " with " + strStringval2, strOperation.toUpperCase());
							}else
							{
								objRepotrsHelper.WriteResults("fail", "Verify both values","Verification failed and values are : " + strStringVal1 +" :: "+strStringval2 , "WebElement ID - " + strElementId, strOperation.toUpperCase());;
							}
							break;
						case "checkpartialtext":
							strValue = Get_Data(strValue).trim();
							if (element.getText().contains(strValue ))
							{
								objRepotrsHelper.WriteResults("pass", "Verify the WebElement partial text present", "Successfully verified the partial text : " + strValue, "WebElement ID - " + strElementId, strOperation.toUpperCase());
							}else
							{
								objRepotrsHelper.WriteResults("fail", "Verify the WebElement partial text present", "Verification failed and actual text is : " + element.getText(), "WebElement ID - " + strElementId, strOperation.toUpperCase());;
							}
							break;
						case "switchtoframe":
						case "toframe":
						case "frame":
							driver.switchTo().frame(element);
							break;

						case "mousehover":
							Actions act = new Actions(driver);
							act.moveToElement(element).perform();
							Thread.sleep(1000);
							objRepotrsHelper.WriteResults("pass", "Mouse hover to the element", "successfully completed", "WebElement ID - " + strElementId, strOperation.toUpperCase());;
							break;
						default:
							objRepotrsHelper.WriteResults("fail", "Perform Operation", "Invalid Operation", "WebElement ID - " + strElementId, strOperation.toUpperCase());
					}


			} catch (Exception Ex) {
	            objRepotrsHelper.WriteResults("fail", "Perform Operation", "Failed to identity the WebElement.", "WebElement : " + objWebElement + " ", strOperation.toUpperCase());
				System.out.println("Failed in performOperation. Exception - " +Ex + ". Operation - " + strOperation +". element - " + objWebElement);
				if (PropertyFileReader.getPropertyValue("SkipTestCaseifStepFail").toString().equalsIgnoreCase("true"))
				{
					throw new Exception("Failed in performOperation. Exception - " +Ex + ". Operation - " + strOperation +". element - " + objWebElement);
				}
			}
	}
	
	public void click(By locator) {
		click(driver.findElement(locator));
		oLog.info(locator);
	}

	public void click(WebElement element) {
		GenericHelper objGenericHelper = new GenericHelper(driver);
		PropertyFileReader objPropertyFileReader = new PropertyFileReader();
		WaitHelper objWaitHelper = new WaitHelper(driver, objPropertyFileReader);

		try {
			objGenericHelper.takeScreenShot(objGenericHelper.generateFilename());
			element.click();
			objWaitHelper.hardWait(100);
			objGenericHelper.takeScreenShot(objGenericHelper.generateFilename());

		} catch (Exception e) {
			oLog.info(element + "-" + e);
		}

		oLog.info(element);
	}
	
	

	public boolean exist(WebElement element) {
		GenericHelper objGenericHelper = new GenericHelper(driver);
		try {
			objGenericHelper.takeScreenShot(objGenericHelper.generateFilename());
			if (element.isDisplayed()) {
				oLog.info(element);				
				return true;
			} else {
				return false;
			}
		} catch (Exception EX) {
			
		}
		return false;

	}
	public boolean enabled(WebElement element) {
		GenericHelper objGenericHelper = new GenericHelper(driver);
		try {
			objGenericHelper.takeScreenShot(objGenericHelper.generateFilename());
			if (element.isEnabled()) {
				oLog.info(element);				
				return true;
			} else {
				return false;
			}
		} catch (Exception EX) {
			
		}
		return false;

	}
	
	public void selectCheckBox(By locator) {
		oLog.info(locator);
		selectCheckBox(driver.findElement(locator));
	}
	
	public void unSelectCheckBox(By locator) {
		oLog.info(locator);
		unSelectCheckBox(driver.findElement(locator));
	}
	
	public boolean isIselected(By locator) {
		oLog.info(locator);
		return isIselected(driver.findElement(locator));
	}
	
	public boolean isIselected(WebElement element) {
		boolean flag = element.isSelected();
		oLog.info(flag);
		return flag;
	}
	
	public void selectCheckBox(WebElement element) {
		if(!isIselected(element))
			element.click();
		oLog.info(element);
	}
	
	public void unSelectCheckBox(WebElement element) {
		if(isIselected(element))
			element.click();
		oLog.info(element);
	}
	
	public void SelectUsingVisibleValue(By locator,String visibleValue) {
		GenericHelper objGenericHelper = new GenericHelper(driver);
		SelectUsingVisibleValue(objGenericHelper.getElement(locator),visibleValue);
	}
	
	public void SelectUsingVisibleValue(WebElement element,String visibleValue) {
		Select select = new Select(element);
		select.selectByVisibleText(visibleValue);
		oLog.info("Locator : " + element + " Value : " + visibleValue);
	}
	
	public void SelectUsingValue(By locator,String value) {
		GenericHelper objGenericHelper = new GenericHelper(driver);
		Select select = new Select(objGenericHelper.getElement(locator));
		select.selectByValue(value);
		oLog.info("Locator : " + locator + " Value : " + value);
	}
	
	public void SelectUsingIndex(By locator,int index) {
		GenericHelper objGenericHelper = new GenericHelper(driver);
		Select select = new Select(objGenericHelper.getElement(locator));
		select.selectByIndex(index);
		oLog.info("Locator : " + locator + " Index : " + index);
	}
	
	public String getSelectedValue(By locator) {
		GenericHelper objGenericHelper = new GenericHelper(driver);
		oLog.info(locator);
		return getSelectedValue(objGenericHelper.getElement(locator));
	}
	
	public String getSelectedValue(WebElement element) {
		String value = new Select(element).getFirstSelectedOption().getText();
		oLog.info("WebELement : " + element + " Value : "+ value);
		return value;
	}	
	
	public List<String> getAllDropDownValues(By locator) {
		GenericHelper objGenericHelper = new GenericHelper(driver);
		Select select = new Select(objGenericHelper.getElement(locator));
		List<WebElement> elementList = select.getOptions();
		List<String> valueList = new LinkedList<String>();
		
		for (WebElement element : elementList) {
			oLog.info(element.getText());
			valueList.add(element.getText());
		}
		return valueList;
	}
	
	public void clickLink(String linkText) {
		GenericHelper objGenericHelper = new GenericHelper(driver);
		oLog.info(linkText);
		objGenericHelper.getElement(By.linkText(linkText)).click();
	}
	
	public void clickPartialLink(String partialLinkText) {
		GenericHelper objGenericHelper = new GenericHelper(driver);
		oLog.info(partialLinkText);
		objGenericHelper.getElement(By.partialLinkText(partialLinkText)).click();
	}
	
	public String getHyperLink(By locator){
		GenericHelper objGenericHelper = new GenericHelper(driver);
		oLog.info(locator);
		return getHyperLink(objGenericHelper.getElement(locator));
	}
	
	public String getHyperLink(WebElement element) {
		String link = element.getAttribute("hreg");
		oLog.info("Element : " + element + " Value : " + link);
		return link;
	}
	
	public void sendKeys(By locator, String value) {
		GenericHelper objGenericHelper = new GenericHelper(driver);
		oLog.info("Locator : " + locator + " Value : " + value);
		objGenericHelper.getElement(locator).sendKeys(value);
	}

	public void sendKeys(WebElement element, String value) {
		oLog.info(element);
		element.sendKeys(value);
	}

	public void clearAndSendKeys(WebElement element, String value) {
		oLog.info(element);
		element.clear();
		element.sendKeys(value);
	}
	
	public void sendKeysAndEnter(WebElement element, String value) {
		oLog.info(element);
		element.clear();
		element.sendKeys(value);
		element.sendKeys(Keys.RETURN);
	}

	public void clear(By locator) {
		GenericHelper objGenericHelper = new GenericHelper(driver);
		oLog.info("Locator : " + locator);
		objGenericHelper.getElement(locator).clear();
	}

	public String getText(By locator) {
		GenericHelper objGenericHelper = new GenericHelper(driver);
		oLog.info("Locator : " + locator);
		return objGenericHelper.getElement(locator).getText();
	}
	
	public String getText(WebElement element) {
		oLog.info(element);
		return element.getText();
	}
	

	public void clearAndSendKeys(By locator, String value) {
		GenericHelper objGenericHelper = new GenericHelper(driver);
		WebElement element = objGenericHelper.getElement(locator);
		element.clear();
		element.sendKeys(value);
		oLog.info("Locator : " + locator + " Value : " + value);
	}
	public  String Get_Data(String strKey)
	{
		String strReturnKeyVal = null;

		try{
			if (strKey.contains("ENV_")){
				strReturnKeyVal = PropertyFileReader.getPropertyValue(strKey.replace("ENV_", ""));
			}else if (strKey.contains("DT_")){
				strReturnKeyVal = strKey.replace("DT_", "");
				strReturnKeyVal = ScriptRunner.hashMapTestData.get(strReturnKeyVal);
				//strReturnKeyVal = DriverScript.rsTestData.getString(strKey.replace("DT_", ""));
				//strReturnKeyVal = TestDataRepo.get(strKey.replace("DT_", "")).toString();


			}else if (strKey.contains("RUN_"))
			{
				//strReturnKeyVal = this.objPropertyFileReader.getPropertyValue(strKey.replace("ENV_", ""),DriverScript.EnvPropFilePath);
			}
			else
			{
				strReturnKeyVal = strKey;
			}
		}catch (Exception Ex) {
			System.out.println("Exception in Get_Data - " + Ex);
		}
		return strReturnKeyVal;
	}
}