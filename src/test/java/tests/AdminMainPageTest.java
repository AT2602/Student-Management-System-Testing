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
import org.testng.annotations.Test;

public class AdminMainPageTest extends BaseTest {
	
	@Test
	public void runAdminMainPageTest() {
		
        
		
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
            Sheet sheet = workbook.getSheetAt(5);            
            
         // Loop through test cases
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String action = row.getCell(1).getStringCellValue(); 
                String expected = row.getCell(2).getStringCellValue();
                String actual = "";
                
                // Click the appropriate button
                if (action.equalsIgnoreCase("adminStudentManage")) {
                    driver.findElement(By.id("studentManageLink")).click();
                    actual = driver.getCurrentUrl().contains("/student_main") ? "Success" : "Failed";
                    WebElement backButtonCourse = driver.findElement(By.id("backLink"));
                    backButtonCourse.click();
                
                } 
               
                
                else if (action.equalsIgnoreCase("adminCoursesManage")) {
                    driver.findElement(By.id("coursesManageLink")).click();
                    actual = driver.getCurrentUrl().contains("/courses_main") ? "Success" : "Failed";
                    WebElement backButtonCourse = driver.findElement(By.id("backLink"));
                    backButtonCourse.click();
                } 
                
                else if (action.equalsIgnoreCase("adminMarksManage")) {
                    driver.findElement(By.id("marksManageLink")).click();
                    actual = driver.getCurrentUrl().contains("/marks") ? "Success" : "Failed";
                    WebElement backButtonCourse = driver.findElement(By.id("backLink"));
                    backButtonCourse.click();
                } 
                  
                
                
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