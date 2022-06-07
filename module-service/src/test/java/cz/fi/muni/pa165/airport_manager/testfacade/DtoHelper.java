package cz.fi.muni.pa165.airport_manager.testfacade;

import cz.fi.muni.pa165.airport_manager.dto.AdminDto;
import cz.fi.muni.pa165.airport_manager.dto.FlightCreateDto;
import cz.fi.muni.pa165.airport_manager.dto.AirplaneDto;
import cz.fi.muni.pa165.airport_manager.dto.AirportDto;
import cz.fi.muni.pa165.airport_manager.dto.StewardDto;
import cz.fi.muni.pa165.airport_manager.dto.FlightDto;
import cz.fi.muni.pa165.airport_manager.dto.ManagerDto;
import cz.fi.muni.pa165.airport_manager.entity.Admin;
import cz.fi.muni.pa165.airport_manager.entity.Manager;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Steward;
import cz.fi.muni.pa165.airport_manager.entity.Flight;

import java.util.stream.Collectors;

/**
 * @author Kamil Fňukal
 * DtoHelper is helper class for transforming Entities to their DTOs
 */
public class DtoHelper {
    /**
     * Transform admin entity to dto
     * @param admin admin entity
     * @return admin dto
     */
    public static AdminDto createDto(Admin admin) {
        var dto = new AdminDto();
        dto.setId(admin.getId());
        dto.setPassword(admin.getPassword());
        dto.setEmail(admin.getEmail());
        dto.setFirstName(admin.getFirstName());
        dto.setLastName(admin.getLastName());
        dto.setPhone(admin.getPhone());
        dto.setCreatedAt(admin.getCreatedAt());
        dto.setUpdatedAt(admin.getUpdatedAt());
        dto.setAirports(admin.getAirports().stream().map(DtoHelper::createDto).collect(Collectors.toSet()));
        dto.setAirplanes(admin.getAirplanes().stream().map(DtoHelper::createDto).collect(Collectors.toSet()));
        dto.setStewards(admin.getStewards().stream().map(DtoHelper::createDto).collect(Collectors.toSet()));
        return dto;
    }

    /**
     * Transform airplane entity to dto
     * @param airplane airplane entity
     * @return airplane dto
     */
    public static AirplaneDto createDto(Airplane airplane) {
        var dto = new AirplaneDto();
        dto.setId(airplane.getId());
        dto.setSerialNumber(airplane.getSerialNumber());
        dto.setManufacturer(airplane.getManufacturer());
        dto.setCapacity(dto.getCapacity());
        dto.setModel(dto.getModel());
        return dto;
    }

    /**
     * Transform airport entity to dto
     * @param airport airport entity
     * @return airport dto
     */
    public static AirportDto createDto(Airport airport) {
        var dto = new AirportDto();
        dto.setId(airport.getId());
        dto.setCountry(airport.getCountry());
        dto.setCity(airport.getCity());
        dto.setName(airport.getName());
        dto.setCode(airport.getCode());
        return dto;
    }

    /**
     * Transform flight entity to dto
     * @param flight flight entity
     * @return flight dto
     */
    public static FlightDto createDto(Flight flight) {
        var dto = new FlightDto();
        dto.setId(flight.getId());
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());
        dto.setAirplane(createDto(flight.getAirplane()));
        dto.setOrigin(createDto(flight.getOrigin()));
        dto.setDestination(createDto(flight.getDestination()));
        dto.setStewards(flight.getStewards().stream().map(DtoHelper::createDto).collect(Collectors.toSet()));
        return dto;
    }

    /**
     * Transform flight entity to create dto
     * @param flight flight entity
     * @return flight createDto
     */
    public static FlightCreateDto createCreateDto(Flight flight) {
        var dto = new FlightCreateDto();
        dto.setId(flight.getId());
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());
        dto.setAirplaneId(flight.getAirplane().getId());
        dto.setOriginId(flight.getOrigin().getId());
        dto.setDestinationId(flight.getDestination().getId());
        dto.setStewardIds(flight.getStewards().stream().map(Steward::getId).collect(Collectors.toSet()));
        return dto;
    }

    /**
     * Transform manager entity to dto
     * @param manager manager entity
     * @return manager dto
     */
    public static ManagerDto createDto(Manager manager) {
        var dto = new ManagerDto();
        dto.setId(manager.getId());
        dto.setPassword(manager.getPassword());
        dto.setEmail(manager.getEmail());
        dto.setFirstName(manager.getFirstName());
        dto.setLastName(manager.getLastName());
        dto.setPhone(manager.getPhone());
        dto.setCreatedAt(manager.getCreatedAt());
        dto.setUpdatedAt(manager.getUpdatedAt());
        dto.setFlights(manager.getFlights().stream().map(DtoHelper::createDto).collect(Collectors.toSet()));
        return dto;
    }

    /**
     * Transform steward entity to dto
     * @param steward steward entity
     * @return steward dto
     */
    public static StewardDto createDto(Steward steward) {
        var dto = new StewardDto();
        dto.setId(steward.getId());
        dto.setCitizenId(steward.getCitizenId());
        dto.setFirstName(steward.getFirstName());
        dto.setLastName(steward.getLastName());
        return dto;
    }
}
