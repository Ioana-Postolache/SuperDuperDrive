package com.udacity.jwdnd.course1.cloudstorage.tests;

import com.udacity.jwdnd.course1.cloudstorage.Pages.CredentialsTab;
import com.udacity.jwdnd.course1.cloudstorage.Pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialListService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static com.udacity.jwdnd.course1.cloudstorage.Utils.CommonUtilities.login;
import static com.udacity.jwdnd.course1.cloudstorage.Utils.CommonUtilities.signup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CredentialsTests {
    private static ChromeDriver driver;
    @Autowired
    EncryptionService encryptionService = new EncryptionService();
    @Autowired
    private CredentialListService credentialService;
    @LocalServerPort
    private int port;

    private String baseURL;
    private HomePage home;
    private CredentialsTab credentialsTab;


    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterAll
    public static void afterAll() {
        driver.quit();
        driver = null;
    }

    @BeforeEach
    public void beforeEach() {
        baseURL = "http://localhost:" + port;
        driver.get(baseURL + "/login");
        home = new HomePage(driver);
        credentialsTab = new CredentialsTab(driver);
    }

    @Test
    @Order(1)
    public void testAddCredentials() {

        String url = "www.example.com";
        String username = "username";
        String password = "password";

        signup(driver);
        login(driver, baseURL);
        home.goToNavCredentialsTab(driver);
        Assertions.assertEquals(home.getActiveTabText(), "Credentials");


        //create Credential
        credentialsTab.addCredential(url, username, password, driver);
        // verify that the credential is being displayed
        Assertions.assertEquals(credentialsTab.getFirstRowUrl(), url);
        Assertions.assertEquals(credentialsTab.getFirstRowUsername(), username);
        String credentialKey = credentialService.getCredentialKey(1, 1);
        // verify that the displayed password is encrypted
        Assertions.assertEquals(encryptionService.encryptValue(password, credentialKey), credentialsTab.getFirstRowPassword());

        // view existing set of credentials
        credentialsTab.viewCredential(driver);
        Assertions.assertEquals(url, credentialsTab.getCredentialUrlInput());
        Assertions.assertEquals(username, credentialsTab.getCredentialUsernameInput());
        // verify that the viewable password is unencrypted
        Assertions.assertEquals(credentialsTab.getCredentialPasswordInput(), password);
    }


    @Test
    @Order(2)
    public void testEditCredentials() {
        // edits the credentials and verify that the changes are displayed.
        String changedText = "Changed";

        login(driver, baseURL);
        home.goToNavCredentialsTab(driver);
        Assertions.assertEquals(home.getActiveTabText(), "Credentials");

        // view existing set of credentials
        credentialsTab.viewCredential(driver);
        String credentialKey = credentialService.getCredentialKey(1, 1);
        credentialsTab.createOrUpdateCredential(changedText, changedText, changedText);
        Assertions.assertEquals(credentialsTab.getFirstRowUrl(), changedText);
        Assertions.assertEquals(credentialsTab.getFirstRowUsername(), changedText);
        // verify that the displayed password is encrypted
        Assertions.assertEquals(credentialsTab.getFirstRowPassword(), encryptionService.encryptValue(changedText, credentialKey));
    }


    @Test
    @Order(3)
    public void testDeleteCredentials() {
        // delete the created credential and verify that credentials table doesn't display any credentials
        login(driver, baseURL);
        home.goToNavCredentialsTab(driver);
        Assertions.assertEquals(home.getActiveTabText(), "Credentials");

        credentialsTab.deleteFirstCredential(driver);
        Assertions.assertTrue(credentialsTab.isTableCredentialEmpty(driver));
    }
}
