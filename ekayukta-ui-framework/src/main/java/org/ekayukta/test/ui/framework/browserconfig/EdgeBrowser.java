package org.ekayukta.test.ui.framework.browserconfig;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

import org.ekayukta.test.ui.framework.helper.DateTimeHelper;
import org.ekayukta.test.ui.framework.helper.ResourceHelper;

public class EdgeBrowser {

	public WebDriver getEdgeDriver() {
		System.setProperty("webdriver.edge.driver", ResourceHelper.getResourcePath("driver/MicrosoftWebDriver.exe"));

		System.setProperty("webdriver.edge.logfile", ResourceHelper.getResourcePath("logs/edgelogs/") + "edgelog"
				+ DateTimeHelper.getCurrentDateTime() + ".log");
		return new EdgeDriver();
	}

}
