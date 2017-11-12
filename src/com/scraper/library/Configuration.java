package com.scraper.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

	private String url;
	private String user;
	private String pwd;
	private String browserName;
	private String chromeDriverPath;
	private String ieDriverPath;
	private Boolean isBrowserStackExecution;
	private Boolean isRemoteExecution;
	private String remoteWebDriverUrl;
	private String browserStackUserName;
	private String browserStackAuthKey;
	private String browserStackBrowserVersion;
	private String browserStackOS;
	private String browserStackOSVersion;
	private String browserStackPlatform;
	private String browserStackDevice;
	private String isEmulator;
	private String os;
	private String remoteGridUrl;
	private String browserVersion;
	private String deviceName;
	private String deviceVersion;
	private String sourcepath;
	private String resultpath;

	public Configuration() {
		final Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("config.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		browserName = prop.getProperty("browser.name");
		url = prop.getProperty("instance.url");
		user = prop.getProperty("user");
		chromeDriverPath = prop.getProperty("chrome.driver");
		isBrowserStackExecution = Boolean.parseBoolean(prop.getProperty("isbrowserstack.execution", "false").trim());
		isRemoteExecution = Boolean.parseBoolean(prop.getProperty("isremote.execution", "false").trim());
		remoteGridUrl = prop.getProperty("remote.grid.url");
		remoteWebDriverUrl = prop.getProperty("remote.webdriver.url");
		os = prop.getProperty("os");
		browserStackUserName = prop.getProperty("browserstack.username");
		browserStackAuthKey = prop.getProperty("browserstack.authkey");
		browserStackBrowserVersion = prop.getProperty("browserstack.browserversion");
		browserStackOS = prop.getProperty("browserstack.os");
		browserStackOSVersion = prop.getProperty("browserstack.osversion");
		browserStackPlatform = prop.getProperty("browserstack.platform");
		browserStackDevice = prop.getProperty("browserstack.device");
		isEmulator = prop.getProperty("browserstack.isEmulator");
		browserVersion = prop.getProperty("browser.version");
		sourcepath = prop.getProperty("sourcepath");
		resultpath = prop.getProperty("resultpath");
	}

	public String getSourcePath() {
		return sourcepath;
	}

	public String getResultPath() {
		return resultpath;
	}
	
	public String getURL() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPwd() {
		return pwd;
	}

	public String getBrowserName() {
		return browserName;
	}

	public String getChromeDriverPath() {
		return chromeDriverPath;
	}

	public String getIEDriverPath() {
		return ieDriverPath;
	}
	

	public boolean isBrowserStackExecution() {
		return isBrowserStackExecution;
	}

	public String getBrowserStackUserName() {
		return browserStackUserName;
	}

	public String getBrowserStackAuthKey() {
		return browserStackAuthKey;
	}

	public String getBrowserStackBrowserVersion() {
		return browserStackBrowserVersion;
	}

	public String getBrowserStackOS() {
		return browserStackOS;
	}

	public String getBrowserStackOSVersion() {
		return browserStackOSVersion;
	}

	public String getRemoteWebDriverUrl() {
		return remoteWebDriverUrl;
	}

	public boolean isRemoteExecution() {
		return isRemoteExecution;
	}

	public String getRemoteGridUrl() {
		return remoteGridUrl;
	}

	public String getBrowserStackPlatform() {
		return browserStackPlatform;
	}

	public String getBrowserStackDevice() {
		return browserStackDevice;
	}

	public String getBrowserStackIsEmulator() {
		return isEmulator;
	}

	public String getOS() {
		return os;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}
}