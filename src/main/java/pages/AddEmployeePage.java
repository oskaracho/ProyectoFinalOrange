package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URISyntaxException;
import java.nio.file.Paths;

public class AddEmployeePage extends BasePage {
    private By header = By.xpath("//h6[normalize-space()='Add Employee']");
    private By firstNameInput = By.name("firstName");
    private By middleNameInput = By.name("middleName");
    private By lastNameInput = By.name("lastName");
    private By employeeIdInput = By.xpath("//label[text()='Employee Id']/../following-sibling::div/input");
    private By loginDetailsSwitch = By.cssSelector(".oxd-switch-input");
    private By usernameInput = By.xpath("//label[text()='Username']/../following-sibling::div/input");
    private By passwordInput = By.xpath("//label[text()='Password']/../following-sibling::div/input");
    private By confirmPasswordInput = By.xpath("//label[text()='Confirm Password']/../following-sibling::div/input");
    private By saveButton = By.xpath("//button[normalize-space()='Save']");

    private By employeeImageUpload = By.cssSelector("input[type='file']");

    private By statusOption(String status){
        return By.xpath("//label[normalize-space()='" + status + "']");
    }

    public AddEmployeePage(WebDriver webDriver){
        super(webDriver);
    }

    public void enterFullName(String firstName, String middleName, String lastName){
        waitForVisibility(header);
        logger.info("Nombre completo: {} {} {}", firstName, middleName, lastName);
        type(firstNameInput, firstName);
        type(middleNameInput, middleName);
        type(lastNameInput, lastName);
    }

    public void enterEmployeeId(String employeeId){
        waitForVisibility(employeeIdInput);
        WebElement campo = webDriver.findElement(employeeIdInput);
        while (!campo.getDomProperty("value").isEmpty()) {
            campo.sendKeys(Keys.BACK_SPACE);
        }
        campo.sendKeys(employeeId);
    }

    public void enableLoginDetails(){
        clickAndWaitFor(loginDetailsSwitch, usernameInput);
    }

    public void enterLoginDetails(String username, String password, String status){
        logger.info("Credenciales del usuario {} con estado {}", username, status);
        type(usernameInput, username);
        click(statusOption(status));
        type(passwordInput, password);
        type(confirmPasswordInput, password);
    }

    public EmployeeDetailsPage save(){
        logger.info("Guardando empleado");
        click(saveButton);
        return new EmployeeDetailsPage(webDriver);
    }

    public boolean isAddEmployeePageDisplayed(){
        return isVisible(header);
    }

    public void addImageEmployee(String imageName )   {
        try {
            logger.info("Subiendo imagen {}", imageName);
            String pathFile = "test-data/%s".formatted(imageName);
            String imagePath = Paths.get(
                    getClass()
                            .getClassLoader()
                            .getResource(pathFile)
                            .toURI()
            ).toString();
            webDriver.findElement(employeeImageUpload).sendKeys(imagePath);
        } catch (URISyntaxException e) {
            logger.error("Ruta de imagen invalida", e);
        }
    }
}
