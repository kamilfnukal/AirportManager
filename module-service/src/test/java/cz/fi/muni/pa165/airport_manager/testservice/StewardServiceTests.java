package cz.fi.muni.pa165.airport_manager.testservice;

import cz.fi.muni.pa165.airport_manager.config.ServiceConfiguration;
import cz.fi.muni.pa165.airport_manager.dao.AdminDao;
import cz.fi.muni.pa165.airport_manager.dao.StewardDao;
import cz.fi.muni.pa165.airport_manager.entity.*;
import cz.fi.muni.pa165.airport_manager.enums.AirplaneManufacturer;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.service.FlightService;
import cz.fi.muni.pa165.airport_manager.service.StewardService;
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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Matej Michálek
 * StewardServiceTests class is used to verify correct functionality of Steward service.
 */

@ContextConfiguration(classes = ServiceConfiguration.class)
public class StewardServiceTests extends AbstractTransactionalTestNGSpringContextTests {

    @Mock
    private StewardDao stewardDao;
    @Mock
    private AdminDao adminDao;

    @Mock
    private FlightService flightService;

    @Autowired
    @InjectMocks
    private StewardService stewardService;

    private Steward stewardJohn;
    private Steward stewardSteve;
    private Steward stewardAdam;
    private Flight flightA;
    private Flight flightB;
    private Admin adminTom;

    @BeforeMethod
    public void prepareStewards() {
        MockitoAnnotations.openMocks(this);
        stewardJohn = new Steward("John", "Stone", "1");
        stewardJohn.setId(1L);
        stewardSteve = new Steward("Steve", "Wonder", "2");
        stewardSteve.setId(2L);
        stewardAdam = new Steward("Adam", "Woods", "3");
        stewardAdam.setId(3L);

        prepareAdminForStewards();
        prepareFlightsForStewards();

        when(stewardDao.findById(stewardJohn.getId())).thenReturn(stewardJohn);
        when(stewardDao.findById(stewardSteve.getId())).thenReturn(stewardSteve);
        when(stewardDao.findById(stewardAdam.getId())).thenReturn(stewardAdam);
        when(stewardDao.findAll()).thenReturn(List.of(stewardJohn, stewardSteve, stewardAdam));
        when(adminDao.findAdminOfSteward(stewardJohn.getId())).thenReturn(adminTom);
        when(adminDao.findByEmail(adminTom.getEmail())).thenReturn(adminTom);
    }

    @Test
    public void testCreateSteward() {
        stewardService.createSteward(stewardJohn, adminTom.getEmail());
        verify(stewardDao, times(1)).create(stewardJohn);
        verify(adminDao, times(1)).update(adminTom);

        Assert.assertTrue(adminTom.getStewards().contains(stewardJohn));
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testCreateStewardWithNull() {
        var email = "test@email.com";
        stewardService.createSteward(null, email);
    }

    @Test
    public void testFindAllStewards() {
        List<Steward> stewards = stewardService.findAllStewards();
        Assert.assertNotNull(stewards);
        Assert.assertEquals(stewards.size(), 3);
        Assert.assertTrue(stewards.contains(stewardJohn));
        Assert.assertTrue(stewards.contains(stewardSteve));
        Assert.assertTrue(stewards.contains(stewardAdam));
    }

    @Test
    public void testFindStewardById() {
        Steward airport = stewardService.findStewardById(stewardJohn.getId());
        Assert.assertNotNull(airport);
        Assert.assertEquals(airport, stewardJohn);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testFindStewardByIdWhenIdNull() {
        stewardService.findStewardById(null);
    }

    @Test
    public void testUpdateSteward() {
        stewardJohn.setCitizenId("55");
        stewardService.updateSteward(stewardJohn);
        verify(stewardDao, times(1)).update(stewardJohn);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testUpdateStewardWithNull() throws IllegalArgumentException {
        stewardService.updateSteward(null);
    }

    @Test
    public void testRemoveSteward() {
        flightA.addSteward(stewardJohn);
        flightB.addSteward(stewardJohn);

        stewardService.removeSteward(stewardJohn.getId());
        verify(adminDao, times(1)).update(adminTom);
        verify(flightService, times(1)).updateFlight(flightA);
        verify(flightService, times(1)).updateFlight(flightB);
        verify(stewardDao, times(1)).remove(stewardJohn.getId());

        Assert.assertFalse(adminTom.getStewards().contains(stewardJohn));
        Assert.assertFalse(flightA.getStewards().contains(stewardJohn));
        Assert.assertFalse(flightB.getStewards().contains(stewardJohn));
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testRemoveStewardWithNull() {
        stewardService.removeSteward(null);
    }

    @Test
    public void testFindAllAvailableStewardsForPeriod() {
        flightA.addSteward(stewardJohn);
        flightA.addSteward(stewardSteve);

        flightB.addSteward(stewardAdam);

        var from = flightB.getDepartureTime().plusHours(1);
        var to = from.plusHours(5);

        var availableSteward = stewardService.findAllAvailableStewardsForPeriod(from, to);
        Assert.assertEquals(availableSteward.size(), 2);
        Assert.assertTrue(availableSteward.contains(stewardJohn));
        Assert.assertTrue(availableSteward.contains(stewardSteve));
    }

    @Test
    public void testFindAllAvailableStewardsForPeriod_WhenAlsoStewardsWithoutFlights() {
        flightB.addSteward(stewardAdam);

        var from = flightB.getDepartureTime().plusHours(1);
        var to = from.plusHours(5);

        var availableSteward = stewardService.findAllAvailableStewardsForPeriod(from, to);
        Assert.assertEquals(availableSteward.size(), 2);
        Assert.assertTrue(availableSteward.contains(stewardJohn));
        Assert.assertTrue(availableSteward.contains(stewardSteve));
    }

    @Test
    public void testFindAllAvailableStewardsForPeriod_WhenNoneStewardAvailable() {
        flightB.addSteward(stewardJohn);
        flightB.addSteward(stewardSteve);
        flightB.addSteward(stewardAdam);

        var from = flightB.getDepartureTime().plusHours(1);
        var to = from.plusHours(5);

        var availableSteward = stewardService.findAllAvailableStewardsForPeriod(from, to);
        Assert.assertEquals(availableSteward.size(), 0);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testFindAllAvailableStewardsForPeriod_WhenFromIsNull() {
        var to = LocalDateTime.of(2022, 7, 15, 15, 30);
        stewardService.findAllAvailableStewardsForPeriod(null, to);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testFindAllAvailableStewardsForPeriod_WhenToIsNull() {
        var from = LocalDateTime.of(2022, 7, 15, 15, 30);
        stewardService.findAllAvailableStewardsForPeriod(from, null);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testFindAllAvailableStewardsForPeriod_WhenFromIsAfterTo() {
        var from = LocalDateTime.of(2022, 7, 15, 15, 30);
        var to = from.minusSeconds(1);
        stewardService.findAllAvailableStewardsForPeriod(from, to);
    }

    private void prepareAdminForStewards() {
        var stewards = new HashSet<Steward>();
        stewards.add(stewardJohn);
        stewards.add(stewardSteve);

        adminTom = new Admin("Tom", "Parker", "pwd11357", "em1@em.cz", "654789123");
        adminTom.setId(1L);
        adminTom.setStewards(stewards);
    }

    private void prepareFlightsForStewards() {
        var airplane = new Airplane(AirplaneManufacturer.BOMBARDIER, 104, "CRJ1000", "2");
        var airportA = new Airport("Austria", "Vienna", "Vienna International Airport", "VIE");
        var airportB = new Airport("France", "Paris", "Paris Charles de Gaulle Airport", "CDG");

        flightA = new Flight(
                LocalDateTime.of(2022, 7, 15, 15, 30),
                LocalDateTime.of(2022, 7, 15, 17, 30),
                airportA,
                airportB,
                airplane
        );
        flightB = new Flight(
                LocalDateTime.of(2022, 7, 15, 20, 30),
                LocalDateTime.of(2022, 7, 15, 22, 30),
                airportB,
                airportA,
                airplane
        );
    }
}