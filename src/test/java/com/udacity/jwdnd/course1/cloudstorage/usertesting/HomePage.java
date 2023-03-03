package com.udacity.jwdnd.course1.cloudstorage.usertesting;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HomePage {

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "addNote-button")
    private WebElement addNoteButton;

    @FindBy(id="addCredential-button")
    private WebElement addCredentialButton;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id = "credential-url")
    private WebElement credentialUrl;

    @FindBy(id = "credential-username")
    private WebElement credentialUsername;

    @FindBy(id = "credential-password")
    private WebElement credentialPassword;

    @FindBy(id = "saveNote")
    private WebElement saveNote;

    @FindBy(id = "saveCredential" )
    private WebElement saveCredential;

    @FindBy(id = "user-notes")
    private List<WebElement> userNotes;

    @FindBy(id = "nav-notes-tab")
    private WebElement navNotesTab;

    @FindBy(id="nav-credentials-tab")
    private WebElement navCredentialsTab;

    @FindBy(xpath = "//*[@id=\"credentialTable\"]/tbody/tr/td[3]")
    private WebElement encryptedPassword;

    @FindBy(id = "nav-notes")
    private WebElement notesTab;

    @FindBy(id = "nav-credentials")
    private WebElement credentialsTab;

    @FindBy(xpath = "//*[@id=\"user-notes\"]/td[1]/button")
    private WebElement firstNoteEditButton;

    @FindBy(xpath = "//*[@id=\"userTable\"]")
    private WebElement noteParentElement;

    @FindBy(xpath = "//*[@id=\"credentialTable\"]")
    private WebElement credentialParentElement;

    @FindBy(css = "div[name='usernote_modal_dialog']")
    private WebElement noteDeleteModalDialog;

    @FindBy(css = "div[name='credential_modal_dialog']")
    private WebElement credentialDeleteModalDialog;

    @FindBy(id = "user-notes")
    private List<WebElement> allUserNotes;

    @FindBy(id = "user-credentials")
    private List<WebElement> allCredentials;

    private WebDriverWait webDriverWait;

    private WebElement noteEditButton;

    private WebElement credentialEditButton;

    private WebElement noteDeleteButton;

    private WebElement credentialDeleteButton;

    private WebElement noteConfirmDeleteButton;

    private WebElement credentialConfirmDeleteButton;

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

    public WebElement getNoteEditElement(int noteIndex){
        return noteParentElement.findElement(By.xpath("//*[@id=\"user-notes\"]/td["+ noteIndex +"]/button"));
    }

    public void editNote(String title, String description){

        webDriverWait.until(ExpectedConditions.visibilityOf(noteParentElement));

        int noteIndex = findNoteIndex(title);
        if(noteIndex != -1){
            noteEditButton = getNoteEditElement(noteIndex);

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

    public void deleteNote(String title){

        webDriverWait.until(ExpectedConditions.visibilityOf(noteParentElement));

        int noteIndex = findNoteIndex(title);
        if(noteIndex != -1){
            noteDeleteButton = getNoteDeleteElement(noteIndex);

            webDriverWait.until(ExpectedConditions.visibilityOf(notesTab));

            webDriverWait.until(ExpectedConditions.visibilityOf(noteDeleteButton));
            noteDeleteButton.click();

            noteConfirmDeleteButton = getNoteDeleteConfirmationElement();
            webDriverWait.until(ExpectedConditions.visibilityOf(noteConfirmDeleteButton));
            noteConfirmDeleteButton.click();
        }

    }

    private WebElement getNoteDeleteConfirmationElement(){

        webDriverWait.until(ExpectedConditions.visibilityOf(noteDeleteModalDialog));
        return noteDeleteModalDialog.findElement(By.xpath(".//a[contains(.,'Yes')]"));
    }

    private WebElement getNoteDeleteElement(int noteIndex) {
        return noteParentElement.findElement(By.xpath("//*[@id=\"user-notes\"]/td["+noteIndex +"]/a"));
    }

    public void getUserNotesTab(){

        webDriverWait.until(ExpectedConditions.visibilityOf(navNotesTab));
        navNotesTab.click();

    }

    public void userLogOut(){
        this.logoutButton.click();
    }

    public void addNewCredential(String url, String username, String password) {

        webDriverWait.until(ExpectedConditions.visibilityOf(navCredentialsTab));
        navCredentialsTab.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(addCredentialButton));
        addCredentialButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(credentialUrl));
        credentialUrl.click();
        credentialUrl.sendKeys(url);

        webDriverWait.until(ExpectedConditions.visibilityOf(credentialUsername));
        credentialUsername.click();
        credentialUsername.sendKeys(username);

        webDriverWait.until(ExpectedConditions.visibilityOf(credentialPassword));
        credentialPassword.click();
        credentialPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOf(saveCredential));
        saveCredential.click();
    }

    public void getUserCredentialsTab() {
        webDriverWait.until(ExpectedConditions.visibilityOf(navCredentialsTab));
        navCredentialsTab.click();
    }

    // why not use decryptpassword here to get the text
    public String getCredentialPassword(){
        webDriverWait.until(ExpectedConditions.visibilityOf(encryptedPassword));
        return encryptedPassword.getText();
    }

    public void editCredential(String url, String username, String password) {

        webDriverWait.until(ExpectedConditions.visibilityOf(credentialParentElement));

        int credentialIndex = findCredentialIndex(url);

        if(credentialIndex != -1){

            credentialEditButton = getCredentialEditElement(credentialIndex);

            webDriverWait.until(ExpectedConditions.visibilityOf(credentialsTab));

            webDriverWait.until(ExpectedConditions.visibilityOf(credentialEditButton));
            credentialEditButton.click();

            webDriverWait.until(ExpectedConditions.visibilityOf(credentialUsername));
            credentialUsername.click();
            credentialUsername.clear();
            credentialUsername.sendKeys(username);

            webDriverWait.until(ExpectedConditions.visibilityOf(credentialPassword));
            credentialPassword.click();
            credentialPassword.clear();
            credentialPassword.sendKeys(password);

            webDriverWait.until(ExpectedConditions.visibilityOf(saveCredential));
            saveCredential.click();
        }
    }

    private WebElement getCredentialEditElement(int credentialIndex) {
        return credentialParentElement.findElement(By.xpath("//*[@id=\"user-credentials\"]/td["+ credentialIndex +"]/button"));

    }

    private int findCredentialIndex(String url) {

        int rowIndex = 1;

        for (WebElement credential : allCredentials) {
            String[] credentialRows = credential.getText().split("\\r?\\n|\\r");;

            if(credentialRows[1].contains(url)){
                return rowIndex;
            }

            rowIndex++;
        }

        return -1;
    }

    public void deleteCredential(String url) {

        webDriverWait.until(ExpectedConditions.visibilityOf(credentialParentElement));

        int credentialIndex = findCredentialIndex(url);
        if(credentialIndex != -1){
            credentialDeleteButton = getCredentialDeleteElement(credentialIndex);

            webDriverWait.until(ExpectedConditions.visibilityOf(credentialsTab));

            webDriverWait.until(ExpectedConditions.visibilityOf(credentialDeleteButton));
            credentialDeleteButton.click();

            credentialConfirmDeleteButton = getCredentialDeleteConfirmationElement();
            webDriverWait.until(ExpectedConditions.visibilityOf(credentialConfirmDeleteButton));
            credentialConfirmDeleteButton.click();
        }
    }

    private WebElement getCredentialDeleteConfirmationElement() {

        webDriverWait.until(ExpectedConditions.visibilityOf(credentialDeleteModalDialog));
        return credentialDeleteModalDialog.findElement(By.xpath(".//a[contains(.,'Yes')]"));
    }

    private WebElement getCredentialDeleteElement(int credentialIndex) {
        return credentialParentElement.findElement(By.xpath("//*[@id=\"user-credentials\"]/td["+credentialIndex +"]/a"));
    }
}
