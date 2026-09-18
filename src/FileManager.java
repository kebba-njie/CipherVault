import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileManager {

    public static void saveAccount(String username, String hashedPassword, String role) {

        try {
            FileWriter writer = new FileWriter("accounts.txt", true);

            writer.write(username + ":" + hashedPassword + ":" + role + "\n");

            writer.close();

            System.out.println("Account saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving account.");
        }
    }

    public static void storeFile(String fileName, String content) {

        try {
            FileWriter writer = new FileWriter(fileName);

            writer.write(content);

            writer.close();

            System.out.println("File stored successfully!");
            logActivity("File stored: " + fileName);

        } catch (IOException e) {
            System.out.println("Error storing file.");
        }
    }

    public static void viewFiles() {

        java.io.File folder = new java.io.File(".");

        java.io.File[] files = folder.listFiles();

        System.out.println();
        System.out.println("-------- STORED FILES --------");

        if (files != null) {

            for (java.io.File file : files) {

                if (file.isFile()) {
                    System.out.println(file.getName());
                }
            }

        } else {

            System.out.println("No files found.");
        }
    }

    public static void encryptFile(String fileName, String key) {

        try {

            byte[] fileData = java.nio.file.Files.readAllBytes(
                    java.nio.file.Paths.get(fileName)
            );

            javax.crypto.spec.SecretKeySpec secretKey =
                    new javax.crypto.spec.SecretKeySpec(
                            key.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                            "AES"
                    );

            javax.crypto.Cipher cipher =
                    javax.crypto.Cipher.getInstance("AES");

            cipher.init(
                    javax.crypto.Cipher.ENCRYPT_MODE,
                    secretKey
            );

            byte[] encryptedData = cipher.doFinal(fileData);

            java.nio.file.Files.write(
                    java.nio.file.Paths.get(fileName + ".enc"),
                    encryptedData
            );

            System.out.println("File encrypted successfully!");
            logActivity("File encrypted: " + fileName);

        } catch (Exception e) {

            System.out.println("Error encrypting file.");
        }
    }

    public static void decryptFile(String fileName, String key) {

        try {

            byte[] encryptedData = java.nio.file.Files.readAllBytes(
                    java.nio.file.Paths.get(fileName)
            );

            javax.crypto.spec.SecretKeySpec secretKey =
                    new javax.crypto.spec.SecretKeySpec(
                            key.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                            "AES"
                    );

            javax.crypto.Cipher cipher =
                    javax.crypto.Cipher.getInstance("AES");

            cipher.init(
                    javax.crypto.Cipher.DECRYPT_MODE,
                    secretKey
            );

            byte[] decryptedData = cipher.doFinal(encryptedData);

            java.nio.file.Files.write(
                    java.nio.file.Paths.get(
                            "decrypted_" + fileName.replace(".enc", "")
                    ),
                    decryptedData
            );

            System.out.println("File decrypted successfully!");

        } catch (Exception e) {

            System.out.println("Error decrypting file.");
        }
    }

    public static void checkFileIntegrity(String fileName) {

        try {

            byte[] fileData = java.nio.file.Files.readAllBytes(
                    java.nio.file.Paths.get(fileName)
            );

            java.security.MessageDigest digest =
                    java.security.MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(fileData);

            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {

                String hex = Integer.toHexString(0xff & b);

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            System.out.println();
            System.out.println("-------- FILE INTEGRITY --------");
            System.out.println("File: " + fileName);
            System.out.println("SHA-256 Hash:");
            System.out.println(hexString);
            System.out.println("Integrity check completed.");
            logActivity("Integrity check performed: " + fileName);

        } catch (Exception e) {

            System.out.println("Error checking file integrity.");
        }
    }

    public static void logActivity(String activity) {

        try {

            FileWriter writer = new FileWriter("security.log", true);

            writer.write(activity + "\n");

            writer.close();

            System.out.println("Security activity logged.");

        } catch (IOException e) {

            System.out.println("Error writing security log.");
        }
    }

    public static void viewSecurityLog() {

        try {

            java.io.File logFile = new java.io.File("security.log");

            if (!logFile.exists()) {

                System.out.println("No security activity recorded yet.");
                return;
            }

            Scanner logScanner = new Scanner(logFile);

            System.out.println();
            System.out.println("-------- SECURITY LOG --------");

            while (logScanner.hasNextLine()) {

                System.out.println(logScanner.nextLine());
            }

            logScanner.close();

        } catch (Exception e) {

            System.out.println("Error reading security log.");
        }
    }
}