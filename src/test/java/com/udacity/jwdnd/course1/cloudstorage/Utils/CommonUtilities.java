package com.udacity.jwdnd.course1.cloudstorage.Utils;

import com.udacity.jwdnd.course1.cloudstorage.Pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.Pages.SignupPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonUtilities {
    private final static String username = "username";
    private final static String password = "password";
    private static LoginPage login;

    public static void signup(WebDriver driver) {
        login = new LoginPage(driver);
        SignupPage signup = new SignupPage(driver);
        // sign up a new users
        login.goToSignup();
        signup.signup(username, password, driver);
    }

    public static void login(WebDriver driver, String baseURL) {
        // login
        driver.get(baseURL + "/login");
        login.login(username, password);
        // go to home
        WebDriverWait waitForHome = new WebDriverWait(driver, 10);
        waitForHome.until(webDriver -> webDriver.getCurrentUrl().equals(baseURL + "/home"));
    }
}
