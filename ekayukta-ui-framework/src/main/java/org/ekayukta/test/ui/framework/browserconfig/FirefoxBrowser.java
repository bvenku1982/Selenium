package org.ekayukta.test.ui.framework.browserconfig;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.ekayukta.test.ui.framework.exception.NoSutiableDriverFoundException;
import org.ekayukta.test.ui.framework.helper.ResourceHelper;

public class FirefoxBrowser {

	public Capabilities getFirefoxCapabilities() {
		DesiredCapabilities firefox = DesiredCapabilities.firefox();
		FirefoxProfile profile = new FirefoxProfile();
		profile.setAcceptUntrustedCertificates(true);
		profile.setAssumeUntrustedCertificateIssuer(true);
		firefox.setCapability(FirefoxDriver.PROFILE, profile);
		firefox.setCapability("marionette", true);
		return firefox;
	}

	public WebDriver getFirefoxDriver(Capabilities cap) {
		System.setProperty("webdriver.gecko.driver", ResourceHelper.getResourcePath("driver/geckodriver.exe"));
		// System.setProperty("webdriver.firefox.bin",
		// "D:/RunningSoftwares/FirefoxPortable32-64.0.2/FirefoxPortable32/FirefoxPortable.exe");
		return new FirefoxDriver(cap);
	}

	public WebDriver getFirefoxDriver(String hubUrl, Capabilities cap) throws MalformedURLException {
		return new RemoteWebDriver(new URL(hubUrl), cap);
	}

	public WebDriver getFirefoxDriver(BrowserType bType) {

		/*DesiredCapabilities cap = DesiredCapabilities.firefox();
		FirefoxProfile profile = new FirefoxProfile();
		profile.setAcceptUntrustedCertificates(true);
		profile.setAssumeUntrustedCertificateIssuer(true);
		cap.setCapability(FirefoxDriver.PROFILE, profile);
		cap.setCapability("marionette", false);
		*/
		try {
			switch (bType) {

			case Firefox:
			case FirefoxV68p0p2:
				System.setProperty("webdriver.gecko.driver", ResourceHelper.getResourcePath("driver/geckodriver.exe"));
				return new FirefoxDriver();			
		/*								
				System.setProperty("webdriver.gecko.driver", ResourceHelper.getResourcePath("driver/geckodriver.exe"));
				System.setProperty("webdriver.firefox.bin","D:\\RunningSoftwares\\FirefoxPortable32-68.0.2\\FirefoxPortable32\\App\\Firefox\\Firefox.exe");
				//System.setProperty("webdriver.firefox.marionette","false");
				return new FirefoxDriver();*/
			case FirefoxV67p0p4:
				System.setProperty("webdriver.gecko.driver", ResourceHelper.getResourcePath("driver/geckodriver.exe"));
				System.setProperty("webdriver.firefox.bin","D:\\RunningSoftwares\\FirefoxPortable32-67.0.4\\FirefoxPortable32\\App\\Firefox\\Firefox.exe");
				return new FirefoxDriver();
			case FirefoxV65p0p2:
				System.setProperty("webdriver.gecko.driver", ResourceHelper.getResourcePath("driver/geckodriver.exe"));
				System.setProperty("webdriver.firefox.bin",	"D:\\RunningSoftwares\\FirefoxPortable32-65.0.2\\FirefoxPortable32\\App\\Firefox\\Firefox.exe");
				return new FirefoxDriver();
			case FirefoxV64p0p2:
				//FirefoxOptions options = new FirefoxOptions();
				//options.setCapability("marionette", false);
								
				System.setProperty("webdriver.gecko.driver", ResourceHelper.getResourcePath("driver/geckodriver.exe"));
				System.setProperty("webdriver.firefox.bin", "D:\\RunningSoftwares\\FirefoxPortable32-64.0.2\\FirefoxPortable32\\App\\Firefox\\Firefox.exe");
				FirefoxProfile profile = new FirefoxProfile();
				new FirefoxDriver();
				
				return new FirefoxDriver();
			default:
				throw new NoSutiableDriverFoundException(" Driver Not Found : " + bType.toString());
			}
		} catch (Exception e) {

			throw e;

		}

	}

}
