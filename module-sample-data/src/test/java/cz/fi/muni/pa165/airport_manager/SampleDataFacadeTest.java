package cz.fi.muni.pa165.airport_manager;

import cz.fi.muni.pa165.airport_manager.dao.AdminDao;
import cz.fi.muni.pa165.airport_manager.dao.ManagerDao;
import cz.fi.muni.pa165.airport_manager.dao.StewardDao;
import cz.fi.muni.pa165.airport_manager.dao.AirplaneDao;
import cz.fi.muni.pa165.airport_manager.dao.AirportDao;
import cz.fi.muni.pa165.airport_manager.dao.FlightDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Pavel Sklenar
 * Class used for data loading testing
 */
@ContextConfiguration(classes = {AirportManagerSampleDataConfiguration.class})
@TestExecutionListeners(TransactionalTestExecutionListener.class)
@Transactional
public class SampleDataFacadeTest extends AbstractTestNGSpringContextTests {

    @Autowired
    public AdminDao adminDao;

    @Autowired
    public ManagerDao managerDao;

    @Autowired
    public StewardDao stewardDao;

    @Autowired
    public AirplaneDao airplaneDao;

    @Autowired
    public AirportDao airportDao;

    @Autowired
    public FlightDao flightDao;

    @Autowired
    public SampleDataLoadingFacade sampleDataLoadingFacade;

    @Test
    public void verifySampleData() {
        Assert.assertTrue(adminDao.findAll().size() > 0, "Zero admin users.");
        Assert.assertTrue(managerDao.findAll().size() > 0, "Zero manager users.");
        Assert.assertEquals(adminDao.findAll().size(),2, "There are not two admin users.");
        Assert.assertEquals(managerDao.findAll().size(),4, "There are not four manager users.");
        Assert.assertEquals(stewardDao.findById(1L).getFirstName(),"John", "Steward with ID one is not John.");
        Assert.assertEquals(airplaneDao.findAll().size(),6, "There are not six airplanes.");
        Assert.assertTrue(airportDao.findAll().size() > 0, "There are not any airports.");
        Assert.assertEquals(stewardDao.findById(1L).getFlights().size(), 3, "Steward John doesnt operate in 3 flights");
        Assert.assertTrue(flightDao.findAll().size() > 0, "There are zero flights in information system");
    }

}