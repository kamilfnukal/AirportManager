package cz.fi.muni.pa165.airport_manager.dto;

import cz.fi.muni.pa165.airport_manager.enums.AirplaneManufacturer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

/**
 * @author Pavel Sklenar
 * This class represents DTO used for Airplane entity.
 */

@Getter
@Setter
public class AirplaneDto {

    private Long id;

    @NotBlank
    @Pattern.List({
            @Pattern(regexp = "(?=.*[\\da-zA-Z]).+", message = "Serial number must contain at least one digit or letter."),
            @Pattern(regexp = "^[\\da-zA-Z.\\-/_ ]*$", message = "Serial number must contain only letters, digits and the following characters: '.' '-' '/' '_'"),
    })
    private String serialNumber;

    @NotNull(message = "A manufacturer must be selected.")
    @Enumerated(EnumType.STRING)
    private AirplaneManufacturer manufacturer;

    @Positive
    @NotNull
    private Integer capacity;

    @NotBlank
    @Pattern.List({
            @Pattern(regexp = "(?=.*[\\da-zA-Z]).+", message = "Model must contain at least one digit or letter."),
            @Pattern(regexp = "^[\\da-zA-Z.\\-/_ ]*$", message = "Model must contain only letters, digits and the following characters: '.' '-' '/' '_'"),
    })
    private String model;

    @Override
    public String toString() {
        return "Airplane{" +
                "id=" + id +
                ", serialNumber='" + serialNumber + '\'' +
                ", manufacturer=" + manufacturer +
                ", capacity=" + capacity +
                ", model='" + model + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AirplaneDto)) return false;

        AirplaneDto that = (AirplaneDto) o;

        if (getCapacity() != that.getCapacity()) return false;
        if (getSerialNumber() != null ? !getSerialNumber().equals(that.getSerialNumber()) : that.getSerialNumber() != null)
            return false;
        if (getManufacturer() != that.getManufacturer()) return false;
        return getModel() != null ? getModel().equals(that.getModel()) : that.getModel() == null;
    }

    @Override
    public int hashCode() {
        int result = getSerialNumber() != null ? getSerialNumber().hashCode() : 0;
        result = 31 * result + (getManufacturer() != null ? getManufacturer().hashCode() : 0);
        result = 31 * result + getCapacity();
        result = 31 * result + (getModel() != null ? getModel().hashCode() : 0);
        return result;
    }

}
