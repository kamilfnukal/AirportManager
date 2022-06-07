package cz.fi.muni.pa165.airport_manager.dto;

import lombok.Getter;
import lombok.Setter;


import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Kalina
 * This class represents DTO used for Flight entity.
 */


@Getter
@Setter
public class FlightDto {
    private Long id;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureTime;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalTime;
    @NotNull
    private AirportDto destination;
    @NotNull
    private AirportDto origin;
    @NotNull
    private AirplaneDto airplane;
    private Set<StewardDto> stewards = new HashSet<>();

    @Override
    public String toString() {
        return "FlightDto{" +
                "id=" + id +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", destination=" + destination +
                ", origin=" + origin +
                ", airplane=" + airplane +
                ", stewards=" + stewards +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlightDto)) return false;

        FlightDto flightDto = (FlightDto) o;

        if (getDepartureTime() != null ? !getDepartureTime().equals(flightDto.getDepartureTime()) : flightDto.getDepartureTime() != null)
            return false;
        if (getArrivalTime() != null ? !getArrivalTime().equals(flightDto.getArrivalTime()) : flightDto.getArrivalTime() != null)
            return false;
        if (getDestination() != null ? !getDestination().equals(flightDto.getDestination()) : flightDto.getDestination() != null)
            return false;
        if (getOrigin() != null ? !getOrigin().equals(flightDto.getOrigin()) : flightDto.getOrigin() != null)
            return false;
        return getAirplane() != null ? getAirplane().equals(flightDto.getAirplane()) : flightDto.getAirplane() == null;
    }

    @Override
    public int hashCode() {
        int result = getDepartureTime() != null ? getDepartureTime().hashCode() : 0;
        result = 31 * result + (getArrivalTime() != null ? getArrivalTime().hashCode() : 0);
        result = 31 * result + (getDestination() != null ? getDestination().hashCode() : 0);
        result = 31 * result + (getOrigin() != null ? getOrigin().hashCode() : 0);
        result = 31 * result + (getAirplane() != null ? getAirplane().hashCode() : 0);
        return result;
    }
}
