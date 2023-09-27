package POS;

/**
 *
 * @author qihong
 */
public class QuantityMoreThanStockException extends RuntimeException {

    public QuantityMoreThanStockException() {
    }

    public QuantityMoreThanStockException(String message) {
        super(message);
    }

    public QuantityMoreThanStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuantityMoreThanStockException(Throwable cause) {
        super(cause);
    }

    public QuantityMoreThanStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
