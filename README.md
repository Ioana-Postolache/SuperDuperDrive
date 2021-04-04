# SuperDuperDrive
Interface for an app for cloud storage with 3 user-facing features:

1. **Simple File Storage:** Upload/download/remove files
2. **Note Management:** Add/update/remove text notes
3. **Password Management:** Save, edit, and delete website credentials.  

## Starter Project
Starter project downloaded from:  [You can download or clone the starter repository here](https://github.com/udacity/nd035-c1-spring-boot-basics-project-starter/tree/master/starter/cloudstorage).

Database schema for the project: `src/main/resources` directory.  

HTML templates: `src/main/resources/templates` folder. 

## Project Structure
1. The back-end with Spring Boot
2. The front-end with Thymeleaf
3. Application tests with Selenium

### The Back-End
The back-end is all about security and connecting the front-end to database data and actions. 

1. Managing user access with Spring Security
 - unauthorized users are restricted from accessing pages other than the login and signup pages. 
 - Spring Boot has built-in support for handling calls to the `/login` and `/logout` endpoints. 
 - `AuthenticationProvider` authorizes user logins by matching their credentials against those stored in the database.  


2. Handling front-end calls with controllers
 - Controllers are in the package: `controller`
 - Repeating tasks are abstracted into services.
 
3. Making calls to the database with MyBatis mappers
 - Since you were provided with a database schema to work with, Java classes are designed to match the data in the database. These are POJOs (Plain Old Java Objects) with fields that match the names and data types in the schema, and one class corresponds to one database table. These classes are placed in the `model` package.
 - To connect these model classes with database data, MyBatis mapper interfaces are implemented for each of the model types. These mappers should have methods that represent specific SQL queries and statements required by the functionality of the application. They should support the basic CRUD (Create, Read, Update, Delete) operations for their respective models at the very least. You can place these classes in (you guessed it!) the `mapper` package.


### The Front-End
The HTML templates for the required application pages include fields, modal forms, success and error message elements, as well as styling and functional components using Bootstrap as a framework. 
Thymeleaf attributes are used to supply the back-end data and functionality described by the following individual page requirements:

1. Login page
 - Everyone should be allowed access to this page, and users can use this page to login to the application. 
 - Show login errors, like invalid username/password, on this page. 


2. Sign Up page
 - Everyone should be allowed access to this page, and potential users can use this page to sign up for a new account. 
 - Validate that the username supplied does not already exist in the application, and show such signup errors on the page when they arise.
 - Remember to store the user's password securely!


3. Home page
The home page is the center of the application and hosts the three required pieces of functionality. The existing template presents them as three tabs that can be clicked through by the user:


 i. Files
  - The user should be able to upload files and see any files they previously uploaded. 

  - The user should be able to view/download or delete previously-uploaded files.
  - Any errors related to file actions should be displayed. For example, a user should not be able to upload two files with the same name, but they'll never know unless you tell them!


 ii. Notes
  - The user should be able to create notes and see a list of the notes they have previously created.
  - The user should be able to edit or delete previously-created notes.

 iii. Credentials
 - The user should be able to store credentials for specific websites and see a list of the credentials they've previously stored. If you display passwords in this list, make sure they're encrypted!
 - The user should be able to view/edit or delete individual credentials. When the user views the credential, they should be able to see the unencrypted password.

The home page should have a logout button that allows the user to logout of the application and keep their data private.

### Testing
Simple Selenium tests are used to verify user-facing functionality. These include:

1. tests for user signup, login, and unauthorized access restrictions.
 - a test that verifies that an unauthorized user can only access the login and signup pages.
 - a test that signs up a new user, logs in, verifies that the home page is accessible, logs out, and verifies that the home page is no longer accessible. 


2. tests for note creation, viewing, editing, and deletion.
 - a test that creates a note, and verifies it is displayed.
 - Write a test that edits an existing note and verifies that the changes are displayed.
 - Write a test that deletes a note and verifies that the note is no longer displayed.


3. tests for credential creation, viewing, editing, and deletion.
 - a test that creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted.
 - a test that views an existing set of credentials, verifies that the viewable password is unencrypted, edits the credentials, and verifies that the changes are displayed.
 - a test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.


### Password Security
We are using a hashing function to store a scrambled version of the credentials of the application's users in the database. The `HashService` class hashes passwords. When the user signs up, we are only storing a hashed version of their password in the database, and on login, we hash the password attempt before comparing it with the hashed password in the database. 
Code sample to help illustrate the idea:

```
byte[] salt = new byte[16];
random.nextBytes(salt);
String encodedSalt = Base64.getEncoder().encodeToString(salt);
String hashedPassword = hashService.getHashedValue(plainPassword, encodedSalt);
return hashedPassword;
```

For storing credentials in the main part of the application, we can't hash passwords because it's a one-way operation. The user needs access to the unhashed password. 
So instead, the passwords are encrypted. `EncryptionService` class encrypts and decrypts passwords. When a user adds new credentials, we encrypt the password before storing it in the database. When the user views those credentials, we decrypt the password before displaying it. 

```
SecureRandom random = new SecureRandom();
byte[] key = new byte[16];
random.nextBytes(key);
String encodedKey = Base64.getEncoder().encodeToString(key);
String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
String decryptedPassword = encryptionService.decryptValue(encryptedPassword, encodedKey);
```

[Hash Function](https://en.wikipedia.org/wiki/Hash_function)
[Encryption](https://en.wikipedia.org/wiki/Encryption)
