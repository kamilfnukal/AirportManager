package cz.fi.muni.pa165.airport_manager.exception;

import org.springframework.dao.EmptyResultDataAccessException;

/**
 * @author Matej Michálek
 * Exception used when object is not found in Database.
 */

public class ObjectNotFoundException extends EmptyResultDataAccessException {

    public ObjectNotFoundException(Class clazz, Long id) {
        super(clazz.getSimpleName() + " (id:" + id + ") was not found.", 1);
    }
}
