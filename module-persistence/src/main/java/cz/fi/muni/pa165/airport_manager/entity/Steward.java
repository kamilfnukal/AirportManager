package cz.fi.muni.pa165.airport_manager.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kamil Fnukal
 * An entity which is used for managing Steward.
 * Manages id, citizenId, firstName, lastName and flights fields
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Steward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String citizenId;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @ManyToMany(mappedBy = "stewards", fetch = FetchType.EAGER)
    private Set<Flight> flights = new HashSet<>();

    protected void addFlight(Flight flight) {
        flights.add(flight);
    }

    protected void removeFlight(Flight flight) {
        flights.remove(flight);
    }

    public Steward(String firstName, String lastName, String citizenId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.citizenId = citizenId;
    }

    @Override
    public String toString() {
        return "Steward{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Steward)) return false;

        Steward steward = (Steward) o;

        if (!getCitizenId().equals(steward.getCitizenId())) return false;
        if (!getFirstName().equals(steward.getFirstName())) return false;
        return getLastName().equals(steward.getLastName());
    }

    @Override
    public int hashCode() {
        int result = getCitizenId().hashCode();
        result = 31 * result + getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        return result;
    }

}
