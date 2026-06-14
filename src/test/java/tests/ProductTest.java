package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductPage;

public class ProductTest extends BaseTest {

    @Test(priority = 1)
    public void testProductDisplayVerification_TC003() {
        LoginPage loginPage = new LoginPage(driver);
        
        // Log in to access the products page
        ProductPage productPage = loginPage.login("standard_user", "secret_sauce");

        // Verify we are on the Product Page
        Assert.assertTrue(productPage.isProductPageDisplayed(), "Product Page header is not displayed.");

        // Verify the product list items container is present
        Assert.assertTrue(productPage.isProductListDisplayed(), "Product list items container is not displayed.");
    }
}
