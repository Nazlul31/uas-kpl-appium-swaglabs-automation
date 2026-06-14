package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductPage;

public class LoginTest extends BaseTest {

    @Test(priority = 1)
    public void testFailedLogin_TC002() {
        LoginPage loginPage = new LoginPage(driver);
        
        // Ensure we are on the login screen
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed");

        // Attempt login with incorrect credentials
        loginPage.enterUsername("standard_user")
                 .enterPassword("wrong_sauce")
                 .clickLoginFailed();

        // Validate error message
        String expectedError = "Username and password do not match any user in this service.";
        String actualError = loginPage.getErrorMessage();
        
        // Assert error matches expected string (flexible match containing primary keyword)
        Assert.assertTrue(actualError.contains("do not match"), 
            "Error message validation failed. Expected containing 'do not match', but got: " + actualError);
    }

    @Test(priority = 2)
    public void testSuccessfulLogin_TC001() {
        LoginPage loginPage = new LoginPage(driver);
        
        // Login with valid credentials
        // LoginPage.login returns a new instance of ProductPage (Page Chaining)
        ProductPage productPage = loginPage.login("standard_user", "secret_sauce");

        // Verify successful redirection to Product List page
        Assert.assertTrue(productPage.isProductPageDisplayed(), "Successful login failed, Product Page not displayed");
    }
}
