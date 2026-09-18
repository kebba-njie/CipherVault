import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main {

    public static String hashPassword(String password) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8)
            );

            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not available.", e);
        }
    }

    public static void main(String[] args) {
        FileManager.initializeVault();

        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("          CIPHERVAULT");
        System.out.println("     Security Management System");
        System.out.println("=================================");

        System.out.println();
        System.out.println("1. Create Account");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.println();

        System.out.print("Select an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {

            System.out.println();
            System.out.println("---------- CREATE ACCOUNT ----------");

            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            if (username.trim().isEmpty()) {
                System.out.println("Username cannot be empty.");
                scanner.close();
                return;
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (password.trim().isEmpty()) {
                System.out.println("Password cannot be empty.");
                scanner.close();
                return;
            }

            if (password.length() < 8) {
                System.out.println("Password must be at least 8 characters.");
                scanner.close();
                return;
            }
            String role = "USER";

            String hashedPassword = hashPassword(password);

            FileManager.saveAccount(username, hashedPassword, role);

            System.out.println();
            System.out.println("Account created successfully!");
            System.out.println("Password secured and account saved.");

        } else if (choice == 2) {

            System.out.println();
            System.out.println("------------- LOGIN -------------");

            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();

            if (loginUsername.trim().isEmpty()) {
                System.out.println("Username cannot be empty.");
                scanner.close();
                return;
            }

            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();

            if (loginPassword.trim().isEmpty()) {
                System.out.println("Password cannot be empty.");
                scanner.close();
                return;
            }

            String loginHash = hashPassword(loginPassword);


            try {

                Scanner fileScanner = new Scanner(
                        new java.io.File("accounts.txt")
                );

                boolean loginSuccessful = false;
                String loggedInRole = "USER";

                while (fileScanner.hasNextLine()) {

                    String account = fileScanner.nextLine();

                    String[] accountData = account.split(":");

                    String storedUsername = accountData[0];
                    String storedHash = accountData[1];
                    String storedRole = accountData.length >= 3
                            ? accountData[2]
                            : "USER";

                    if (loginUsername.equals(storedUsername)
                            && loginHash.equals(storedHash)) {

                        loginSuccessful = true;
                        loggedInRole = storedRole;
                        break;
                    }
                }

                fileScanner.close();

                if (loginSuccessful) {

                    System.out.println();
                    System.out.println("Login successful!");
                    System.out.println("Welcome back, " + loginUsername + "!");
                    System.out.println("Role: " + loggedInRole);

                    FileManager.logActivity(
                            "User logged in: " + loginUsername
                    );

                    boolean loggedIn = true;

                    while (loggedIn) {

                        System.out.println();
                        System.out.println("-------- CIPHERVAULT --------");
                        System.out.println("1. Store File");
                        System.out.println("2. View Files");
                        System.out.println("3. Encrypt File");
                        System.out.println("4. Decrypt File");
                        System.out.println("5. File Integrity Check");
                        System.out.println("6. Security Log");
                        System.out.println("7. Logout");

                        System.out.print("Select an option: ");
                        int vaultChoice = scanner.nextInt();
                        scanner.nextLine();

                        if (vaultChoice == 1) {

                            System.out.print("Enter file name: ");
                            String fileName = scanner.nextLine();

                            System.out.print("Enter file content: ");
                            String content = scanner.nextLine();

                            FileManager.storeFile(fileName, content);

                        } else if (vaultChoice == 2) {

                            FileManager.viewFiles();

                        } else if (vaultChoice == 3) {

                            System.out.print("Enter file name to encrypt: ");
                            String fileName = scanner.nextLine();

                            System.out.print("Enter encryption key (16 characters): ");
                            String key = scanner.nextLine();

                            FileManager.encryptFile(fileName, key);

                        } else if (vaultChoice == 4) {

                            System.out.print("Enter encrypted file name: ");
                            String fileName = scanner.nextLine();

                            System.out.print("Enter encryption key (16 characters): ");
                            String key = scanner.nextLine();

                            FileManager.decryptFile(fileName, key);

                        } else if (vaultChoice == 5) {

                            System.out.print("Enter file name to check: ");
                            String fileName = scanner.nextLine();

                            FileManager.checkFileIntegrity(fileName);

                        } else if (vaultChoice == 6) {

                            if (loggedInRole.equals("ADMIN")) {

                                FileManager.viewSecurityLog();

                            } else {

                                System.out.println("Access denied. Admin permission required.");
                            }
                        } else if (vaultChoice == 7) {

                            loggedIn = false;

                            System.out.println("Logged out successfully!");

                            FileManager.logActivity(
                                    "User logged out: " + loginUsername
                            );

                        } else {

                            System.out.println("This feature is coming soon.");
                        }
                    }

                } else {

                    System.out.println();
                    System.out.println("Invalid username or password.");
                }

            } catch (Exception e) {

                System.out.println("Error reading account data.");
            }

        } else if (choice == 3) {

            System.out.println("Exiting CipherVault...");

        } else {

            System.out.println("Invalid option.");
        }

        scanner.close();
    }
}
