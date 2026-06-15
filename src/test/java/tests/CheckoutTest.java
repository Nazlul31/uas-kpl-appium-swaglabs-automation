package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductPage;

public class CheckoutTest extends BaseTest {

    @Test(priority = 1)
    public void testCheckoutProcess_TC005() {
        System.out.println("----- TC005: Start Complete Checkout Process Test -----");
        LoginPage loginPage = new LoginPage(driver);
        
        // 1. Login
        System.out.println("TC005 - Step 1: Logging in with valid credentials...");
        ProductPage productPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(productPage.isProductPageDisplayed(), "Redirection to Product Page failed.");

        // 2. Add product to cart
        System.out.println("TC005 - Step 2: Adding first product to cart...");
        productPage.addFirstProductToCart();

        // 3. Navigate to Cart
        System.out.println("TC005 - Step 3: Navigating to Cart Page...");
        CartPage cartPage = productPage.clickCartIcon();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Redirection to Cart Page failed.");
        Assert.assertTrue(cartPage.isCartItemDisplayed(), "No items found in the cart.");

        // 4. Click Checkout
        System.out.println("TC005 - Step 4: Navigating to Checkout Details form...");
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        Assert.assertTrue(checkoutPage.isCheckoutPageDisplayed(), "Redirection to Checkout Information Page failed.");

        // 5. Fill customer information
        System.out.println("TC005 - Step 5: Filling customer form (First Name, Last Name, Zip)...");
        checkoutPage.fillCheckoutDetails("John", "Doe", "12345");

        // 6. Click Continue (Navigates to Overview Screen)
        System.out.println("TC005 - Step 6: Submitting details form to go to Overview...");
        checkoutPage.clickContinue();

        // 7. Click Finish (Places the order and goes to Checkout Complete Screen)
        System.out.println("TC005 - Step 7: Scrolling and clicking Finish on Overview...");
        checkoutPage.clickFinish();

        // 8. Verify order confirmation is displayed successfully
        System.out.println("TC005 - Step 8: Verifying complete checkout success screen...");
        Assert.assertTrue(checkoutPage.isOrderSuccessful(), "Order placement was unsuccessful.");
        
        String actualMsg = checkoutPage.getSuccessMessageText();
        Assert.assertTrue(actualMsg.toUpperCase().contains("THANK YOU"), 
            "Success message mismatch. Expected containing 'THANK YOU', but got: " + actualMsg);
        System.out.println("----- TC005: Test Completed Successfully -----");
    }
}
