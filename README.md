# Student Management System (Testing)

## Overview

This is the testing of my full-stack Student Management System designed to manage students, courses and marks. It features automated testing using Selenium WebDriver, TestNG and integrates with MongoDB for data storage. Test results are logged in Excel files using Apache POI.

## Features

- **Student Management:** Add, search, update, and list students.
- **Course Management:** Add, assign, and search courses.
- **Marks Management:** Enter and update student marks.
- **Authentication:** Login and registration for users.
- **Admin Panel:** Admin functionalities for managing students and courses.
- **Automated Testing:** Selenium-based UI tests, TestNG test suite, and reporting.
- **Database Integration:** Uses MongoDB for storing student and course data.

## Technologies Used

- **Java 17**
- **Maven** (for dependency management)
- **Selenium WebDriver** (UI automation)
- **TestNG** (test framework)
- **MongoDB** (database)
- **Apache POI** (Excel operations)
- **Web Application** (runs at `http://127.0.0.1:3000/`)

## How It Works

### 1. Main Application

- The entry point is `App.java`, which can be extended for application logic.

### 2. Utilities

- **ExcelUtils.java:** Handles reading/writing test results to Excel.
- **MongoDBUtils.java:** Provides methods to check existence of students/courses in MongoDB.

### 3. Automated Tests

- **BaseTest.java:** Sets up Selenium WebDriver and browser preferences.
- **AdminAddStudentTest.java, AdminAddCourseTest.java, etc.:** Test cases for admin functionalities.
- **TestNG:** Used to organize and run tests, with results output to HTML and XML reports.

### 4. Database

- MongoDB is used for storing student and course data.
- Connection details are set in `MongoDBUtils.java`.

### 5. Reporting

- Test results are written to `TestResults.xlsx` using Apache POI.
- TestNG generates HTML/XML reports in the `test-output/` directory.

## Setup Instructions

1. **Install Dependencies:**
	- Ensure Java 17+ and Maven are installed.
	- Install MongoDB and start the service.
	- Download ChromeDriver and set the path in `BaseTest.java`.

2. **Configure Database:**
	- MongoDB should be running locally at `mongodb://localhost:27017`.
	- Database name: `studentDB`
	- Collections: `students`, `courses`

3. **Run Tests:**
	- Use Maven to run tests:  
	  ```
	  mvn test
	  ```
	- Or use TestNG via IDE or command line.

4. **View Reports:**
	- Check `TestResults.xlsx` for Excel-based results.
	- View HTML/XML reports in `test-output/`.

## Working

- Admin logs in and manages students/courses via the web interface.
- Automated tests simulate user actions and verify outcomes.
- Data is validated against MongoDB.
- Results are logged in Excel and TestNG reports.
