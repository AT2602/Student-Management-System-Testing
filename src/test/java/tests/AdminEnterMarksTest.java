//Student Courses Test
package tests;

import org.openqa.selenium.*;
import utils.ExcelUtils;
import utils.MongoDBUtils;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.Arrays;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.List;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AdminEnterMarksTest extends BaseTest {
	
	@Test
	public void runAdminEnterMarksTest() {
		
        
		
        // Open Excel File
        String excelPath = "C:\\Users\\Dell\\eclipse-workspace\\StudentManagementTest\\TestResults.xlsx";
        
        driver.get("http://127.0.0.1:3000/login");
        
        
        // Find Elements
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginButton"));
        
     // Perform Login
        emailInput.clear();
        emailInput.sendKeys("test@example.com");
        passwordInput.clear();
        passwordInput.sendKeys("Test1234");
        loginButton.click();
		
        
        
        try {
            FileInputStream file = new FileInputStream(new File(excelPath));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(16);            
            
         // Loop through test cases
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String action = row.getCell(1).getStringCellValue(); 
                String expected = row.getCell(2).getStringCellValue();
                String actual = "";
                
                
                if (action.equalsIgnoreCase("TableCheck")) {
                    driver.findElement(By.id("marksManageLink")).click();
                    driver.findElement(By.id("enterMarksButton")).click();

                    // wait until the table is visible
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                    WebElement table = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table"))
                    );

                    // find all <tr> in the tbody
                    List<WebElement> rows = table.findElements(By.cssSelector("tbody tr"));
                    boolean hasRows = rows.size() > 0;
                    Assert.assertTrue(hasRows, "Expected at least one course row");

                    // assign to the existing variable, do NOT redeclare
                    actual = hasRows ? "Success" : "Failure";

                    // write into Excel
                    row.createCell(3).setCellValue(actual);
                   

                    // click back
                    driver.findElement(By.id("backLink")).click();
                    
                }                 
                
                
                /*
                else if (action.equalsIgnoreCase("studentMarks")) {
                    driver.findElement(By.id("marksStudent")).click();
                    actual = driver.getCurrentUrl().contains("/marks_norm") ? "Success" : "Failed";
                } 
                  */
                
                
                // Check Result
                Thread.sleep(500); // Wait for result                                
                              
             // Write Test Result to Excel
                Cell resultCell = row.createCell(3);
                resultCell.setCellValue(actual);
                
                // Print Console Output
                System.out.println("Test Case: " + row.getCell(0).getStringCellValue() + " - Expected: " + expected + " - Actual: " + actual);
               
            }

            // Save Excel Results
            file.close();
            FileOutputStream outFile = new FileOutputStream(new File(excelPath));
            workbook.write(outFile);
            outFile.close();

           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}