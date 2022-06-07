package cz.fi.muni.pa165.airport_manager.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 * @author Martin Kalina
 * An entity which is used for managing an Airport.
 * Manages id, country, city, name and code fields
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String country;

    @NotNull
    private String city;

    @NotNull
    private String name;

    @NotNull
    @Column(unique = true)
    private String code;

    public Airport(String country, String city, String name, String code) {
        this.country = country;
        this.city = city;
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Airport)) return false;

        Airport airport = (Airport) o;

        if (getCountry() != null ? !getCountry().equals(airport.getCountry()) : airport.getCountry() != null)
            return false;
        if (getCity() != null ? !getCity().equals(airport.getCity()) : airport.getCity() != null) return false;
        if (getName() != null ? !getName().equals(airport.getName()) : airport.getName() != null) return false;
        return getCode() != null ? getCode().equals(airport.getCode()) : airport.getCode() == null;
    }

    @Override
    public int hashCode() {
        int result = getCountry() != null ? getCountry().hashCode() : 0;
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
        return result;
    }

}
