package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    private By formLoader = By.cssSelector(".oxd-form-loader");
    private By spinner = By.cssSelector(".oxd-loading-spinner");
    protected WebDriver webDriver;
    protected WebDriverWait wait;
    protected final Logger logger = LogManager.getLogger(getClass());

    public BasePage(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
        this.wait.ignoring(StaleElementReferenceException.class);
    }

    protected void waitForVisibility(By elementBy) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(elementBy));
    }

    protected boolean isVisible(By elementBy) {
        try {
            waitForVisibility(elementBy);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected void clickAndWaitFor(By clickBy, By expectedBy) {
        click(clickBy);
        waitForVisibility(expectedBy);
    }

    protected void waitForLoadersToFinish() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
    }

    protected void type(By elementBy, String text) {
        waitForVisibility(elementBy);
        waitForLoadersToFinish();
        logger.info("Escribiendo en {}", elementBy);
        webDriver.findElement(elementBy).sendKeys(text);
    }

    protected void click(By elementBy) {
        waitForVisibility(elementBy);
        waitForLoadersToFinish();
        logger.info("Click en {}", elementBy);
        wait.until(ExpectedConditions.elementToBeClickable(elementBy)).click();
    }
}