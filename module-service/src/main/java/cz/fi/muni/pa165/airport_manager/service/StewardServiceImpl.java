package cz.fi.muni.pa165.airport_manager.service;

import cz.fi.muni.pa165.airport_manager.dao.AdminDao;
import cz.fi.muni.pa165.airport_manager.dao.StewardDao;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.entity.Steward;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Matej Michálek
 * This class represents service layer for Steward entity.
 */

@Service
public class StewardServiceImpl implements StewardService {

    @Autowired
    private StewardDao stewardDao;
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private FlightService flightService;

    @Override
    public void createSteward(Steward steward, String email) {
        if (steward == null)
            throw new AirportManagerServiceException("Steward is null.");
        if(email == null)
            throw new AirportManagerServiceException("Email is null.");

        try {
            stewardDao.create(steward);
            var admin = adminDao.findByEmail(email);
            admin.addSteward(steward);
            adminDao.update(admin);
        } catch (NoResultException exxxx) {
            throw new IllegalArgumentException("This email doesn't exist");
        } catch (JpaSystemException e) {
            throw new AirportManagerServiceException("Steward with citizen ID " + steward.getCitizenId() + " already exists.");
        }
    }

    @Override
    public List<Steward> findAllStewards() {
        return stewardDao.findAll();
    }

    @Override
    public Steward findStewardById(Long id) {
        if (id == null)
            throw new AirportManagerServiceException("ID is null.");
        return stewardDao.findById(id);
    }

    @Override
    public void updateSteward(Steward steward) {
        if (steward == null)
            throw new AirportManagerServiceException("Steward is null.");
        try {
            stewardDao.update(steward);
        } catch (ObjectNotFoundException e) {
            throw new AirportManagerServiceException("Steward to update could not be found.", e);
        } catch (JpaSystemException e) {
            throw new AirportManagerServiceException("Steward with citizen ID " + steward.getCitizenId() + " already exists.");
        }
    }

    @Override
    public void removeSteward(Long stewardId) {
        if(stewardId == null)
            throw new AirportManagerServiceException("Steward ID is null.");
        try {
            var stewardToDelete = stewardDao.findById(stewardId);
            var admin = adminDao.findAdminOfSteward(stewardId);
            admin.removeSteward(stewardToDelete);
            adminDao.update(admin);

            var flights = new ArrayList<>(stewardToDelete.getFlights());
            flights.forEach(flight -> {
                flight.removeSteward(stewardToDelete);
                flightService.updateFlight(flight);
            });

            stewardDao.remove(stewardId);
        } catch (ObjectNotFoundException e) {
            throw new AirportManagerServiceException(e);
        }
    }

    @Override
    public List<Steward> findAllAvailableStewardsForPeriod(LocalDateTime from, LocalDateTime to) {
        if (from == null)
            throw new AirportManagerServiceException("Parameter 'from' is null.");
        if (to == null)
            throw new AirportManagerServiceException("Parameter 'to' is null.");
        if (from.isAfter(to))
            throw new AirportManagerServiceException("Invalid period, from is after to");

        var stewards = findAllStewards();

        return stewards.stream()
                .filter(s -> s.getFlights().isEmpty() || filterFlightsForGivenPeriod(s.getFlights(), from, to).isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Filters and returns only those flights that overlap with period between from and to parameter.
     * @param flights   list of flights to filter
     * @param from time of departure
     * @param to   time of arrival
     * @return list of filtered flights
     */
    private List<Flight> filterFlightsForGivenPeriod(Set<Flight> flights, LocalDateTime from, LocalDateTime to) {
        return flights.stream().filter(f -> f.getDepartureTime().isBefore(from) ?
                        f.getArrivalTime().isAfter(from)
                        : f.getDepartureTime().isBefore(to))
                .collect(Collectors.toList());
    }
}
