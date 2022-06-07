package cz.fi.muni.pa165.airport_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Matej Michálek
 * This class represents DTO used for Flight entity when creating/editting a flight.
 */


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightCreateDto {
    private Long id;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalTime;
    @NotNull
    @Positive(message = "Origin must be selected.")
    private Long originId;
    @NotNull
    @Positive(message = "Destination must be selected.")
    private Long destinationId;
    @NotNull
    @Positive(message = "Airplane must be selected.")
    private Long airplaneId;
    private Set<Long> stewardIds = new HashSet<>();

    @Override
    public String toString() {
        return "FlightCreateDto{" +
                "id=" + id +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", destinationId=" + destinationId +
                ", originId=" + originId +
                ", airplaneId=" + airplaneId +
                ", stewardIds=" + stewardIds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlightCreateDto)) return false;

        FlightCreateDto that = (FlightCreateDto) o;

        if (getDepartureTime() != null ? !getDepartureTime().equals(that.getDepartureTime()) : that.getDepartureTime() != null)
            return false;
        if (getArrivalTime() != null ? !getArrivalTime().equals(that.getArrivalTime()) : that.getArrivalTime() != null)
            return false;
        if (getDestinationId() != null ? !getDestinationId().equals(that.getDestinationId()) : that.getDestinationId() != null)
            return false;
        if (getOriginId() != null ? !getOriginId().equals(that.getOriginId()) : that.getOriginId() != null)
            return false;
        return getAirplaneId() != null ? getAirplaneId().equals(that.getAirplaneId()) : that.getAirplaneId() == null;
    }

    @Override
    public int hashCode() {
        int result = getDepartureTime() != null ? getDepartureTime().hashCode() : 0;
        result = 31 * result + (getArrivalTime() != null ? getArrivalTime().hashCode() : 0);
        result = 31 * result + (getDestinationId() != null ? getDestinationId().hashCode() : 0);
        result = 31 * result + (getOriginId() != null ? getOriginId().hashCode() : 0);
        result = 31 * result + (getAirplaneId() != null ? getAirplaneId().hashCode() : 0);
        return result;
    }

}
