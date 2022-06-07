package cz.fi.muni.pa165.airport_manager.exception;

/**
 * @author Matej Michálek
 * This class represents a wrapper for exceptions from services
 */

public class AirportManagerServiceException extends RuntimeException {
    public AirportManagerServiceException() {
        super();
    }

    public AirportManagerServiceException(String message) {
        super(message);
    }

    public AirportManagerServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public AirportManagerServiceException(Throwable cause) {
        super(cause);
    }

    protected AirportManagerServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
