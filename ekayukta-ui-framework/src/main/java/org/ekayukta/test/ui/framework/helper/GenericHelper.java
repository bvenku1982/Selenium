package org.ekayukta.test.ui.framework.helper;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class GenericHelper implements IwebComponent {

	private WebDriver driver;
	private Logger oLog = LoggerHelper.getLogger(GenericHelper.class);

	public GenericHelper(WebDriver driver) {
		this.driver = driver;
		oLog.debug("GenericHelper : " + this.driver.hashCode());
	}

	public WebElement getElement(By locator) {
		oLog.info(locator);
		if (IsElementPresentQuick(locator))
			return driver.findElement(locator);

		try {
			throw new NoSuchElementException("Element Not Found : " + locator);
		} catch (RuntimeException re) {
			oLog.error(re);
			throw re;
		}
	}

	/**
	 * Check for element is present based on locator If the element is present
	 * return the web element otherwise null
	 * 
	 * @param locator
	 * @return WebElement or null
	 */

	public WebElement getElementWithNull(By locator) {
		oLog.info(locator);
		try {
			return driver.findElement(locator);
		} catch (NoSuchElementException e) {
			// Ignore
		}
		return null;
	}

	public boolean IsElementPresentQuick(By locator) {
		boolean flag = driver.findElements(locator).size() >= 1;
		oLog.info(flag);
		return flag;
	}

	public String generateFilename() {
		//PropertyFileReader objPropertyFileReader = new PropertyFileReader();

		Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
		String browserName = caps.getBrowserName().toLowerCase();
		String browserVersion = caps.getVersion().toLowerCase();
		// String strBrowserName =
		// objPropertyFileReader.getBrowser().toString().toLowerCase();
		String strOS = System.getProperty("os.name").toLowerCase().replace(" ", "_");
		String strFileName = strOS + "_" + browserName + browserVersion
				+ DateTimeHelper.getCurrentDateTime().replace("-", "_");
		return strFileName;
	}

	public String getBrowserName() {
		//PropertyFileReader objPropertyFileReader = new PropertyFileReader();
		String browserName = null;
		Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
		browserName = caps.getBrowserName().toLowerCase();
		return browserName;
	}

	public String takeScreenShot(String name) throws IOException {

		if (driver instanceof HtmlUnitDriver) {
			oLog.fatal("HtmlUnitDriver Cannot take the ScreenShot");
			return "";
		}

		File destDir = new File(ResourceHelper.getResourcePath("screenshots/") + DateTimeHelper.getCurrentDate());
		if (!destDir.exists())
			destDir.mkdir();

		File destPath = new File(destDir.getAbsolutePath() + System.getProperty("file.separator") + name + ".jpg");
		try {
			FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE), destPath);
		} catch (IOException e) {
			oLog.error(e);
			throw e;
		}
		oLog.info(destPath.getAbsolutePath());
		return destPath.getAbsolutePath();
	}

	public String takeScreenShot() {
		oLog.info("");
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
	}
	
	public void takeFullScreenShot(String name)  {
		
		try {
			File destPath = new File(name.toString());
			Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1)).takeScreenshot(driver); 
			ImageIO.write(screenshot.getImage(),"JPG",destPath);
			
			//FileUtils.copyFile(	((TakesScreenshot) Driver).getScreenshotAs(OutputType.FILE),destPath);
		} catch (Exception EX) {
		
			System.out.println("Exception in takeScreenShot - " + EX.toString());
		}
		
	}	
	
	public void FileUpload_Handler(String strFilePathExe, String strFilePath) {
		System.out.println("			File Upload Handler - Started");
		//String strPath = Paths.get("").toAbsolutePath().toString() +"\\src\\test\\resources\\resources\\";
				
		try {
			Thread.sleep(5000);
			Process process = new ProcessBuilder( strFilePathExe,strFilePath).start();	
			Thread.sleep(9000);
		} catch (Exception EX) {
			EX.printStackTrace();
		}
		System.out.println("			File Upload Handler - Ended");
	}

	public void FileUpload_Handler(String strFilePathExe) {
		System.out.println("			File Upload Handler - Started");
		//String strPath = Paths.get("").toAbsolutePath().toString() +"\\src\\test\\resources\\resources\\";
				
		try {
			Thread.sleep(5000);
			Process process = new ProcessBuilder( strFilePathExe).start();	
			Thread.sleep(9000);
		} catch (Exception EX) {
			EX.printStackTrace();
		}
		System.out.println("			File Upload Handler - Ended");
	}
	
	public void FileDownload_Handler(String strFilePathExe, String strFilePath) {
		System.out.println("			Chrome Popup Handler (Chrome Download) Started");

		try {
			Process process = new ProcessBuilder(strFilePathExe, strFilePath).start();			
			Thread.sleep(2000);
		} catch (Exception EX) {
			EX.printStackTrace();
		}
		System.out.println("			Chrome Popup Handler (Chrome Download) Ended");
	}
	
	
	public Document convertStringToXMLDocument(String xmlString) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();

			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			return doc;
		} catch (Exception EX) {
			System.out.println("Exception in convertStringToXMLDocument - " + EX.toString());
		}
		return null;
	}
}
