package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EmployeeDetailsPage extends BasePage {
    private By employeeNameHeader = By.cssSelector(".orangehrm-edit-employee-name h6");

    public EmployeeDetailsPage(WebDriver webDriver){
        super(webDriver);
    }

    public String getEmployeeName(){
        wait.until(driver -> !driver.findElement(employeeNameHeader).getText().isEmpty());
        return webDriver.findElement(employeeNameHeader).getText();
    }

    public boolean isEmployeeDetailsDisplayed(){
        return isVisible(employeeNameHeader);
    }
}
