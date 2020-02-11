package org.ekayukta.test.ui.framework.browserconfig;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.ekayukta.test.ui.framework.filereader.PropertyFileReader;
import org.ekayukta.test.ui.framework.helper.DateTimeHelper;
import org.ekayukta.test.ui.framework.helper.ResourceHelper;

public class ChromeBrowser {

	
	public WebDriver getChromeDriver() {

		ChromeOptions options = new ChromeOptions();
		ChromeDriver driver = null; 
		if (System.getProperty("os.name").contains("Windows")) {

			System.setProperty("webdriver.chrome.driver", ResourceHelper.getResourcePath("/driver/chromedriver.exe"));
			System.setProperty("webdriver.chrome.logfile", ResourceHelper.getResourcePath("/logs/chromelogs/")
					+ "chromelog" + DateTimeHelper.getCurrentDateTime() + ".log");
			if (PropertyFileReader.getPropertyValue("Headless").toString().equalsIgnoreCase("true")) {
				options.addArguments("headless");
			} 
			
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			options.addArguments("window-size=" + screenSize.width + "x" + screenSize.height);
			System.out.println("WindowsSize=" + screenSize.width + "x" + screenSize.height);
			options.addArguments("--disable-notifications");
			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(ChromeOptions.CAPABILITY, options);
			driver = new ChromeDriver(options);
			driver.manage().window().maximize();

		} if (System.getProperty("os.name").contains("Linux")) {
			if (PropertyFileReader.getPropertyValue("Headless").toString().equalsIgnoreCase("true")) {
				options.addArguments("headless");
			}			
			options.addArguments("--start-maximized");
			driver = new ChromeDriver(options);
		}
		return driver;

	}
}
