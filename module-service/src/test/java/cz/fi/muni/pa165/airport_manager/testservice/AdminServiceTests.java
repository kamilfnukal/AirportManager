package cz.fi.muni.pa165.airport_manager.testservice;

import cz.fi.muni.pa165.airport_manager.config.ServiceConfiguration;
import cz.fi.muni.pa165.airport_manager.dao.AdminDao;
import cz.fi.muni.pa165.airport_manager.entity.Admin;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.service.AdminService;
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
 * AdminServiceTests class is used to verify correct functionality of Admin service.
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class AdminServiceTests extends AbstractTransactionalTestNGSpringContextTests {

    @Mock
    private AdminDao adminDao;

    @Autowired
    @InjectMocks
    private AdminService adminService;

    private Admin adminTom;
    private Admin adminSteve;
    private Admin adminJosh;

    @BeforeMethod
    public void prepareAdmins() {
        MockitoAnnotations.openMocks(this);
        adminTom = new Admin("Tom", "Parker", "pwd11357", "em1@em.cz", "654789123");
        adminTom.setId(1L);
        adminSteve = new Admin("Steve", "Parker", "pwd11357", "em2@em.cz", "654789124");
        adminSteve.setId(2L);
        adminJosh = new Admin("Josh", "Parker", "pwd11357", "em3@em.cz", "654789125");
        adminJosh.setId(3L);
        when(adminDao.findById(adminTom.getId())).thenReturn(adminTom);
        when(adminDao.findByEmail("em1@em.cz")).thenReturn(adminTom);
        when(adminDao.findById(adminSteve.getId())).thenReturn(adminSteve);
        when(adminDao.findById(adminJosh.getId())).thenReturn(adminJosh);
        when(adminDao.findAll()).thenReturn(List.of(adminTom, adminSteve, adminJosh));
    }

    @Test
    public void testCreateAdmin() {
        adminService.createAdmin(adminTom);
        verify(adminDao, times(1)).create(adminTom);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testCreateAdminWithNull() {
        adminService.createAdmin(null);
    }

    @Test
    public void testFindAllAdmins() {
        List<Admin> Admins = adminService.findAllAdmins();
        Assert.assertNotNull(Admins);
        Assert.assertEquals(Admins.size(), 3);
        Assert.assertTrue(Admins.contains(adminTom));
        Assert.assertTrue(Admins.contains(adminSteve));
        Assert.assertTrue(Admins.contains(adminJosh));
    }

    @Test
    public void testFindAdminById() {
        Admin admin = adminService.findAdminById(adminTom.getId());
        Assert.assertNotNull(admin);
        Assert.assertEquals(admin, adminTom);
    }

    @Test
    public void testFindAdminByEmail() {
        Admin admin = adminService.findByEmail("em1@em.cz");
        Assert.assertNotNull(admin);
        Assert.assertEquals(admin, adminTom);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testFindAdminByEmailWhenEmailNull() {
        adminService.findByEmail(null);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testFindAdminByIdWhenIdNull() {
        adminService.findAdminById(null);
    }

    @Test
    public void testUpdateAdmin() {
        adminTom.setPassword("newpass");
        adminService.updateAdmin(adminTom);
        verify(adminDao, times(1)).update(adminTom);
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testUpdateAdminWithNull() throws IllegalArgumentException {
        adminService.updateAdmin(null);
    }

    @Test
    public void testRemoveAdmin() {
        adminService.removeAdmin(adminTom.getId());
        verify(adminDao, times(1)).remove(adminTom.getId());
    }

    @Test(expectedExceptions = AirportManagerServiceException.class)
    public void testRemoveAdminWithNull() {
        adminService.removeAdmin(null);
    }
}
