package cz.fi.muni.pa165.airport_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Kamil Fňukal
 * This class represents DTO used for BaseUser entity.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DatesDto {
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureTime;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalTime;


    @Override
    public String toString() {
        return "FlightDto{" +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatesDto)) return false;

        DatesDto datesDto = (DatesDto) o;

        if (getDepartureTime() != null ? !getDepartureTime().equals(datesDto.getDepartureTime()) : datesDto.getDepartureTime() != null)
            return false;
        return getArrivalTime() != null ? getArrivalTime().equals(datesDto.getArrivalTime()) : datesDto.getArrivalTime() == null;
    }

    @Override
    public int hashCode() {
        int result = getDepartureTime() != null ? getDepartureTime().hashCode() : 0;
        result = 31 * result + (getArrivalTime() != null ? getArrivalTime().hashCode() : 0);
        return result;
    }
}
