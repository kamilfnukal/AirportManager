package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.PersistenceAirportManagerContext;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.entity.Steward;
import cz.fi.muni.pa165.airport_manager.enums.AirplaneManufacturer;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Martin Kalina
 * FlightDaoImplTests class is used to verify correct functionality of Flight DAO and Flight Entity.
 */

@ContextConfiguration(classes = PersistenceAirportManagerContext.class)
public class FlightDaoImplTests extends TestBase<Flight> {

    @Autowired
    private FlightDaoImpl flightDao;

    private Flight flightA;
    private Flight flightB;
    private Flight flightC;
    private Airport airportA;
    private Airport airportB;
    private Airport airportC;
    private Airplane bombardier;
    private Airplane embraer;

    public FlightDaoImplTests() {
        super(Flight.class);
    }

    @Override
    protected void beforeEach() {
        bombardier = new Airplane(AirplaneManufacturer.BOMBARDIER, 104, "CRJ1000", "2");
        embraer = new Airplane(AirplaneManufacturer.EMBRAER, 50, "145", "3");

        airportA = new Airport("Austria", "Vienna", "Vienna International Airport", "VIE");
        airportB = new Airport("France", "Paris", "Paris Charles de Gaulle Airport", "CDG");
        airportC = new Airport("Czechia", "Prague", "Letiste Vaclava Havla", "RUZ");
        flightA = new Flight(
                LocalDateTime.of(2022, 3, 15, 15, 30),
                LocalDateTime.of(2022, 3, 15, 18, 00),
                airportA,
                airportB,
                bombardier
        );
        flightB = new Flight(
                LocalDateTime.of(2022, 3, 15, 17, 30),
                LocalDateTime.of(2022, 3, 15, 20, 30),
                airportA,
                airportB,
                bombardier
        );

        flightC = new Flight(
                LocalDateTime.of(2022, 3, 15, 19, 00),
                LocalDateTime.of(2022, 3, 15, 22, 30),
                airportB,
                airportA,
                bombardier
        );

        persistEntities(List.of(bombardier, embraer, airportA, airportB, airportC, flightA, flightB, flightC));
    }

    @Test
    public void testCreateFlight() {
        var flight = new Flight(
                LocalDateTime.of(2022, 7, 15, 15, 30),
                LocalDateTime.of(2022, 7, 15, 17, 30),
                airportA,
                airportB,
                bombardier
        );
        flightDao.create(flight);

        Assert.assertEquals(findAll().size(), 4);
        var flightFromDb = findById(flight.getId());
        Assert.assertEquals(flightFromDb, flight);
    }

    @Test
    public void testCreateMultipleFlights() {
        var flight1 = new Flight(
                LocalDateTime.of(2022, 7, 15, 15, 30),
                LocalDateTime.of(2022, 7, 15, 17, 30),
                airportA,
                airportB,
                bombardier
        );
        var flight2 = new Flight(
                LocalDateTime.of(2022, 7, 15, 20, 30),
                LocalDateTime.of(2022, 7, 15, 22, 30),
                airportA,
                airportB,
                bombardier
        );
        var flight3 = new Flight(
                LocalDateTime.of(2022, 6, 8, 10, 30),
                LocalDateTime.of(2022, 6, 8, 12, 30),
                airportB,
                airportA,
                bombardier
        );
        flightDao.create(flight1);
        flightDao.create(flight2);
        flightDao.create(flight3);

        Assert.assertEquals(findAll().size(), 6);
        Assert.assertEquals(findById(flight1.getId()), flight1);
        Assert.assertEquals(findById(flight2.getId()), flight2);
        Assert.assertEquals(findById(flight3.getId()), flight3);
    }

    @Test
    public void testFindById() {
        var flightFromDb = flightDao.findById(flightA.getId());
        Assert.assertEquals(flightFromDb, flightA);
    }

    @Test
    public void testFindAll() {
        var allFlights = flightDao.findAll();
        Assert.assertNotNull(allFlights);
        Assert.assertEquals(allFlights.size(), 3);
        Assert.assertTrue(allFlights.contains(flightA));
        Assert.assertTrue(allFlights.contains(flightB));
    }

    @Test
    public void testFindByNonExistingId_shouldReturnNull() {
        Assert.assertNull(flightDao.findById(45L));
    }

    @Test
    public void testAddStewardToFlight() {
        var flight = new Flight(
                LocalDateTime.of(2022, 6, 8, 10, 30),
                LocalDateTime.of(2022, 6, 8, 12, 30),
                airportB,
                airportA,
                bombardier
        );
        var steward = new Steward("John", "Stone", "1");

        flight.addSteward(steward);
        persistEntities(List.of(flight, steward));

        var flightFromDB = flightDao.findById(flight.getId());
        Assert.assertNotNull(flightFromDB);
        Assert.assertEquals(flightFromDB.getStewards().size(), 1);
        Assert.assertEquals(flightFromDB.getStewards().toArray()[0], steward);
    }

    @Test
    public void testUpdateFlight() {
        flightA.setArrivalTime(LocalDateTime.of(2023, 3, 25, 15, 30));
        flightA.setDepartureTime(LocalDateTime.of(2023, 3, 25, 13, 30));

        flightDao.update(flightA);

        var updatedFlight = findById(flightA.getId());
        Assert.assertNotNull(updatedFlight);
        Assert.assertEquals(updatedFlight.getDepartureTime(), LocalDateTime.of(2023, 3, 25, 13, 30));
        Assert.assertEquals(updatedFlight.getArrivalTime(), LocalDateTime.of(2023, 3, 25, 15, 30));
        Assert.assertEquals(updatedFlight, flightA);
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void testUpdateNonExistingFlight_shouldThrowObjectNotFoundException() {
        var flight = new Flight(
                LocalDateTime.of(2022, 1, 10, 15, 30),
                LocalDateTime.of(2022, 1, 10, 17, 30),
                airportB,
                airportA,
                bombardier
        );
        flight.setId(99L);

        flight.setOrigin(airportB);
        flightDao.update(flight);
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void testRemoveNonExistingFlight_shouldThrowObjectNotFoundException() {
        var flight = new Flight(
                LocalDateTime.of(2022, 1, 10, 15, 30),
                LocalDateTime.of(2022, 1, 10, 17, 30),
                airportB,
                airportA,
                bombardier
        );
        flight.setId(99L);

        flightDao.remove(flight);
    }

    @Test
    public void testRemoveFlight() {
        flightDao.remove(flightA);
        Assert.assertNull(findById(flightA.getId()));
        Assert.assertEquals(findAll().size(), 2);
    }

    @Test
    public void testGetDepartureFlights() {
        var departureFlights = flightDao.getDepartureFlights(airportA);
        Assert.assertEquals(departureFlights.size(), 2);
        Assert.assertTrue(departureFlights.contains(flightA));
        Assert.assertTrue(departureFlights.contains(flightB));
    }

    @Test
    public void testGetOriginFlightsWithZeroFlights() {
        var departureFlights = flightDao.getDepartureFlights(airportC);
        Assert.assertEquals(departureFlights.size(), 0);
    }


    @Test
    public void testGetArrivalFlights() {
        var departureFlights = flightDao.getArrivalFlights(airportA);
        Assert.assertEquals(departureFlights.size(), 1);
        Assert.assertTrue(departureFlights.contains(flightC));
    }

    @Test
    public void testGetDestinationFlightsWithZeroFlights() {
        var arrivalFlights = flightDao.getArrivalFlights(airportC);
        Assert.assertEquals(arrivalFlights.size(), 0);
    }

    @Test
    public void testGetFlightsByAirplane() {
        var flightsByAirplane = flightDao.getFlightsByAirplane(bombardier);
        Assert.assertEquals(flightsByAirplane.size(), 3);
        Assert.assertTrue(flightsByAirplane.contains(flightA));
        Assert.assertTrue(flightsByAirplane.contains(flightB));
        Assert.assertTrue(flightsByAirplane.contains(flightC));
    }

    @Test
    public void testGetFlightsByAirplaneWithZeroFlights() {
        var flightsByAirplane = flightDao.getFlightsByAirplane(embraer);
        Assert.assertEquals(flightsByAirplane.size(), 0);
    }

    @Test
    public void testGetFlightsForPeriod() {
        var flightsForPeriod = flightDao.getFlightsForPeriod(
                flightA.getDepartureTime(), flightA.getArrivalTime().plusMinutes(30));
        Assert.assertEquals(flightsForPeriod.size(), 2);
        Assert.assertTrue(flightsForPeriod.contains(flightA));
        Assert.assertTrue(flightsForPeriod.contains(flightB));
    }

    @Test
    public void testGetFlightsForPeriodWithZeroFlights() {
        var flightsForPeriod = flightDao.getFlightsForPeriod(
                LocalDateTime.of(2021, 1, 15, 15, 30),
                LocalDateTime.of(2021, 5, 15, 15, 30));
        Assert.assertEquals(flightsForPeriod.size(), 0);
    }

}
