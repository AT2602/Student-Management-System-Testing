package tests;

import com.mongodb.client.*;
import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.ExcelUtils;
import utils.MongoDBUtils;

import java.io.*;
import java.time.Duration;

public class AdminAssignCourseTest extends BaseTest {

    @Test
    public void runAdminAssignCourseTest() {
        String excelPath = "C:\\Users\\Dell\\eclipse-workspace\\StudentManagementTest\\TestResults.xlsx";
        FileInputStream fileInput = null;
        Workbook workbook = null;
        FileOutputStream fileOutput = null;

        try {
            // 1. Open the Excel workbook and select sheet #14 (0-based index 13)
            fileInput = new FileInputStream(new File(excelPath));
            workbook = new XSSFWorkbook(fileInput);
            Sheet sheet = workbook.getSheetAt(13);

            // 2. Navigate to the login page
            driver.get("http://127.0.0.1:3000/login");

            // 3. Perform login as admin
            WebElement emailInput = driver.findElement(By.id("email"));
            WebElement passwordInput = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.id("loginButton"));

            emailInput.clear();
            emailInput.sendKeys("test@example.com");
            passwordInput.clear();
            passwordInput.sendKeys("Test1234");
            loginButton.click();

            // 4. Go to the Assign Course form
            //    (Assuming there’s a link or button with id="assignCourseLink" on the courses management page)
            
            driver.findElement(By.id("coursesManageLink")).click();
            driver.findElement(By.id("assignCourseLink")).click();

            DataFormatter formatter = new DataFormatter();

            // 5. Iterate through each test row
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) break;

                String testCaseId = formatter.formatCellValue(row.getCell(0));   // e.g. "TC-01"
                String expected   = formatter.formatCellValue(row.getCell(2));   // "Success" or "Failure"

                // 5a. Locate the dropdowns and the assign button afresh for each iteration
                Select studentSelect = new Select(driver.findElement(By.id("studentID")));
                Select courseSelect  = new Select(driver.findElement(By.id("courseCode")));
                WebElement assignButton  = driver.findElement(By.id("assignCourseButton"));

                // 5b. Choose the first non-empty option or use data from Excel if desired
                studentSelect.selectByIndex(9);  // selects the second <option>, adjust as needed
                courseSelect.selectByIndex(5);

                // 5c. Click “Assign Course”
                assignButton.click();

                // 5d. Wait up to 5 seconds for URL to contain "courses_main"
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                boolean success = false;
                try {
                    success = wait.until(d -> d.getCurrentUrl().contains("courses_main"));
                } catch (TimeoutException te) {
                    success = false;
                }

                String actual = success ? "Success" : "Failure";

                // 5e. Write the actual result back to Excel 
                Cell resultCell = row.createCell(3);
                resultCell.setCellValue(actual);

                // 5f. Assert against expectation
                Assert.assertEquals(actual, expected,
                    testCaseId + " - expected [" + expected + "] but was [" + actual + "]");

                // 5g. Log the outcome
                System.out.printf("Test Case: %s | Expected: %s | Actual: %s%n",
                                  testCaseId, expected, actual);

                // 5h. Navigate back to the assign page for the next iteration

                
                driver.findElement(By.id("assignCourseLink")).click();

            }

            // 6. Save the Excel with updated results
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
