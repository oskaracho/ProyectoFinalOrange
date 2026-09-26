package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private By userInput = By.name("username");
    private By passWordInput = By.name("password");
    private By loginButton = By.cssSelector("button[type='submit']");

    public LoginPage(WebDriver webDriver){
        super(webDriver);
    }

    public void typeUserName(String user){
        type(userInput, user);
    }

    public void typePassWord(String passWord){
        type(passWordInput, passWord);
    }

    public DashboardPage clickOnLoginButton(){
        click(loginButton);
        return new DashboardPage(webDriver);
    }

    public DashboardPage loginAs(String user, String passWord){
        typeUserName(user);
        typePassWord(passWord);
        return clickOnLoginButton();
    }

    public boolean isLoginPageDisplayed(){
        return isVisible(loginButton);
    }
}
