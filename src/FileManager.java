import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileOutputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;

public class FileManager {

    private static final String VAULT_FOLDER = "vault";
    private static final String INTEGRITY_FILE = "integrity.txt";
    private static String getSafeVaultPath(String fileName) {

        java.nio.file.Path vaultPath =
                Paths.get(VAULT_FOLDER).toAbsolutePath().normalize();

        java.nio.file.Path requestedPath =
                vaultPath.resolve(fileName).normalize();

        if (!requestedPath.startsWith(vaultPath)) {
            throw new SecurityException("Invalid file path.");
        }

        return requestedPath.toString();
    }

    public static void initializeVault() {

        try {

            Files.createDirectories(Paths.get(VAULT_FOLDER));

        } catch (IOException e) {

            System.out.println("Error creating vault folder.");
        }
    }

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

            String filePath = getSafeVaultPath(fileName);

            FileWriter writer = new FileWriter(filePath);

            writer.write(content);

            writer.close();

            System.out.println("File stored successfully!");
            System.out.println("Location: vault/" + fileName);

            logActivity("File stored: " + fileName);

        } catch (SecurityException e) {

            System.out.println("Invalid file path.");

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

            byte[] fileData = Files.readAllBytes(
                    Paths.get(fileName)
            );

            SecretKeySpec secretKey =
                    new SecretKeySpec(
                            key.getBytes(StandardCharsets.UTF_8),
                            "AES"
                    );

            Cipher cipher =
                    Cipher.getInstance("AES/GCM/NoPadding");

            byte[] iv = new byte[12];

            new SecureRandom().nextBytes(iv);

            GCMParameterSpec gcmSpec =
                    new GCMParameterSpec(128, iv);

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    secretKey,
                    gcmSpec
            );

            byte[] encryptedData = cipher.doFinal(fileData);

            FileOutputStream output =
                    new FileOutputStream(fileName + ".enc");

            output.write(iv);
            output.write(encryptedData);

            output.close();

            System.out.println("File encrypted successfully!");
            System.out.println("Encryption mode: AES-GCM");

            logActivity("File encrypted: " + fileName);

        } catch (Exception e) {

            System.out.println("Error encrypting file.");
        }
    }

    public static void decryptFile(String fileName, String key) {

        try {

            byte[] encryptedFileData = Files.readAllBytes(
                    Paths.get(fileName)
            );

            byte[] iv = new byte[12];

            System.arraycopy(
                    encryptedFileData,
                    0,
                    iv,
                    0,
                    12
            );

            byte[] encryptedData = new byte[
                    encryptedFileData.length - 12
                    ];

            System.arraycopy(
                    encryptedFileData,
                    12,
                    encryptedData,
                    0,
                    encryptedData.length
            );

            SecretKeySpec secretKey =
                    new SecretKeySpec(
                            key.getBytes(StandardCharsets.UTF_8),
                            "AES"
                    );

            Cipher cipher =
                    Cipher.getInstance("AES/GCM/NoPadding");

            GCMParameterSpec gcmSpec =
                    new GCMParameterSpec(128, iv);

            cipher.init(
                    Cipher.DECRYPT_MODE,
                    secretKey,
                    gcmSpec
            );

            byte[] decryptedData = cipher.doFinal(encryptedData);

            String outputFileName =
                    "decrypted_" + fileName.replace(".enc", "");

            Files.write(
                    Paths.get(outputFileName),
                    decryptedData
            );

            System.out.println("File decrypted successfully!");
            System.out.println("Encryption mode: AES-GCM");

            logActivity("File decrypted: " + fileName);

        } catch (Exception e) {

            System.out.println("Error decrypting file.");
        }
    }

    public static void checkFileIntegrity(String fileName) {

        try {

            String filePath = getSafeVaultPath(fileName);

            byte[] fileData = Files.readAllBytes(
                    Paths.get(filePath)
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

            String currentHash = hexString.toString();

            java.io.File integrityFile =
                    new java.io.File(INTEGRITY_FILE);

            String storedHash = null;

            if (integrityFile.exists()) {

                Scanner scanner =
                        new Scanner(integrityFile);

                while (scanner.hasNextLine()) {

                    String line = scanner.nextLine();

                    if (line.startsWith(fileName + ":")) {

                        storedHash =
                                line.substring(fileName.length() + 1);

                        break;
                    }
                }

                scanner.close();
            }

            System.out.println();
            System.out.println("-------- FILE INTEGRITY --------");
            System.out.println("File: " + fileName);
            System.out.println("SHA-256 Hash:");
            System.out.println(currentHash);

            if (storedHash == null) {

                FileWriter writer =
                        new FileWriter(INTEGRITY_FILE, true);

                writer.write(fileName + ":" + currentHash + "\n");

                writer.close();

                System.out.println("Integrity status: BASELINE CREATED");

            } else if (storedHash.equals(currentHash)) {

                System.out.println("Integrity status: VERIFIED");
                System.out.println("File has not been modified.");

            } else {

                System.out.println("Integrity status: WARNING");
                System.out.println("File has been modified!");

                logActivity(
                        "INTEGRITY WARNING: File modified - "
                                + fileName
                );
            }

            logActivity(
                    "Integrity check performed: "
                            + fileName
            );

        } catch (SecurityException e) {

            System.out.println("Invalid file path.");

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