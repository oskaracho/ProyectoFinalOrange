# Proyecto final — Alta de empleado en OrangeHRM

Automatiza, en una sola prueba, el alta de un empleado en
https://opensource-demo.orangehrmlive.com/:

1. Iniciar sesión en la aplicación.
2. Ir al módulo PIM desde el menú.
3. Crear un empleado nuevo con sus datos personales y sus datos de usuario.

La prueba verifica cada paso: que después del login se ve el Dashboard, que
el menú PIM lleva al listado, y que al guardar se abre la ficha del empleado
con su nombre en el encabezado.

## Cómo se corre

Desde la carpeta del proyecto, sin IDE:

```
mvn test
```

Maven ejecuta `testEnd.xml`: el caso corre en **Chrome** y en **Firefox** en
paralelo, una vez por cada empleado del JSON (con 3 empleados, 6 ejecuciones en total).
Selenium Manager descarga solo los drivers, y también el navegador si no está instalado.

## Reporte

Al terminar queda un reporte HTML de ExtentReports en `target/reports/Index.html`,
con una entrada por empleado y navegador. El nombre de cada entrada sale del
`description` del `@Test`, más el empleado y el navegador: `Crear un empleado
nuevo con sus datos de usuario desde el modulo PIM - Laura Beatriz Gomez (chrome)`. Cada entrada tiene tres capturas: después del login, con el formulario de
alta completo (antes de guardar) y al final, en la ficha del empleado. Si una
prueba falla, el reporte incluye además el mensaje del error y una captura de
pantalla del momento de la falla.

## Estructura

| Carpeta | Contenido |
|---|---|
| `src/main/java/pages` | Page Objects. Cada página tiene sus locators arriba, sus acciones y un `isXDisplayed()` para las aserciones. |
| `src/main/java/helper` | `ReportManager`: crea el reporte y una entrada por prueba. `ScreenShotHelper`: toma la captura y la agrega al reporte. `JsonTestDataHelper`: lee un JSON y lo entrega al DataProvider. |
| `src/main/java/models` | `Employee`: cada objeto del JSON se convierte en uno. |
| `resources/testdata/employees/employeeData.json` | Datos de los empleados. Para sumar uno, se agrega un objeto a la lista. |
| `src/test/java/employees` | La prueba y su DataProvider: se lee como el caso de negocio, sin locators. Las aserciones están acá. |
| `src/test/java/base` | `BaseTest`: URL y credenciales del demo; abre el navegador que indica la suite, registra el resultado en el reporte y lo cierra. |
| `testEnd.xml` | Suite: un `<test>` por navegador. |

## Datos únicos en cada corrida

- **Del JSON:** nombre, segundo nombre, apellido, base del usuario, contraseña y estado.
- **Generado al ejecutar:** un código aleatorio de 8 caracteres. Se agrega al
  apellido (`Gomez 06a90f59`) y al usuario (`lgomez.06a90f59`), y se usa como ID de empleado.

El demo es compartido y no acepta IDs ni usuarios repetidos: sin el código,
la segunda corrida ya fallaría.


## Punto 4: búsqueda del empleado en el listado

Después de guardar, la prueba vuelve al listado de empleados y busca al empleado recién creado por su Employee Id.

| Archivo | Qué se agregó |
|---|---|
| `EmployeeDetailsPage` | Locator `pimMenuItem` y método `goToEmployeeList()`: hace clic en PIM y entrega el control a `EmployeeListPage`. |
| `EmployeeListPage` | Locators `employeeIdFilterInput` y `searchButton`, y método `searchByEmployeeId(employeeId)`: escribe el ID en el filtro y hace clic en Search. |
| `AddEmployeeTest` | Llama a `goToEmployeeList()` y a `searchByEmployeeId(employeeId)`. No contiene locators. |

Se busca por ID porque es único en cada corrida (lleva el código aleatorio), así que la grilla devuelve solo el empleado recién creado.

## Carga de foto del empleado (opcional)

La foto está fuera del alcance del enunciado y se agregó como extra.

| Archivo | Qué se agregó |
|---|---|
| `resources/testdata/employees/foto.jpg` | Imagen que se sube al crear el empleado. |
| `AddEmployeePage` | Locator `photoInput` y método `uploadPhoto(photoPath)`. |
| `AddEmployeeTest` | Llamada a `uploadPhoto(...)` después de ingresar el nombre. |

No se hace clic en el botón **+**, porque abriría el explorador de archivos del sistema, que Selenium no controla. Se envía la ruta de la imagen directo al campo de archivo oculto.

## Punto 5: verificación en la grilla de resultados

| Archivo | Qué se agregó |
|---|---|
| `EmployeeListPage` | Locator `resultCellWithId(employeeId)` y método `isEmployeeInResults(employeeId)`, que devuelve `true` o `false`. |
| `AddEmployeeTest` | `Assert.assertTrue(...)` con ese resultado y una captura de la grilla para el reporte. |

La aserción está en la prueba y no en la página, como pide el enunciado. Si la fila con el ID aparece, la prueba pasa. Si no aparece en 20 segundos, falla con un mensaje en el reporte.
