package tests;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.Duration;
import java.util.List;

public class AdminSearchCourseTest extends BaseTest {

    @Test
    public void runAdminSearchCourseTest() {
        String excelPath = "C:\\Users\\Dell\\eclipse-workspace\\StudentManagementTest\\TestResults.xlsx";

        // Navigate & Login
        driver.get("http://127.0.0.1:3000/login");
        driver.findElement(By.id("email")).sendKeys("test@example.com");
        driver.findElement(By.id("password")).sendKeys("Test1234");
        driver.findElement(By.id("loginButton")).click();

        try {
            // Open Excel File
            FileInputStream file = new FileInputStream(new File(excelPath));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(14);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            // Loop through test cases
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String action = row.getCell(1).getStringCellValue();
                String expected = row.getCell(2).getStringCellValue();
                String actual = "";

                if ("SearchCheckCode".equalsIgnoreCase(action)) {
                    // Test Case: Search by ID
                    driver.findElement(By.id("coursesManageLink")).click();

                    WebElement searchInput = wait.until(
                        ExpectedConditions.elementToBeClickable(
                            By.cssSelector("form.search-bar input[name='query']")
                        )
                    );
                    searchInput.clear();
                    searchInput.sendKeys("CS101");
                    searchInput.submit();

                    List<WebElement> rows = wait.until(d ->
                        d.findElements(By.cssSelector("table tbody tr"))
                    );
                    boolean found = rows.size() > 0;
                    actual = found ? "Success" : "Failure";
                    System.out.println("SearchCheckCode: " + actual);

                    driver.findElement(By.id("backLink")).click();

                } else if ("SearchCheckName".equalsIgnoreCase(action)) {
                    // Test Case: Search by Name
                    //driver.findElement(By.id("studentManageLink")).click();

                    WebElement searchInput = wait.until(
                        ExpectedConditions.elementToBeClickable(
                            By.cssSelector("form.search-bar input[name='query']")
                        )
                    );
                    searchInput.clear();
                    searchInput.sendKeys("Introduction to Computer Science");
                    searchInput.submit();

                    List<WebElement> rows = wait.until(d ->
                        d.findElements(By.cssSelector("table tbody tr"))
                    );
                    boolean found = rows.size() > 0;
                    actual = found ? "Success" : "Failure";
                    System.out.println("SearchCheckName: " + actual);

                    driver.findElement(By.id("backLink")).click();
                }

                // Write back result
                row.createCell(3).setCellValue(actual);
                System.out.println(
                    "Test Case: " + row.getCell(0).getStringCellValue() +
                    " - Expected: " + expected + " - Actual: " + actual
                );
                Thread.sleep(500);
            }

            // Save Excel Results
            file.close();
            FileOutputStream outFile = new FileOutputStream(new File(excelPath));
            workbook.write(outFile);
            outFile.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception in AdminSearchCourseTest: " + e.getMessage());
        }
    }
}
