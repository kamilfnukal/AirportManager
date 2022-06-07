package cz.fi.muni.pa165.airport_manager.entity;

import cz.fi.muni.pa165.airport_manager.enums.AirplaneManufacturer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author Matej Michálek
 * An entity which is used for managing an Airplane.
 * Manages id, serialNumber, manufacturer, capacity, model fields
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Airplane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String serialNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AirplaneManufacturer manufacturer;

    private int capacity;

    @NotNull
    private String model;

    public Airplane(AirplaneManufacturer manufacturer, int capacity, String model, String serialNumber) {
        this.manufacturer = manufacturer;
        this.capacity = capacity;
        this.model = model;
        this.serialNumber = serialNumber;
    }

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
        if (!(o instanceof Airplane)) return false;

        Airplane airplane = (Airplane) o;

        if (getCapacity() != airplane.getCapacity()) return false;
        if (!getSerialNumber().equals(airplane.getSerialNumber())) return false;
        if (!this.getManufacturer().equals(airplane.getManufacturer())) return false;
        return this.getModel().equals(airplane.getModel());
    }

    @Override
    public int hashCode() {
        int result = getSerialNumber().hashCode();
        result = 31 * result + this.getManufacturer().hashCode();
        result = 31 * result + getCapacity();
        result = 31 * result + this.getModel().hashCode();
        return result;
    }

}
