package cz.fi.muni.pa165.airport_manager.testservice;

import cz.fi.muni.pa165.airport_manager.config.ServiceConfiguration;
import cz.fi.muni.pa165.airport_manager.dao.AdminDao;
import cz.fi.muni.pa165.airport_manager.dao.AirportDao;
import cz.fi.muni.pa165.airport_manager.entity.Admin;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.enums.AirplaneManufacturer;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.service.AirportService;
import cz.fi.muni.pa165.airport_manager.service.FlightService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Pavel Sklenar
 * AirportServiceTests class is used to verify correct functionality of Airport service.
 */

@ContextConfiguration(classes= ServiceConfiguration.class)
public class AirportServiceTests extends AbstractTransactionalTestNGSpringContextTests {

    @Mock
    private AirportDao airportDao;
    @Mock
    private AdminDao adminDao;
    @Mock
    private FlightService flightService;

    @Autowired
    @InjectMocks
    private AirportService airportService;

    private Airport viennaAirport;
    private Airport munichAirport;
    private Airport pragueAirport;
    private Flight flightBombardier;
    private Flight flightEmbraer;
    private Flight flightBeoing;
    private Admin adminTom;
    private Admin adminBob;

    @BeforeMethod
    public void prepareAirports() {
        MockitoAnnotations.openMocks(this);
        viennaAirport = new Airport("Austria", "Vienna", "Vienna International Airport", "VIE");
        viennaAirport.setId(1L);
        munichAirport = new Airport("Germany", "Munich", "Munich Airport", "MUC");
        munichAirport.setId(2L);
        pragueAirport = new Airport("Czech republic", "Prague", "Václav Havel Airport Prague", "PRG");
        pragueAirport.setId(3L);

        prepareAdminForAirports();
        prepareFlightsForAirports();

        when(airportDao.findById(viennaAirport.getId())).thenReturn(viennaAirport);
        when(airportDao.findById(munichAirport.getId())).thenReturn(munichAirport);
        when(airportDao.findById(pragueAirport.getId())).thenReturn(pragueAirport);
        when(adminDao.findAdminOfAirport(viennaAirport.getId())).thenReturn(adminTom);
        when(adminDao.findByEmail(adminBob.getEmail())).thenReturn(adminBob);
        when(airportDao.findAll()).thenReturn(List.of(viennaAirport, munichAirport, pragueAirport));
        when(flightService.getDepartureFlights(viennaAirport)).thenReturn(List.of(flightBombardier, flightBeoing));
        when(flightService.getArrivalFlights(viennaAirport)).thenReturn(List.of(flightEmbraer));
    }

    @Test
    public void testCreateAirport() {
        airportService.createAirport(viennaAirport, adminBob.getEmail());
        verify(airportDao, times(1)).create(viennaAirport);
        verify(adminDao, times(1)).update(adminBob);

        Assert.assertTrue(adminBob.getAirports().contains(viennaAirport));
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testCreateAirportWithAirportNull() {
        var email = "test@email.com";
        airportService.createAirport(null, email);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testCreateAirportWithEmailNull() {
        var airport = new Airport("Austria", "Vienna", "Vienna International Airport", "VIE");
        airportService.createAirport(airport, null);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testCreateAirportWithAlreadyUsedCode() {
        var airport = new Airport("Austria", "Vienna", "Vienna International Airport", "VIE");
        doThrow(new AirportManagerServiceException()).when(airportDao).create(airport);
        airportService.createAirport(airport, adminTom.getEmail());
    }

    @Test
    public void testFindAllAirports() {
        List<Airport> airports = airportService.findAllAirports();
        Assert.assertNotNull(airports);
        Assert.assertEquals(airports.size(), 3);
        Assert.assertTrue(airports.contains(viennaAirport));
        Assert.assertTrue(airports.contains(munichAirport));
        Assert.assertTrue(airports.contains(pragueAirport));
    }

    @Test
    public void testFindAirportById() {
        Airport airport = airportService.findAirportById(viennaAirport.getId());
        Assert.assertNotNull(airport);
        Assert.assertEquals(airport, viennaAirport);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testFindAirportByIdWhenIdNull() {
        airportService.findAirportById(null);
    }

    @Test
    public void testUpdateAirport() {
        viennaAirport.setCode("VAO");
        airportService.updateAirport(viennaAirport);
        verify(airportDao, times(1)).update(viennaAirport);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testUpdateAirportWithNull() throws IllegalArgumentException {
        airportService.updateAirport(null);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testUpdateAirportWithAlreadyUsedCode() {
        viennaAirport.setCode(munichAirport.getCode());
        doThrow(new AirportManagerServiceException()).when(airportDao).update(viennaAirport);
        airportService.updateAirport(viennaAirport);
    }

    @Test
    public void testRemoveAirport() {
        airportService.removeAirport(viennaAirport.getId());
        verify(adminDao, times(1)).update(adminTom);
        verify(flightService, times(1)).removeFlight(flightBeoing);
        verify(flightService, times(1)).removeFlight(flightBombardier);
        verify(flightService, times(1)).removeFlight(flightEmbraer);
        verify(airportDao, times(1)).remove(viennaAirport.getId());

        Assert.assertFalse(adminTom.getAirports().contains(viennaAirport));
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testRemoveAirportWithNull() {
        airportService.removeAirport(null);
    }

    private void prepareAdminForAirports() {
        var airports = new HashSet<Airport>();
        airports.add(viennaAirport);
        airports.add(munichAirport);
        airports.add(pragueAirport);

        adminTom = new Admin("Tom", "Parker", "pwd11357", "em1@em.cz", "654789123");
        adminTom.setId(1L);
        adminTom.setAirports(airports);

        adminBob = new Admin("Bob", "Peace", "pwd123", "em2@em.cz", "654789345");
        adminBob.setId(2L);
    }

    private void prepareFlightsForAirports() {
        Airplane boeing = new Airplane(AirplaneManufacturer.BOEING, 420, "770", "1");
        boeing.setId(1L);
        Airplane embraer = new Airplane(AirplaneManufacturer.EMBRAER, 300, "200", "2");
        embraer.setId(2L);
        Airplane bombardier = new Airplane(AirplaneManufacturer.BOMBARDIER, 200, "400", "3");
        bombardier.setId(3L);

        flightBombardier = new Flight(
                LocalDateTime.of(2022, 7, 15, 15, 30),
                LocalDateTime.of(2022, 7, 15, 19, 30),
                viennaAirport,
                munichAirport,
                bombardier
        );
        flightEmbraer = new Flight(
                LocalDateTime.of(2022, 7, 15, 16, 00),
                LocalDateTime.of(2022, 7, 15, 21, 30),
                munichAirport,
                viennaAirport,
                embraer
        );
        flightBeoing = new Flight(
                LocalDateTime.of(2022, 7, 15, 20, 00),
                LocalDateTime.of(2022, 7, 15, 23, 30),
                viennaAirport,
                munichAirport,
                boeing
        );
    }
}