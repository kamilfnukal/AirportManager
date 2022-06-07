package cz.fi.muni.pa165.airport_manager.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Martin Kalina
 * Exception used when steward with specified citizenID already exists
 */


@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason="Steward with the specified citizenID already exists")
public class ResourceAlreadyExistingException extends RuntimeException {

}