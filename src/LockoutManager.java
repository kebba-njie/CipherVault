import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class LockoutManager {

    private static final String LOCKOUT_FILE = "lockouts.txt";

    private static final int MAX_ATTEMPTS = 3;

    private static final long LOCKOUT_TIME = 5 * 60 * 1000;

    public static boolean isLocked(String username) {

        File file = new File(LOCKOUT_FILE);

        if (!file.exists()) {
            return false;
        }

        try {

            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();

                String[] data = line.split(":");

                if (data.length != 2) {
                    continue;
                }

                String storedUsername = data[0];

                long lockoutTime =
                        Long.parseLong(data[1]);

                if (username.equals(storedUsername)) {

                    long currentTime =
                            System.currentTimeMillis();

                    if (currentTime < lockoutTime) {

                        scanner.close();

                        return true;
                    }
                }
            }

            scanner.close();

        } catch (Exception e) {

            System.out.println(
                    "Error checking account lockout."
            );
        }

        return false;
    }

    public static void recordFailedAttempt(
            String username,
            int attempts) {

        if (attempts < MAX_ATTEMPTS) {
            return;
        }

        long lockoutUntil =
                System.currentTimeMillis()
                        + LOCKOUT_TIME;

        try {

            FileWriter writer =
                    new FileWriter(
                            LOCKOUT_FILE,
                            true
                    );

            writer.write(
                    username
                            + ":"
                            + lockoutUntil
                            + "\n"
            );

            writer.close();

        } catch (Exception e) {

            System.out.println(
                    "Error recording account lockout."
            );
        }
    }

    public static int getMaxAttempts() {

        return MAX_ATTEMPTS;
    }
}

