package pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumBy;
import java.time.Duration;

public class LoginPage {
    private AndroidDriver driver;
    private WebDriverWait wait;

    // --- Locators (Prioritizing Accessibility ID & Resource ID) ---
    // Locators are based on the official Swag Labs Mobile App (native components)
    private By usernameField = AppiumBy.accessibilityId("test-Username");
    private By passwordField = AppiumBy.accessibilityId("test-Password");
    private By loginButton = AppiumBy.accessibilityId("test-LOGIN");
    
    // Error container element
    private By errorContainer = AppiumBy.accessibilityId("test-Error message");
    // Text inside error message
    private By errorMessageText = AppiumBy.xpath("//android.view.ViewGroup[@content-desc='test-Error message']/android.widget.TextView");

    // Constructor receiving AndroidDriver
    public LoginPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Method to check if Login Page is displayed
    public boolean isLoginPageDisplayed() {
        try {
            // Use a short wait first to check if login page is immediately visible
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).isDisplayed();
        } catch (Exception e) {
            // Attempt to dismiss potential biometric prompt if username field is blocked
            try {
                System.out.println("----- LoginPage: Username not visible, attempting to dismiss biometric dialog -----");
                driver.navigate().back();
                Thread.sleep(1200); // Wait for screen to settle
                return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).isDisplayed();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    // Input Username
    public LoginPage enterUsername(String username) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        element.clear();
        element.sendKeys(username);
        return this;
    }

    // Input Password
    public LoginPage enterPassword(String password) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        element.clear();
        element.sendKeys(password);
        return this;
    }

    // Click Login Button - Page Chaining implementation
    public ProductPage clickLoginSuccess() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        return new ProductPage(driver);
    }

    // Click Login Button for failure flow (stays on Login Page)
    public LoginPage clickLoginFailed() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        return this;
    }

    // Complete Login action helper (successful flow)
    public ProductPage login(String username, String password) {
        isLoginPageDisplayed(); // Ensure login page is visible and biometric overlays are dismissed
        enterUsername(username);
        enterPassword(password);
        return clickLoginSuccess();
    }

    // Get error message text
    public String getErrorMessage() {
        try {
            // Wait for the container to be visible
            WebElement container = wait.until(ExpectedConditions.visibilityOfElementLocated(errorContainer));
            return container.findElement(AppiumBy.className("android.widget.TextView")).getText();
        } catch (Exception e) {
            return "";
        }
    }
}
