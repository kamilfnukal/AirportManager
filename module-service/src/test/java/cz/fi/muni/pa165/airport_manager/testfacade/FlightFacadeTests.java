package cz.fi.muni.pa165.airport_manager.testfacade;

import cz.fi.muni.pa165.airport_manager.config.ServiceConfiguration;
import cz.fi.muni.pa165.airport_manager.dto.AirplaneDto;
import cz.fi.muni.pa165.airport_manager.dto.AirportDto;
import cz.fi.muni.pa165.airport_manager.dto.FlightCreateDto;
import cz.fi.muni.pa165.airport_manager.dto.FlightDto;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.entity.Steward;
import cz.fi.muni.pa165.airport_manager.enums.AirplaneManufacturer;
import cz.fi.muni.pa165.airport_manager.facade.FlightFacade;
import cz.fi.muni.pa165.airport_manager.service.AirplaneService;
import cz.fi.muni.pa165.airport_manager.service.AirportService;
import cz.fi.muni.pa165.airport_manager.service.BeanMappingService;
import cz.fi.muni.pa165.airport_manager.service.FlightService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Martin Kalina
 * FlightFacadeTests class is used to verify correct functionality of Flight facade.
 */

@ContextConfiguration(classes = ServiceConfiguration.class)
public class FlightFacadeTests extends AbstractTransactionalTestNGSpringContextTests {

    @Mock
    private FlightService flightService;
    @Mock
    private AirportService airportService;
    @Mock
    private AirplaneService airplaneService;
    @Mock
    private StewardService stewardService;

    @Mock
    private BeanMappingService beanMappingService;

    @Autowired
    @InjectMocks
    private FlightFacade flightFacade;

    private Flight flightA;
    private Flight flightB;
    private Flight flightC;
    private Flight flightD;
    private Flight flightE;
    private Flight flightBWithStewards;
    private Flight flightCreate;
    private Flight flightCreateEmpty;

    private FlightDto flightADto = new FlightDto();
    private FlightDto flightBDto = new FlightDto();
    private FlightDto flightCDto = new FlightDto();
    private FlightDto flightDDto = new FlightDto();
    private FlightDto flightEDto = new FlightDto();
    private FlightDto flightBWithStewardsDto = new FlightDto();
    private FlightCreateDto flightWithStewardsCreateDto = new FlightCreateDto();

    private Airplane bombardier;
    private AirplaneDto bombardierDto;

    private Steward stewardJohn;
    private Steward stewardSteve;

    private Airport airportA;
    private AirportDto airportADto;
    private Airport airportB;
    private AirportDto airportBDto;

    @BeforeMethod
    public void prepareFlights() {
        MockitoAnnotations.openMocks(this);
        airportA = new Airport("Austria", "Vienna", "Vienna International Airport", "VIE");
        airportA.setId(1L);
        airportB = new Airport("France", "Paris", "Paris Charles de Gaulle Airport", "CDG");
        airportB.setId(2L);
        airportADto = DtoHelper.createDto(airportA);
        airportBDto = DtoHelper.createDto(airportB);
        bombardier = new Airplane(AirplaneManufacturer.BOMBARDIER, 104, "CRJ1000", "2");
        bombardier.setId(1L);
        bombardierDto = DtoHelper.createDto(bombardier);

        stewardJohn = new Steward("John", "Stone", "1");
        stewardJohn.setId(1L);
        stewardSteve = new Steward("Steve", "Wonder", "2");
        stewardSteve.setId(2L);

        flightA = new Flight(
                LocalDateTime.of(2022, 7, 15, 15, 30),
                LocalDateTime.of(2022, 7, 15, 17, 30),
                airportA,
                airportB,
                bombardier
        );
        flightA.setId(1L);
        flightADto = DtoHelper.createDto(flightA);

        flightB = new Flight(
                LocalDateTime.of(2022, 7, 21, 20, 30),
                LocalDateTime.of(2022, 7, 22, 10, 30),
                airportB,
                airportA,
                bombardier
        );
        flightB.setId(2L);
        flightBDto = DtoHelper.createDto(flightB);

        flightC = new Flight(
                LocalDateTime.of(2022, 8, 14, 15, 30),
                LocalDateTime.of(2022, 8, 15, 22, 30),
                airportA,
                airportB,
                bombardier
        );
        flightC.setId(3L);
        flightCDto = DtoHelper.createDto(flightC);

        flightD = new Flight(
                LocalDateTime.of(2022, 9, 12, 18, 30),
                LocalDateTime.of(2022, 9, 13, 22, 30),
                airportA,
                airportB,
                bombardier
        );
        flightD.setId(4L);
        flightDDto = DtoHelper.createDto(flightD);

        flightE = new Flight(
                LocalDateTime.of(2022, 10, 11, 18, 30),
                LocalDateTime.of(2022, 10, 12, 22, 30),
                airportB,
                airportA,
                bombardier
        );
        flightE.setId(5L);
        flightEDto = DtoHelper.createDto(flightE);

        flightBWithStewards = new Flight(
                LocalDateTime.of(2022, 7, 15, 20, 30),
                LocalDateTime.of(2022, 7, 15, 22, 30),
                airportB,
                airportA,
                bombardier
        );
        flightBWithStewards.setId(2L);
        flightBWithStewards.addSteward(stewardJohn);
        flightBWithStewards.addSteward(stewardSteve);
        flightBWithStewardsDto = DtoHelper.createDto(flightBWithStewards);

        flightCreate = new Flight(
                LocalDateTime.of(2022, 12, 15, 20, 30),
                LocalDateTime.of(2022, 12, 15, 22, 30),
                airportB,
                airportA,
                bombardier
        );
        flightCreate.addSteward(stewardJohn);
        flightCreate.addSteward(stewardSteve);

        flightWithStewardsCreateDto = DtoHelper.createCreateDto(flightCreate);
        flightCreateEmpty = new Flight(
                flightCreate.getDepartureTime(),
                flightCreate.getArrivalTime(),
                null,
                null,
                null
        );

        when(beanMappingService.mapTo(bombardier, AirplaneDto.class)).thenReturn(bombardierDto);
        when(beanMappingService.mapTo(bombardierDto, Airplane.class)).thenReturn(bombardier);

        when(beanMappingService.mapTo(airportA, AirportDto.class)).thenReturn(airportADto);
        when(beanMappingService.mapTo(airportB, AirportDto.class)).thenReturn(airportBDto);
        when(beanMappingService.mapTo(airportADto, Airport.class)).thenReturn(airportA);
        when(beanMappingService.mapTo(airportBDto, Airport.class)).thenReturn(airportB);

        when(flightService.findFlightById(flightA.getId())).thenReturn(flightA);
        when(beanMappingService.mapTo(flightA, FlightDto.class)).thenReturn(flightADto);
        when(beanMappingService.mapTo(flightADto, Flight.class)).thenReturn(flightA);

        when(flightService.findFlightById(flightB.getId())).thenReturn(flightB);
        when(beanMappingService.mapTo(flightB, FlightDto.class)).thenReturn(flightBDto);
        when(beanMappingService.mapTo(flightBDto, Flight.class)).thenReturn(flightB);

        when(flightService.findFlightById(flightC.getId())).thenReturn(flightC);
        when(beanMappingService.mapTo(flightC, FlightDto.class)).thenReturn(flightCDto);
        when(beanMappingService.mapTo(flightCDto, Flight.class)).thenReturn(flightC);

        when(flightService.findFlightById(flightD.getId())).thenReturn(flightD);
        when(beanMappingService.mapTo(flightD, FlightDto.class)).thenReturn(flightDDto);
        when(beanMappingService.mapTo(flightDDto, Flight.class)).thenReturn(flightD);

        when(flightService.findFlightById(flightE.getId())).thenReturn(flightE);
        when(beanMappingService.mapTo(flightE, FlightDto.class)).thenReturn(flightEDto);
        when(beanMappingService.mapTo(flightEDto, Flight.class)).thenReturn(flightE);

        when(flightService.findFlightById(flightBWithStewards.getId())).thenReturn(flightBWithStewards);
        when(beanMappingService.mapTo(flightBWithStewards, FlightDto.class)).thenReturn(flightBWithStewardsDto);

        when(flightService.findAllFlights()).thenReturn(List.of(flightBWithStewards, flightA));
        when(beanMappingService.mapTo(List.of(flightBWithStewards, flightA), FlightDto.class)).thenReturn(new ArrayList<>(Arrays.asList(flightBWithStewardsDto, flightADto)));

        when(airportService.findAirportById(airportA.getId())).thenReturn(airportA);
        when(airportService.findAirportById(airportB.getId())).thenReturn(airportB);
        when(airplaneService.findAirplaneById(bombardier.getId())).thenReturn(bombardier);
        when(stewardService.findStewardById(stewardJohn.getId())).thenReturn(stewardJohn);
        when(stewardService.findStewardById(stewardSteve.getId())).thenReturn(stewardSteve);
    }

    @Test
    public void testCreateFlight() {
        when(beanMappingService.mapTo(flightWithStewardsCreateDto, Flight.class)).thenReturn(flightCreateEmpty);
        var email = "email@test.cz";
        flightFacade.createFlight(flightWithStewardsCreateDto, email);

        verify(flightService, times(1)).createFlight(flightCreateEmpty, email);
        Assert.assertEquals(flightCreateEmpty.getAirplane(), bombardier);
        Assert.assertEquals(flightCreateEmpty.getOrigin(), airportB);
        Assert.assertEquals(flightCreateEmpty.getDestination(), airportA);
        Assert.assertEquals(flightCreateEmpty.getStewards().size(), 2);
        Assert.assertTrue(flightCreateEmpty.getStewards().contains(stewardSteve));
        Assert.assertTrue(flightCreateEmpty.getStewards().contains(stewardJohn));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreateFlightWithNullFlight() {
        var email = "test@email.com";
        flightFacade.createFlight(null, email);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreateFlightWithNullEmail() {
        flightFacade.createFlight(flightWithStewardsCreateDto, null);
    }

    @Test
    public void testFindAllFlights() {
        List<FlightDto> flights = flightFacade.findAllFlights();

        var expectedFlightsInOrder = List.of(flightADto, flightBWithStewardsDto);
        Assert.assertNotNull(flights);
        Assert.assertEquals(flights.size(), 2);
        Assert.assertTrue(flights.contains(flightADto));
        Assert.assertTrue(flights.contains(flightBWithStewardsDto));
        Assert.assertEquals(flights, expectedFlightsInOrder);
    }

    @Test
    public void testFindFlightById() {
        var flightDto = flightFacade.findFlightById(flightBWithStewards.getId());
        Assert.assertNotNull(flightDto);
        Assert.assertEquals(flightDto, flightBWithStewardsDto);
        Assert.assertEquals(flightDto.getStewards().size(), flightBWithStewardsDto.getStewards().size());
        Assert.assertEqualsNoOrder(flightDto.getStewards().toArray(), flightBWithStewardsDto.getStewards().toArray());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindFlightByIdWhenIdNull() {
        flightFacade.findFlightById(null);
    }

    @Test
    public void testUpdateFlight() {
        flightWithStewardsCreateDto.setOriginId(airportADto.getId());
        flightWithStewardsCreateDto.setDestinationId(airportBDto.getId());
        var updatedStewards = new HashSet<Long>();
        updatedStewards.add(stewardSteve.getId());
        flightWithStewardsCreateDto.setStewardIds(updatedStewards);

        when(beanMappingService.mapTo(flightWithStewardsCreateDto, Flight.class)).thenReturn(flightCreateEmpty);

        flightFacade.updateFlight(flightWithStewardsCreateDto);

        verify(flightService, times(1)).updateFlight(flightCreateEmpty);
        Assert.assertEquals(flightCreateEmpty.getAirplane(), bombardier);
        Assert.assertEquals(flightCreateEmpty.getOrigin(), airportA);
        Assert.assertEquals(flightCreateEmpty.getDestination(), airportB);
        Assert.assertEquals(flightCreateEmpty.getStewards().size(), 1);
        Assert.assertTrue(flightCreateEmpty.getStewards().contains(stewardSteve));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateFlightWithNull() {
        flightFacade.updateFlight(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveFlight() {
        flightFacade.removeFlight(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveFlightWithNull() {
        flightFacade.removeFlight(null);
    }

    @Test
    public void testGetDepartureFlights() {
        when(flightService.getDepartureFlights(airportA)).thenReturn(List.of(flightA, flightC, flightD));
        when(beanMappingService.mapTo(List.of(flightA, flightC, flightD), FlightDto.class)).thenReturn(List.of(flightADto, flightCDto, flightDDto));

        List<FlightDto> flights = flightFacade.getDepartureFlights(airportADto);
        Assert.assertNotNull(flights);
        Assert.assertEquals(flights.size(), 3);
        Assert.assertTrue(flights.contains(flightADto));
        Assert.assertTrue(flights.contains(flightCDto));
        Assert.assertTrue(flights.contains(flightDDto));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetDepartureFlightsWithNullOrigin() {
        flightFacade.getDepartureFlights(null);
    }

    @Test
    public void testGetArrivalFlights() {
        when(flightService.getDepartureFlights(airportA)).thenReturn(List.of(flightB, flightE));
        when(beanMappingService.mapTo(List.of(flightB, flightE), FlightDto.class)).thenReturn(List.of(flightBDto, flightEDto));

        List<FlightDto> flights = flightFacade.getDepartureFlights(airportADto);
        Assert.assertNotNull(flights);
        Assert.assertEquals(flights.size(), 2);
        Assert.assertTrue(flights.contains(flightBDto));
        Assert.assertTrue(flights.contains(flightEDto));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetArrivalFlightsWithNullDestination() {
        flightFacade.getArrivalFlights(null);
    }

    @Test
    public void testGetFlightsByAirplane() {
        when(flightService.getFlightsByAirplane(bombardier)).thenReturn(List.of(flightA, flightB, flightC, flightD, flightE));
        when(beanMappingService.mapTo(List.of(flightA, flightB, flightC, flightD, flightE), FlightDto.class)).thenReturn(List.of(flightADto, flightBDto, flightCDto, flightDDto, flightEDto));

        List<FlightDto> flights = flightFacade.getFlightsByAirplane(bombardierDto);
        Assert.assertNotNull(flights);
        Assert.assertEquals(flights.size(), 5);
        Assert.assertTrue(flights.contains(flightADto));
        Assert.assertTrue(flights.contains(flightBDto));
        Assert.assertTrue(flights.contains(flightCDto));
        Assert.assertTrue(flights.contains(flightDDto));
        Assert.assertTrue(flights.contains(flightEDto));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetFlightsByAirplaneWithNullAirplane() {
        flightFacade.getFlightsByAirplane(null);
    }

}