package base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import utils.ConfigReader;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

public class BaseTest {
    // Shared static driver across all test classes
    protected static AndroidDriver driver;

    @BeforeClass
    public void setUp() throws MalformedURLException {
        if (driver == null) {
            // Load configurations
            String appiumUrl = ConfigReader.getAppiumServerUrl();
            String platformName = ConfigReader.getPlatformName();
            String automationName = ConfigReader.getAutomationName();
            String deviceName = ConfigReader.getDeviceName();
            String appPath = ConfigReader.getAppPath();
            
            // Resolve absolute path for APK
            File app = new File(appPath);
            String absoluteAppPath = app.getAbsolutePath();

            System.out.println("----- Initializing Shared Appium Session -----");
            UiAutomator2Options options = new UiAutomator2Options();
            options.setPlatformName(platformName);
            options.setAutomationName(automationName);
            options.setDeviceName(deviceName);
            options.setApp(absoluteAppPath);
            options.setAutoGrantPermissions(true);
            options.setNewCommandTimeout(Duration.ofSeconds(60));
            options.setAppWaitActivity("*");
            options.setCapability("appium:appWaitPackage", "com.swaglabsmobileapp,com.android.systemui,com.google.android.systemui");
            options.setCapability("appium:noReset", true); // Fast startup by reusing installed app
            options.setCapability("appium:waitForIdleTimeout", 100);
            options.setCapability("appium:unicodeKeyboard", false);
            options.setCapability("appium:resetKeyboard", false);

            URL url = URI.create(appiumUrl).toURL();
            driver = new AndroidDriver(url, options);
        } else {
            System.out.println("----- Reusing Shared Appium Session - Restarting App -----");
            try {
                driver.terminateApp("com.swaglabsmobileapp");
                Thread.sleep(1200);
                driver.activateApp("com.swaglabsmobileapp");
                Thread.sleep(1800); // Allow app to settle
            } catch (Exception e) {
                System.out.println("Failed to restart app: " + e.getMessage());
            }
        }

        // Configure implicit wait (0 seconds)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));

        // Check and handle logout/biometrics on startup/restart
        handleAppStartupState();
    }

    private void handleAppStartupState() {
        System.out.println("----- Synchronizing App State on Startup/Restart -----");
        try {
            WebDriverWait startupWait = new WebDriverWait(driver, Duration.ofSeconds(4));
            
            // 1. Check if we are on the Product Page (already logged in)
            boolean loggedIn = false;
            try {
                startupWait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("test-Menu")));
                loggedIn = true;
            } catch (Exception e) {
                // Not on Product Page immediately
            }
            
            if (loggedIn) {
                System.out.println("----- Already logged in. Performing auto-logout to ensure clean state -----");
                driver.findElement(AppiumBy.accessibilityId("test-Menu")).click();
                
                WebDriverWait logoutWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                logoutWait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("test-LOGOUT"))).click();
                Thread.sleep(2500); // Allow logout transition
                return;
            }
            
            // 2. Check if we are on the Login Page.
            boolean loginPageVisible = false;
            try {
                startupWait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("test-Username")));
                loginPageVisible = true;
            } catch (Exception e) {
                // Login page not visible
            }
            
            if (!loginPageVisible) {
                System.out.println("----- Login page not visible. Dismissing potential biometric overlay -----");
                driver.navigate().back();
                Thread.sleep(1500);
                
                // Confirm login page is now visible
                try {
                    startupWait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("test-Username")));
                    System.out.println("----- Login page is now visible after back navigation -----");
                } catch (Exception e) {
                    System.out.println("----- Warning: Login page still not visible -----");
                }
            } else {
                System.out.println("----- Login page is visible -----");
            }
            
        } catch (Exception e) {
            System.out.println("Error synchronizing app state: " + e.getMessage());
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        if (driver != null) {
            System.out.println("----- Closing Shared Appium Session -----");
            driver.quit();
            driver = null;
            System.out.println("----- Session Closed -----");
        }
    }
}
