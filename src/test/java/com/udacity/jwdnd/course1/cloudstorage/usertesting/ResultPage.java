package com.udacity.jwdnd.course1.cloudstorage.usertesting;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ResultPage {

    @FindBy(id = "all-files-after-success-link")
    private WebElement homeButton;
    private WebDriverWait webDriverWait;

    public ResultPage(WebDriver webDriver){
        PageFactory.initElements(webDriver, this);
        webDriverWait = new WebDriverWait(webDriver, 2);
    }

    public void getHomePage(){
        webDriverWait.until(ExpectedConditions.visibilityOf(homeButton));
        homeButton.click();
    }
}
