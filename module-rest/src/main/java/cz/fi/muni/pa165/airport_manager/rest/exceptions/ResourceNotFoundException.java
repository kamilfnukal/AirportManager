package cz.fi.muni.pa165.airport_manager.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Martin Kalina
 * Exception used when specified steward was not found in DB
 */

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="The requested steward was not found")
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super(HttpStatus.NOT_FOUND.getReasonPhrase());
    }
}