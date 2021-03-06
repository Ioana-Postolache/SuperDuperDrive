package com.udacity.jwdnd.course1.cloudstorage.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "nav-files-tab")
    private WebElement navFilesTab;

    @FindBy(id = "nav-notes-tab")
    private WebElement navNotesTab;

    @FindBy(id = "nav-credentials-tab")
    private WebElement navCredentialsTab;

    @FindBy(css = ".active.show")
    private WebElement activeTav;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void logout(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutButton);
    }

    public WebElement getNavFilesTab() {
        return navFilesTab;
    }

    public WebElement getNavNotesTab() {
        return navNotesTab;
    }

    public void goToNavNotesTab(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", navNotesTab);
        WebDriverWait waitForNotesTab = new WebDriverWait(driver, 10);
        waitForNotesTab.until(webDriver -> webDriver.findElement(By.id("add-note-button")).isDisplayed());
    }

    public WebElement getNavCredentialsTab() {
        return navCredentialsTab;
    }

    public void goToNavCredentialsTab(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", navCredentialsTab);
        WebDriverWait waitForNotesTab = new WebDriverWait(driver, 10);
        waitForNotesTab.until(webDriver -> webDriver.findElement(By.id("add-credential-button")).isDisplayed());
    }

    public String getActiveTabText() {
        return activeTav.getText();
    }
}
