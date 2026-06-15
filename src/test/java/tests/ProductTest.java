package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductPage;

public class ProductTest extends BaseTest {

    @Test(priority = 1)
    public void testProductDisplayVerification_TC003() {
        System.out.println("----- TC003: Start Product Display Verification Test -----");
        LoginPage loginPage = new LoginPage(driver);
        
        System.out.println("TC003 - Step 1: Logging in with valid credentials...");
        ProductPage productPage = loginPage.login("standard_user", "secret_sauce");
        System.out.println("TC003 - Step 2: Login method completed.");

        System.out.println("TC003 - Step 3: Verifying Product Page is displayed...");
        boolean isHeaderDisplayed = productPage.isProductPageDisplayed();
        System.out.println("TC003 - Step 3: Product Page Header Displayed = " + isHeaderDisplayed);
        Assert.assertTrue(isHeaderDisplayed, "Product Page header is not displayed.");

        System.out.println("TC003 - Step 4: Verifying Product List is displayed...");
        boolean isListDisplayed = productPage.isProductListDisplayed();
        System.out.println("TC003 - Step 4: Product List Displayed = " + isListDisplayed);
        Assert.assertTrue(isListDisplayed, "Product list items container is not displayed.");
        System.out.println("----- TC003: Test Completed Successfully -----");
    }
}
