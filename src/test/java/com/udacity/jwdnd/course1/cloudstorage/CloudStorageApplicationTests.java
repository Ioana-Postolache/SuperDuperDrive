package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.Pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.Pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.Pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private String baseURL;
	private SignupPage signup;
	private LoginPage login;
	private HomePage home;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		baseURL = "http://localhost:" + port;
		driver.get(baseURL + "/login");
		signup = new SignupPage(driver);
		login = new LoginPage(driver);
		home = new HomePage(driver);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	// verifies that an unauthorized user can only access the login and signup pages.
	@Test
	public void getLoginPage() {
		driver.get(baseURL+ "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get(baseURL+ "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
		driver.get(baseURL+ "/home");
		Assertions.assertEquals("Ldmogin", driver.getTitle());
		driver.get(baseURL+ "/files");
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get(baseURL+ "/notes");
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get(baseURL+ "/credentials");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testSignup() throws InterruptedException {
		// sign up a new user
		String username = "username";
		String password = "password";
		login.goToSignup();
		WebDriverWait waitForSignup = new WebDriverWait(driver, 10);
		waitForSignup.until(webDriver -> webDriver.findElement(By.cssSelector("form[action=\"/signup\"]")));
		signup.signup(username, password);
		// login
		driver.get(baseURL + "/login");
		login.login(username, password);
		// go to home
		WebDriverWait waitForHome = new WebDriverWait(driver, 10);
		waitForHome.until(webDriver -> webDriver.getCurrentUrl().equals(baseURL + "/home"));
		// verify that the home page is accessible
		Assertions.assertNotNull(home.getNavFilesTab());
		Assertions.assertNotNull(home.getNavNotesTab());
        Assertions.assertNotNull(home.getNavCredentialsTab());
        // log out
		home.logout();
		WebDriverWait waitForLogin = new WebDriverWait(driver, 10);
		waitForLogin.until(webDriver -> webDriver.getCurrentUrl().equals(baseURL + "/login"));
		// verify that the home page is no longer accessible
		Assertions.assertNotEquals(driver.getCurrentUrl(), baseURL + "/home");
	}

}
