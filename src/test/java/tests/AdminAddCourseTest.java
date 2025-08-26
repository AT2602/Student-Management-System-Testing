package tests;

import com.mongodb.client.*;
import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.ExcelUtils;
import utils.MongoDBUtils;

import java.io.*;
import java.time.Duration;

import static com.mongodb.client.model.Filters.eq;

public class AdminAddCourseTest extends BaseTest {

    @Test
    public void runAdminAddCourseTest() {
        String excelPath = "C:\\Users\\Dell\\eclipse-workspace\\StudentManagementTest\\TestResults.xlsx";
        FileInputStream fileInput = null;
        Workbook workbook = null;
        FileOutputStream fileOutput = null;

        try {
            // 1. Open Excel workbook
            fileInput = new FileInputStream(new File(excelPath));
            workbook = new XSSFWorkbook(fileInput);
            Sheet sheet = workbook.getSheetAt(12);

            // 2. Navigate to login page
            driver.get("http://127.0.0.1:3000/login");

            // 3. Perform login
            WebElement emailInput = driver.findElement(By.id("email"));
            WebElement passwordInput = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.id("loginButton"));

            emailInput.clear();
            emailInput.sendKeys("test@example.com");
            passwordInput.clear();
            passwordInput.sendKeys("Test1234");
            loginButton.click();

            // 4. Navigate to Add Student page
            driver.findElement(By.id("coursesManageLink")).click();
            driver.findElement(By.id("addCourseLink")).click();

            // 5. Iterate through each test case row in Excel
            DataFormatter formatter = new DataFormatter();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    System.out.println("Row " + i + " is NULL. Exiting loop.");
                    break;
                }

                // 5a. Read test data
                String TestCaseId = formatter.formatCellValue(row.getCell(0));
                String courseCode = formatter.formatCellValue(row.getCell(1));
                String CourseName      = formatter.formatCellValue(row.getCell(2));
                String description       = formatter.formatCellValue(row.getCell(3));
                String credits    = formatter.formatCellValue(row.getCell(4));
                String expected  = formatter.formatCellValue(row.getCell(5));

                // 5b. Fill form fields
                WebElement courseCodeInput = driver.findElement(By.id("courseCode"));
                WebElement CourseNameInput      = driver.findElement(By.id("courseName"));
                WebElement descriptionInput       = driver.findElement(By.id("description"));
                WebElement creditsInput    = driver.findElement(By.id("credits"));
                WebElement addButton      = driver.findElement(By.id("addCourseButton"));

                courseCodeInput.clear();
                courseCodeInput.sendKeys(courseCode);
                CourseNameInput.clear();
                CourseNameInput.sendKeys(CourseName);
                descriptionInput.clear();
                descriptionInput.sendKeys(description);
                creditsInput.clear();
                creditsInput.sendKeys(credits);

                // 5c. Submit form
                addButton.click();

                // 5d. Wait for either success or validation failure
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                boolean successPage = false;
                try {
                    successPage = wait.until(d -> d.getCurrentUrl().contains("courses_main"));
                } catch (TimeoutException te) {
                    successPage = false; // stayed on the form or saw errors
                }

                String actual;
                if (successPage) {
                    // 5e. Check database for the new student
                    boolean dbHasCourse = MongoDBUtils.courseExists(courseCode);

                    if (dbHasCourse) {
                        actual = "Success";
                        // Return to Add Student page for next input
                        driver.findElement(By.id("addCourseLink")).click();
                    } else {
                        actual = "Failure";
                    }
                } else {
                    // Skip DB check, just consider it failure
                    actual = "Failure";

                    // Clear fields manually for next iteration
                    courseCodeInput.clear();
                    CourseNameInput.clear();
                    descriptionInput.clear();
                    creditsInput.clear();
                }

                // 5f. Write result back to Excel
                Cell resultCell = row.createCell(6, CellType.STRING);
                resultCell.setCellValue(actual);

                // 5g. Assert
                if ("Success".equalsIgnoreCase(expected)) {
                    Assert.assertEquals(actual, "Success",
                        TestCaseId + " - Expected Success but got " + actual);
                } else {
                    Assert.assertEquals(actual, "Failure",
                        TestCaseId + " - Expected Failure but got " + actual);
                }

                // 5h. Log outcome
                System.out.printf("Test Case: %s - Expected: %s - Actual: %s%n",
                                  TestCaseId, expected, actual);
            }

            // 6. Save Excel results
            fileInput.close();
            fileOutput = new FileOutputStream(new File(excelPath));
            workbook.write(fileOutput);
            fileOutput.close();

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception during test execution: " + e.getMessage());
        } finally {
            try {
                if (fileInput  != null) fileInput.close();
                if (fileOutput != null) fileOutput.close();
                if (workbook   != null) workbook.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
