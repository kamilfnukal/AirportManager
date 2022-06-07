package cz.fi.muni.pa165.airport_manager.testservice;

import cz.fi.muni.pa165.airport_manager.config.ServiceConfiguration;
import cz.fi.muni.pa165.airport_manager.dto.FlightDto;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.entity.Steward;
import cz.fi.muni.pa165.airport_manager.enums.AirplaneManufacturer;
import cz.fi.muni.pa165.airport_manager.service.BeanMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for BeanMappingService
 */
@ContextConfiguration(classes= ServiceConfiguration.class)
public class BeanMappingServiceTests extends AbstractTestNGSpringContextTests {
    @Autowired
    BeanMappingService beanMappingService;
    private final List<Flight> flights = new ArrayList<>();

    @BeforeMethod
    public void prepareFlights() {
        var airportA = new Airport("Austria", "Vienna", "Vienna International Airport", "VIE");
        var airportB = new Airport("France", "Paris", "Paris Charles de Gaulle Airport", "CDG");
        var bombardier = new Airplane(AirplaneManufacturer.BOMBARDIER, 104, "CRJ1000", "2");
        var stewardJohn = new Steward("John", "Stone", "1");
        var stewardSteve = new Steward("Steve", "Wonder", "2");

        var flightA = new Flight(
                LocalDateTime.of(2022, 7, 15, 15, 30),
                LocalDateTime.of(2022, 7, 15, 17, 30),
                airportA,
                airportB,
                bombardier
        );
        flightA.setId(1L);
        flightA.addSteward(stewardJohn);
        flightA.addSteward(stewardSteve);

        var flightB = new Flight(
                LocalDateTime.of(2022, 7, 21, 20, 30),
                LocalDateTime.of(2022, 7, 22, 10, 30),
                airportB,
                airportA,
                bombardier
        );
        flightB.setId(2L);

        flights.add(flightA);
        flights.add(flightB);
    }

    @Test
    public void testMapList() {
        var flightDtos = beanMappingService.mapTo(flights, FlightDto.class);
        Assert.assertNotNull(flightDtos);
        Assert.assertEquals(flightDtos.size(), flights.size());
    }

    @Test
    public void testMapFlightWithStewards() {
        var flightDto = beanMappingService.mapTo(flights.get(0), FlightDto.class);
        Assert.assertNotNull(flightDto);
        Assert.assertEquals(flightDto.getStewards().size(), 2);
    }
}
