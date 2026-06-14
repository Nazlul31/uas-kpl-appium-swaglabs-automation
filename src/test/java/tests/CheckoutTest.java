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
        LoginPage loginPage = new LoginPage(driver);
        
        // 1. Login
        ProductPage productPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(productPage.isProductPageDisplayed(), "Redirection to Product Page failed.");

        // 2. Add product to cart
        productPage.addFirstProductToCart();

        // 3. Navigate to Cart
        CartPage cartPage = productPage.clickCartIcon();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Redirection to Cart Page failed.");
        Assert.assertTrue(cartPage.isCartItemDisplayed(), "No items found in the cart.");

        // 4. Click Checkout (using page chaining)
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        Assert.assertTrue(checkoutPage.isCheckoutPageDisplayed(), "Redirection to Checkout Information Page failed.");

        // 5. Fill customer information
        checkoutPage.fillCheckoutDetails("John", "Doe", "12345");

        // 6. Click Continue (Navigates to Overview Screen)
        checkoutPage.clickContinue();

        // 7. Click Finish (Places the order and goes to Checkout Complete Screen)
        checkoutPage.clickFinish();

        // 8. Verify order confirmation is displayed successfully
        Assert.assertTrue(checkoutPage.isOrderSuccessful(), "Order placement was unsuccessful.");
        
        String actualMsg = checkoutPage.getSuccessMessageText();
        Assert.assertEquals(actualMsg, "THANK YOU FOR YOUR ORDER", 
            "Success message mismatch. Got: " + actualMsg);
    }
}
