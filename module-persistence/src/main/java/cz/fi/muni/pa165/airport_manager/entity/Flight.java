package cz.fi.muni.pa165.airport_manager.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kamil Fnukal
 * An entity which is used for managing Flight.
 * Manages departureTime, arrivalTime, destination, origin, airplane and stewards fields
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;

    @NotNull
    @ManyToOne
    private Airport destination;

    @NotNull
    @ManyToOne
    private Airport origin;

    @NotNull
    @ManyToOne
    private Airplane airplane;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Steward> stewards = new HashSet<>();

    public void addSteward(Steward steward) {
        stewards.add(steward);
        steward.addFlight(this);
    }

    public void removeSteward(Steward steward) {
        stewards.remove(steward);
        steward.removeFlight(this);
    }

    public Flight(LocalDateTime departureTime, LocalDateTime arrivalTime, Airport origin, Airport destination, Airplane airplane) {
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.origin = origin;
        this.destination = destination;
        this.airplane = airplane;
    }

    @Override
    public String toString() {
        return "Flight{" +
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
        if (!(o instanceof Flight)) return false;

        Flight flight = (Flight) o;

        if (!getDepartureTime().equals(flight.getDepartureTime())) return false;
        if (!getArrivalTime().equals(flight.getArrivalTime())) return false;
        if (!getDestination().equals(flight.getDestination())) return false;
        if (!getOrigin().equals(flight.getOrigin())) return false;
        return getAirplane().equals(flight.getAirplane());
    }

    @Override
    public int hashCode() {
        int result = getDepartureTime().hashCode();
        result = 31 * result + getArrivalTime().hashCode();
        result = 31 * result + getDestination().hashCode();
        result = 31 * result + getOrigin().hashCode();
        result = 31 * result + getAirplane().hashCode();
        return result;
    }

}
