package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumBy;
import java.time.Duration;

public class ProductPage {
    private AndroidDriver driver;
    private WebDriverWait wait;

    // --- Locators (Prioritizing Accessibility ID & Resource ID) ---
    // Header title verifying we are on the Product page
    private By productHeader = AppiumBy.xpath("//android.widget.TextView[@text='PRODUCTS']");
    
    // Grid or container containing the products
    private By productListContainer = AppiumBy.accessibilityId("test-PRODUCTS");
    
    // Individual product item containers
    private By productItem = AppiumBy.accessibilityId("test-Item");
    
    // Add to Cart button (first item card as default fallback/priority)
    // XPath allows targetting the first Add to Cart button dynamically if multiple exist
    private By addToCartButton = AppiumBy.xpath("(//android.view.ViewGroup[@content-desc='test-ADD TO CART'])[1]");
    
    // Shopping Cart icon button to view added items
    private By cartButton = AppiumBy.accessibilityId("test-Cart");

    public ProductPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Validation method to check if the Product page is displayed
    public boolean isProductPageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(productHeader)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Verify product list container is visible
    public boolean isProductListDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(productListContainer)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Add first item to cart
    public ProductPage addFirstProductToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        return this;
    }

    // Navigate to Cart Page - Page Chaining implementation
    public CartPage clickCartIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(cartButton)).click();
        return new CartPage(driver);
    }
}
