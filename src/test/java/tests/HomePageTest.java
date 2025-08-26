//home page
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

public class HomePageTest extends BaseTest {
	
	@Test
	public void runHomePageTest() {
		
        
        // Open Excel File
        String excelPath = "C:\\Users\\Dell\\eclipse-workspace\\StudentManagementTest\\TestResults.xlsx";
        
        driver.get("http://127.0.0.1:3000/");
        
        try {
            FileInputStream file = new FileInputStream(new File(excelPath));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);            
            
         // Loop through test cases
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String action = row.getCell(1).getStringCellValue(); // L or R
                String expected = row.getCell(2).getStringCellValue();
                String actual = "";
                
                // Click the appropriate button
                if (action.equalsIgnoreCase("loginAdmin")) {
                    driver.findElement(By.id("loginLinkMain")).click();
                    actual = driver.getCurrentUrl().contains("/login") ? "Success" : "Failed";
                } 
                else if (action.equalsIgnoreCase("registerAdmin")) {
                    driver.findElement(By.id("registerLinkMain")).click();
                    actual = driver.getCurrentUrl().contains("/register") ? "Success" : "Failed";
                } 
                else if (action.equalsIgnoreCase("loginStudent")) {
                    driver.findElement(By.id("loginLinkMain")).click();
                    driver.findElement(By.id("studentLoginLink")).click();
                    actual = driver.getCurrentUrl().contains("/login_norm") ? "Success" : "Failed";
                }
                
                // Check Result
                Thread.sleep(2000); // Wait for result                                
                              
                // Write Test Result to Excel
                Cell resultCell = row.createCell(3);
                resultCell.setCellValue(actual);
                
                // Print Console Output
                System.out.println("Test Case: " + row.getCell(0).getStringCellValue() + " - Expected: " + expected + " - Actual: " + actual);
                WebElement homeButton = driver.findElement(By.id("homeLink"));
                homeButton.click();
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