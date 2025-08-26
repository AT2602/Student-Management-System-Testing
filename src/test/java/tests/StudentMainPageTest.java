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

public class StudentMainPageTest extends BaseTest {
	
	@Test
	public void runStudentMainPageTest() {
		
        
		
        // Open Excel File
        String excelPath = "C:\\Users\\Dell\\eclipse-workspace\\StudentManagementTest\\TestResults.xlsx";
        
        driver.get("http://127.0.0.1:3000/login_norm");
        
        
        // Find Elements
        WebElement studentIDInput = driver.findElement(By.id("studentID"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement studentloginButton = driver.findElement(By.id("studentLoginButton"));
        
     // Perform Login
        studentIDInput.clear();
        studentIDInput.sendKeys("STU001");
        passwordInput.clear();
        passwordInput.sendKeys("1469");
        studentloginButton.click();
		
        
        
        try {
            FileInputStream file = new FileInputStream(new File(excelPath));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(4);            
            
         // Loop through test cases
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String action = row.getCell(1).getStringCellValue(); 
                String expected = row.getCell(2).getStringCellValue();
                String actual = "";
                
                // Click the appropriate button
                if (action.equalsIgnoreCase("studentCourses")) {
                    driver.findElement(By.id("coursesStudent")).click();
                    actual = driver.getCurrentUrl().contains("/course_norm") ? "Success" : "Failed";
                    WebElement backButtonCourse = driver.findElement(By.id("backLink"));
                    backButtonCourse.click();
                
                } 
               
                
                else if (action.equalsIgnoreCase("studentMarks")) {
                    driver.findElement(By.id("marksStudent")).click();
                    actual = driver.getCurrentUrl().contains("/marks_norm") ? "Success" : "Failed";
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