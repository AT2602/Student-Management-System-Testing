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


public class LoginTest extends BaseTest {
	
	@Test
	
    public void runLoginTest() {
        
        // Open Excel File
        String excelPath = "C:\\Users\\Dell\\eclipse-workspace\\StudentManagementTest\\TestResults.xlsx";
        try {
            FileInputStream file = new FileInputStream(new File(excelPath));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(1);
            
            driver.get("http://127.0.0.1:3000/login");

            // Loop Through Excel Rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                DataFormatter formatter = new DataFormatter();
                
                if (row == null) {
                    System.out.println("Row " + i + " is NULL. Exiting loop.");
                    break; // Exit loop immediately
                }

                
                String email = row.getCell(1) != null ? formatter.formatCellValue(row.getCell(1)) : "";
                String password = row.getCell(3) != null ? formatter.formatCellValue(row.getCell(2)) : "";
                String expected = row.getCell(5) != null ? formatter.formatCellValue(row.getCell(3)) : "";
                
                /*
                String email = row.getCell(1).getStringCellValue();
                String password = row.getCell(2).getStringCellValue();
                String expected = row.getCell(3).getStringCellValue();
                */
                // Find Elements
                WebElement emailInput = driver.findElement(By.id("email"));
                WebElement passwordInput = driver.findElement(By.id("password"));
                WebElement loginButton = driver.findElement(By.id("loginButton"));
                
                // Perform Login
                emailInput.clear();
                emailInput.sendKeys(email);
                passwordInput.clear();
                passwordInput.sendKeys(password);
                loginButton.click();
                
                // Check Result
                Thread.sleep(2000); // Wait for result
                String result;
                if (driver.getCurrentUrl().contains("admin_main")) {
                    result = "Success";
                 // Write Test Result to Excel
                    Cell resultCell = row.createCell(4);
                    resultCell.setCellValue(result);
                    
                    // Print Console Output
                    System.out.println("Test Case: " + row.getCell(0).getStringCellValue() + " - Expected: " + expected + " - Actual: " + result);
                    WebElement logoutButton = driver.findElement(By.id("logoutLink"));
                    logoutButton.click();
                } else {
                    result = "Failure";
                 // Write Test Result to Excel
                    Cell resultCell = row.createCell(4);
                    resultCell.setCellValue(result);
                    
                    // Print Console Output
                    System.out.println("Test Case: " + row.getCell(0).getStringCellValue() + " - Expected: " + expected + " - Actual: " + result);
                }
                
                // Write Test Result to Excel
              /*  Cell resultCell = row.createCell(4);
                resultCell.setCellValue(result);
                
                // Print Console Output
                System.out.println("Test Case: " + row.getCell(0).getStringCellValue() + " - Expected: " + expected + " - Actual: " + result);
                WebElement logoutButton = driver.findElement(By.id("logoutLink"));
                logoutButton.click();
                */
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