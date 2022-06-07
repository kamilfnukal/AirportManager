package cz.fi.muni.pa165.airport_manager.testfacade;

import cz.fi.muni.pa165.airport_manager.config.ServiceConfiguration;
import cz.fi.muni.pa165.airport_manager.dto.ManagerDto;
import cz.fi.muni.pa165.airport_manager.entity.Manager;
import cz.fi.muni.pa165.airport_manager.facade.ManagerFacade;
import cz.fi.muni.pa165.airport_manager.service.BeanMappingService;
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

import static cz.fi.muni.pa165.airport_manager.testfacade.DtoHelper.createDto;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Kamil Fňukal
 * ManagerFacadeTests class is used to verify correct functionality of Manager facade.
 */

@ContextConfiguration(classes= ServiceConfiguration.class)
public class ManagerFacadeTests extends AbstractTransactionalTestNGSpringContextTests {

    @Mock
    private ManagerService managerService;

    @Mock
    private BeanMappingService beanMappingService;

    @Autowired
    @InjectMocks
    private ManagerFacade managerFacade;

    private Manager managerTom;
    private Manager managerSteve;
    private Manager managerJosh;

    private ManagerDto managerTomDto = new ManagerDto();
    private ManagerDto managerSteveDto = new ManagerDto();
    private ManagerDto managerJoshDto = new ManagerDto();

    @BeforeMethod
    public void prepareManagers(){
        MockitoAnnotations.openMocks(this);
        managerTom = new Manager("Tom", "Parker", "pwd11357", "em1@em.cz", "654789123");
        managerTom.setId(1L);
        managerTomDto = createDto(managerTom);

        managerSteve = new Manager("Steve", "Parker", "pwd11357", "em2@em.cz", "654789124");
        managerSteve.setId(2L);
        managerSteveDto = createDto(managerSteve);

        managerJosh = new Manager("Josh", "Parker", "pwd11357", "em3@em.cz", "654789125");
        managerJosh.setId(3L);
        managerJoshDto = createDto(managerJosh);


        when(managerService.findManagerById(managerTom.getId())).thenReturn(managerTom);
        when(managerService.findByEmail("em1@em.cz")).thenReturn(managerTom);
        when(beanMappingService.mapTo(managerTom, ManagerDto.class)).thenReturn(managerTomDto);
        when(beanMappingService.mapTo(managerTomDto, Manager.class)).thenReturn(managerTom);

        when(managerService.findManagerById(managerSteve.getId())).thenReturn(managerSteve);
        when(beanMappingService.mapTo(managerSteve, ManagerDto.class)).thenReturn(managerSteveDto);

        when(managerService.findManagerById(managerJosh.getId())).thenReturn(managerJosh);
        when(beanMappingService.mapTo(managerJosh, ManagerDto.class)).thenReturn(managerJoshDto);

        when(managerService.findAllManagers()).thenReturn(List.of(managerTom, managerSteve, managerJosh));
        when(beanMappingService.mapTo(List.of(managerTom, managerSteve, managerJosh), ManagerDto.class)).thenReturn(List.of(managerTomDto, managerSteveDto, managerJoshDto));
    }

    @Test
    public void testCreateManager(){
        managerFacade.createManager(managerTomDto);
        verify(managerService, times(1)).createManager(managerTom);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreateManagerWithNull(){
        managerFacade.createManager(null);
    }

    @Test
    public void testFindAllManagers(){
        List<ManagerDto> managers = managerFacade.findAllManagers();
        Assert.assertNotNull(managers);
        Assert.assertEquals(managers.size(), 3);
        Assert.assertTrue(managers.contains(managerTomDto));
        Assert.assertTrue(managers.contains(managerSteveDto));
        Assert.assertTrue(managers.contains(managerJoshDto));
    }

    @Test
    public void testFindManagerById() {
        var managerDto = managerFacade.findManagerById(managerTom.getId());
        Assert.assertNotNull(managerDto);
        Assert.assertEquals(managerDto, managerTomDto);
    }

    @Test
    public void testFindManagerByEmail() {
        var managerDto = managerFacade.findByEmail("em1@em.cz");
        Assert.assertNotNull(managerDto);
        Assert.assertEquals(managerDto, managerTomDto);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindManagerByIdWhenIdNull(){
        managerFacade.findManagerById(null);
    }

    @Test
    public void testUpdateManager() {
        managerTomDto.setPassword("newpass");
        managerFacade.updateManager(managerTomDto);
        verify(managerService, times(1)).updateManager(managerTom);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateManagerWithNull() throws IllegalArgumentException{
        managerFacade.updateManager(null);
    }

    @Test
    public void testRemoveManager(){
        managerFacade.removeManager(managerTom.getId());
        verify(managerService, times(1)).removeManager(managerTom.getId());
    }

}