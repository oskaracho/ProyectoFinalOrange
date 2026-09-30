package employees;

import base.BaseTest;
import com.aventstack.extentreports.Status;
import helper.JsonTestDataHelper;
import helper.ScreenShotHelper;
import models.Employee;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.AddEmployeePage;
import pages.DashboardPage;
import pages.EmployeeDetailsPage;
import pages.EmployeeListPage;
import pages.LoginPage;
import java.util.UUID;

public class AddEmployeeTest extends BaseTest {

    @DataProvider(name = "employeeDataProvider")
    public Object[][] employeeData() {
        return JsonTestDataHelper.getTestData("resources/testdata/employees/employeeData.json", Employee[].class);
    }

    @Test(description = "Crear un empleado nuevo", dataProvider = "employeeDataProvider")
    public void testAdminCreatesEmployeeWithLoginDetails(Employee employee) {
        String codigo = UUID.randomUUID().toString().substring(0, 8);
        String firstName = employee.getFirstName();
        String middleName = employee.getMiddleName();
        String lastName = employee.getLastName() + " " + codigo;
        String employeeId = codigo;
        String username = employee.getUsernameBase() + "." + codigo;
        String password = employee.getPassword();
        String status = employee.getStatus();
        String imageEmployee = employee.getImageName();
////////

        LoginPage loginPage = new LoginPage(webDriver);
        DashboardPage dashboard = loginPage.loginAs(adminUsername, adminPassword);
        Assert.assertTrue(dashboard.isDashboardDisplayed(), "Despues del login deberia verse el Dashboard");
        ScreenShotHelper.takeScreenShotAndAdToHTMLReport(webDriver, Status.INFO, "Login exitoso");

        EmployeeListPage listado = dashboard.goToPim();
        Assert.assertTrue(listado.isEmployeeListDisplayed(), "Deberia llevar al listado de empleados");
////

        AddEmployeePage alta = listado.clickAdd();
        alta.enterFullName(firstName, middleName, lastName);
        alta.enterEmployeeId(employeeId);
        alta.enableLoginDetails();
        alta.enterLoginDetails(username, password, status);
        ScreenShotHelper.takeScreenShotAndAdToHTMLReport(webDriver, Status.INFO, "Datos del empleado y del usuario " + username + " cargados");
        alta.addImageEmployee(imageEmployee);
        EmployeeDetailsPage ficha = alta.save();
        //////
        Assert.assertTrue(ficha.isEmployeeDetailsDisplayed(), "Al guardar deberia abrirse la info del empleado");
        Assert.assertEquals(ficha.getEmployeeName(), firstName + " " + lastName);
        // NUEVO 1: validar que la foto cargo
        Assert.assertTrue(ficha.isEmployeePhotoLoaded(), "La foto del empleado deberia haberse cargado");
        ScreenShotHelper.takeScreenShotAndAdToHTMLReport(webDriver, Status.INFO, "Empleado " + firstName + " " + lastName + " guardado");

        EmployeeListPage listadoFinal = ficha.goToEmployeeList();
        listadoFinal.searchByEmployeeId(employeeId);
        Assert.assertTrue(listadoFinal.isEmployeeInResults(employeeId), "El empleado " + employeeId + " deberia aparecer en la grilla de resultados");
        Assert.assertEquals(listadoFinal.getFirstResultLastName(), lastName, "El apellido en la grilla no coincide con el cargado");
        ScreenShotHelper.takeScreenShotAndAdToHTMLReport(webDriver, Status.INFO, "Empleado " + employeeId + " encontrado en el listado");
    }

    // NUEVO 2: caso negativo con un archivo que no es imagen
    @Test(description = "Subir un archivo que no es imagen muestra error")
    public void testUploadInvalidFileShowsError() {
        AddEmployeePage alta = new LoginPage(webDriver)
                .loginAs(adminUsername, adminPassword)
                .goToPim()
                .clickAdd();
        alta.addImageEmployee("archivo-invalido.txt");
        Assert.assertEquals(alta.getImageError(), "File type not allowed");
    }
}