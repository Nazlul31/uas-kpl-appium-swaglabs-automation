package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductPage;

public class LoginTest extends BaseTest {

    @Test(priority = 1)
    public void testFailedLogin_TC002() {
        System.out.println("----- TC002: Start Failed Login Test -----");
        LoginPage loginPage = new LoginPage(driver);
        
        System.out.println("TC002 - Step 1: Verifying LoginPage is displayed...");
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed");

        System.out.println("TC002 - Step 2: Attempting login with incorrect credentials...");
        loginPage.enterUsername("standard_user")
                 .enterPassword("wrong_sauce")
                 .clickLoginFailed();

        System.out.println("TC002 - Step 3: Validating error message display...");
        String expectedError = "Username and password do not match any user in this service.";
        String actualError = loginPage.getErrorMessage();
        
        Assert.assertTrue(actualError.contains("do not match"), 
            "Error message validation failed. Expected containing 'do not match', but got: " + actualError);
        System.out.println("----- TC002: Test Completed Successfully -----");
    }

    @Test(priority = 2)
    public void testSuccessfulLogin_TC001() {
        System.out.println("----- TC001: Start Successful Login Test -----");
        LoginPage loginPage = new LoginPage(driver);
        
        System.out.println("TC001 - Step 1: Logging in with valid credentials...");
        ProductPage productPage = loginPage.login("standard_user", "secret_sauce");

        System.out.println("TC001 - Step 2: Verifying redirection to Product Page...");
        Assert.assertTrue(productPage.isProductPageDisplayed(), "Successful login failed, Product Page not displayed");
        System.out.println("----- TC001: Test Completed Successfully -----");
    }
}
