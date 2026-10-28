public class AppConfig {

    private AppConfig() {
        // Prevent creating objects of this configuration class.
    }

    public static final String LOCKOUT_FILE = "lockouts.txt";

    public static final int MAX_LOGIN_ATTEMPTS = 3;

    public static final long LOCKOUT_DURATION_MILLIS = 5L * 60 * 1000;
}
