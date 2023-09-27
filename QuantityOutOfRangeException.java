package POS;

/**
 *
 * @author qihong
 */
public class QuantityOutOfRangeException extends IllegalArgumentException {

    public QuantityOutOfRangeException() {
    }

    public QuantityOutOfRangeException(String s) {
        super(s);
    }

    public QuantityOutOfRangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuantityOutOfRangeException(Throwable cause) {
        super(cause);
    }
}
