package model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GoogleHomePage {

    private WebDriver driver;

    // Locators for the elements on the Google homepage
    private By searchBoxLocator = By.name("q");
    private By searchButtonLocator = By.name("btnK");
    private By feelingLuckyButtonLocator = By.name("btnI");

    // Constructor
    public GoogleHomePage(WebDriver driver) {
        this.driver = driver;
    }

    // Method to get the search box element
    public WebElement getSearchBox() {
        return driver.findElement(searchBoxLocator);
    }

    // Method to get the search button element
    public WebElement getSearchButton() {
        return driver.findElement(searchButtonLocator);
    }

    // Method to get the "I'm Feeling Lucky" button element
    public WebElement getFeelingLuckyButton() {
        return driver.findElement(feelingLuckyButtonLocator);
    }

    // Method to enter text into the search box
    public void enterSearchText(String text) {
        WebElement searchBox = getSearchBox();
        searchBox.clear();
        searchBox.sendKeys(text);
    }

    // Method to click the search button
    public void clickSearchButton() {
        getSearchButton().click();
    }

    // Method to click the "I'm Feeling Lucky" button
    public void clickFeelingLuckyButton() {
        getFeelingLuckyButton().click();
    }

    // Method to perform a search
    public void search(String text) {
        enterSearchText(text);
        clickSearchButton();
    }
}

