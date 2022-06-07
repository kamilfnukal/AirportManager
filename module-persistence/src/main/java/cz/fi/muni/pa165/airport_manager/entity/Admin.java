package cz.fi.muni.pa165.airport_manager.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Pavel Sklenar
 * An entity which is used for Admin role.
 * Manages airports field
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Admin extends BaseUser {

    public Admin(String firstName, String lastName, String password, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Airport> airports = new HashSet<>();

    public void addAirport(Airport airport){
        airports.add(airport);
    }

    public void removeAirport(Airport airport){
        airports.remove(airport);
    }

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Airplane> airplanes = new HashSet<>();

    public void addAirplane(Airplane airplane){
        airplanes.add(airplane);
    }

    public void removeAirplane(Airplane airplane){
        airplanes.remove(airplane);
    }

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Steward> stewards = new HashSet<>();

    public void addSteward(Steward steward){
        stewards.add(steward);
    }

    public void removeSteward(Steward steward){
        stewards.remove(steward);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", airports=" + airports +
                ", airplanes=" + airplanes +
                ", stewards=" + stewards +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Admin)) return false;

        Admin that = (Admin) o;

        if (!getFirstName().equals(that.getFirstName())) return false;
        if (!getLastName().equals(that.getLastName())) return false;
        if (!getPassword().equals(that.getPassword())) return false;
        if (!getEmail().equals(that.getEmail())) return false;
        if (getPhone() != null ? !getPhone().equals(that.getPhone()) : that.getPhone() != null) return false;
        if (getCreatedAt() != null ? !getCreatedAt().equals(that.getCreatedAt()) : that.getCreatedAt() != null)
            return false;
        return getUpdatedAt() != null ? getUpdatedAt().equals(that.getUpdatedAt()) : that.getUpdatedAt() == null;
    }

    @Override
    public int hashCode() {
        int result = getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getPassword().hashCode();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        result = 31 * result + (getUpdatedAt() != null ? getUpdatedAt().hashCode() : 0);
        return result;
    }

}