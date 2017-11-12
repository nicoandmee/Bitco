package com.scraper.BitcoinScrapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.scraper.library.AppLibrary;
import com.scraper.library.TestBase;
import com.twocaptcha.api.ProxyType;
import com.twocaptcha.api.TwoCaptchaService;

public class BitcoVerifier1 extends TestBase {

	private WebDriver driver;
	private AppLibrary appLibrary;
	public Logger logger;
	PrintWriter writer;
	BufferedWriter bw = null;
	FileWriter fw = null;
	String verificationMessage = "";

	@DataProvider(name = "data")
	public String[][] getRegistrationDataFromExcel() throws Exception {

		String str[][] = AppLibrary
				.readExcel(appLibrary.getConfiguration().getSourcePath() + File.separator + "bitco.xls");
		return str;
	}

	@BeforeClass
	public void setup() throws Exception {
		try {
			appLibrary = new AppLibrary();
			writer = new PrintWriter(appLibrary.getConfiguration().getResultPath() + File.separator + "bitco1.txt",
					"UTF-8");
			writer.close();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("–lang= en");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		} catch (IOException e) {
			throw new Exception("File creation failed");
		}
	}

	@Test(dataProvider = "data")
	public void jokerOne(String ID, String pathToFront, String pathToBack, String proxy, String ExecutionIndicator) {

		if (ExecutionIndicator.equalsIgnoreCase("Yes")) {

			verificationMessage = "ID: " + ID;
			driver.get("http://www.google.com");
			driver.manage().deleteAllCookies();
			driver.get(appLibrary.getBaseUrl());
			AppLibrary.sleep(5000);
			try {
				SyncElement(driver, "//*[@id='id_username']", GLOBALTIMEOUT);

				String apiKey = "b0d49362f1aa63d3ffe43ba4409ef47c";
				String googleKey = "6Le95uoSAAAAAH3LKzssY-LHQOMu6eBag0yqlA6O";
				String pageUrl = "https://localbitcoins.com/register/";
				String[] papa = proxy.split(":");
				String proxyIp = papa[0];
				String proxyPort = papa[1];

				String username = "testeme" + new Random().nextInt();
				String password = "!@#$%^761" + new Random().nextInt();
				String email = "emebcoin" + new Random().nextInt() + "@gmail.com";

				TwoCaptchaService service = new TwoCaptchaService(apiKey, googleKey, pageUrl, proxyIp, proxyPort,
						ProxyType.SOCKS5);
				String responseToken = "";

				responseToken = service.solveCaptcha();
				System.out.println("The response token is: " + responseToken);

				JavascriptExecutor jse = (JavascriptExecutor) (driver);
				String jScript = "document.getElementById(\"g-recaptcha-response\").innerHTML=" + '"' + responseToken
						+ '"';
				jse.executeScript(jScript);

				driver.findElement(By.xpath("//*[@id='id_username']")).sendKeys(username);
				driver.findElement(By.xpath("//*[@id='id_email']")).sendKeys(email);
				driver.findElement(By.xpath("//*[@id='id_password1']")).sendKeys(password);
				driver.findElement(By.xpath("//*[@id='id_password2']")).sendKeys(password);

				driver.findElement(By.id("register-page-confirm-button")).click();
				SyncElement(driver, "//div[@id='base-status-messages']//b[contains(text(),'" + email + "')]",
						GLOBALTIMEOUT);
				SyncElement(driver, "//*[@id='navbar-site']", 5);

				driver.get("https://localbitcoins.com/accounts/identification/");

				// Switch focus to Jumio iframe
				new WebDriverWait(driver, 30).until(ExpectedConditions
						.frameToBeAvailableAndSwitchToIt(By.xpath("//iframe[@id='JUMIOIFRAME-iframe']")));
				driver.findElement(By.name("submitButton")).click();

				SyncElement(driver, "(//div[@class='id-box'])[3]", 9).click();

				// Choose File Upload vs. Webcam
				SyncElement(driver, "//button[@class='btn btn-large btn-upload']", 9).click();

				// Upload the front
				SyncElement(driver, "//input[@class='file-input']", 9)
						.sendKeys(appLibrary.getConfiguration().getSourcePath() + File.separator + pathToFront);

				// Continue
				SyncElement(driver, "//button[@class='btn btn-primary']", 9).click();

				Thread.sleep(5000);
				// Upload the back
				SyncElement(driver, "//input[@class='file-input']", 9)
						.sendKeys(appLibrary.getConfiguration().getSourcePath() + File.separator + pathToBack);

				// Continue
				SyncElement(driver, "//button[@class='btn btn-primary']", 9).click();

				// Jump out of the iframe
				driver.switchTo().defaultContent();

				try {
					// Warning: NetVerify slow as a hoe
					SyncElement(driver,
							"//div[@class='alert alert-success'][text()='You have been succesfully identified.']", 500);
					System.out.println(
							"Succesfully verified " + pathToFront + "as FRONT, and " + pathToBack + "as BACK.");

					verificationMessage = verificationMessage + "    PASS";
				} catch (Exception e) {
					System.out.println(e.getMessage());
					verificationMessage = verificationMessage + "    FAIL";
				}

			} catch (InterruptedException e) {
				System.out.println("Interrupted Exception while solving captcha" + e.toString());
				verificationMessage = verificationMessage + "    Interrupted Exception while solving captcha";
			} catch (IOException e) {
				System.out.println("IO Exception while solving captcha" + e.toString());
				verificationMessage = verificationMessage + "    IO Exception while solving captcha";
			} catch (Exception e) {
				System.out.println("Script Failure " + e.toString());
				verificationMessage = verificationMessage + "    Script Failure";
			}

			try {
				File file = new File(appLibrary.getConfiguration().getResultPath() + File.separator + "bitco1.txt");
				fw = new FileWriter(file.getAbsoluteFile(), true);
				bw = new BufferedWriter(fw);
				bw.write(ID + "\t\t");
				bw.write(verificationMessage);
				bw.newLine();
				bw.close();
				fw.close();
			} catch (Exception e) {
				System.out.println("FILE IO FAILURE");
			}
		}

	}

	@AfterClass(alwaysRun = true)
	public void quitBrowser() {
		tearDown(driver);
	}

}