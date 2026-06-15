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
            options.setCapability("appium:replaceElementValue", true); // Fast instant typing
            options.setCapability("appium:noReset", true); // Fast startup by reusing installed app
            options.setCapability("appium:waitForIdleTimeout", 0);
            options.setCapability("appium:disableWindowAnimation", true);
            options.setCapability("appium:ignoreUnimportantViews", true);
            options.setCapability("appium:unicodeKeyboard", false);
            options.setCapability("appium:resetKeyboard", false);

            URL url = URI.create(appiumUrl).toURL();
            driver = new AndroidDriver(url, options);
            
            // Explicitly activate the app in case noReset:true skipped launching it
            try {
                driver.activateApp("com.swaglabsmobileapp");
            } catch (Exception e) {
                System.out.println("Failed to activate app on start: " + e.getMessage());
            }
        } else {
            System.out.println("----- Reusing Shared Appium Session - Performing Fast Reset -----");
            resetAppToHomeOrLogin();
        }

        // Configure implicit wait (0 seconds)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));

        // Check and handle logout/biometrics on startup/restart
        handleAppStartupState();
    }

    private void resetAppToHomeOrLogin() {
        try {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 4000) {
                boolean usernameVisible = !driver.findElements(AppiumBy.accessibilityId("test-Username")).isEmpty();
                boolean menuVisible = !driver.findElements(AppiumBy.accessibilityId("test-Menu")).isEmpty();
                if (usernameVisible || menuVisible) {
                    break;
                }
                var backHome = driver.findElements(AppiumBy.accessibilityId("test-BACK HOME"));
                if (!backHome.isEmpty()) {
                    backHome.get(0).click();
                    Thread.sleep(100);
                    continue;
                }
                var backHomeText = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, 'BACK HOME')]"));
                if (!backHomeText.isEmpty()) {
                    backHomeText.get(0).click();
                    Thread.sleep(100);
                    continue;
                }
                driver.navigate().back();
                Thread.sleep(100);
            }
        } catch (Exception e) {
            System.out.println("Error during fast reset: " + e.getMessage());
        }
    }

    private void handleAppStartupState() {
        System.out.println("----- Synchronizing App State on Startup/Restart -----");
        try {
            boolean mainUiVisible = false;
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 3000) {
                boolean usernameVisible = !driver.findElements(AppiumBy.accessibilityId("test-Username")).isEmpty();
                boolean menuVisible = !driver.findElements(AppiumBy.accessibilityId("test-Menu")).isEmpty();
                if (usernameVisible || menuVisible) {
                    mainUiVisible = true;
                    break;
                }
                Thread.sleep(200);
            }
            
            if (!mainUiVisible) {
                System.out.println("----- Main UI blocked. Dismissing potential biometric overlay -----");
                driver.navigate().back();
                Thread.sleep(300);
            }
            
            // Re-verify login state after overlay handling
            boolean menuVisible = !driver.findElements(AppiumBy.accessibilityId("test-Menu")).isEmpty();
            boolean usernameVisible = !driver.findElements(AppiumBy.accessibilityId("test-Username")).isEmpty();
            
            if (menuVisible) {
                System.out.println("----- Already logged in. Performing auto-logout to ensure clean state -----");
                driver.findElement(AppiumBy.accessibilityId("test-Menu")).click();
                
                WebDriverWait logoutWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                logoutWait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("test-LOGOUT"))).click();
                logoutWait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("test-Username"))); // Allow logout transition
            } else if (usernameVisible) {
                System.out.println("----- App is on Login Page and ready -----");
            } else {
                System.out.println("----- Warning: Neither Login Page nor Menu visible -----");
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
