package cz.fi.muni.pa165.airport_manager.testfacade;

import cz.fi.muni.pa165.airport_manager.config.ServiceConfiguration;
import cz.fi.muni.pa165.airport_manager.dto.StewardDto;
import cz.fi.muni.pa165.airport_manager.entity.Steward;
import cz.fi.muni.pa165.airport_manager.facade.StewardFacade;
import cz.fi.muni.pa165.airport_manager.service.BeanMappingService;
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
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Matej Michálek
 * StewardFacadeTests class is used to verify correct functionality of Steward facade.
 */

@ContextConfiguration(classes = ServiceConfiguration.class)
public class StewardFacadeTests extends AbstractTransactionalTestNGSpringContextTests {

    @Mock
    private StewardService stewardService;

    @Mock
    private BeanMappingService beanMappingService;

    @Autowired
    @InjectMocks
    private StewardFacade stewardFacade;

    private Steward stewardJohn;
    private Steward stewardSteve;
    private Steward stewardAdam;

    private StewardDto stewardJohnDto = new StewardDto();
    private StewardDto stewardSteveDto = new StewardDto();
    private StewardDto stewardAdamDto = new StewardDto();

    @BeforeMethod
    public void prepareStewards() {
        MockitoAnnotations.openMocks(this);
        stewardJohn = new Steward("John", "Stone", "1");
        stewardJohn.setId(1L);
        stewardJohnDto = DtoHelper.createDto(stewardJohn);

        stewardSteve = new Steward("Steve", "Wonder", "2");
        stewardSteve.setId(2L);
        stewardSteveDto = DtoHelper.createDto(stewardSteve);

        stewardAdam = new Steward("Adam", "Woods", "3");
        stewardAdam.setId(3L);
        stewardAdamDto = DtoHelper.createDto(stewardAdam);

        when(stewardService.findStewardById(stewardJohn.getId())).thenReturn(stewardJohn);
        when(beanMappingService.mapTo(stewardJohn, StewardDto.class)).thenReturn(stewardJohnDto);
        when(beanMappingService.mapTo(stewardJohnDto, Steward.class)).thenReturn(stewardJohn);

        when(stewardService.findStewardById(stewardSteve.getId())).thenReturn(stewardSteve);
        when(beanMappingService.mapTo(stewardSteve, StewardDto.class)).thenReturn(stewardSteveDto);

        when(stewardService.findStewardById(stewardAdam.getId())).thenReturn(stewardAdam);
        when(beanMappingService.mapTo(stewardAdam, StewardDto.class)).thenReturn(stewardAdamDto);

        when(stewardService.findAllStewards()).thenReturn(List.of(stewardJohn, stewardSteve, stewardAdam));
        when(beanMappingService.mapTo(List.of(stewardJohn, stewardSteve, stewardAdam), StewardDto.class)).thenReturn(List.of(stewardJohnDto, stewardSteveDto, stewardAdamDto));
    }

    @Test
    public void testCreateSteward() {
        var email = "test@email.com";
        stewardFacade.createSteward(stewardJohnDto, email);
        verify(stewardService, times(1)).createSteward(stewardJohn, email);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreateStewardWithNull() {
        var email = "test@email.com";
        stewardFacade.createSteward(null, email);
    }

    @Test
    public void testFindAllStewards() {
        List<StewardDto> stewards = stewardFacade.findAllStewards();
        Assert.assertNotNull(stewards);
        Assert.assertEquals(stewards.size(), 3);
        Assert.assertTrue(stewards.contains(stewardJohnDto));
        Assert.assertTrue(stewards.contains(stewardAdamDto));
        Assert.assertTrue(stewards.contains(stewardSteveDto));
    }

    @Test
    public void testFindStewardById() {
        var stewardDto = stewardFacade.findStewardById(stewardJohn.getId());
        Assert.assertNotNull(stewardDto);
        Assert.assertEquals(stewardDto, stewardJohnDto);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindStewardByIdWhenIdNull() {
        stewardFacade.findStewardById(null);
    }

    @Test
    public void testUpdateSteward() {
        stewardJohnDto.setCitizenId("55");
        stewardFacade.updateSteward(stewardJohnDto);
        verify(stewardService, times(1)).updateSteward(stewardJohn);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateStewardWithNull() throws IllegalArgumentException {
        stewardFacade.updateSteward(null);
    }

    @Test
    public void testRemoveSteward() {
        stewardFacade.removeSteward(stewardJohn.getId());
        verify(stewardService, times(1)).removeSteward(stewardJohn.getId());
    }

    @Test
    public void testFindAllAvailableStewardsForFlight() {
        var from = LocalDateTime.of(2022, 7, 15, 15, 30);
        var to = from.plusHours(5);

        when(stewardService.findAllAvailableStewardsForPeriod(from, to)).thenReturn(List.of(stewardJohn, stewardSteve));
        when(beanMappingService.mapTo(List.of(stewardJohn, stewardSteve), StewardDto.class)).thenReturn(List.of(stewardJohnDto, stewardSteveDto));

        List<StewardDto> stewards = stewardFacade.findAllAvailableStewardsForPeriod(from, to);
        Assert.assertNotNull(stewards);
        Assert.assertEquals(stewards.size(), 2);
        Assert.assertTrue(stewards.contains(stewardJohnDto));
        Assert.assertTrue(stewards.contains(stewardSteveDto));
    }

}