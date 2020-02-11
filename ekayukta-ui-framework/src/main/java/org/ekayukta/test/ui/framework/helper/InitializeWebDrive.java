package org.ekayukta.test.ui.framework.helper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.ekayukta.test.ui.framework.exception.NoSutiableDriverFoundException;
import org.ekayukta.test.ui.framework.filereader.PropertyFileReader;
import org.openqa.selenium.WebDriver;

import org.ekayukta.test.ui.framework.browserconfig.BrowserType;
import org.ekayukta.test.ui.framework.browserconfig.ChromeBrowser;
import org.ekayukta.test.ui.framework.browserconfig.EdgeBrowser;
import org.ekayukta.test.ui.framework.browserconfig.FirefoxBrowser;
import org.ekayukta.test.ui.framework.browserconfig.HtmlUnitBrowser;
import org.ekayukta.test.ui.framework.browserconfig.IExploreBrowser;
import org.ekayukta.test.ui.framework.browserconfig.PhantomJsBrowser;

public class InitializeWebDrive {

	public static WebDriver driver;
	//public static IconfigReader reader;
	RepotrsHelper objReportsHelper; 
	public static Map<String, Object> data = new LinkedHashMap<String, Object>();
	
	private Logger oLog = LoggerHelper.getLogger(InitializeWebDrive.class);

	/*public InitializeWebDrive(PropertyFileReader fileReader) {

		reader = fileReader;
	}*/

	public WebDriver standAloneStepUp(BrowserType bType) throws Exception {
		
		try {
			oLog.info(bType);

			switch (bType) {

			case Chrome:
				ChromeBrowser chrome = ChromeBrowser.class.newInstance();
				
				/*return chrome.getChromeDriver(chrome.getChromeCapabilities());	*/
				return chrome.getChromeDriver();	
			case Edge:
				EdgeBrowser edge = EdgeBrowser.class.newInstance();
				return edge.getEdgeDriver();	
			case Firefox:
			case FirefoxV68p0p2:
			case FirefoxV67p0p4:
			case FirefoxV65p0p2:	
			case FirefoxV64p0p2:
				FirefoxBrowser firefox = FirefoxBrowser.class.newInstance();
				//return firefox.getFirefoxDriver(firefox.getFirefoxCapabilities());
				return firefox.getFirefoxDriver(bType);
			case HtmlUnitDriver:
				HtmlUnitBrowser htmlUnit = HtmlUnitBrowser.class.newInstance();
				return htmlUnit.getHtmlUnitDriver(htmlUnit.getHtmlUnitDriverCapabilities());

			case Iexplorer:
				IExploreBrowser iExplore = IExploreBrowser.class.newInstance();
				return iExplore.getIExplorerDriver(iExplore.getIExplorerCapabilities());

			case PhantomJs:
				PhantomJsBrowser jsBrowser = PhantomJsBrowser.class.newInstance();
				return jsBrowser.getPhantomJsDriver(jsBrowser.getPhantomJsService(),
						jsBrowser.getPhantomJsCapability());

			default:
				throw new NoSutiableDriverFoundException(" Driver Not Found : " + PropertyFileReader.getBrowser());
			}
		} catch (Exception e) {
			oLog.error(e);
			throw e;
		}
	}	

	public WebDriver browserInit() throws Exception {
		
		driver = setUpDriver(PropertyFileReader.getBrowser());
		objReportsHelper = new RepotrsHelper();
		//Thread.sleep(5000);
		//ObjectRepo.driver = setUpDriver(ObjectRepo.reader.getBrowser());
		oLog.info(PropertyFileReader.getBrowser());
		BrowserHelper objBrowserHelper = new BrowserHelper(driver);
		objBrowserHelper.navigateTo(PropertyFileReader.getWebsite());
		objReportsHelper.WriteResults("pass", "Launch the Application", "Successfully Launched : " + PropertyFileReader.getWebsite() , "Browser Name : " + PropertyFileReader.getBrowser(), "Launch");
		oLog.info("Initializing the driver");
		return driver;
	}

	public WebDriver browserInit(String url) throws Exception {

		driver = setUpDriver(PropertyFileReader.getBrowser());
		//Thread.sleep(5000);
		//ObjectRepo.driver = setUpDriver(ObjectRepo.reader.getBrowser());
		oLog.info(PropertyFileReader.getBrowser());
		BrowserHelper objBrowserHelper = new BrowserHelper(driver);
		objBrowserHelper.navigateTo(url);
		oLog.info("Initializing the driver");
		return driver;
	}


	public WebDriver setUpDriver(BrowserType bType) throws Exception {
		driver = standAloneStepUp(bType);
		oLog.debug("InitializeWebDrive : " + driver.hashCode());
		driver.manage().timeouts().pageLoadTimeout(PropertyFileReader.getPageLoadTimeOut(), TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(PropertyFileReader.getImplicitWait(), TimeUnit.SECONDS);
		
		return driver;
	}

	public void tearDownDriver() throws Exception {
		try {

			if (driver != null) {
				driver.quit();
				//ObjectRepo.driver.close();
				//reader = null;
				driver = null;
				//Runtime.getRuntime().exec("TASKKILL /F /IM geckodriver.exe");
				//Runtime.getRuntime().exec("TASKKILL /F /IM firefox.exe");
				//Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
				oLog.info("Shutting Down the driver");
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			oLog.error(e);
			throw e;
		}
	}
	
	/*@Before({ "~@firefox", "~@chrome", "~@phantomjs", "~@iexplorer" })
	public void before() throws Exception {
		oLog.info(ObjectRepo.reader.getBrowser());
		setUpDriver(ObjectRepo.reader.getBrowser());
		oLog.info(ObjectRepo.reader.getBrowser());
	}

	@After({ "~@firefox", "~@chrome", "~@phantomjs", "~@iexplorer" })
	public void after(Scenario scenario) throws Exception {
		tearDownDriver(scenario);
		oLog.info("");
	}

	@Before(order = 4, value = { "@iexplorer" })
	public void beforeExplorer() throws Exception {
		setUpDriver(BrowserType.Iexplorer);
		oLog.info(BrowserType.Iexplorer);
	}

	@After(order = 4, value = { "@iexplorer" })
	public void afterExplorer(Scenario scenario) throws Exception {
		tearDownDriver(scenario);
		oLog.info("");
	}

	@Before(order = 3, value = { "@firefox" })
	public void beforeFirefox() throws Exception {
		setUpDriver(BrowserType.Firefox);
		oLog.info(BrowserType.Firefox);
	}

	@After(order = 3, value = { "@firefox" })
	public void afterFirefox(Scenario scenario) throws Exception {
		tearDownDriver(scenario);
		oLog.info("");
	}

	@Before(order = 2, value = { "@chrome" })
	public void beforeChrome() throws Exception {
		setUpDriver(BrowserType.Chrome);
		oLog.info(BrowserType.Chrome);
	}

	@After(order = 2, value = { "@chrome" })
	public void afterChrome(Scenario scenario) throws Exception {
		tearDownDriver(scenario);
		oLog.info("");
	}

	@Before(order = 1, value = { "@phantomjs" })
	public void beforePhantomjs() throws Exception {
		setUpDriver(BrowserType.PhantomJs);
		oLog.info(BrowserType.PhantomJs);
	}

	@After(order = 1, value = { "@phantomjs" })
	public void afterPhantomjs(Scenario scenario) throws Exception {
		tearDownDriver(scenario);
		oLog.info("");
	}

	public void tearDownDriver(Scenario scenario) throws Exception {

		try {
			if (ObjectRepo.driver != null) {

				if (scenario.isFailed())
					scenario.write(new GenericHelper(ObjectRepo.driver).takeScreenShot(scenario.getName()));

				ObjectRepo.driver.quit();
				ObjectRepo.reader = null;
				ObjectRepo.driver = null;
				oLog.info("Shutting Down the driver");
			}
		} catch (Exception e) {
			oLog.error(e);
			throw e;
		}
	}*/

}
