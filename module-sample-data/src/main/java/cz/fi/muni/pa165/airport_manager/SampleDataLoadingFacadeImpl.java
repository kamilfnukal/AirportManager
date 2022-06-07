package cz.fi.muni.pa165.airport_manager;

import cz.fi.muni.pa165.airport_manager.entity.Admin;
import cz.fi.muni.pa165.airport_manager.entity.Manager;
import cz.fi.muni.pa165.airport_manager.entity.Steward;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.service.AdminService;
import cz.fi.muni.pa165.airport_manager.service.ManagerService;
import cz.fi.muni.pa165.airport_manager.service.StewardService;
import cz.fi.muni.pa165.airport_manager.service.AirplaneService;
import cz.fi.muni.pa165.airport_manager.service.AirportService;
import cz.fi.muni.pa165.airport_manager.service.FlightService;
import cz.fi.muni.pa165.airport_manager.enums.AirplaneManufacturer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Pavel Sklenar
 */
@Component
@Transactional
public class SampleDataLoadingFacadeImpl implements SampleDataLoadingFacade {

    final static Logger log = LoggerFactory.getLogger(SampleDataLoadingFacadeImpl.class);

    @Autowired
    private AdminService adminService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private StewardService stewardService;
    @Autowired
    private AirplaneService airplaneService;
    @Autowired
    private AirportService airportService;
    @Autowired
    private FlightService flightService;

    @Override
    @SuppressWarnings("unused")
    public void loadData() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Admin martin = createAdmin("Martin", "Velky", encoder.encode("hesloheslo5"), "velky@gmail.com", "123456785");
        Admin ondrej = createAdmin("Ondrej", "Janousek", encoder.encode("hesloheslo6"), "janousek@gmail.com", "123456784");
        log.info("Admins successfully seeded.");

        Manager pavel = createManager("Pavel", "Sklenar", encoder.encode("hesloheslo"), "sklenar@gmail.com", "123456789");
        Manager jan = createManager("Jan", "Pospisil", encoder.encode("hesloheslo2"), "pospisil@gmail.com", "123456788");
        Manager matej = createManager("Matej", "Kral", encoder.encode("hesloheslo3"), "kral@gmail.com", "123456787");
        Manager josef = createManager("Josef", "Dvorak", encoder.encode("hesloheslo4"), "dvorak@gmail.com", "123456786");
        log.info("Managers successfully seeded.");

        Airport viennaAirport = createAirport("Austria", "Vienna", "Vienna International Airport", "VIE", martin.getEmail());
        Airport munichAirport = createAirport("Germany", "Munich", "Munich Airport", "MUC", martin.getEmail());
        Airport pragueAirport = createAirport("Czech republic", "Prague", "Václav Havel Airport Prague", "PRG", martin.getEmail());
        Airport atlantaAirport = createAirport("Georgie", "Atlanta", "Hartsfield-Jackson Airport", "ATL", ondrej.getEmail());
        Airport laAirport = createAirport("California", "Los Angeles", "Los Angeles International Airport", "LAX", ondrej.getEmail());
        Airport heathrowAirport = createAirport("England", "London", "Heathrow Airport", "LHR", ondrej.getEmail());
        log.info("Airports successfully seeded.");

        Airplane boeing = createAirplane(AirplaneManufacturer.BOEING, 420, "770", "H65FDG9S8G46S6D5G", martin.getEmail());
        Airplane boeing2 = createAirplane(AirplaneManufacturer.BOEING, 210, "650", "65SDGS8SVS568AFW5", martin.getEmail());
        Airplane embraer = createAirplane(AirplaneManufacturer.EMBRAER, 180, "250", "ASF8FAW65F48CA654", ondrej.getEmail());
        Airplane embraer2 = createAirplane(AirplaneManufacturer.EMBRAER, 300, "200", "5ER5L65848Z411439", ondrej.getEmail());
        Airplane bombardier = createAirplane(AirplaneManufacturer.BOMBARDIER, 150, "450", "Y48R518456S654861", martin.getEmail());
        Airplane bombardier2 = createAirplane(AirplaneManufacturer.BOMBARDIER, 200, "400", "GS98GS846S51681SE", ondrej.getEmail());
        log.info("Airplanes successfully seeded.");

        Steward stewardJohn = createSteward("John", "Stone", "1", martin.getEmail());
        Steward stewardSteve = createSteward("Steve", "Wonder", "2", martin.getEmail());
        Steward stewardAdam = createSteward("Adam", "Woods", "3", martin.getEmail());
        Steward stewardEmanuel = createSteward("Emanuel", "Adams", "4", ondrej.getEmail());
        Steward stewardIsaac = createSteward("Isaac", "Washington", "5", ondrej.getEmail());
        Steward stewardSean = createSteward("Sean", "Kowalski", "6", ondrej.getEmail());
        log.info("Stewards successfully seeded.");

        Flight flightA = createFlight(LocalDateTime.of(2022, 7, 15, 15, 30), LocalDateTime.of(2022, 7, 15, 17, 30), viennaAirport, pragueAirport, boeing, pavel.getEmail());
        Flight flightB = createFlight(LocalDateTime.of(2022, 7, 21, 20, 30), LocalDateTime.of(2022, 7, 22, 10, 30), pragueAirport, viennaAirport, bombardier, pavel.getEmail());
        Flight flightC = createFlight(LocalDateTime.of(2022, 8, 14, 15, 30), LocalDateTime.of(2022, 8, 15, 22, 30), viennaAirport, pragueAirport, boeing, pavel.getEmail());
        Flight flightD = createFlight(LocalDateTime.of(2022, 9, 12, 18, 30), LocalDateTime.of(2022, 9, 13, 22, 30), viennaAirport, pragueAirport, bombardier, jan.getEmail());
        Flight flightE = createFlight(LocalDateTime.of(2022, 10, 11, 18, 30), LocalDateTime.of(2022, 10, 12, 22, 30), pragueAirport, viennaAirport, embraer, jan.getEmail());
        Flight flightF = createFlight(LocalDateTime.of(2022, 12, 10, 17, 30), LocalDateTime.of(2022, 12, 10, 23, 30), laAirport, atlantaAirport, embraer2, jan.getEmail());
        Flight flightG = createFlight(LocalDateTime.of(2022, 11, 9, 15, 30), LocalDateTime.of(2022, 11, 9, 22, 30), heathrowAirport, atlantaAirport, bombardier2, pavel.getEmail());
        Flight flightH = createFlight(LocalDateTime.of(2022, 11, 11, 15, 30), LocalDateTime.of(2022, 11, 11, 20, 30), pragueAirport, heathrowAirport, boeing2, pavel.getEmail());
        Flight flightI = createFlight(LocalDateTime.of(2022, 8, 10, 18, 30), LocalDateTime.of(2022, 8, 10, 19, 50), pragueAirport, munichAirport, boeing, josef.getEmail());
        log.info("Flights successfully seeded.");

        flightA.setStewards(Set.of(stewardJohn, stewardSteve));
        flightB.setStewards(Set.of(stewardAdam, stewardEmanuel));
        flightC.setStewards(Set.of(stewardIsaac, stewardSean));
        flightD.setStewards(Set.of(stewardJohn, stewardSteve));
        flightE.setStewards(Set.of(stewardAdam, stewardEmanuel));
        flightF.setStewards(Set.of(stewardIsaac, stewardSean));
        flightG.setStewards(Set.of(stewardJohn, stewardSteve));
        flightH.setStewards(Set.of(stewardAdam, stewardEmanuel));
        flightH.setStewards(Set.of(stewardIsaac, stewardSean));
        log.info("Stewards set to flights successfully.");

        pavel.setFlights(Set.of(flightA, flightB));
        jan.setFlights(Set.of(flightC, flightD, flightE));
        matej.setFlights(Set.of(flightF, flightG));
        josef.setFlights(Set.of(flightH, flightI));
        log.info("Flights set to managers successfully.");

        martin.setStewards(Set.of(stewardJohn, stewardSteve, stewardAdam));
        ondrej.setStewards(Set.of(stewardEmanuel, stewardIsaac, stewardSean));
        log.info("Stewards set to admins successfully.");

        stewardJohn.setFlights(Set.of(flightA, flightD, flightG));
        stewardSteve.setFlights(Set.of(flightA, flightD, flightG));
        stewardAdam.setFlights(Set.of(flightB, flightE, flightH));
        stewardEmanuel.setFlights(Set.of(flightB, flightE, flightH));
        stewardIsaac.setFlights(Set.of(flightC, flightF, flightH));
        log.info("Flights set to stewards successfully.");

        log.info("Seeding done.");
    }

    /** Method used for creating an admin */
    private Admin createAdmin(String firstName, String lastName, String password, String email, String phone) {
        Admin admin = new Admin();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setPassword(password);
        admin.setEmail(email);
        admin.setPhone(phone);
        adminService.createAdmin(admin);
        return admin;
    }

    /** Method used for creating a manager */
    private Manager createManager(String firstName, String lastName, String password, String email, String phone) {
        Manager manager = new Manager();
        manager.setFirstName(firstName);
        manager.setLastName(lastName);
        manager.setPassword(password);
        manager.setEmail(email);
        manager.setPhone(phone);
        managerService.createManager(manager);
        return manager;
    }

    /** Method used for creating an airport */
    private Airport createAirport(String country, String city, String name, String code, String userEmail) {
        Airport airport = new Airport();
        airport.setCountry(country);
        airport.setCity(city);
        airport.setName(name);
        airport.setCode(code);
        airportService.createAirport(airport, userEmail);
        return airport;
    }

    /** Method used for creating an airplane */
    private Airplane createAirplane(AirplaneManufacturer manufacturer, int capacity, String model, String serialNumber, String userEmail) {
        Airplane airplane = new Airplane();
        airplane.setManufacturer(manufacturer);
        airplane.setCapacity(capacity);
        airplane.setModel(model);
        airplane.setSerialNumber(serialNumber);
        airplaneService.createAirplane(airplane, userEmail);
        return airplane;
    }

    /** Method used for creating a steward */
    private Steward createSteward(String firstName, String lastName, String citizenId, String userEmail) {
        Steward steward = new Steward();
        steward.setFirstName(firstName);
        steward.setLastName(lastName);
        steward.setCitizenId(citizenId);
        stewardService.createSteward(steward, userEmail);
        return steward;
    }

    /** Method used for creating a flight */
    private Flight createFlight(LocalDateTime departureTime, LocalDateTime arrivalTime, Airport origin, Airport destination, Airplane airplane, String managerEmail) {
        Flight flight = new Flight();
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setAirplane(airplane);
        flightService.createFlight(flight, managerEmail);
        return flight;
    }

}
