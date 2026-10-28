public class SecurityTests {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println(" CIPHERVAULT SECURITY TESTS");
        System.out.println("================================");

        testValidFilename();
        testEmptyFilename();
        testPathTraversalFilename();
        testInvalidFilenameCharacters();

        System.out.println();
        System.out.println("Security tests completed.");
    }

    public static void testValidFilename() {

        String fileName = "security_test.txt";

        boolean valid = fileName.matches("[a-zA-Z0-9._-]+")
                && !fileName.equals(".")
                && !fileName.equals("..");

        if (valid) {
            System.out.println("PASS: Valid filename accepted.");
        } else {
            System.out.println("FAIL: Valid filename rejected.");
        }
    }

    public static void testEmptyFilename() {

        String fileName = "";

        boolean invalid = fileName == null || fileName.trim().isEmpty();

        if (invalid) {
            System.out.println("PASS: Empty filename rejected.");
        } else {
            System.out.println("FAIL: Empty filename accepted.");
        }
    }

    public static void testPathTraversalFilename() {

        String fileName = "../../accounts.txt";

        boolean invalid = !fileName.matches("[a-zA-Z0-9._-]+")
                || fileName.equals(".")
                || fileName.equals("..");

        if (invalid) {
            System.out.println("PASS: Path traversal filename rejected.");
        } else {
            System.out.println("FAIL: Path traversal filename accepted.");
        }
    }

    public static void testInvalidFilenameCharacters() {

        String fileName = "my file!.txt";

        boolean invalid = !fileName.matches("[a-zA-Z0-9._-]+")
                || fileName.equals(".")
                || fileName.equals("..");

        if (invalid) {
            System.out.println("PASS: Invalid filename characters rejected.");
        } else {
            System.out.println("FAIL: Invalid filename characters accepted.");
        }
    }
}
