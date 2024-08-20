package model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class GoogleResultsPage {

    private WebDriver driver;

    // Locators for elements on the search results page
    private By resultStatsLocator = By.id("result-stats");
    private By searchResultsLocator = By.cssSelector("h3");
    private By nextPageButtonLocator = By.id("pnnext");

    // Constructor
    public GoogleResultsPage(WebDriver driver) {
        this.driver = driver;
    }

    // Method to get the result stats element
    public WebElement getResultStats() {
        return driver.findElement(resultStatsLocator);
    }

    // Method to get a list of search result titles
    public List<WebElement> getSearchResults() {
        return driver.findElements(searchResultsLocator);
    }

    // Method to click the "Next" button to go to the next page of results
    public void clickNextPageButton() {
        WebElement nextPageButton = driver.findElement(nextPageButtonLocator);
        if (nextPageButton.isDisplayed() && nextPageButton.isEnabled()) {
            nextPageButton.click();
        }
    }

    // Method to get the number of search results displayed
    public int getSearchResultCount() {
        return getSearchResults().size();
    }

    // Method to verify if the next page button is present
    public boolean isNextPageButtonPresent() {
        return driver.findElement(nextPageButtonLocator).isDisplayed();
    }
}

