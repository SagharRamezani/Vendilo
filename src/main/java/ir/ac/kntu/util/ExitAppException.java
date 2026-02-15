package ir.ac.kntu.util;

/**
 * Typed exception used to exit the application loop.
 */
public class ExitAppException extends Exception {
    public ExitAppException() {
        super("EXIT");
    }
}
