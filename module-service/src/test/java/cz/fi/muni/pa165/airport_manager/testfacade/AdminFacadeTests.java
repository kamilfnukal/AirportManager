package cz.fi.muni.pa165.airport_manager.testfacade;

import cz.fi.muni.pa165.airport_manager.config.ServiceConfiguration;
import cz.fi.muni.pa165.airport_manager.dto.AdminDto;
import cz.fi.muni.pa165.airport_manager.entity.Admin;
import cz.fi.muni.pa165.airport_manager.facade.AdminFacade;
import cz.fi.muni.pa165.airport_manager.service.AdminService;
import cz.fi.muni.pa165.airport_manager.service.BeanMappingService;
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
 * AdminFacadeTests class is used to verify correct functionality of Admin facade.
 */

@ContextConfiguration(classes= ServiceConfiguration.class)
public class AdminFacadeTests extends AbstractTransactionalTestNGSpringContextTests {

    @Mock
    private AdminService adminService;

    @Mock
    private BeanMappingService beanMappingService;

    @Autowired
    @InjectMocks
    private AdminFacade adminFacade;

    private Admin adminTom;
    private Admin adminSteve;
    private Admin adminJosh;

    private AdminDto adminTomDto = new AdminDto();
    private AdminDto adminSteveDto = new AdminDto();
    private AdminDto adminJoshDto = new AdminDto();

    @BeforeMethod
    public void prepareAdmins(){
        MockitoAnnotations.openMocks(this);
        adminTom = new Admin("Tom", "Parker", "pwd11357", "em1@em.cz", "654789123");
        adminTom.setId(1L);
        adminTomDto = DtoHelper.createDto(adminTom);

        adminSteve = new Admin("Steve", "Parker", "pwd11357", "em2@em.cz", "654789124");
        adminSteve.setId(2L);
        adminSteveDto = DtoHelper.createDto(adminSteve);

        adminJosh = new Admin("Josh", "Parker", "pwd11357", "em3@em.cz", "654789125");
        adminJosh.setId(3L);
        adminJoshDto = DtoHelper.createDto(adminJosh);

        when(adminService.findAdminById(adminTom.getId())).thenReturn(adminTom);
        when(adminService.findByEmail("em1@em.cz")).thenReturn(adminTom);
        when(beanMappingService.mapTo(adminTom, AdminDto.class)).thenReturn(adminTomDto);
        when(beanMappingService.mapTo(adminTomDto, Admin.class)).thenReturn(adminTom);

        when(adminService.findAdminById(adminSteve.getId())).thenReturn(adminSteve);
        when(beanMappingService.mapTo(adminSteve, AdminDto.class)).thenReturn(adminSteveDto);

        when(adminService.findAdminById(adminJosh.getId())).thenReturn(adminJosh);
        when(beanMappingService.mapTo(adminJosh, AdminDto.class)).thenReturn(adminJoshDto);

        when(adminService.findAllAdmins()).thenReturn(List.of(adminTom, adminSteve, adminJosh));
        when(beanMappingService.mapTo(List.of(adminTom, adminSteve, adminJosh), AdminDto.class)).thenReturn(List.of(adminTomDto, adminSteveDto, adminJoshDto));
    }

    @Test
    public void testCreateAdmin(){
        adminFacade.createAdmin(adminTomDto);
        verify(adminService, times(1)).createAdmin(adminTom);
    }

    @Test (expectedExceptions = IllegalArgumentException.class)
    public void testCreateAdminWithNull() throws IllegalArgumentException{
        adminFacade.createAdmin(null);
    }

    @Test
    public void testFindAllAdmins(){
        List<AdminDto> admins = adminFacade.findAllAdmins();
        Assert.assertNotNull(admins);
        Assert.assertEquals(admins.size(), 3);
        Assert.assertTrue(admins.contains(adminTomDto));
        Assert.assertTrue(admins.contains(adminSteveDto));
        Assert.assertTrue(admins.contains(adminJoshDto));
    }

    @Test
    public void testFindAdminById(){
        var adminDto = adminFacade.findAdminById(adminTom.getId());
        Assert.assertNotNull(adminDto);
        Assert.assertEquals(adminDto, adminTomDto);
    }

    @Test
    public void testFindAdminByEmail(){
        var adminDto = adminFacade.findByEmail("em1@em.cz");
        Assert.assertNotNull(adminDto);
        Assert.assertEquals(adminDto, adminTomDto);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindAdminByIdWhenIdNull(){
        adminFacade.findAdminById(null);
    }

    @Test
    public void testUpdateAdmin(){
        adminTomDto.setPassword("newpass");
        adminFacade.updateAdmin(adminTomDto);
        verify(adminService, times(1)).updateAdmin(adminTom);
    }

    @Test (expectedExceptions = IllegalArgumentException.class)
    public void testUpdateAdminWithNull() throws IllegalArgumentException{
        adminFacade.updateAdmin(null);
    }

    @Test
    public void testRemoveAdmin(){
        adminFacade.removeAdmin(adminTom.getId());
        verify(adminService, times(1)).removeAdmin(adminTom.getId());
    }
}