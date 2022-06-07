package cz.fi.muni.pa165.airport_manager.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Matej Michalek
 * This class represents DTO used for Steward entity.
 */

@Getter
@Setter
public class StewardDto {

    private Long id;

    @NotBlank
    @Pattern.List({
        @Pattern(regexp = "(?=.*[\\da-zA-Z]).+", message = "Citizen ID must contain at least one digit or letter."),
        @Pattern(regexp = "^[\\da-zA-Z.\\-/ ]*$", message = "Citizen ID must contain only letters, digits and the following characters: '.' '-' '/'"),
    })
    private String citizenId;

    @Size(min=3, max=25)
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z ]*$",
            message = "Firstname must contain only letters.")
    private String firstName;

    @Size(min=3, max=25)
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z ]*$",
            message = "Lastname must contain only letters.")
    private String lastName;

    @Override
    public String toString() {
        return "StewardDto{" +
                "id=" + id +
                ", citizenId='" + citizenId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StewardDto)) return false;

        StewardDto that = (StewardDto) o;

        if (getCitizenId() != null ? !getCitizenId().equals(that.getCitizenId()) : that.getCitizenId() != null)
            return false;
        if (getFirstName() != null ? !getFirstName().equals(that.getFirstName()) : that.getFirstName() != null)
            return false;
        return getLastName() != null ? getLastName().equals(that.getLastName()) : that.getLastName() == null;
    }

    @Override
    public int hashCode() {
        int result = getCitizenId() != null ? getCitizenId().hashCode() : 0;
        result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        return result;
    }
}
