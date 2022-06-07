package cz.fi.muni.pa165.airport_manager.forms;

import cz.fi.muni.pa165.airport_manager.dto.DatesDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Kamil Fňukal
 * Class used for additional validation in case of creating/updating a flight.
 */
public class DatesDTOValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return DatesDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var flightCreateDto = (DatesDto) target;

        var departureTime = flightCreateDto.getDepartureTime();
        var arrivalTime = flightCreateDto.getArrivalTime();
        if (arrivalTime != null && departureTime != null && !arrivalTime.isAfter(departureTime)) {
            errors.rejectValue("arrivalTime", "arrivalBeforeDeparture", "Arrival time must be after departure time");
        }
    }
}
