package cz.fi.muni.pa165.airport_manager.testservice;

import cz.fi.muni.pa165.airport_manager.config.ServiceConfiguration;
import cz.fi.muni.pa165.airport_manager.dao.AdminDao;
import cz.fi.muni.pa165.airport_manager.dao.AirplaneDao;
import cz.fi.muni.pa165.airport_manager.entity.Admin;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.enums.AirplaneManufacturer;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.service.AirplaneService;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Pavel Sklenar
 * AirplaneServiceTests class is used to verify correct functionality of Airplane service.
 */

@ContextConfiguration(classes = ServiceConfiguration.class)
public class AirplaneServiceTests extends AbstractTransactionalTestNGSpringContextTests {

    @Mock
    private AirplaneDao airplaneDao;
    @Mock
    private AdminDao adminDao;

    @Mock
    private FlightService flightService;

    @Autowired
    @InjectMocks
    private AirplaneService airplaneService;

    private Airplane boeing;
    private Airplane embraer;
    private Airplane bombardier;
    private Flight flightBombardier;
    private Flight flightEmbraer;
    private Flight flightBeoing;
    private Admin adminTom;
    private Admin adminBob;

    @BeforeMethod
    public void prepareAirplane() {
        MockitoAnnotations.openMocks(this);
        boeing = new Airplane(AirplaneManufacturer.BOEING, 420, "770", "1");
        boeing.setId(1L);
        embraer = new Airplane(AirplaneManufacturer.EMBRAER, 300, "200", "2");
        embraer.setId(2L);
        bombardier = new Airplane(AirplaneManufacturer.BOMBARDIER, 200, "400", "3");
        bombardier.setId(3L);

        prepareAdminsForAirplanes();
        prepareFlightsForAirplanes();

        when(airplaneDao.findById(boeing.getId())).thenReturn(boeing);
        when(airplaneDao.findById(embraer.getId())).thenReturn(embraer);
        when(airplaneDao.findById(bombardier.getId())).thenReturn(bombardier);
        when(airplaneDao.findAll()).thenReturn(List.of(boeing, embraer, bombardier));
        when(adminDao.findAdminOfAirplane(bombardier.getId())).thenReturn(adminTom);
        when(adminDao.findByEmail(adminBob.getEmail())).thenReturn(adminBob);
        when(flightService.getFlightsByAirplane(bombardier)).thenReturn(List.of(flightBombardier));
    }

    @Test
    public void testCreateAirplane() {
        airplaneService.createAirplane(boeing, adminBob.getEmail());
        verify(airplaneDao, times(1)).create(boeing);
        verify(adminDao, times(1)).update(adminBob);

        Assert.assertTrue(adminBob.getAirplanes().contains(boeing));
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testCreateAirplaneWithAirplaneNull() {
        var email = "test@email.com";
        airplaneService.createAirplane(null, email);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testCreateAirplaneWithEmailNull() {
        var boeing2 = new Airplane(AirplaneManufacturer.BOEING, 850, "770", "1");
        airplaneService.createAirplane(boeing2, null);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testCreateAirplaneWithAlreadyUsedSerialNumber() {
        var boeing2 = new Airplane(AirplaneManufacturer.BOEING, 850, "770", "1");
        doThrow(new AirportManagerServiceException()).when(airplaneDao).create(boeing2);
        airplaneService.createAirplane(boeing2, adminTom.getEmail());
    }

    @Test
    public void testFindAllAirplanes() {
        List<Airplane> airplanes = airplaneService.findAllAirplanes();
        Assert.assertNotNull(airplanes);
        Assert.assertEquals(airplanes.size(), 3);
        Assert.assertTrue(airplanes.contains(boeing));
        Assert.assertTrue(airplanes.contains(bombardier));
        Assert.assertTrue(airplanes.contains(embraer));
    }

    @Test
    public void testFindAirplaneById() {
        Airplane airplane = airplaneService.findAirplaneById(boeing.getId());
        Assert.assertNotNull(airplane);
        Assert.assertEquals(airplane, boeing);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testFindAirplaneByIdWhenIdNull() {
        airplaneService.findAirplaneById(null);
    }

    @Test
    public void testUpdateAirplane() {
        boeing.setModel("800");
        airplaneService.updateAirplane(boeing);
        verify(airplaneDao, times(1)).update(boeing);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testUpdateAirplaneWithNull() throws IllegalArgumentException {
        airplaneService.updateAirplane(null);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testUpdateAirplaneWithAlreadyUsedSerialNumber() {
        bombardier.setSerialNumber(boeing.getSerialNumber());
        doThrow(new AirportManagerServiceException()).when(airplaneDao).update(bombardier);
        airplaneService.updateAirplane(bombardier);
    }

    @Test
    public void testRemoveAirplane() {
        airplaneService.removeAirplane(bombardier.getId());
        verify(adminDao, times(1)).update(adminTom);
        verify(flightService, times(1)).removeFlight(flightBombardier);
        verify(airplaneDao, times(1)).remove(bombardier.getId());

        Assert.assertFalse(adminTom.getAirplanes().contains(bombardier));
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testRemoveAirplaneWithNull() {
        airplaneService.removeAirplane(null);
    }

    @Test
    public void testFindAllAvailableAirplanesForPeriod() {
        when(flightService.getFlightsForPeriod(any(), any())).thenReturn(List.of(flightBombardier, flightEmbraer));
        var from = flightBombardier.getDepartureTime().minusMinutes(30);
        var to = from.plusHours(5);

        var availableAirplanes = airplaneService.findAllAvailableAirplanesForPeriod(from, to);
        Assert.assertEquals(availableAirplanes.size(), 1);
        Assert.assertTrue(availableAirplanes.contains(boeing));
    }

    @Test
    public void testFindAllAvailableAirplanesForPeriod_WhenNoneFlightFoundForPeriod() {
        when(flightService.getFlightsForPeriod(any(), any())).thenReturn(Collections.emptyList());
        var from = flightBombardier.getDepartureTime().plusHours(1);
        var to = from.plusHours(5);

        var availableAirplanes = airplaneService.findAllAvailableAirplanesForPeriod(from, to);
        Assert.assertEquals(availableAirplanes.size(), 3);
        Assert.assertTrue(availableAirplanes.contains(embraer));
        Assert.assertTrue(availableAirplanes.contains(bombardier));
        Assert.assertTrue(availableAirplanes.contains(boeing));
    }

    @Test
    public void testFindAllAvailableAirplanesForPeriod_WhenNoneAirplaneAvailable() {
        when(flightService.getFlightsForPeriod(any(), any())).thenReturn(List.of(flightBombardier, flightEmbraer, flightBeoing));
        var from = flightBombardier.getDepartureTime().plusHours(1);
        var to = from.plusHours(5);

        var availableAirplanes = airplaneService.findAllAvailableAirplanesForPeriod(from, to);
        Assert.assertEquals(availableAirplanes.size(), 0);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testFindAllAvailableAirplanesForPeriod_WhenFromIsNull() {
        var to = LocalDateTime.of(2022, 7, 15, 15, 30);
        airplaneService.findAllAvailableAirplanesForPeriod(null, to);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testFindAllAvailableAirplanesForPeriod_WhenToIsNull() {
        var from = LocalDateTime.of(2022, 7, 15, 15, 30);
        airplaneService.findAllAvailableAirplanesForPeriod(from, null);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testFindAllAvailableAirplanesForPeriod_WhenFromIsAfterTo() {
        var from = LocalDateTime.of(2022, 7, 15, 15, 30);
        var to = from.minusSeconds(1);
        airplaneService.findAllAvailableAirplanesForPeriod(from, to);
    }

    private void prepareAdminsForAirplanes() {
        var airplanes = new HashSet<Airplane>();
        airplanes.add(boeing);
        airplanes.add(bombardier);

        adminTom = new Admin("Tom", "Parker", "pwd11357", "em1@em.cz", "654789123");
        adminTom.setId(1L);
        adminTom.setAirplanes(airplanes);

        adminBob = new Admin("Bob", "Peace", "pwd123", "em2@em.cz", "654789345");
        adminBob.setId(2L);
    }

    private void prepareFlightsForAirplanes() {
        var airportA = new Airport("Austria", "Vienna", "Vienna International Airport", "VIE");
        var airportB = new Airport("France", "Paris", "Paris Charles de Gaulle Airport", "CDG");

        flightBombardier = new Flight(
                LocalDateTime.of(2022, 7, 15, 15, 30),
                LocalDateTime.of(2022, 7, 15, 19, 30),
                airportA,
                airportB,
                bombardier
        );
        flightEmbraer = new Flight(
                LocalDateTime.of(2022, 7, 15, 16, 00),
                LocalDateTime.of(2022, 7, 15, 21, 30),
                airportB,
                airportA,
                embraer
        );
        flightBeoing = new Flight(
                LocalDateTime.of(2022, 7, 15, 20, 00),
                LocalDateTime.of(2022, 7, 15, 23, 30),
                airportA,
                airportB,
                boeing
        );
    }
}