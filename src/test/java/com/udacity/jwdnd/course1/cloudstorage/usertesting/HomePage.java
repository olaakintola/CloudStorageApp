package com.udacity.jwdnd.course1.cloudstorage.usertesting;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HomePage {

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "addNote-button")
    private WebElement addNoteButton;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id = "saveNote")
    private WebElement saveNote;

    @FindBy(id = "user-notes")
    private List<WebElement> userNotes;

    @FindBy(id = "nav-notes-tab")
    private WebElement navNotesTab;

    @FindBy(id = "nav-notes")
    private WebElement notesTab;

    @FindBy(xpath = "//*[@id=\"user-notes\"]/td[1]/button")
    private WebElement firstNoteEditButton;

    private WebDriverWait webDriverWait;

    public HomePage(WebDriver webDriver){
        PageFactory.initElements(webDriver, this);
        webDriverWait = new WebDriverWait(webDriver, 2);
    }

    public void addNewNote(String title, String description){

        webDriverWait.until(ExpectedConditions.visibilityOf(navNotesTab));
        navNotesTab.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(addNoteButton));
        addNoteButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(noteTitle));
        noteTitle.click();
        noteTitle.sendKeys(title);

        webDriverWait.until(ExpectedConditions.visibilityOf(noteDescription));
        noteDescription.click();
        noteDescription.sendKeys(description);

        webDriverWait.until(ExpectedConditions.visibilityOf(saveNote));
        saveNote.click();

    }

    public void editNote( String description){

        webDriverWait.until(ExpectedConditions.visibilityOf(notesTab));

        webDriverWait.until(ExpectedConditions.visibilityOf(firstNoteEditButton));
        firstNoteEditButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(noteDescription));
        noteDescription.click();
        noteDescription.clear();
        noteDescription.sendKeys(description);

        webDriverWait.until(ExpectedConditions.visibilityOf(saveNote));
        saveNote.click();

    }

    public void getUserNotesTab(){

        webDriverWait.until(ExpectedConditions.visibilityOf(navNotesTab));
        navNotesTab.click();

    }

    public void userLogOut(){
        this.logoutButton.click();
    }
}
