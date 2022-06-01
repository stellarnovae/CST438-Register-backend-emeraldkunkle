package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;


/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 *      
 *    Make sure that TEST_COURSE_ID is a valid course for TEST_SEMESTER.
 *    
 *    URL is the server on which Node.js is running.
 */

@SpringBootTest
public class EndToEndNewStudentTest {

	public static final String FIREFOX_DRIVER_FILE_LOCATION = "C:\\Users\\emerald.kunkle\\Downloads\\geckodriver-v0.31.0-win64\\geckodriver.exe";

	public static final String URL = "http://localhost:3000";

	public static final String TEST_USER_EMAIL = "test@csumb.edu";
	
	public static final String TEST_USER_NAME = "john doe";
	
	public static final int TEST_COURSE_ID = 40443; 

	public static final String TEST_SEMESTER = "2021 Fall";

	public static final int SLEEP_DURATION = 1000; // 1 second.

	/*
	 * When running in @SpringBootTest environment, database repositories can be used
	 * with the actual database.
	 */
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	StudentRepository studentRepository;

	@Autowired
	CourseRepository courseRepository;

	/*
	 * Student add course TEST_COURSE_ID to schedule for 2021 Fall semester.
	 */
	
	@Test
	public void addNewStudentTest() throws Exception {

		/*
		 * if student is already enrolled, then delete the enrollment.
		 */
		

		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on

		System.setProperty("webdriver.gecko.driver", FIREFOX_DRIVER_FILE_LOCATION);
		WebDriver driver = new FirefoxDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Student student = null;
		try {

			driver.get(URL + "/AddStudent");
			Thread.sleep(SLEEP_DURATION);
			
			driver.findElement(By.id("StudentName")).clear();
			driver.findElement(By.id("StudentName")).sendKeys(TEST_USER_NAME);
			driver.findElement(By.id("StudentEmail")).sendKeys(TEST_USER_EMAIL);
			driver.findElement(By.xpath("//button[@id='AddButton']")).click();
            Thread.sleep(SLEEP_DURATION);
            
            student = studentRepository.findByEmail(TEST_USER_EMAIL);
            Boolean found = false;
            
            if(student.getName().equalsIgnoreCase(TEST_USER_NAME)) {
            	found = true;
            }
            
            assertTrue(found, "Unable to locate student in database.");
			
		} catch (Exception ex) {
			throw ex;
		} finally {
            if(student != null) {
            	studentRepository.delete(student);
            }
			driver.quit();
		}

	}
}