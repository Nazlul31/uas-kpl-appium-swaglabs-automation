package pages;

import io.appium.java-client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java-client.AppiumBy;
import java.time.Duration;

public class CheckoutPage {
    private AndroidDriver driver;
    private WebDriverWait wait;

    // --- Locators (Prioritizing Accessibility ID & Resource ID) ---
    // Checkout Information Page Header
    private By checkoutInfoHeader = AppiumBy.xpath("//android.widget.TextView[@text='CHECKOUT: INFORMATION']");

    // Checkout Form Fields
    private By firstNameField = AppiumBy.accessibilityId("test-First Name");
    private By lastNameField = AppiumBy.accessibilityId("test-Last Name");
    private By postalCodeField = AppiumBy.accessibilityId("test-Zip/Postal Code");
    
    // Buttons
    private By continueButton = AppiumBy.accessibilityId("test-CONTINUE");
    private By finishButton = AppiumBy.accessibilityId("test-FINISH");

    // Order Success Elements
    private By successHeader = AppiumBy.xpath("//android.widget.TextView[@text='CHECKOUT: COMPLETE!']");
    private By successMessage = AppiumBy.xpath("//android.widget.TextView[@text='THANK YOU FOR YOUR ORDER']");

    public CheckoutPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Validation method to check if the Checkout Information page is displayed
    public boolean isCheckoutPageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutInfoHeader)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Input first name
    public CheckoutPage enterFirstName(String firstName) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        element.clear();
        element.sendKeys(firstName);
        return this;
    }

    // Input last name
    public CheckoutPage enterLastName(String lastName) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField));
        element.clear();
        element.sendKeys(lastName);
        return this;
    }

    // Input postal code
    public CheckoutPage enterPostalCode(String postalCode) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(postalCodeField));
        element.clear();
        element.sendKeys(postalCode);
        return this;
    }

    // Fill all checkout details
    public CheckoutPage fillCheckoutDetails(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        return this;
    }

    // Click Continue to navigate to Checkout Overview
    public CheckoutPage clickContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
        return this;
    }

    // Click Finish to place order
    public CheckoutPage clickFinish() {
        // We will make sure we scroll to/locate the finish button and click it
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();
        return this;
    }

    // Verify order completed successfully
    public boolean isOrderSuccessful() {
        try {
            boolean isHeaderVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(successHeader)).isDisplayed();
            boolean isMsgVisible = driver.findElement(successMessage).isDisplayed();
            return isHeaderVisible && isMsgVisible;
        } catch (Exception e) {
            return false;
        }
    }

    // Get order completion success message text
    public String getSuccessMessageText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
        } catch (Exception e) {
            return "";
        }
    }
}
