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
        System.out.println("----- TC004: Start Add Product To Cart Test -----");
        LoginPage loginPage = new LoginPage(driver);
        
        System.out.println("TC004 - Step 1: Logging in with valid credentials...");
        ProductPage productPage = loginPage.login("standard_user", "secret_sauce");
        System.out.println("TC004 - Step 2: Verifying redirection to Product Page...");
        boolean isProductPageVis = productPage.isProductPageDisplayed();
        System.out.println("TC004 - Step 2: Product Page Displayed = " + isProductPageVis);
        Assert.assertTrue(isProductPageVis, "Redirection to Product Page failed.");
 
        System.out.println("TC004 - Step 3: Adding first product to cart...");
        productPage.addFirstProductToCart();
        System.out.println("TC004 - Step 3: Product added.");
 
        System.out.println("TC004 - Step 4: Clicking cart icon...");
        CartPage cartPage = productPage.clickCartIcon();
        System.out.println("TC004 - Step 4: Cart icon clicked, verifying Cart Page display...");
        boolean isCartPageVis = cartPage.isCartPageDisplayed();
        System.out.println("TC004 - Step 4: Cart Page Displayed = " + isCartPageVis);
        Assert.assertTrue(isCartPageVis, "Redirection to Cart Page failed.");
 
        System.out.println("TC004 - Step 5: Verifying product is displayed in the cart...");
        boolean isCartItemVis = cartPage.isCartItemDisplayed();
        System.out.println("TC004 - Step 5: Cart Item Displayed = " + isCartItemVis);
        Assert.assertTrue(isCartItemVis, "The cart is empty. Product was not added.");
        System.out.println("----- TC004: Test Completed Successfully -----");
    }
}
