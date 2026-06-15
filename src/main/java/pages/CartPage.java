package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumBy;
import java.time.Duration;

public class CartPage {
    private AndroidDriver driver;
    private WebDriverWait wait;

    // --- Locators (Prioritizing Accessibility ID & Resource ID) ---
    private By cartHeader = AppiumBy.xpath("//android.widget.TextView[@text='YOUR CART']");
    
    // Individual item container in the cart
    private By cartItem = AppiumBy.accessibilityId("test-Description");
    
    // Remove button inside cart item description
    private By removeButton = AppiumBy.accessibilityId("test-REMOVE");
    
    // Checkout button to proceed to customer details page
    private By checkoutButton = AppiumBy.accessibilityId("test-CHECKOUT");

    public CartPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Validation method to check if the Cart page is displayed
    public boolean isCartPageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(cartHeader)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Check if at least one item is present in the cart
    public boolean isCartItemDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(cartItem)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Click remove button (optional utility)
    public CartPage removeProduct() {
        wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
        return this;
    }

    // Navigate to Checkout Page - Page Chaining implementation
    public CheckoutPage clickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        return new CheckoutPage(driver);
    }
}
