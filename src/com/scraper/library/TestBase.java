package com.scraper.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class TestBase {

	public static final int GLOBALTIMEOUT = 60;
	public WebDriver driver;
	protected AppLibrary appLibrary; // Application Library instance
	Properties usersProperties = null;
	private String suite;
	protected String testName;
	ITestContext testContext;

	@BeforeClass(alwaysRun = true)
	public void aaasetUp(ITestContext context) throws Exception {

		suite = context.getCurrentXmlTest().getSuite().getName();
		suite = ((suite != null && !(suite.equals("Default suite"))) ? suite
				: InetAddress.getLocalHost().getHostName());
		suite = (suite.contains("NJT") ? "Nishant" : suite);
		testName = this.getClass().getSimpleName();
		testName = ((testName != null && !(testName.equals("Default test"))) ? testName
				: this.getClass().getSimpleName());

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MMMddyyyyhhmmssaz");
		String currentDate = sdf.format(date);
		// System.out.println(currentDate);

		if (System.getProperty("Build") == null) {
			System.setProperty("Build", suite + "_" + currentDate);
			System.setProperty("Suite", suite);
		}
		System.setProperty("Test", testName);

		// System.out.println("BuildName:" + System.getProperty("BuildName"));
		// System.out.println("Suite: " + suite);
		System.out.println("TestName: " + testName);
		testContext = context;

	}

	public void getScreenshot(String name) throws IOException {
		driver = appLibrary.getCurrentDriverInstance();
		String path = "screenshots/" + name;
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(src, new File(path));
		System.out.println("screenshot at :" + path);
		Reporter.log("screenshot for " + name + " available at :" + path, true);
	}

	public Properties loadUserProperties() {
		if (usersProperties == null) {
			usersProperties = new Properties();
			try {
				File f = new File("Users.properties");
				if (!f.exists()) {
					f = new File("TestData/users.properties");
				}

				usersProperties.load(new FileInputStream(f));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return usersProperties;
	}

	//@AfterMethod
	public void checkAlerts(ITestResult result) throws Exception {

		String screenshotName = result.getName() + "_" + appLibrary.browser + "_" + AppLibrary.randInt() + ".png";

		if (result.getStatus() == ITestResult.FAILURE) {
			try {

				// String screenshotName = result.getName() + "_" + appLibrary.browser + "_" +
				// AppLibrary.randInt()
				// + ".png";
				getScreenshot(screenshotName);
				Reporter.log("Failed at URL: " + appLibrary.getCurrentDriverInstance().getCurrentUrl(), true);
				int paramsLength = result.getParameters().length;
				Reporter.log("Screenshot Name : " + screenshotName, true);
				Reporter.log("ScreenShot for " + testName + " "
						+ ((paramsLength > 0) ? " with parameter " + result.getParameters()[1] : "") + " saved as "
						+ screenshotName + ".png", true);

			} catch (Exception e) {
				Reporter.log("Failed fetching URL and screenshot due to error:" + e.getMessage(), true);
				e.printStackTrace();
			}

			if (appLibrary.getCurrentSessionID() != null) {
				Reporter.log("Session id for " + testName + " is " + appLibrary.getCurrentSessionID(), true);
				Reporter.log("Session details for " + testName
						+ " can be found at https://www.browserstack.com/automate/sessions/"
						+ appLibrary.getCurrentSessionID() + ".json", true);
			}
		}

		appLibrary.getCurrentDriverInstance().close();
		appLibrary.getCurrentDriverInstance().quit();

	}

	@AfterClass(alwaysRun = true)
	public void quitBrowser() {
		appLibrary.closeBrowser();
		Reporter.log("Closing the Browser Successfully", true);
		System.out.println("Closing the Browser Successfully");
		Reporter.log("</table>");
	}

	// @BeforeMethod
	// public void nameBefore(Method method) {
	// appLibrary = new AppLibrary(testName + "." + method.getName());
	// }

	// public String returnTestName(String name) {
	// String a[];
	// a = name.split("\\.");
	// System.out.println(a.length);
	// return a[a.length - 1];
	// }

	public void tearDown(WebDriver driver) {
		if (driver != null) {
			driver.close();
			driver.quit();
			System.out.println("Closing the Browser Successfully" + "Joker1");
		}
	}

	public static WebElement SyncElement(WebDriver driver, String locator, int counter1) throws Exception {
		int counter = 0;
		for (counter = counter1; counter > 0; counter--) {
			try {
				return driver.findElement(By.xpath(locator));
			} catch (Exception e) {
				Thread.sleep(1000);
				continue;
			}
		}
		if (counter == counter1) {
			System.out.println("Element was not found, Locator:" + locator);
			throw new Exception("Failed to find the element");

		}
		return null;
	}

	public static void enterText(WebDriver driver, String locator, String text) throws Exception {

		if (text.equalsIgnoreCase("tab")) {
			SyncElement(driver, locator, GLOBALTIMEOUT).click();
			SyncElement(driver, locator, GLOBALTIMEOUT).sendKeys(Keys.TAB);
		} else if (!text.equalsIgnoreCase("")) {
			SyncElement(driver, locator, GLOBALTIMEOUT).click();
			SyncElement(driver, locator, GLOBALTIMEOUT).clear();
			SyncElement(driver, locator, GLOBALTIMEOUT).sendKeys(text + Keys.TAB);
		}

	}

}
