/**
 * FileInvalidException class.
 */
public class FileInvalidException extends Exception {

    /**
     * Default Constructor.
     */
    public FileInvalidException() {
        super("Error: Input file cannot be parsed due to missing information\n" + "(i.e. month={}, title={}, etc.)");
    }

    /**
     * Parametrized Constructor.
     * @param message Error Message.
     */
    public FileInvalidException(String message) {
        super(message);
    }
}
