package cz.fi.muni.pa165.airport_manager.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Pavel Sklenar
 * This class represents DTO used for Airport entity.
 */

@Getter
@Setter
public class AirportDto {

    private Long id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z.,\\-/& ]*$",
            message = "Country must contain only letters and the following characters: '.' ',' '-' '/' '&'")
    private String country;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z.,\\-/& ]*$",
            message = "City must contain only letters and the following characters: '.' ',' '-' '/' '&'")
    private String city;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z.,\\-/& ]*$",
            message = "Airport name must contain only letters and the following characters: '.' ',' '-' '/' '&'")
    private String name;

    /**
     * IATA airport code that uniquely identifies an airport
     */
    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$",
            message = "Wrong IATA airport code. It must be exactly three-letter identifier. For example: LHR, PRG, BOS")
    private String code;

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
        if (!(o instanceof AirportDto)) return false;

        AirportDto that = (AirportDto) o;

        if (getCountry() != null ? !getCountry().equals(that.getCountry()) : that.getCountry() != null) return false;
        if (getCity() != null ? !getCity().equals(that.getCity()) : that.getCity() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        return getCode() != null ? getCode().equals(that.getCode()) : that.getCode() == null;
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
