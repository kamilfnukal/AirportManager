package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.PersistenceAirportManagerContext;

import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.enums.AirplaneManufacturer;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author Pavel Sklenar
 * AirplaneDaoImplTests class is used to verify correct functionality of Airplane DAO and Airplane Entity.
 */

@ContextConfiguration(classes = PersistenceAirportManagerContext.class)
public class AirplaneDaoImplTests extends TestBase<Airplane> {

    @Autowired
    private AirplaneDao airplaneDao;

    private Airplane embraerAirplane;
    private Airplane bombardierAirplane;

    public AirplaneDaoImplTests() {
        super(Airplane.class);
    }

    @Override
    protected void beforeEach() {
        bombardierAirplane = new Airplane(AirplaneManufacturer.BOMBARDIER, 104, "CRJ1000", "2");
        embraerAirplane = new Airplane(AirplaneManufacturer.EMBRAER, 50, "145", "3");

        persistEntities(List.of(embraerAirplane, bombardierAirplane));
    }

    @Test
    public void testCreateAirplane() {
        var boeing = new Airplane(AirplaneManufacturer.BOEING, 440, "777", "1");
        airplaneDao.create(boeing);

        Assert.assertEquals(findAll().size(), 3);
        var boeingFromDb = findById(boeing.getId());
        Assert.assertEquals(boeingFromDb, boeing);
    }

    @Test
    public void testCreateMultipleAirplanes() {
        var boeing = new Airplane(AirplaneManufacturer.BOEING, 510, "779", "4");
        var bombardier = new Airplane(AirplaneManufacturer.BOMBARDIER, 80, "CRJ1100", "5");
        var embraer = new Airplane(AirplaneManufacturer.EMBRAER, 40, "130", "6");
        airplaneDao.create(boeing);
        airplaneDao.create(bombardier);
        airplaneDao.create(embraer);

        Assert.assertEquals(findAll().size(), 5);
        Assert.assertEquals(findById(boeing.getId()), boeing);
        Assert.assertEquals(findById(bombardier.getId()), bombardier);
        Assert.assertEquals(findById(embraer.getId()), embraer);
    }

    @Test
    public void testFindById() {
        var airplaneFromDb = airplaneDao.findById(bombardierAirplane.getId());
        Assert.assertEquals(airplaneFromDb, bombardierAirplane);
    }

    @Test
    public void testFindByNonExistingId_shouldReturnNull() {
        Assert.assertNull(airplaneDao.findById(999L));
    }

    @Test
    public void testFindAll(){
        var allAirplanes = airplaneDao.findAll();
        Assert.assertNotNull(allAirplanes);
        Assert.assertEquals(allAirplanes.size(), 2);
        Assert.assertTrue(allAirplanes.contains(bombardierAirplane));
        Assert.assertTrue(allAirplanes.contains(embraerAirplane));
    }

    @Test
    public void testUpdateAirplane() {
        bombardierAirplane.setModel("650");
        bombardierAirplane.setCapacity(400);
        airplaneDao.update(bombardierAirplane);

        var updatedAirplane = findById(bombardierAirplane.getId());
        Assert.assertNotNull(updatedAirplane);
        Assert.assertEquals(updatedAirplane.getModel(), "650");
        Assert.assertEquals(updatedAirplane.getCapacity(), 400);
        Assert.assertEquals(updatedAirplane, bombardierAirplane);
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void testUpdateNonExistingAirplane_shouldThrowObjectNotFoundException() {
        var boeing = new Airplane(AirplaneManufacturer.BOEING, 500, "800", "10");
        boeing.setId(500L);

        boeing.setCapacity(400);
        airplaneDao.update(boeing);
    }

    @Test
    public void testRemoveAirplane() {
        airplaneDao.remove(bombardierAirplane.getId());
        Assert.assertNull(findById(bombardierAirplane.getId()));
        Assert.assertEquals(findAll().size(), 1);
    }

    @Test(expectedExceptions = JpaSystemException.class)
    public void createAirplaneWithNonUniqueSerialNumber_shouldThrowJpaSystemException() {
        var bombardier = new Airplane(AirplaneManufacturer.BOMBARDIER, 104, "CRJ1000", "2");
        airplaneDao.create(bombardier);
    }

    @Test(expectedExceptions = JpaSystemException.class)
    public void testUpdateAirplaneWithNonUniqueSerialNumber_shouldThrowJpaSystemException() {
        bombardierAirplane.setSerialNumber("3");
        airplaneDao.update(bombardierAirplane);
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void testRemoveNonExistingAirplane_shouldThrowObjectNotFoundException() {
        var boeing = new Airplane(AirplaneManufacturer.BOEING, 250, "700", "15");
        boeing.setId(10L);

        airplaneDao.remove(boeing.getId());
    }

}
