package base;

import com.aventstack.extentreports.Status;
import helper.ScreenShotHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import helper.ReportManager;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseTest {
    protected WebDriver webDriver;
    private String url = "https://opensource-demo.orangehrmlive.com/";
    protected String adminUsername = "Admin";
    protected String adminPassword = "admin123";

    @BeforeSuite
    public static void setUpSuite() throws Exception {
        ReportManager.init("target/reports", "OrangeHRM");
    }

    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser, ITestResult iTestResult){
        // En el reporte se muestra la description del @Test; el navegador distingue la corrida de Chrome y la de Firefox
        String nombre = iTestResult.getMethod().getDescription();
        if (nombre == null || nombre.isEmpty()) {
            nombre = iTestResult.getMethod().getMethodName();
        }
        // Con DataProvider el mismo test corre una vez por dato: el dato (su toString) distingue cada corrida
        if (iTestResult.getParameters().length > 0) {
            nombre = nombre + " - " + iTestResult.getParameters()[0];
        }
        ReportManager.getInstance().startTest(nombre + " (" + browser + ")").assignCategory(browser);

        switch (browser){
            case "chrome":
                webDriver = new ChromeDriver(chromeSinGestorDeContrasenas());
                break;
            case "firefox":
                webDriver = new FirefoxDriver();
                break;
            default:
                throw new IllegalArgumentException(browser + " no soportado");
        }
        webDriver.manage().window().maximize();
        webDriver.get(url);
    }

    @AfterMethod
    public void tearDown(ITestResult iTestResult){
        try {
            switch (iTestResult.getStatus()){
                case ITestResult.FAILURE:
                    ReportManager.getInstance().getTest().log(Status.FAIL, "Test failed");
                    break;
                case ITestResult.SKIP:
                    ReportManager.getInstance().getTest().log(Status.SKIP, "Test skipped");
                    break;
                case ITestResult.SUCCESS:
                    ReportManager.getInstance().getTest().log(Status.PASS, "Test passed");
                    break;
                default:
                    ReportManager.getInstance().getTest().log(Status.FAIL, "Test incomplete");
            }

            if(iTestResult.getStatus() != ITestResult.SUCCESS && iTestResult.getThrowable() != null){
                ReportManager.getInstance().getTest().log(Status.FAIL, iTestResult.getThrowable().getMessage());
                ScreenShotHelper.takeScreenShotAndAdToHTMLReport(webDriver, Status.FAIL, "Failure Image");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(webDriver != null)
                webDriver.quit();
        }
    }

    @AfterSuite
    public static void tearDownSuite(){
        ReportManager.getInstance().flush();
    }

    private ChromeOptions chromeSinGestorDeContrasenas(){
        Map<String, Object> preferencias = new HashMap<String, Object>();
        preferencias.put("credentials_enable_service", false);
        preferencias.put("profile.password_manager_enabled", false);
        preferencias.put("profile.password_manager_leak_detection", false);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", preferencias);
        options.addArguments("--disable-features=PasswordLeakDetection,AutofillServerCommunication");
        return options;
    }
}
