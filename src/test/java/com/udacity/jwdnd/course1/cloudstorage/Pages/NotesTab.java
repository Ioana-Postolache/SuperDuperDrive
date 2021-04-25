package com.udacity.jwdnd.course1.cloudstorage.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NotesTab {
    @FindBy(id = "add-note-button")
    private WebElement addNoteButton;

    @FindBy(css = "#note-title.form-control")
    private WebElement noteTitleInput;

    @FindBy(css = "#note-description.form-control")
    private WebElement noteDescriptionTextArea;

    @FindBy(id = "save-note-button")
    private WebElement submitButton;

    @FindBy(css = "#noteTable > tbody > tr")
    private WebElement noteTableFirstRow;

    @FindBy(id = "noteTable")
    private WebElement noteTable;

    public NotesTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void createOrUpdateNote(String noteTitle, String noteDescription) {
        noteTitleInput.clear();
        noteTitleInput.sendKeys(noteTitle);
        noteDescriptionTextArea.clear();
        noteDescriptionTextArea.sendKeys(noteDescription);
        submitButton.click();
    }

    public String getFirstRowNoteTitle() {
        return noteTableFirstRow.findElement(By.cssSelector("[data-content='noteTitle']")).getText();
    }

    public String getFirstRowNoteDescription() {
        return noteTableFirstRow.findElement(By.cssSelector("[data-content='noteDescription']")).getText();
    }

    public void addNote(String noteTitle, String noteDescription, WebDriver driver) {
        addNoteButton.click();
        waitForNotesModal(driver);
        createOrUpdateNote(noteTitle, noteDescription);
        WebDriverWait waitForNotesTable = new WebDriverWait(driver, 10);
        waitForNotesTable.until(webDriver -> webDriver.findElement(By.cssSelector("#noteTable > tbody")).isDisplayed());
    }

    public void editNote(String noteTitle, String noteDescription, WebDriver driver) {
        WebElement firstRowEditButton = noteTableFirstRow.findElement(By.cssSelector("[data-buttonType='edit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstRowEditButton);
        waitForNotesModal(driver);
        createOrUpdateNote(noteTitle, noteDescription);
    }

    public void deleteFirstNote(WebDriver driver) {
        WebElement firstRowEditButton = noteTableFirstRow.findElement(By.linkText("Delete"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstRowEditButton);
    }

    // inspired from:
    // https://stackoverflow.com/questions/12270092/best-way-to-check-that-element-is-not-present-using-selenium-webdriver-with-java
    public boolean isTableNoteEmpty(WebDriver driver) {
        try {
            WebDriverWait waitForTableToBeEmpty = new WebDriverWait(driver, 10);
            waitForTableToBeEmpty.until(ExpectedConditions.not(ExpectedConditions.visibilityOf(noteTable.findElement(By.tagName("tbody")))));
            return false;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return true;
        }
    }

    public void waitForNotesModal(WebDriver driver) {
        WebDriverWait waitForNotesModal = new WebDriverWait(driver, 10);
        waitForNotesModal.until(webDriver -> webDriver.findElement(By.id("noteModal")).isDisplayed());
    }
}
