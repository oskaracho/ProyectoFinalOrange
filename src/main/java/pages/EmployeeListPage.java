package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EmployeeListPage extends BasePage {
    private By header = By.xpath("//h5[normalize-space()='Employee Information']");
    private By addButton = By.xpath("//button[normalize-space()='Add']");
    private By employeeIdFilterInput = By.xpath("//label[text()='Employee Id']/../following-sibling::div/input");
    private By searchButton = By.xpath("//button[normalize-space()='Search']");
    private By firstRowCells = By.cssSelector("div.oxd-table-body div.oxd-table-card:first-child div.oxd-table-cell");

    public EmployeeListPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void searchByEmployeeId(String employeeId) {
        waitForVisibility(header);
        type(employeeIdFilterInput, employeeId);
        click(searchButton);
    }

    public boolean isEmployeeInResults(String employeeId) {
        return isVisible(resultCellWithId(employeeId));
    }

    public String getFirstResultLastName() {
        waitForVisibility(firstRowCells);
        return webDriver.findElements(firstRowCells).get(3).getText().trim();
    }

    public AddEmployeePage clickAdd() {
        waitForVisibility(header);
        click(addButton);
        return new AddEmployeePage(webDriver);
    }

    private By resultCellWithId(String employeeId) {
        return By.xpath("//div[@role='row']//div[@role='cell'][normalize-space()='" + employeeId + "']");
    }

    public boolean isEmployeeListDisplayed() {
        return isVisible(header);
    }
}
