package cz.fi.muni.pa165.airport_manager.testfacade;

import cz.fi.muni.pa165.airport_manager.config.ServiceConfiguration;
import cz.fi.muni.pa165.airport_manager.dto.AirportDto;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.facade.AirportFacade;
import cz.fi.muni.pa165.airport_manager.service.AirportService;
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
 * @author Pavel Sklenar
 * AirportFacadeTests class is used to verify correct functionality of Airport facade.
 */

@ContextConfiguration(classes= ServiceConfiguration.class)
public class AirportFacadeTests extends AbstractTransactionalTestNGSpringContextTests {

    @Mock
    private AirportService airportService;

    @Mock
    private BeanMappingService beanMappingService;

    @Autowired
    @InjectMocks
    private AirportFacade airportFacade;

    private Airport viennaAirport;
    private Airport munichAirport;
    private Airport pragueAirport;

    private AirportDto viennaAirportDto = new AirportDto();
    private AirportDto munichAirportDto = new AirportDto();
    private AirportDto pragueAirportDto = new AirportDto();

    @BeforeMethod
    public void prepareAirports(){
        MockitoAnnotations.openMocks(this);
        viennaAirport = new Airport("Austria", "Vienna", "Vienna International Airport", "VIE");
        viennaAirport.setId(1L);
        viennaAirportDto = DtoHelper.createDto(viennaAirport);

        munichAirport = new Airport("Germany", "Munich", "Munich Airport", "MUC");
        munichAirport.setId(2L);
        munichAirportDto = DtoHelper.createDto(munichAirport);

        pragueAirport = new Airport("Czech republic", "Prague", "Václav Havel Airport Prague", "PRG");
        pragueAirport.setId(3L);
        pragueAirportDto = DtoHelper.createDto(pragueAirport);

        when(airportService.findAirportById(viennaAirport.getId())).thenReturn(viennaAirport);
        when(beanMappingService.mapTo(viennaAirport, AirportDto.class)).thenReturn(viennaAirportDto);
        when(beanMappingService.mapTo(viennaAirportDto, Airport.class)).thenReturn(viennaAirport);

        when(airportService.findAirportById(munichAirport.getId())).thenReturn(munichAirport);
        when(beanMappingService.mapTo(munichAirport, AirportDto.class)).thenReturn(munichAirportDto);

        when(airportService.findAirportById(pragueAirport.getId())).thenReturn(pragueAirport);
        when(beanMappingService.mapTo(pragueAirport, AirportDto.class)).thenReturn(pragueAirportDto);

        when(airportService.findAllAirports()).thenReturn(List.of(viennaAirport, munichAirport, pragueAirport));
        when(beanMappingService.mapTo(List.of(viennaAirport, munichAirport, pragueAirport), AirportDto.class)).thenReturn(List.of(viennaAirportDto, munichAirportDto, pragueAirportDto));
    }

    @Test
    public void testCreateAirport() {
        var email = "test@email.com";
        airportFacade.createAirport(viennaAirportDto, email);
        verify(airportService, times(1)).createAirport(viennaAirport, email);
    }

    @Test (expectedExceptions = IllegalArgumentException.class)
    public void testCreateAirportWithAirportNull() throws IllegalArgumentException  {
        var email = "test@email.com";
        airportFacade.createAirport(null, email);
    }

    @Test (expectedExceptions = IllegalArgumentException.class)
    public void testCreateAirportWithEmailNull() throws IllegalArgumentException  {
        airportFacade.createAirport(viennaAirportDto, null);
    }

    @Test
    public void testFindAllAirports() {
        List<AirportDto> airports = airportFacade.findAllAirports();
        Assert.assertNotNull(airports);
        Assert.assertEquals(airports.size(), 3);
        Assert.assertTrue(airports.contains(viennaAirportDto));
        Assert.assertTrue(airports.contains(munichAirportDto));
        Assert.assertTrue(airports.contains(pragueAirportDto));
    }

    @Test
    public void testFindAirportById() {
        var airportDto = airportFacade.findAirportById(viennaAirport.getId());
        Assert.assertNotNull(airportDto);
        Assert.assertEquals(airportDto, viennaAirportDto);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindAirportByIdWhenIdNull() {
        airportFacade.findAirportById(null);
    }

    @Test
    public void testUpdateAirport() {
        viennaAirportDto.setCode("VIO");
        airportFacade.updateAirport(viennaAirportDto);
        verify(airportService, times(1)).updateAirport(viennaAirport);
    }

    @Test (expectedExceptions = IllegalArgumentException.class)
    public void testUpdateAirportWithNull() throws IllegalArgumentException {
        airportFacade.updateAirport(null);
    }

    @Test
    public void testRemoveAirport() {
        airportFacade.removeAirport(viennaAirport.getId());
        verify(airportService, times(1)).removeAirport(viennaAirport.getId());
    }

}