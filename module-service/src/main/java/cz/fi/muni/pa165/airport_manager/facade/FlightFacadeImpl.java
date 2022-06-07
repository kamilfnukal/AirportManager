package cz.fi.muni.pa165.airport_manager.facade;

import cz.fi.muni.pa165.airport_manager.dto.AirplaneDto;
import cz.fi.muni.pa165.airport_manager.dto.AirportDto;
import cz.fi.muni.pa165.airport_manager.dto.FlightCreateDto;
import cz.fi.muni.pa165.airport_manager.dto.FlightDto;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * @author Martin Kalina
 * This class represents implementation of FlightFacade
 */

@Service
public class FlightFacadeImpl implements FlightFacade {

    @Autowired
    private FlightService flightService;

    @Autowired
    private AirplaneService airplaneService;

    @Autowired
    private AirportService airportService;

    @Autowired
    private StewardService stewardService;

    @Autowired
    private BeanMappingService beanMappingService;

    @Override
    public void createFlight(FlightCreateDto flight, String email) {
        if (flight == null)
            throw new IllegalArgumentException("Flight is null");
        if(email == null)
            throw new IllegalArgumentException("Email is null");
        var mappedFlight = beanMappingService.mapTo(flight, Flight.class);
        mapEntitiesToFlight(flight, mappedFlight);

        flightService.createFlight(mappedFlight, email);
    }

    @Override
    public List<FlightDto> findAllFlights() {
        var flights = beanMappingService.mapTo(flightService.findAllFlights(), FlightDto.class);
        flights.sort(Comparator.comparing(FlightDto::getDepartureTime));
        return flights;
    }

    @Override
    public FlightDto findFlightById(Long id) {
        if (id == null) throw new IllegalArgumentException("Id is null");

        var flight = flightService.findFlightById(id);
        return flight == null ? null : beanMappingService.mapTo(flight, FlightDto.class);
    }

    @Override
    public void updateFlight(FlightCreateDto flight) {
        if (flight == null)
            throw new IllegalArgumentException("Flight is null");
        var mappedFlight = beanMappingService.mapTo(flight, Flight.class);
        mapEntitiesToFlight(flight, mappedFlight);

        flightService.updateFlight(mappedFlight);
    }

    @Override
    public void removeFlight(FlightDto flight) {
        if (flight == null)
            throw new IllegalArgumentException("Flight is null");
        var mappedFlight = beanMappingService.mapTo(flight, Flight.class);
        flightService.removeFlight(mappedFlight);
    }

    @Override
    public List<FlightDto> getDepartureFlights(AirportDto origin) {
        if (origin == null)
            throw new IllegalArgumentException("Origin is null");
        var mappedOrigin = beanMappingService.mapTo(origin, Airport.class);
        return beanMappingService.mapTo(flightService.getDepartureFlights(mappedOrigin), FlightDto.class);
    }

    @Override
    public List<FlightDto> getArrivalFlights(AirportDto destination) {
        if (destination == null)
            throw new IllegalArgumentException("Destination is null");
        var mappedDestination = beanMappingService.mapTo(destination, Airport.class);
        return beanMappingService.mapTo(flightService.getArrivalFlights(mappedDestination), FlightDto.class);
    }

    @Override
    public List<FlightDto> getFlightsByAirplane(AirplaneDto airplane) {
        if (airplane == null)
            throw new IllegalArgumentException("Airplane is null");
        var mappedAirplane = beanMappingService.mapTo(airplane, Airplane.class);
        return beanMappingService.mapTo(flightService.getFlightsByAirplane(mappedAirplane), FlightDto.class);
    }

    private void mapEntitiesToFlight(FlightCreateDto flightDto, Flight mappedFlight) {
        mappedFlight.setOrigin(airportService.findAirportById(flightDto.getOriginId()));
        mappedFlight.setDestination(airportService.findAirportById(flightDto.getDestinationId()));
        mappedFlight.setAirplane(airplaneService.findAirplaneById(flightDto.getAirplaneId()));
        flightDto.getStewardIds().forEach(id -> mappedFlight.addSteward(stewardService.findStewardById(id)));
    }
}
