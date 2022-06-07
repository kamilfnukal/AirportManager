package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.PersistenceAirportManagerContext;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author Matej Michalek
 * AirportDaoImplTests class is used to verify correct functionality of Airport DAO and Airport Entity.
 */

@ContextConfiguration(classes = PersistenceAirportManagerContext.class)
public class AirportDaoImplTests extends TestBase<Airport> {

    @Autowired
    private AirportDaoImpl airportDao;

    private Airport dummyAirportParis;
    private Airport dummyAirportFrankfurt;

    public AirportDaoImplTests() {
        super(Airport.class);
    }

    @Override
    protected void beforeEach() {
        dummyAirportParis = new Airport("France", "Paris", "Paris Charles de Gaulle Airport", "CDG");
        dummyAirportFrankfurt = new Airport("Germany", "Frankfurt", "Frankfurt Airport", "FRA");

        persistEntities(List.of(dummyAirportParis, dummyAirportFrankfurt));
    }

    @Test
    public void testCreateAirport() {
        var airport = new Airport("Austria", "Vienna", "Vienna International Airport", "VIE");
        airportDao.create(airport);

        Assert.assertEquals(findAll().size(), 3);
        var airportFromDb = findById(airport.getId());
        Assert.assertEquals(airportFromDb, airport);
    }

    @Test
    public void testCreateMultipleAirports() {
        var airport1 = new Airport("Austria", "Vienna", "Vienna International Airport", "VIE");
        var airport2 = new Airport("Germany", "Munich", "Munich Airport", "MUC");
        var airport3 = new Airport("Czech republic", "Prague", "Václav Havel Airport Prague", "PRG");
        airportDao.create(airport1);
        airportDao.create(airport2);
        airportDao.create(airport3);

        Assert.assertEquals(findAll().size(), 5);
        Assert.assertEquals(findById(airport1.getId()), airport1);
        Assert.assertEquals(findById(airport2.getId()), airport2);
        Assert.assertEquals(findById(airport3.getId()), airport3);
    }

    @Test(expectedExceptions = JpaSystemException.class)
    public void testCreateAirportWithNonUniqueCode_shouldThrowJpaSystemException() {
        var airport = new Airport("France", "Paris", "Paris Charles de Gaulle Airport", dummyAirportParis.getCode());
        airportDao.create(airport);
    }

    @Test
    public void testFindById() {
        var airportFromDb = airportDao.findById(dummyAirportParis.getId());
        Assert.assertEquals(airportFromDb, dummyAirportParis);
    }

    @Test
    public void testFindByNonExistingId_shouldReturnNull() {
        Assert.assertNull(airportDao.findById(999L));
    }

    @Test
    public void testFindAll() {
        var allAirports = airportDao.findAll();
        Assert.assertNotNull(allAirports);
        Assert.assertEquals(allAirports.size(), 2);
        Assert.assertTrue(allAirports.contains(dummyAirportParis));
        Assert.assertTrue(allAirports.contains(dummyAirportFrankfurt));
    }

    @Test
    public void testUpdateAirport() {
        dummyAirportParis.setName("Paris Airport");
        dummyAirportParis.setCity("Pariss");
        airportDao.update(dummyAirportParis);

        var updatedAirport = findById(dummyAirportParis.getId());
        Assert.assertNotNull(updatedAirport);
        Assert.assertEquals(updatedAirport.getCity(), "Pariss");
        Assert.assertEquals(updatedAirport.getName(), "Paris Airport");
        Assert.assertEquals(updatedAirport, dummyAirportParis);
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void testUpdateNonExistingAirport_shouldThrowObjectNotFoundException() {
        var airport = new Airport("Austria", "Vienna", "Vienna International Airport", "VIE");
        airport.setId(1L);

        airport.setCity("Schwechat");
        airportDao.update(airport);
    }

    @Test(expectedExceptions = JpaSystemException.class)
    public void testUpdateAirportWithNonUniqueCode_shouldThrowJpaSystemException() {
        dummyAirportParis.setCode(dummyAirportFrankfurt.getCode());
        airportDao.update(dummyAirportParis);
    }

    @Test
    public void testRemoveAirport() {
        airportDao.remove(dummyAirportParis.getId());
        Assert.assertNull(findById(dummyAirportParis.getId()));
        Assert.assertEquals(findAll().size(), 1);
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void testRemoveNonExistingAirport_shouldThrowObjectNotFoundException() {
        var airport = new Airport("Austria", "Vienna", "Vienna International Airport", "VIE");
        airport.setId(1L);

        airportDao.remove(airport.getId());
    }

}
