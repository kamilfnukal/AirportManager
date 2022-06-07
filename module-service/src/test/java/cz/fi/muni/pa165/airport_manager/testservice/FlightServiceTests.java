package cz.fi.muni.pa165.airport_manager.testservice;

import cz.fi.muni.pa165.airport_manager.config.ServiceConfiguration;
import cz.fi.muni.pa165.airport_manager.dao.FlightDao;
import cz.fi.muni.pa165.airport_manager.dao.ManagerDao;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.entity.Manager;
import cz.fi.muni.pa165.airport_manager.enums.AirplaneManufacturer;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.service.FlightService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.times;

/**
 * @author Martin Kalina
 * FlightServiceTests class is used to verify correct functionality of Flight service.
 */

@ContextConfiguration(classes= ServiceConfiguration.class)
public class FlightServiceTests extends AbstractTransactionalTestNGSpringContextTests {

    @Mock
    private FlightDao flightDao;
    @Mock
    private ManagerDao managerDao;

    @Autowired
    @InjectMocks
    private FlightService flightService;

    private Flight flightA;
    private Flight flightB;
    private Flight flightC;
    private Flight flightD;
    private Flight flightE;
    private Airplane bombardier;
    private Airplane embraer;
    private Airport airportA;
    private Airport airportB;
    private Airport airportC;
    private LocalDateTime periodFrom;
    private LocalDateTime periodTo;
    private Manager managerTom;
    private Manager managerBob;

    @BeforeMethod
    public void prepareFlights() {
        MockitoAnnotations.openMocks(this);
        bombardier = new Airplane(AirplaneManufacturer.BOMBARDIER, 104, "CRJ1000", "2");
        embraer = new Airplane(AirplaneManufacturer.EMBRAER, 50, "145", "3");
        airportA = new Airport("Austria", "Vienna", "Vienna International Airport", "VIE");
        airportB = new Airport("France", "Paris", "Paris Charles de Gaulle Airport", "CDG");
        airportC = new Airport("Czech republic", "Prague", "Václav Havel Airport Prague", "PRG");

        flightA = new Flight(
                LocalDateTime.of(2022, 7, 15, 15, 30),
                LocalDateTime.of(2022, 7, 15, 17, 30),
                airportA,
                airportB,
                bombardier
        );
        flightA.setId(1L);
        flightB = new Flight(
                LocalDateTime.of(2022, 7, 21, 20, 30),
                LocalDateTime.of(2022, 7, 22, 10, 30),
                airportB,
                airportA,
                bombardier
        );
        flightB.setId(2L);

        flightC = new Flight(
                LocalDateTime.of(2022, 8, 14, 15, 30),
                LocalDateTime.of(2022, 8, 15, 22, 30),
                airportA,
                airportB,
                bombardier
        );
        flightC.setId(3L);

        flightD = new Flight(
                LocalDateTime.of(2022, 9, 12, 18, 30),
                LocalDateTime.of(2022, 9, 13, 22, 30),
                airportA,
                airportB,
                bombardier
        );
        flightD.setId(4L);

        flightE = new Flight(
                LocalDateTime.of(2022, 10, 11, 18, 30),
                LocalDateTime.of(2022, 10, 12, 22, 30),
                airportB,
                airportA,
                bombardier
        );
        flightE.setId(5L);

        periodFrom = LocalDateTime.of(2022, 7, 1, 0, 00);
        periodTo = LocalDateTime.of(2022, 7, 31, 23, 59);

        prepareManagersForFlights();

        Mockito.when(flightDao.findById(flightA.getId())).thenReturn(flightA);
        Mockito.when(flightDao.findById(flightB.getId())).thenReturn(flightB);
        Mockito.when(flightDao.findById(flightB.getId())).thenReturn(flightC);
        Mockito.when(flightDao.findById(flightB.getId())).thenReturn(flightD);
        Mockito.when(flightDao.findAll()).thenReturn(List.of(flightA, flightB, flightC, flightD));
        Mockito.when(flightDao.getFlightsForPeriod(periodFrom, periodTo)).thenReturn(List.of(flightA, flightB));
        Mockito.when(managerDao.findManagerOfFlight(flightA.getId())).thenReturn(managerTom);
        Mockito.when(managerDao.findByEmail(managerTom.getEmail())).thenReturn(managerTom);
    }

    @Test
    public void testCreateFlight() {
        flightService.createFlight(flightA, managerTom.getEmail());
        Mockito.verify(flightDao, Mockito.times(1)).create(flightA);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testCreateFlightWithNull() {
        flightService.createFlight(null, managerTom.getEmail());
    }

    @Test
    public void testFindAllFlights() {
        List<Flight> flights = flightService.findAllFlights();
        Assert.assertNotNull(flights);
        Assert.assertEquals(flights.size(), 4);
        Assert.assertTrue(flights.contains(flightA));
        Assert.assertTrue(flights.contains(flightB));
        Assert.assertTrue(flights.contains(flightC));
        Assert.assertTrue(flights.contains(flightD));
    }

    @Test
    public void testFindFlightById() {
        Flight flight = flightService.findFlightById(flightA.getId());
        Assert.assertNotNull(flight);
        Assert.assertEquals(flight, flightA);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testFindFlightByIdWhenIdNull() {
        flightService.findFlightById(null);
    }

    @Test
    public void testUpdateFlight() {
        flightA.setDepartureTime(LocalDateTime.of(2022, 9, 15, 20, 30));
        flightService.updateFlight(flightA);
        Mockito.verify(flightDao, Mockito.times(1)).update(flightA);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testUpdateFlightWithNull() throws IllegalArgumentException {
        flightService.updateFlight(null);
    }

    @Test
    public void testRemoveFlight() {
        flightService.removeFlight(flightA);
        Mockito.verify(managerDao, times(1)).update(managerTom);
        Mockito.verify(flightDao, times(1)).remove(flightA);

        Assert.assertFalse(managerTom.getFlights().contains(flightA));
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testRemoveFlightWithNull() {
        flightService.removeFlight(null);
    }

    @Test
    public void testGetDepartureFlights() {
        Mockito.when(flightDao.getDepartureFlights(airportA)).thenReturn(List.of(flightA, flightC, flightD));
        var departureFlights = flightService.getDepartureFlights(airportA);
        Assert.assertEquals(departureFlights.size(), 3);
        Assert.assertTrue(departureFlights.contains(flightA));
        Assert.assertTrue(departureFlights.contains(flightC));
        Assert.assertTrue(departureFlights.contains(flightD));
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testGetDepartureFlightsWithNullOrigin() {
        flightService.getDepartureFlights(null);
    }

    @Test
    public void testGetOriginFlightsWithZeroFlights() {
        var departureFlights = flightService.getDepartureFlights(airportC);
        Assert.assertEquals(departureFlights.size(), 0);
    }

    @Test
    public void testGetArrivalFlights() {
        Mockito.when(flightDao.getArrivalFlights(airportA)).thenReturn(List.of(flightB, flightE));
        var departureFlights = flightService.getArrivalFlights(airportA);
        Assert.assertEquals(departureFlights.size(), 2);
        Assert.assertTrue(departureFlights.contains(flightB));
        Assert.assertTrue(departureFlights.contains(flightE));
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testGetArrivalFlightsWithNullDestination() {
        flightService.getArrivalFlights(null);
    }

    @Test
    public void testGetDestinationFlightsWithZeroFlights() {
        var arrivalFlights = flightService.getArrivalFlights(airportC);
        Assert.assertEquals(arrivalFlights.size(), 0);
    }

    @Test
    public void testGetFlightsByAirplane() {
        Mockito.when(flightDao.getFlightsByAirplane(bombardier)).thenReturn(List.of(flightA, flightB, flightC, flightD, flightE));
        var airplaneFlights = flightService.getFlightsByAirplane(bombardier);
        Assert.assertEquals(airplaneFlights.size(), 5);
        Assert.assertTrue(airplaneFlights.contains(flightA));
        Assert.assertTrue(airplaneFlights.contains(flightB));
        Assert.assertTrue(airplaneFlights.contains(flightC));
        Assert.assertTrue(airplaneFlights.contains(flightD));
        Assert.assertTrue(airplaneFlights.contains(flightE));
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testGetFlightsByAirplaneWithNullAirplane() {
        flightService.getFlightsByAirplane(null);
    }

    @Test
    public void testGetFlightsByAirplaneWithZeroFlights() {
        var arrivalFlights = flightService.getFlightsByAirplane(embraer);
        Assert.assertEquals(arrivalFlights.size(), 0);
    }

    @Test
    public void testGetFlightsForPeriod() {
        var flights = flightService.getFlightsForPeriod(periodFrom, periodTo);
        Assert.assertEquals(flights.size(), 2);
        Assert.assertTrue(flights.contains(flightA));
        Assert.assertTrue(flights.contains(flightB));
    }

    private void prepareManagersForFlights() {
        var flights = new HashSet<Flight>();
        flights.add(flightA);
        flights.add(flightB);

        managerTom = new Manager("Tom", "Parker", "pwd11357", "em1@em.cz", "654789123");
        managerTom.setId(1L);
        managerTom.setFlights(flights);

        managerBob = new Manager("Bob", "Peace", "pwd123", "em2@em.cz", "654789345");
        managerBob.setId(2L);
    }
}