package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EmployeeDetailsPage extends BasePage {
    private By employeeNameHeader = By.cssSelector(".orangehrm-edit-employee-name h6");
    private By pimMenuItem = By.cssSelector("a[href='/web/index.php/pim/viewPimModule']");
    private By employeePhoto = By.cssSelector(".orangehrm-edit-employee-image img.employee-image");

    public EmployeeDetailsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public EmployeeListPage goToEmployeeList() {
        click(pimMenuItem);
        return new EmployeeListPage(webDriver);
    }

    public String getEmployeeName() {
        wait.until(driver -> !driver.findElement(employeeNameHeader).getText().isEmpty());
        String name = webDriver.findElement(employeeNameHeader).getText();
        logger.info("Nombre en la ficha: {}", name);
        return name;
    }

    public boolean isEmployeeDetailsDisplayed() {
        return isVisible(employeeNameHeader);
    }

    public boolean isEmployeePhotoLoaded() {
        try {
            waitForVisibility(employeePhoto);
            boolean loaded = wait.until(driver -> {
                WebElement img = driver.findElement(employeePhoto);
                return (Boolean) ((JavascriptExecutor) driver).executeScript(
                        "return arguments[0].complete && arguments[0].naturalWidth > 0;", img);
            });
            logger.info("Foto del empleado cargada: {}", loaded);
            return loaded;
        } catch (TimeoutException e) {
            logger.error("La foto del empleado no cargo", e);
            return false;
        }
    }
}