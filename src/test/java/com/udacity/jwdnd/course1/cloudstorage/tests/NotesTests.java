package com.udacity.jwdnd.course1.cloudstorage.tests;

import com.udacity.jwdnd.course1.cloudstorage.Pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.Pages.NotesTab;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static com.udacity.jwdnd.course1.cloudstorage.Utils.CommonUtilities.login;
import static com.udacity.jwdnd.course1.cloudstorage.Utils.CommonUtilities.signup;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotesTests {

    private static ChromeDriver driver;
    @LocalServerPort
    private int port;

    private String baseURL;
    private HomePage home;
    private NotesTab notestab;


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
        notestab = new NotesTab(driver);
    }

    @Test
    @Order(1)
    public void testAddNotes() {
        String noteTitle = "noteTitle";
        String noteDescription = "This is a note description";

        signup(driver);
        login(driver, baseURL);
        home.goToNavNotesTab(driver);
        Assertions.assertEquals(home.getActiveTabText(), "Notes");

        //create Note
        notestab.addNote(noteTitle, noteDescription, driver);
        Assertions.assertEquals(notestab.getFirstRowNoteTitle(), noteTitle);
        Assertions.assertEquals(notestab.getFirstRowNoteDescription(), noteDescription);
    }

    @Test
    @Order(2)
    public void testEditNotes() {
        String changedText = "Changed";

        login(driver, baseURL);
        home.goToNavNotesTab(driver);
        Assertions.assertEquals(home.getActiveTabText(), "Notes");

        // edit existing note and verify that the changes are displayed
        notestab.editNote(changedText, changedText, driver);
        Assertions.assertEquals(notestab.getFirstRowNoteTitle(), changedText);
        Assertions.assertEquals(notestab.getFirstRowNoteDescription(), changedText);
    }

    @Test
    @Order(3)
    public void testDeleteNotes() {
        login(driver, baseURL);
        home.goToNavNotesTab(driver);
        Assertions.assertEquals(home.getActiveTabText(), "Notes");
        // delete the note and verify that note table doesn't have a body
        notestab.deleteFirstNote(driver);
        Assertions.assertTrue(notestab.isTableNoteEmpty(driver));
    }

}
