# 🔐 CipherVault — Security Management System

**A Java-based cybersecurity application for secure file storage, encryption, authentication, and security monitoring.**

CipherVault is a security-focused software project developed to demonstrate the practical application of cybersecurity principles through Java programming. It combines password security, authenticated file encryption, file integrity verification, access protection, and security event logging in a single console-based application.

The project focuses on understanding how security mechanisms can be implemented in software, how sensitive information can be protected, and how defensive programming practices can reduce common security risks.

CipherVault was developed incrementally, with improvements to its security features, file management, authentication controls, configuration, testing, and user interface throughout its development.

> **Project status:** Educational cybersecurity project. The application demonstrates several security mechanisms but requires further testing and independent security review before it can be considered suitable for protecting sensitive production data.

---

## 📌 Table of Contents

* [Project Overview](#-project-overview)
* [Key Features](#-key-features)
* [Security Architecture](#-security-architecture)
* [Technologies Used](#-technologies-used)
* [Project Structure](#-project-structure)
* [Getting Started](#-getting-started)
* [How to Use CipherVault](#-how-to-use-ciphervault)
* [Security Testing](#-security-testing)
* [Security Considerations](#-security-considerations)
* [Future Improvements](#-future-improvements)
* [Learning Objectives](#-learning-objectives)
* [Author](#-author)

---

## 🎯 Project Overview

CipherVault was created to explore how software development and cybersecurity can work together to protect information.

The application provides account creation and login functionality, allowing users to access a file vault through a console-based menu. Within the vault, users can perform supported file operations, including storing files, viewing stored files, encrypting and decrypting files, and checking file integrity.

The application also incorporates password hashing, input validation, temporary login lockouts, and security event logging.

Rather than relying on a single security mechanism, CipherVault demonstrates several complementary techniques that address different aspects of information security.

### Project Goals

The main objectives are to:

* Apply Java programming concepts to a practical cybersecurity project.
* Demonstrate secure password storage and account authentication.
* Explore authenticated encryption and secure file handling.
* Reduce common input-validation and file-path risks.
* Implement basic protection against repeated failed login attempts.
* Record selected security-related activities for auditing.
* Practise software organisation, testing, debugging, and version control.
* Develop a foundation for more advanced cybersecurity projects.

---

## 🚀 Key Features

### 1. Account Creation and Authentication

CipherVault allows users to create accounts and authenticate through a login interface.

Account creation includes input validation and duplicate-username checks. During login, the application checks the supplied credentials against the stored account information.

**Why it matters:** Authentication helps prevent unauthorised users from accessing application functionality.

### 2. Password Hashing

The application uses PBKDF2-based password hashing rather than storing account passwords as ordinary plaintext.

PBKDF2 applies a password-based key derivation process that makes password verification more computationally expensive than comparing ordinary plaintext values.

**Why it matters:** If an account database is exposed, properly implemented password hashing provides an additional layer of protection against recovering the original passwords.

Password hashing and file encryption serve different purposes: hashing supports password verification, while encryption protects data that needs to be recovered later.

### 3. AES-GCM File Encryption

CipherVault uses AES-GCM, an authenticated encryption mode, to protect supported files.

AES-GCM is designed to provide:

* **Confidentiality:** Protects the contents of encrypted files from being read without the appropriate key.
* **Integrity:** Detects unauthorised modification of authenticated encrypted data.
* **Authentication:** Allows the application to verify that encrypted data has not been altered in a way that invalidates its authentication tag.

The implementation uses a password-based key derivation process to derive an encryption key, together with cryptographic salt and initialization-vector data.

**Why it matters:** Encryption helps protect stored information, while authentication helps detect tampering with encrypted content.

Successful decryption requires the correct key and intact encryption data.

### 4. Dedicated File Vault

CipherVault uses a dedicated `vault` directory for file storage.

File operations are directed towards the vault rather than relying on arbitrary file locations. The application also normalises paths and checks that resolved paths remain within the intended vault directory.

**Why it matters:** Restricting file operations to an intended storage location helps reduce the risk of unintended access to files outside the vault.

### 5. Filename Validation

The application validates filenames before processing supported file operations.

The filename checks reject empty or unsupported names and restrict accepted characters to a defined set. The implementation also checks for special names such as `.` and `..`.

**Why it matters:** Input validation reduces the risk of malformed filenames and helps prevent common path-manipulation problems.

Filename validation is only one part of secure file handling; additional checks and tests are still necessary.

### 6. File Integrity Verification

CipherVault includes a file integrity-checking feature that compares file information against previously recorded integrity information.

This helps identify unexpected changes to supported files.

**Why it matters:** Integrity verification is useful when determining whether information may have been modified unexpectedly.

The effectiveness of this feature depends on how integrity information is generated, stored, and protected. A separate integrity record that an attacker can also modify does not provide complete protection against deliberate tampering.

### 7. Failed Login Tracking and Temporary Lockouts

CipherVault tracks failed login attempts and supports persistent temporary lockouts.

The configuration defines a maximum of three failed attempts and a lockout duration of five minutes.

The `LockoutManager` class handles the relevant lockout operations, while `AppConfig` centralises the associated configuration values.

**Why it matters:** Temporary lockouts can help slow repeated password-guessing attempts.

However, lockouts alone do not eliminate brute-force attacks and require careful handling of recovery, concurrent access, and potential denial-of-service risks.

### 8. Security Audit Logging

CipherVault records selected security-related activities in a security log.

This provides a record of events that can help with basic monitoring and troubleshooting.

**Why it matters:** Security logs can help developers and administrators understand what happened during application use and investigate unexpected behaviour.

The usefulness of the logs depends on which events are recorded, how accurately they are recorded, and whether the log files are protected against unauthorised modification.

### 9. Input Validation and Error Handling

The application includes validation for account-related inputs and file operations.

Examples include checking for empty usernames, usernames containing spaces, duplicate usernames, and invalid filenames.

**Why it matters:** Validating input before processing it helps prevent unexpected behaviour and improves application reliability.

Further improvements can make validation more consistent across all application features.

### 10. Centralised Configuration

CipherVault uses `AppConfig.java` to centralise selected configuration settings, including login lockout parameters.

**Why it matters:** Centralising configuration makes security settings easier to locate, understand, and maintain without searching through multiple classes.

### 11. Basic Security Tests

The project includes `SecurityTests.java`, which contains standalone checks for several filename-validation rules.

The current tests cover valid filenames, empty filenames, path-traversal-style input, and unsupported filename characters.

**Why it matters:** Security tests help verify expected behaviour and can identify mistakes during development.

The existing tests are introductory checks rather than a comprehensive automated security test suite.

---

## 🛡️ Security Architecture

CipherVault demonstrates several security concepts that work together.

| Security Layer           | Implementation                                 | Purpose                                                      |
| ------------------------ | ---------------------------------------------- | ------------------------------------------------------------ |
| Authentication           | Account creation and login                     | Checks user credentials before access                        |
| Password security        | PBKDF2-based password hashing                  | Supports safer password storage                              |
| File confidentiality     | AES-GCM encryption                             | Protects encrypted file contents                             |
| Encrypted-data integrity | AES-GCM authentication                         | Detects modification of authenticated encrypted data         |
| File integrity checking  | Stored integrity information                   | Helps identify unexpected file changes                       |
| File-system protection   | Vault directory and path validation            | Restricts supported file operations to the intended location |
| Login protection         | Failed-attempt tracking and temporary lockouts | Slows repeated login attempts                                |
| Security monitoring      | Audit logging                                  | Records selected security events                             |
| Configuration            | Centralised settings                           | Makes selected security controls easier to maintain          |
| Testing                  | Filename-validation checks                     | Verifies basic expected input-validation behaviour           |

These controls address different risks. For example, password hashing does not encrypt files, and encrypted-file authentication does not automatically protect account records or application logs.

A secure application must consider the entire system rather than relying on one cryptographic feature.

---

## 🛠️ Technologies Used

### Programming Language

* **Java** — Application logic, authentication, file management, and security controls.

### Development Environment

* **IntelliJ IDEA** — Writing, running, debugging, and managing the Java project.
* **Java Development Kit (JDK)** — Compiling and running the application.

### Security and Development Concepts

* AES-GCM authenticated encryption.
* PBKDF2 password-based key derivation.
* SHA-256 hashing where used by the implementation.
* Java Cryptography Architecture (JCA).
* File handling and path normalisation.
* Input validation and defensive programming.
* Authentication and login lockouts.
* Security event logging.
* Basic security testing.
* Git and GitHub version control.

---

## 📂 Project Structure

The main source files are organised by responsibility.

```text
CipherVault/
│
├── src/
│   ├── Main.java
│   ├── FileManager.java
│   ├── LockoutManager.java
│   ├── AppConfig.java
│   └── SecurityTests.java
│
├── README.md
├── .gitignore
└── CipherVault.iml
```

The structure above describes the intended organisation of the main project files. Your local project may contain additional files.

### Main Java Classes

| File                  | Responsibility                                                                                                                                |
| --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| `Main.java`           | Provides the main menu, account creation, login, and the application's primary control flow.                                                  |
| `FileManager.java`    | Handles vault initialisation, account-file operations, file storage, encryption, decryption, integrity checking, and related file operations. |
| `LockoutManager.java` | Manages failed login tracking and temporary lockout behaviour.                                                                                |
| `AppConfig.java`      | Centralises selected configuration settings, including login lockout values.                                                                  |
| `SecurityTests.java`  | Contains basic standalone filename-validation tests.                                                                                          |

### Runtime Data

Depending on the operations performed, CipherVault may create runtime files such as:

* `accounts.txt` — Account records.
* `security.log` — Security event records.
* `lockouts.txt` — Login lockout information.
* `integrity.txt` — File integrity information.
* `vault/` — Directory for stored vault files.

These are runtime data files, not source-code files. They should be protected appropriately and should not be committed to a public repository when they contain sensitive information.

---

## ⚙️ Getting Started

### Prerequisites

Before running CipherVault, ensure you have:

* A compatible Java Development Kit (JDK).
* IntelliJ IDEA or another Java IDE.
* Git, if you want to clone the repository from the command line.

### Step 1: Clone the Repository

Open a terminal and run:

```bash
git clone https://github.com/kebba-njie/CipherVault.git
```

Move into the project directory:

```bash
cd CipherVault
```

### Step 2: Open the Project

Open IntelliJ IDEA and select the cloned `CipherVault` project directory.

Allow IntelliJ to load the project and configure a compatible JDK if necessary.

### Step 3: Run the Application

Locate:

```text
src/Main.java
```

Open the file and run its `main()` method using IntelliJ.

The application should display the CipherVault main menu.

> **Note:** Exact setup steps may vary depending on the JDK version and the project's IntelliJ configuration.

---

## 🖥️ How to Use CipherVault

### 1. Start the Application

Run `Main.java`.

The application displays a main menu with these options:

```text
========================================
              CIPHERVAULT
       SECURITY MANAGEMENT SYSTEM
========================================

             MAIN MENU
----------------------------------------
  [1]  Create Account
  [2]  Login
  [3]  Exit
----------------------------------------
```

### 2. Create an Account

Select **Create Account** and enter the requested account details.

The application validates the supplied information and checks whether the username already exists.

Use a password that satisfies the application's password requirements.

### 3. Log In

Select **Login** and enter the credentials associated with your account.

The application checks the credentials and applies the configured login protection when required.

### 4. Access the Vault

After successful authentication, use the available vault menu to access supported operations, including:

* Storing files.
* Viewing stored files.
* Encrypting files.
* Decrypting files.
* Checking file integrity.
* Viewing the security log.
* Logging out.

The available operations depend on the application's current implementation.

### 5. Exit Safely

Use the application's menu to log out or exit when you have finished.

Protect account records, encryption keys, passwords, and stored files. Do not publish private credentials or sensitive runtime data.

---

## 🧪 Security Testing

CipherVault includes a basic test class named `SecurityTests.java`.

To run the included checks:

1. Open the project in IntelliJ IDEA.
2. Locate `src/SecurityTests.java`.
3. Run its `main()` method.
4. Review the reported PASS and FAIL messages.

The included checks examine four filename-validation scenarios:

| Test                                 | Expected Result |
| ------------------------------------ | --------------- |
| Valid filename                       | Accepted        |
| Empty filename                       | Rejected        |
| Path-traversal-style filename        | Rejected        |
| Filename with unsupported characters | Rejected        |

These tests check the filename-validation rules in the test class. They do not, by themselves, verify every aspect of the actual file-management implementation.

Further testing should include authentication, password hashing, encryption and decryption, incorrect encryption keys, tampered encrypted files, path traversal, lockout behaviour, file integrity checking, and error handling.

---

## ⚠️ Security Considerations

CipherVault is an educational project. Its security features demonstrate useful concepts, but implementing cryptography does not automatically make an application secure.

Important considerations include:

* **Key management:** Encryption keys must be handled carefully and must not be exposed in logs or public repositories.
* **Password protection:** Password hashing parameters and account storage require security review.
* **File integrity:** Integrity information must be protected against unauthorised modification.
* **Authentication:** Login protection should be tested against repeated attempts, recovery scenarios, and concurrent access.
* **File handling:** Path validation should be tested against malicious input and unexpected file-system conditions.
* **Data protection:** Account records, vault contents, logs, and other runtime files may contain sensitive information.
* **Testing:** The current filename tests do not establish that all security features work correctly under attack.

**Do not use CipherVault to protect highly sensitive or production data without further development, thorough testing, and an independent security review.**

---

## 🔮 Future Improvements

CipherVault provides a foundation for continued learning and development.

Potential improvements include:

* **Automated testing:** Add a structured test suite covering authentication, encryption, decryption, lockouts, and file handling.
* **Improved key management:** Review how encryption keys are derived, entered, stored, and recovered.
* **Stronger integrity protection:** Review the integrity-record design and protect it against unauthorised modification.
* **Improved account management:** Add carefully designed password-reset and account-recovery functionality.
* **Access control:** Review role handling and enforce explicit permissions where needed.
* **Better error handling:** Provide consistent error messages without exposing sensitive implementation details.
* **Graphical user interface:** Develop a more accessible interface beyond the current console menu.
* **Security testing:** Conduct more extensive testing and independent security review.
* **Documentation:** Add verified screenshots, usage examples, and detailed developer documentation.

These improvements would help extend the project while strengthening its reliability and security.

---

## 📚 Learning Objectives

Through CipherVault, the project demonstrates practical exploration of:

* Java classes, methods, and application structure.
* File input and output.
* Password-based cryptographic operations.
* Authenticated file encryption.
* Input validation and defensive programming.
* Authentication and login protection.
* File integrity verification.
* Security event logging.
* Configuration management.
* Testing and debugging.
* Git commits and GitHub-based project development.

The project was developed incrementally, with changes tracked through Git to maintain a development history and make improvements easier to review.

---

## 👨‍💻 Author

**Kebba Njie**

CipherVault is an independent Java and cybersecurity learning project focused on applying secure programming concepts to a practical application.

**GitHub Repository:**
https://github.com/kebba-njie/CipherVault

---

## ⭐ Final Note

CipherVault represents a practical step towards understanding how software engineering and cybersecurity intersect.

Its development has provided an opportunity to explore authentication, cryptography, file handling, security monitoring, and version control in one project.

The long-term goal is to continue improving the application through testing, code review, and additional security-focused development.
