package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {
    private By header = By.xpath("//h6[normalize-space()='Dashboard']");
    private By pimMenuItem = By.xpath("//aside//a[normalize-space()='PIM']");

    public DashboardPage(WebDriver webDriver){
        super(webDriver);
    }

    public EmployeeListPage goToPim(){
        waitForVisibility(header);
        logger.info("Navegando al modulo PIM");
        click(pimMenuItem);
        return new EmployeeListPage(webDriver);
    }

    public boolean isDashboardDisplayed(){
        return isVisible(header);
    }
}
