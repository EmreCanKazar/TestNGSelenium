package com.enuygun.utilities;

import com.aventstack.extentreports.ExtentTest;
import com.enuygun.data.DataFinder;
import com.enuygun.data.GetData;
import com.enuygun.tests.TestBase;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BrowserUtils {

    protected static ExtentTest extentLogger;

    /*
     * takes screenshot
     * @param name
     * take a name of a test and returns a path to screenshot takes
     */
    public static String getScreenshot(String name) throws IOException {
        // name the screenshot with the current date time to avoid duplicate name
        String date = new SimpleDateFormat("yyyy.MM.dd.hh.mmss").format(new Date());
        // TakesScreenshot ---> interface from selenium which takes screenshots
        TakesScreenshot ts = (TakesScreenshot) Driver.get();
        File source = ts.getScreenshotAs(OutputType.FILE);
        // full path to the screenshot location
        String target = System.getProperty("user.dir") + "/test-output/Screenshots/" + name + date + ".png";
        File finalDestination = new File(target);
        // save the screenshot to the path given
        FileUtils.copyFile(source, finalDestination);
        return target;
    }

    /**
     * Switches to new window by the exact title. Returns to original window if target title not found
     *
     * @param targetTitle
     */
    public  void switchToWindow(String targetTitle, WebDriver driver) {
        String origin = Driver.get().getWindowHandle();
        for (String handle : Driver.get().getWindowHandles()) {
            Driver.get().switchTo().window(handle);
            if (Driver.get().getTitle().equals(targetTitle)) {
                return;
            }
        }
        Driver.get().switchTo().window(origin);
    }

    /**
     * Moves the mouse to given element
     *
     * @param element on which to hover
     */
    public static void hover(WebElement element) {
        Actions actions = new Actions(Driver.get());
        actions.moveToElement(element).perform();
    }

    /**
     * return a list of string from a list of elements
     *
     * @param list of webelements
     * @return list of string
     */
    public static List<String> getElementsText(List<WebElement> list) {
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : list) {
            elemTexts.add(el.getText());
        }
        return elemTexts;
    }

    /**
     * Extracts text from list of elements matching the provided locator into new List<String>
     *
     * @param locator
     * @return list of strings
     */
    public static List<String> getElementsText(By locator) {

        List<WebElement> elems = Driver.get().findElements(locator);
        List<String> elemTexts = new ArrayList<>();

        for (WebElement el : elems) {
            elemTexts.add(el.getText());
        }
        return elemTexts;
    }

    /**
     * Performs a pause
     *
     * @param seconds
     */
    public static void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for the provided element to be visible on the page
     *
     * @param element
     * @param timeToWaitInSec
     * @return
     */
    public static WebElement waitForVisibility(WebElement element, int timeToWaitInSec) {
        WebDriverWait wait = new WebDriverWait(Driver.get(), timeToWaitInSec);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits for element matching the locator to be visible on the page
     *
     * @param locator
     * @param timeout
     * @return
     */
    public static WebElement waitForVisibility(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.get(), timeout);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for provided element to be clickable
     *
     * @param element
     * @param timeout
     * @return
     */
    public static WebElement waitForClickablility(WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.get(), timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits for element matching the locator to be clickable
     *
     * @param locator
     * @param timeout
     * @return
     */
    public static WebElement waitForClickablility(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.get(), timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * waits for backgrounds processes on the browser to complete
     *
     * @param timeOutInSeconds
     */
    public static void waitForPageToLoad(long timeOutInSeconds) {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        try {
            WebDriverWait wait = new WebDriverWait(Driver.get(), timeOutInSeconds);
            wait.until(expectation);
        } catch (Throwable error) {
            error.printStackTrace();
        }
    }

    /**
     * Verifies whether the element matching the provided locator is displayed on page
     *
     * @param by
     * @throws AssertionError if the element matching the provided locator is not found or not displayed
     */
    public static void verifyElementDisplayed(By by) {
        try {
            Assert.assertTrue(Driver.get().findElement(by).isDisplayed(), "Element not visible: " + by);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Assert.fail("Element not found: " + by);

        }
    }

    /**
     * Verifies whether the element matching the provided locator is NOT displayed on page
     *
     * @param by
     * @throws AssertionError the element matching the provided locator is displayed
     */
    public static void verifyElementNotDisplayed(By by) {
        try {
            Assert.assertFalse(Driver.get().findElement(by).isDisplayed(), "Element should not be visible: " + by);
        } catch (NoSuchElementException e) {
            e.printStackTrace();

        }
    }


    /**
     * Verifies whether the element is displayed on page
     *
     * @param element
     * @throws AssertionError if the element is not found or not displayed
     */
    public static void verifyElementDisplayed(WebElement element) {
        try {
            Assert.assertTrue(element.isDisplayed(), "Element not visible: " + element);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Assert.fail("Element not found: " + element);

        }
    }


    /**
     * Waits for element to be not stale
     *
     * @param element
     */
    public static void waitForStaleElement(WebElement element) {
        int y = 0;
        while (y <= 15) {
            if (y == 1)
                try {
                    element.isDisplayed();
                    break;
                } catch (StaleElementReferenceException st) {
                    y++;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (WebDriverException we) {
                    y++;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
    }


    /**
     * Clicks on an element using JavaScript
     *
     * @param element
     */
    public static void clickWithJS(WebElement element) {
        ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].click();", element);
    }


    /**
     * Scrolls down to an element using JavaScript
     *
     * @param element
     */
    public static void scrollToElement(WebElement element) {
        ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].scrollIntoView(true);", element);
    }


    /**
     * Performs double click action on an element
     *
     * @param element
     */
    public static void doubleClick(WebElement element) {
        new Actions(Driver.get()).doubleClick(element).build().perform();
    }

    /**
     * Changes the HTML attribute of a Web Element to the given value using JavaScript
     *
     * @param element
     * @param attributeName
     * @param attributeValue
     */
    public static void setAttribute(WebElement element, String attributeName, String attributeValue) {
        ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, attributeName, attributeValue);
    }

    /**
     * Highlighs an element by changing its background and border color
     *
     * @param element
     */
    public static void highlight(WebElement element) {
        ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
        waitFor(1);
        ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].removeAttribute('style', 'background: yellow; border: 2px solid red;');", element);
    }

    /**
     * Checks or unchecks given checkbox
     *
     * @param element
     * @param check
     */
    public static void selectCheckBox(WebElement element, boolean check) {
        if (check) {
            if (!element.isSelected()) {
                element.click();
            }
        } else {
            if (element.isSelected()) {
                element.click();
            }
        }
    }

    /**
     * attempts to click on provided element until given time runs out
     *
     * @param element
     * @param timeout
     */
    public static void clickWithTimeOut(WebElement element, int timeout) {
        for (int i = 0; i < timeout; i++) {
            try {
                element.click();
                return;
            } catch (WebDriverException e) {
                waitFor(1);
            }
        }
    }

    /**
     * executes the given JavaScript command on given web element
     *
     * @param element
     */
    public static void executeJScommand(WebElement element, String command) {
        JavascriptExecutor jse = (JavascriptExecutor) Driver.get();
        jse.executeScript(command, element);

    }

    /**
     * executes the given JavaScript command on given web element
     *
     * @param command
     */
    public static void executeJScommand(String command) {
        JavascriptExecutor jse = (JavascriptExecutor) Driver.get();
        jse.executeScript(command);

    }


    /**
     * This method will recover in case of exception after unsuccessful the click,
     * and will try to click on element again.
     *
     * @param by
     * @param attempts
     */
    public static void clickWithWait(By by, int attempts) {
        int counter = 0;
        //click on element as many as you specified in attempts parameter
        while (counter < attempts) {
            try {
                //selenium must look for element again
                clickWithJS(Driver.get().findElement(by));
                //if click is successful - then break
                break;
            } catch (WebDriverException e) {
                //if click failed
                //print exception
                //print attempt
                e.printStackTrace();
                ++counter;
                //wait for 1 second, and try to click again
                waitFor(1);
            }
        }
    }

    /**
     * checks that an element is present on the DOM of a page. This does not
     * * necessarily mean that the element is visible.
     *
     * @param by
     * @param time
     */
    public static void waitForPresenceOfElement(By by, long time) {
        new WebDriverWait(Driver.get(), time).until(ExpectedConditions.presenceOfElementLocated(by));
    }


    //*************************************************
    public static void clickElementWaitUntilULwithLi(WebElement byInput, WebElement byUl, List<WebElement> byLi, String itemName) {

        JavascriptExecutor executor = (JavascriptExecutor) Driver.get();
        clickWithTimeOut(byInput, 3);
        waitForVisibility(byUl, 5);

        List<WebElement> webElementsLi = byLi;
        for (int i = 0; i < webElementsLi.size(); i++) {
            if (webElementsLi.get(i).getText().contains(itemName)) {
                executor.executeScript("arguments[0].click();", webElementsLi.get(i));
                break;
            } else {
            }
        }
    }


    public static WebDriver driver = Driver.get();
    protected static WebDriverWait wait = new WebDriverWait(Driver.get(), GetData.DEFAULT_WAIT);
    protected static WebDriverWait waitZero = new WebDriverWait(Driver.get(), 0);
    protected static WebDriverWait waitLoader = new WebDriverWait(Driver.get(), GetData.DEFAULT_WAIT_LOADERBOX);
    public static long waitLoaderr = GetData.DEFAULT_WAIT_LOADERBOXX;


    /**
     * navigate to url
     */
    // Gerek yok
    public static void navigateTo(GetData.Url url) {

        try {
            Driver.get().get(DataFinder.getUrl(url));
            Driver.get().manage()
                    .timeouts()
                    .pageLoadTimeout(waitLoaderr, TimeUnit.SECONDS);
            extentLogger.pass("Web application launched");
        } catch (Exception e) {
            extentLogger.error("Error while getting app url : " + e);
            extentLogger.error("Error while getting app url : " + e);

            throw new RuntimeException(e);
        }
    }


    /**
     * Use this method to get current url
     *
     * @return
     */

    // Gerek yok
    public static String getUrl() {
        String url = null;

        try {
            url = driver.getCurrentUrl();
            extentLogger.info("Url : " + url);
            extentLogger.pass("Url bilgisi başarıyla alındı...");
        } catch (Exception e) {
            extentLogger.fail("Url bilgisi alınamadı!..." + e);
        }

        return url;
    }


    /**
     *
     * Use this method to scroll to an element.
     *
     * @param element
     */

    //  Var
  /*  public void scrollToElement(WebElement element) {

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView();", element);
            extentLogger.pass("Objeye başarıyla scroll edildi : " +element);
        } catch (Exception e){
            extentLogger.error("Error while scrolling to the element : " +e);
            extentLogger.fail("Error while scrolling to the element : " +e);
            throw new RuntimeException(e);
        }

    }*/

    /**
     * Use this method to find element by cssSelector
     *
     * @param by
     * @param index
     * @return A WebElement, or an empty if nothing matches @
     */
    public static WebElement findElement(By by, int... index) {
        // driver.manage().timeouts().implicitlyWait(GetData.DEFAULT_WAIT,

        // TimeUnit.SECONDS);

        WebElement element = null;
        untilElementAppear(by);
        try {
            if (index.length == 0)
                element = driver.findElement(by);
            else
                element = driver.findElements(by)
                        .get(index[0]);

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);arguments[0].focus();", element);
            // ((JavascriptExecutor)
            // driver).executeScript("arguments[0].focus();", element);
            // wait.until(ExpectedConditions.visibilityOf(element));
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            extentLogger.error("Error while clicking webelement : " + e);
            extentLogger.fail("Error while clicking webelement : " + e);

            throw new RuntimeException(e);
        }
        return element;
    }

    // var
    public static WebElement waitForElementClickable(WebElement element) {

        return new WebDriverWait(driver, GetData.DEFAULT_WAIT).until(ExpectedConditions.elementToBeClickable(element));
    }

    // var
    public static void waitForElement(By by, int... index) {

        waitLoaderBox();
        findElement(by, index);
    }

    public static WebElement waitForElement(WebElement element) {

        return new WebDriverWait(driver, GetData.DEFAULT_WAIT).until(ExpectedConditions.visibilityOf(element));
    }

    // var
    public static WebElement waitForElement(WebElement element, int seconds) {

        return new WebDriverWait(driver, seconds).until(ExpectedConditions.visibilityOf(element));
    }

    // yok
    public static void waitLoaderBox() {

        waitLoaderBox(GetData.DEFAULT_WAIT_LOADERBOX);// 90
    }

    //yok
    public static void waitLoaderBox(int time) {

        driver.manage()
                .timeouts()
                .implicitlyWait(0, TimeUnit.SECONDS);
        if (driver.findElements(By.xpath("//div[starts-with(@class,'loader')]"))
                .size() != 0) {
            driver.manage()
                    .timeouts()
                    .implicitlyWait(time, TimeUnit.SECONDS);
            driver.findElement(By.xpath("//div[@class='loader' and @style='display: none;']"));
            driver.findElement(By.xpath("//div[@class='loader-box' and @style='display: none;']"));
        }
        driver.manage()
                .timeouts()
                .implicitlyWait(GetData.DEFAULT_WAIT, TimeUnit.SECONDS);
    }

    //yok
    public static boolean isClickable(WebElement element) {

        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    /*
     * Use this method click to element
     *
     * @param by
     * @param index @
     */
    public static void click(By by, int... index) {

        WebElement element;
        try {
            element = findElement(by, index);
            String elemText = element.getText();
            element.click();
            extentLogger.info("Click Button : " + elemText);
        } catch (Exception e) {
            extentLogger.error("Error while clicking webelement : " + e);
            extentLogger.fail("Error while clicking webelement : " + e);

            throw new RuntimeException(e);
        }
    }

    /*
     * Use this method click to element
     *
     * @param by
     * @param index @
     */
    public static void click(WebElement element) {

        String elemText = "";
        try {
            elemText = element.getText();
            element.click();
            extentLogger.info("Click Button : " + elemText);

        } catch (WebDriverException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});arguments[0].focus();", element);
            element.click();
            extentLogger.info("Click Button : " + elemText);
        } catch (Exception e) {
            extentLogger.error("Error while clicking webelement : " + e);
            extentLogger.fail("Error while clicking webelement : " + e);

            throw new RuntimeException(e);
        }
    }


    /*
     * Use this method click to element
     *
     * @param by
     * @param index @
     */
    public static void click(By by, boolean clickable) {

        try {
            if (!clickable)
                click(by);
            else {
                wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                WebElement elem = wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
                String elemText = elem.getText();
                elem.click();
                extentLogger.info("Click Button : " + elemText);
            }
        } catch (Exception e) {
            extentLogger.error("Error while clicking webelement : " + e);
            extentLogger.fail("Error while clicking webelement : " + e);

            throw new RuntimeException(e);
        }
    }

    /*
     * Use this method to click to an element that is actually clickable/interactable.
     *
     *
     * @param element
     */

    //var
    public static void waitAndClickElement(By element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (Exception e) {
            extentLogger.error("Error while clicking web element: " + e);
            extentLogger.fail("Error while clicking web element: " + e);

            throw new RuntimeException(e);
        }
    }

    //var
    public static void waitAndClickElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (Exception e) {
            extentLogger.error("Error while clicking web element: " + e);
            extentLogger.fail("Error while clicking web element: " + e);

            throw new RuntimeException(e);
        }
    }

    //public WebElement isElementClickable(WebElement element) {

    //	return new WebDriverWait(driver, Duration.ofSeconds(GetData.DEFAULT_WAIT)).until(ExpectedConditions.elementToBeClickable(element));
    //}

    /*
     * Use this method to simulate typing into an element if it is enable. Send enter if pressEnter is
     * true, do nothing otherwise. Note : Before sending operation, element is cleared.
     *
     * @param by
     * @param text
     * @param pressEnter @
     */

    //gerek yok
    public static void sendKeys(By by, String text, boolean pressEnter, int... index) {

        WebElement element = null;
        String elemText = null;
        try {
            element = findElement(by, index);
            if (element.isEnabled()) {
                elemText = element.getText();
                element.clear();
                element.sendKeys(text);
                if (pressEnter) {
                    waitLoaderBox();
                    element.sendKeys(Keys.ENTER);
                }
            }
            extentLogger.info("Value : " + text + " - SendKeys : " + elemText);
        } catch (Exception e) {
            extentLogger.error("Error while filling field : " + e);
            extentLogger.fail("Error while filling field : " + e);

            throw new RuntimeException(e);
        }
    }

    /*
     * Use this method to simulate typing into an element if it is enable. Send enter if pressEnter is
     * true, do nothing otherwise. Note : Before sending operation, element is cleared.
     *
     * @param by
     * @param text
     * @param pressEnter @
     */

    //gerek yok
    public static void sendKeys(WebElement element, String text, boolean pressEnter) {

        String elemText = null;
        try {
            if (element.isEnabled()) {
                elemText = element.getText();
                //				element.clear();
                element.sendKeys(text);
                if (pressEnter) {
                    waitLoaderBox();
                    element.sendKeys(Keys.ENTER);
                }
            }
            extentLogger.info("Value : " + text + " - SendKeys : " + elemText);
        } catch (Exception e) {
            extentLogger.error("Error while filling field : " + e);
            extentLogger.fail("Error while filling field : " + e);

            throw new RuntimeException(e);
        }
    }

    /*
     * Use this method to simulate typing into an element if it is enable. Send enter if pressEnter is
     * true, do nothing otherwise. Note : Before sending operation, element is cleared.
     *
     * @param by
     * @param text
     * @param pressEnter @
     */

    //gerek yok
    public static void sendKeys(By by, Keys key, int... index) {

        WebElement element = null;
        String elemText = null;
        try {
            element = findElement(by, index);
            if (element.isEnabled()) {
                elemText = element.getText();
                element.sendKeys(key);
            }
            extentLogger.info("Value : " + key.toString() + " - SendKeys : " + elemText);
        } catch (Exception e) {
            extentLogger.error("Error while filling field : " + e);
            extentLogger.fail("Error while filling field : " + e);

            throw new RuntimeException(e);
        }
    }


    /*
     * Use this method to simulate typing into an element if it is enable. Note : Before sending
     * operation, element is cleared.
     *
     * @param by
     * @param text @
     */

    // gerek yok
    public static void sendKeys(By by, String text, int... index) {

        sendKeys(by, text, false, index);
    }

    /*
     * Use this method to simulate typing into an element if it is enable. Note : Before sending
     * operation, element is cleared.
     *
     * @param by
     * @param text @
     */

    //gerek yok
    public static void sendKeys(WebElement element, String text) {

        sendKeys(element, text, false);
    }

    //var
    /*public void clickElementWaitUntilULwithLi(WebElement byInput,WebElement byUl,List<WebElement> byLi,String itemName){

        JavascriptExecutor executor = (JavascriptExecutor)driver;
        waitAndClickElement(byInput);
        waitForElement(byUl);

        List<WebElement> webElementsLi = byLi;
        for(int i=0;i<webElementsLi.size();i++){
            if (webElementsLi.get(i).getText().contains(itemName)){
                executor.executeScript("arguments[0].click();",webElementsLi.get(i));
                break;
            }
            else{
            }
        }
    }*/


    //yok
    public static void selectCombobox(By by, String value) {

        WebElement element = findElement(by);
        String elemText = null;
        try {
            if (element.isEnabled()) {
                elemText = element.getText();
                Select selectBox = new Select(driver.findElement(by));
                selectBox.selectByValue(value);
            }
            extentLogger.info("Value : " + value + " - SelectComboBox : " + elemText);
        } catch (Exception e) {
            extentLogger.error("Error while filling field : " + e);
            extentLogger.fail("Error while filling field : " + e);

            throw new RuntimeException(e);
        }
    }

    //yok
    public static void selectCombobox(WebElement element, String value) {

        String elemText = null;
        try {
//			if (element.isEnabled()) {
            elemText = element.getText();
            Select selectBox = new Select(element);
            selectBox.selectByValue(value);
            //	}
            extentLogger.info("Value : " + value + " - SelectComboBox : " + elemText);
        } catch (Exception e) {
            extentLogger.error("Error while filling field : " + e);
            extentLogger.fail("Error while filling field : " + e);

            throw new RuntimeException(e);
        }
    }

    //var
    public static void moveToElement(By by) {

        try {
            Actions action = new Actions(driver);
            WebElement we = driver.findElement(by);
            action.moveToElement(we)
                    .build()
                    .perform();
        } catch (Exception e) {
            extentLogger.error("Error while filling field : " + e);
            extentLogger.fail("Error while filling field : " + e);
            throw new RuntimeException(e);
        }
    }

    /*
     * Get the visible (i.e. not hidden by CSS) innerText of this element.
     *
     * @param by
     * @param index
     * @return The innerText of this element. @
     */

    //gerek yok
    public static String getTextOfElement(By by, int... index) {

        String text = null;
        untilElementAppear(by);

        try {
            if (index.length == 0)

                text = driver.findElement(by)
                        .getText();
            else
                text = driver.findElements(by)
                        .get(index[0])
                        .getText();
        } catch (Exception e) {
            extentLogger.error("Error while getting text of element : " + e);
            extentLogger.fail("Error while getting text of element : " + e);

            throw new RuntimeException(e);
        }
        return text;
    }

    //gerek yok
    @SuppressWarnings("finally")
    public static String getTextOfElement(WebElement elem) {

        String text = null;
        try {
            text = elem.getText();
        } finally {
            return text;
        }
    }

    /*
     * Wait until element appears
     *
     * @param by
     * @param index
     */

    //var
    protected static void untilElementAppear(By by) {

        try {
            // waitLoaderBox(90);// , 40
            // Thread.sleep(1000);
            // driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
            // wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Exception e) {
            extentLogger.error("Error while waiting until element appears : " + e);
            extentLogger.fail("Error while waiting until element appears : " + e);
            throw new RuntimeException(e);
        }
    }

    //yok
    public static List presenceOfAllElements(By element) {

        List<WebElement> elements;

        elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));

        return elements;
    }


    /**
     * Wait until element disappears
     *
     * @param by //
     */

    //var
    protected static void untilElementDisappear(By by) {

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
        } catch (Exception e) {
            extentLogger.error("Error while waiting until element disappears : " + e);
            extentLogger.fail("Error while waiting until element disappears : " + e);
            throw new RuntimeException(e);
        }
    }

    /*
     * Return true if element exist, false otherwise.
     *
     * @param by
     * @param index
     * @return True if element exists, false otherwise.
     */
    public static boolean isElementExist(List<WebElement> elem) {

        return isElementExist(elem, 15);
    }

    public static boolean isElementExist(List<WebElement> elem, int timeSeconds) {


        driver.manage()
                .timeouts()
                .implicitlyWait(timeSeconds, TimeUnit.SECONDS);
        boolean isExist = !elem.isEmpty();
        driver.manage()
                .timeouts()
                .implicitlyWait(GetData.DEFAULT_WAIT, TimeUnit.SECONDS);

        return isExist;
    }

    public static boolean isElementExist(By by) {

        return isElementExist(by, 15);
    }

    public static boolean isElementExist(By by, int timeSeconds) {

        driver.manage()
                .timeouts()
                .implicitlyWait(timeSeconds, TimeUnit.SECONDS);
        boolean isExist = driver.findElements(by)
                .size() > 0;
        driver.manage()
                .timeouts()
                .implicitlyWait(GetData.DEFAULT_WAIT, TimeUnit.SECONDS);

        return isExist;
    }


    public static String getProperty(By by, String expectedPropertyName, int... index) {

        WebElement elem;

        if (index.length == 0)
            elem = driver.findElement(by);
        else
            elem = driver.findElements(by)
                    .get(index[0]);

        return elem.getAttribute(expectedPropertyName);
    }

    public static String getProperty(WebElement elem, String expectedPropertyName) {

        return elem.getAttribute(expectedPropertyName);
    }

    public static void setValue(WebElement element, String value) {

        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '" + value + "';", element);
    }


    public static String selectmounth(int num) {

        num -= 1;
        num = (num % 12 + 12) % 12;
        String[] month = {"Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Temmuz", "Ağustos", "Eylül", "Ekim",
                "Kasım", "Aralık"};

        return month[num];
    }

    //Mevcut günden belirtilen gün kadar sonrasını DatePickerdan seçer. Tek gidiş rotalı caseler için kullanılır.
    public static void selectDate(String selectAfterDay) {

        try {
            int afterDay = Integer.parseInt(selectAfterDay);

            List<WebElement> days = driver.findElements(By.xpath("//*[contains(@class,'CalendarDay CalendarDay_1 CalendarDay__default CalendarDay__default_2')]"));
            WebElement firstDay = days.get(afterDay);
            click(firstDay);
        } catch (Exception e) {
            extentLogger.error("Tarih Seçimi Yapılamadı!  : " + e);
            throw new RuntimeException(e);
        }

    }


    // ay parametresi Aralık 2021, Kasım 2022 olmalı
    // gün parametresi seçilmek istenen gün bilgisi olmalı
    public static void selectDateByMonth(String ay, String gun) {

        try {
            for (int i = 0; i <= 30; ) {
                if (isElementExist(By.xpath("//div[@data-visible='true']//div//strong[text()='" + ay + "']"), 20)) {
                    click(By.xpath("//*[contains(@aria-label,'" + ay + "')]//div[@class='CalendarDay__content' and text()='" + gun + "']"));
                    break;
                } else
                    click(By.xpath("//div[@aria-label='Move forward to switch to the next month.']"));
            }
        } catch (Exception e) {
            extentLogger.error("Yanlis tarih secimi yapildi... : " + e);
            throw new RuntimeException(e);
        }
    }

    public static List<WebElement> findElements(By by) {

        List<WebElement> webElements = null;
        untilElementAppear(by);
        try {
            webElements = driver.findElements(by);
        } catch (Exception e) {
            extentLogger.error("Error while listing webelements by css selector : " + e);
            extentLogger.fail("Error while listing webelements by css selector : " + e);
            extentLogger.info("Error while listing webelements by css selector : " + e);

            throw new RuntimeException(e);
        }
        return webElements;
    }


    public static void selectDateByMonthMobile(String ay, String gun) {

        try {
            for (int i = 0; i <= 30; ) {
                if (isElementExist(By.xpath("(//div[@data-visible='true']//div//strong[text()='" + ay + "'])[1]"), 20)) {
                    click(By.xpath("(//*[contains(@aria-label,'" + ay + "')]//div[@class='CalendarDay__content' and text()='" + gun + "'])[1]"));
                    break;
                }
            }
        } catch (Exception e) {
            extentLogger.error("Yanlis tarih secimi yapildi... : " + e);
            throw new RuntimeException(e);
        }
    }

    //frameler arası geçişi sağlar.
    public static void switchToFrame(By by) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        WebElement iframe = wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
        driver.switchTo().frame(iframe);

    }

    public static void control(WebElement elem, String onTrue, String onFalse) {

        try {
            if (getTextOfElement(elem).contains(onTrue)) {
                extentLogger.info(onTrue);
            } else {
                extentLogger.error(onFalse);
                Assert.assertTrue(false);
            }
        } catch (Exception e) {
            extentLogger.error(onFalse);
            Assert.assertTrue(false);
        }
    }

}
