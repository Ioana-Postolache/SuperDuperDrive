package com.udacity.jwdnd.course1.cloudstorage.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CredentialsTab {
    @FindBy(id = "add-credential-button")
    private WebElement addCredentialButton;

    @FindBy(css = "#credential-url.form-control")
    private WebElement credentialUrlInput;

    @FindBy(css = "#credential-username.form-control")
    private WebElement credentialUsernameInput;

    @FindBy(css = "#credential-password.form-control")
    private WebElement credentialPasswordInput;

    @FindBy(id = "save-credential-button")
    private WebElement submitButton;

    @FindBy(css = "#credentialTable > tbody > tr")
    private WebElement credentialTableFirstRow;

    @FindBy(id = "credentialTable")
    private WebElement credentialTable;

    public CredentialsTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void createOrUpdateCredential(String url, String username, String password) {
        credentialUrlInput.clear();
        credentialUrlInput.sendKeys(url);
        credentialUsernameInput.clear();
        credentialUsernameInput.sendKeys(username);
        credentialPasswordInput.clear();
        credentialPasswordInput.sendKeys(password);
        submitButton.click();
    }

    public String getFirstRowUrl() {
        return credentialTableFirstRow.findElement(By.cssSelector("[data-content='url']")).getText();
    }

    public String getFirstRowUsername() {
        return credentialTableFirstRow.findElement(By.cssSelector("[data-content='username']")).getText();
    }

    public String getFirstRowPassword() {
        return credentialTableFirstRow.findElement(By.cssSelector("[data-content='password']")).getText();
    }

    public String getCredentialUrlInput() {
        return credentialUrlInput.getAttribute("value");
    }

    public String getCredentialUsernameInput() {
        return credentialUsernameInput.getAttribute("value");
    }

    public String getCredentialPasswordInput() {
        return credentialPasswordInput.getAttribute("value");
    }

    public void addCredential(String url, String username, String password, WebDriver driver) {
        addCredentialButton.click();
        waitForCredentialsModal(driver);
        createOrUpdateCredential(url, username, password);
        WebDriverWait waitForCredentialsTable = new WebDriverWait(driver, 10);
        waitForCredentialsTable.until(webDriver -> webDriver.findElement(By.cssSelector("#credentialTable > tbody")).isDisplayed());
    }

    public void viewCredential(WebDriver driver) {
        WebElement firstRowEditButton = credentialTableFirstRow.findElement(By.cssSelector("[data-buttonType='edit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstRowEditButton);
        waitForCredentialsModal(driver);
    }

    public void deleteFirstCredential(WebDriver driver) {
        WebElement firstRowEditButton = credentialTableFirstRow.findElement(By.linkText("Delete"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstRowEditButton);
    }

    // inspired from:
    // https://stackoverflow.com/questions/12270092/best-way-to-check-that-element-is-not-present-using-selenium-webdriver-with-java
    public boolean isTableCredentialEmpty(WebDriver driver) {
        try {
            WebDriverWait waitForTableToBeEmpty = new WebDriverWait(driver, 10);
            waitForTableToBeEmpty.until(ExpectedConditions.not(ExpectedConditions.visibilityOf(credentialTable.findElement(By.tagName("tbody")))));
            return false;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return true;
        }
    }

    public void waitForCredentialsModal(WebDriver driver) {
        WebDriverWait waitForCredentialsModal = new WebDriverWait(driver, 10);
        waitForCredentialsModal.until(webDriver -> webDriver.findElement(By.id("credentialModal")).isDisplayed());
    }
}
