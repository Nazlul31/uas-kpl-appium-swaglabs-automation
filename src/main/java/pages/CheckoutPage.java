package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumBy;
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
    private By successHeader = AppiumBy.xpath("//android.widget.TextView[contains(@text, 'COMPLETE')]");
    private By successMessage = AppiumBy.xpath("//android.widget.TextView[contains(@text, 'THANK YOU')]");

    // Checkout Overview Page Header
    private By checkoutOverviewHeader = AppiumBy.xpath("//android.widget.TextView[contains(@text, 'OVERVIEW')]");

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
        try {
            element.sendKeys(firstName);
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
            element.sendKeys(firstName);
        }
        return this;
    }

    // Input last name
    public CheckoutPage enterLastName(String lastName) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField));
        try {
            element.sendKeys(lastName);
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField));
            element.sendKeys(lastName);
        }
        return this;
    }

    // Input postal code
    public CheckoutPage enterPostalCode(String postalCode) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(postalCodeField));
        try {
            element.sendKeys(postalCode);
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(postalCodeField));
            element.sendKeys(postalCode);
        }
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
        try {
            // Wait for the overview page to load first
            wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutOverviewHeader));
            
            // Scroll to the finish button
            driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"test-FINISH\"))"
            ));
        } catch (Exception e) {
            System.out.println("Scrolling to Finish button failed/not scrollable: " + e.getMessage());
        }
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
