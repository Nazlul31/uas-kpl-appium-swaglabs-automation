package base;

import io.appium.java-client.android.AndroidDriver;
import io.appium.java-client.android.options.UiAutomator2Options;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import utils.ConfigReader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

public class BaseTest {
    protected AndroidDriver driver;

    @BeforeClass
    public void setUp() throws MalformedURLException {
        // Load configurations
        String appiumUrl = ConfigReader.getAppiumServerUrl();
        String platformName = ConfigReader.getPlatformName();
        String automationName = ConfigReader.getAutomationName();
        String deviceName = ConfigReader.getDeviceName();
        String appPath = ConfigReader.getAppPath();
        
        // Resolve absolute path for APK
        File app = new File(appPath);
        String absoluteAppPath = app.getAbsolutePath();

        System.out.println("----- Setting Up Appium Session -----");
        System.out.println("Appium Server URL : " + appiumUrl);
        System.out.println("Platform Name     : " + platformName);
        System.out.println("Automation Name   : " + automationName);
        System.out.println("Device Name       : " + deviceName);
        System.out.println("App Path          : " + absoluteAppPath);

        // Configure UiAutomator2Options (replacing deprecated DesiredCapabilities)
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName(platformName);
        options.setAutomationName(automationName);
        options.setDeviceName(deviceName);
        options.setApp(absoluteAppPath);
        
        // Settings to auto-grant permissions and speed up execution
        options.setAutoGrantPermissions(true);
        options.setNewCommandTimeout(Duration.ofSeconds(60));

        // Initialize AndroidDriver using URI conversion to URL
        URL url = URI.create(appiumUrl).toURL();
        driver = new AndroidDriver(url, options);

        // Configure implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        System.out.println("----- Appium Session Started Successfully -----");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            System.out.println("----- Closing Appium Session -----");
            driver.quit();
            System.out.println("----- Session Closed -----");
        }
    }
}
