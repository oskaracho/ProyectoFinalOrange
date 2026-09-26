package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EmployeeListPage extends BasePage {
    private By header = By.xpath("//h5[normalize-space()='Employee Information']");
    private By addButton = By.xpath("//button[normalize-space()='Add']");

    public EmployeeListPage(WebDriver webDriver){
        super(webDriver);
    }

    public AddEmployeePage clickAdd(){
        waitForVisibility(header);
        click(addButton);
        return new AddEmployeePage(webDriver);
    }

    public boolean isEmployeeListDisplayed(){
        return isVisible(header);
    }
}
