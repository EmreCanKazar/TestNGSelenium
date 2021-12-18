package com.enuygun.pages;

import com.enuygun.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public abstract class  BasePage {

    public BasePage() {
        PageFactory.initElements(Driver.get(), this);
    }

    @FindBy(css = "h1[class='oro-subtitle']")
    public WebElement pageSubTitle;
 


}
