package com.udacity.jwdnd.course1.cloudstorage.usertesting;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;
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

    @FindBy(xpath = "//*[@id=\"userTable\"]")
    private WebElement parentElement;

    @FindBy(id = "user-notes")
    private List<WebElement> allUserNotes;

    private WebDriverWait webDriverWait;

    private WebElement noteEditButton;

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

    public WebElement getNodeEditElement(int rowIndex){

        return parentElement.findElement(By.xpath("//*[@id=\"user-notes\"]/td["+ rowIndex +"]/button"));
    }

    public void editNote(String title, String description){

        webDriverWait.until(ExpectedConditions.visibilityOf(parentElement));

        int noteIndex = findNoteIndex(title);
        if(noteIndex != -1){
            noteEditButton = getNodeEditElement(noteIndex);

            webDriverWait.until(ExpectedConditions.visibilityOf(notesTab));

            webDriverWait.until(ExpectedConditions.visibilityOf(noteEditButton));
            noteEditButton.click();

            webDriverWait.until(ExpectedConditions.visibilityOf(noteDescription));
            noteDescription.click();
            noteDescription.clear();
            noteDescription.sendKeys(description);

            webDriverWait.until(ExpectedConditions.visibilityOf(saveNote));
            saveNote.click();
        }

    }

    private int findNoteIndex(String title) {

        int rowIndex = 1;

        for (WebElement userNote : allUserNotes) {
            String[] noteRows = userNote.getText().split("\\r?\\n|\\r");;

            if(noteRows[1].contains(title)){
                return rowIndex;
            }

            rowIndex++;
        }

        return -1;
    }

    public void deleteNote(String title, String description){

    }

    public void getUserNotesTab(){

        webDriverWait.until(ExpectedConditions.visibilityOf(navNotesTab));
        navNotesTab.click();

    }

    public void userLogOut(){
        this.logoutButton.click();
    }
}
