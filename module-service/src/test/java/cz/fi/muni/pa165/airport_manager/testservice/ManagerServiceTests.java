package cz.fi.muni.pa165.airport_manager.testservice;

import cz.fi.muni.pa165.airport_manager.config.ServiceConfiguration;
import cz.fi.muni.pa165.airport_manager.dao.ManagerDao;
import cz.fi.muni.pa165.airport_manager.entity.Manager;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.service.ManagerService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Kamil Fňukal
 * ManagerServiceTests class is used to verify correct functionality of Manager service.
 */

@ContextConfiguration(classes = ServiceConfiguration.class)
public class ManagerServiceTests extends AbstractTransactionalTestNGSpringContextTests {

    @Mock
    private ManagerDao managerDao;

    @Autowired
    @InjectMocks
    private ManagerService managerService;

    private Manager managerTom;
    private Manager managerSteve;
    private Manager managerJosh;

    @BeforeMethod
    public void prepareManagers() {
        MockitoAnnotations.openMocks(this);
        managerTom = new Manager("Tom", "Parker", "pwd11357", "em1@em.cz", "654789123");
        managerTom.setId(1L);
        managerSteve = new Manager("Steve", "Parker", "pwd11357", "em2@em.cz", "654789124");
        managerSteve.setId(2L);
        managerJosh = new Manager("Josh", "Parker", "pwd11357", "em3@em.cz", "654789125");
        managerJosh.setId(3L);
        when(managerDao.findById(managerTom.getId())).thenReturn((managerTom));
        when(managerDao.findByEmail("em1@em.cz")).thenReturn(managerTom);
        when(managerDao.findById(managerSteve.getId())).thenReturn(managerSteve);
        when(managerDao.findById(managerJosh.getId())).thenReturn(managerJosh);
        when(managerDao.findAll()).thenReturn(List.of(managerTom, managerSteve, managerJosh));
    }

    @Test
    public void testCreateManager() {
        managerService.createManager(managerTom);
        verify(managerDao, times(1)).create(managerTom);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testCreateManagerWithNull() {
        managerService.createManager(null);
    }

    @Test
    public void testFindAllManagers() {
        List<Manager> managers = managerService.findAllManagers();
        Assert.assertNotNull(managers);
        Assert.assertEquals(managers.size(), 3);
        Assert.assertTrue(managers.contains(managerTom));
        Assert.assertTrue(managers.contains(managerJosh));
        Assert.assertTrue(managers.contains(managerSteve));
    }

    @Test
    public void testFindManagerById() {
        Manager manager = managerService.findManagerById(managerTom.getId());
        Assert.assertNotNull(manager);
        Assert.assertEquals(manager, managerTom);
    }

    @Test
    public void testFindManagerByEmail() {
        Manager manager = managerService.findByEmail("em1@em.cz");
        Assert.assertNotNull(manager);
        Assert.assertEquals(manager, managerTom);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testFindManagerByIdWhenIdNull() {
        managerService.findManagerById(null);
    }

    @Test
    public void testUpdateManager() {
        managerTom.setPassword("newpass");
        managerService.updateManager(managerTom);
        verify(managerDao, times(1)).update(managerTom);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testUpdateManagerWithNull() throws IllegalArgumentException {
        managerService.updateManager(null);
    }

    @Test
    public void testRemoveManager() {
        managerService.removeManager(managerTom.getId());
        verify(managerDao, times(1)).remove(managerTom.getId());
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testRemoveManagerWithNull() {
        managerService.removeManager(null);
    }
}