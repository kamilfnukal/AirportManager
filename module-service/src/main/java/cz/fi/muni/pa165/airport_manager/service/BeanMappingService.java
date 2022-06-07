package cz.fi.muni.pa165.airport_manager.service;

import org.dozer.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * Interface with necessary methods for mapping between classes (e.g. Dto to Entity and vice-versa)
 * @author Matej Michálek
 */

public interface BeanMappingService {
    <T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass);

    <T> T mapTo(Object u, Class<T> mapToClass);

    Mapper getMapper();
}
