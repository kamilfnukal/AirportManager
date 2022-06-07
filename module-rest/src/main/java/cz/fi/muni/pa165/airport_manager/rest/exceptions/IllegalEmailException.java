package cz.fi.muni.pa165.airport_manager.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Martin Kalina
 * Exception used when specified user with specified email is not in DB
 */


@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="User with email userEmail doesn't exist")
public class IllegalEmailException extends RuntimeException {
}
