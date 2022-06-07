package cz.fi.muni.pa165.airport_manager.testfacade;

import cz.fi.muni.pa165.airport_manager.config.ServiceConfiguration;
import cz.fi.muni.pa165.airport_manager.dto.AirplaneDto;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.enums.AirplaneManufacturer;
import cz.fi.muni.pa165.airport_manager.facade.AirplaneFacade;
import cz.fi.muni.pa165.airport_manager.service.AirplaneService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Pavel Sklenar
 * AirplaneFacadeTests class is used to verify correct functionality of Airplane facade.
 */

@ContextConfiguration(classes= ServiceConfiguration.class)
public class AirplaneFacadeTests extends AbstractTransactionalTestNGSpringContextTests {

    @Mock
    private AirplaneService airplaneService;

    @Mock
    private BeanMappingService beanMappingService;

    @Autowired
    @InjectMocks
    private AirplaneFacade airplaneFacade;

    private Airplane boeing;
    private Airplane embraer;
    private Airplane bombardier;

    private AirplaneDto boeingDto = new AirplaneDto();
    private AirplaneDto embraerDto = new AirplaneDto();
    private AirplaneDto bombardierDto = new AirplaneDto();

    @BeforeMethod
    public void prepareAirplanes(){
        MockitoAnnotations.openMocks(this);
        boeing = new Airplane(AirplaneManufacturer.BOEING, 420, "770", "1");
        boeing.setId(1L);
        boeingDto = DtoHelper.createDto(boeing);

        embraer = new Airplane(AirplaneManufacturer.EMBRAER, 300, "200", "2");
        embraer.setId(2L);
        embraerDto = DtoHelper.createDto(embraer);

        bombardier = new Airplane(AirplaneManufacturer.BOMBARDIER, 200, "400", "3");
        bombardier.setId(3L);
        bombardierDto = DtoHelper.createDto(bombardier);

        when(airplaneService.findAirplaneById(boeing.getId())).thenReturn(boeing);
        when(beanMappingService.mapTo(boeing, AirplaneDto.class)).thenReturn(boeingDto);
        when(beanMappingService.mapTo(boeingDto, Airplane.class)).thenReturn(boeing);

        when(airplaneService.findAirplaneById(embraer.getId())).thenReturn(embraer);
        when(beanMappingService.mapTo(embraer, AirplaneDto.class)).thenReturn(embraerDto);

        when(airplaneService.findAirplaneById(bombardier.getId())).thenReturn(bombardier);
        when(beanMappingService.mapTo(bombardier, AirplaneDto.class)).thenReturn(bombardierDto);

        when(airplaneService.findAllAirplanes()).thenReturn(List.of(boeing, embraer, bombardier));
        when(beanMappingService.mapTo(List.of(boeing, embraer, bombardier), AirplaneDto.class)).thenReturn(List.of(boeingDto, embraerDto, bombardierDto));
    }

    @Test
    public void testCreateAirplane() {
        var email = "test@email.com";
        airplaneFacade.createAirplane(boeingDto, email);
        verify(airplaneService, times(1)).createAirplane(boeing, email);
    }

    @Test (expectedExceptions = IllegalArgumentException.class)
    public void testCreateAirplaneWithAirplaneNull() throws IllegalArgumentException  {
        var email = "test@email.com";
        airplaneFacade.createAirplane(null, email);
    }

    @Test (expectedExceptions = IllegalArgumentException.class)
    public void testCreateAirplaneWithEmailNull() throws IllegalArgumentException  {
       airplaneFacade.createAirplane(boeingDto, null);
    }

    @Test
    public void testFindAllAirplanes() {
        List<AirplaneDto> airplanes = airplaneFacade.findAllAirplanes();
        Assert.assertNotNull(airplanes);
        Assert.assertEquals(airplanes.size(), 3);
        Assert.assertTrue(airplanes.contains(boeingDto));
        Assert.assertTrue(airplanes.contains(bombardierDto));
        Assert.assertTrue(airplanes.contains(embraerDto));
    }

    @Test
    public void testFindAirplaneById() {
        var airplaneDto = airplaneFacade.findAirplaneById(boeing.getId());
        Assert.assertNotNull(airplaneDto);
        Assert.assertEquals(airplaneDto, boeingDto);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindAirplaneByIdWhenIdNull() {
        airplaneFacade.findAirplaneById(null);
    }

    @Test
    public void testUpdateAirplane() {
        boeingDto.setCapacity(155);
        airplaneFacade.updateAirplane(boeingDto);
        verify(airplaneService, times(1)).updateAirplane(boeing);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateAirplaneWithNull() throws IllegalArgumentException {
        airplaneFacade.updateAirplane(null);
    }

    @Test
    public void testRemoveAirplane() {
        airplaneFacade.removeAirplane(boeing.getId());
        verify(airplaneService, times(1)).removeAirplane(boeing.getId());
    }

    @Test
    public void testFindAllAvailableAirplanesForFlight() {
        var from = LocalDateTime.of(2022, 7, 15, 15, 30);
        var to = from.plusHours(5);

        when(airplaneService.findAllAvailableAirplanesForPeriod(from, to)).thenReturn(List.of(bombardier, boeing));
        when(beanMappingService.mapTo(List.of(bombardier, boeing), AirplaneDto.class)).thenReturn(List.of(bombardierDto, boeingDto));

        List<AirplaneDto> airplanes = airplaneFacade.findAllAvailableAirplanesForPeriod(from, to);
        Assert.assertNotNull(airplanes);
        Assert.assertEquals(airplanes.size(), 2);
        Assert.assertTrue(airplanes.contains(bombardierDto));
        Assert.assertTrue(airplanes.contains(boeingDto));
    }

}