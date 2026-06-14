package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
import pages.ProductPage;

public class CartTest extends BaseTest {

    @Test(priority = 1)
    public void testAddProductToCart_TC004() {
        LoginPage loginPage = new LoginPage(driver);
        
        // 1. Login
        ProductPage productPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(productPage.isProductPageDisplayed(), "Redirection to Product Page failed.");

        // 2. Add product to cart
        productPage.addFirstProductToCart();

        // 3. Navigate to Cart page (using page chaining)
        CartPage cartPage = productPage.clickCartIcon();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Redirection to Cart Page failed.");

        // 4. Verify item is in the cart
        Assert.assertTrue(cartPage.isCartItemDisplayed(), "The cart is empty. Product was not added.");
    }
}
