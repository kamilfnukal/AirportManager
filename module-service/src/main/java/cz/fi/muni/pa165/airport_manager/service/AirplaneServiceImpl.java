package cz.fi.muni.pa165.airport_manager.service;

import cz.fi.muni.pa165.airport_manager.dao.AdminDao;
import cz.fi.muni.pa165.airport_manager.dao.AirplaneDao;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pavel Sklenar
 * This class represents service layer for Airplane entity.
 */

@Service
public class AirplaneServiceImpl implements AirplaneService {

    @Autowired
    private AirplaneDao airplaneDao;
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private FlightService flightService;

    @Override
    public void createAirplane(Airplane airplane, String email) {
        if(airplane == null)
            throw new AirportManagerServiceException("Airplane is null.");
        if(email == null)
            throw new AirportManagerServiceException("Email is null.");
        try {
            airplaneDao.create(airplane);

            var admin = adminDao.findByEmail(email);
            admin.addAirplane(airplane);
            adminDao.update(admin);
        } catch (JpaSystemException e) {
            throw new AirportManagerServiceException("Airplane with serial number " + airplane.getSerialNumber() + " already exists.");
        }
    }

    @Override
    public List<Airplane> findAllAirplanes() {
        return airplaneDao.findAll();
    }

    @Override
    public Airplane findAirplaneById(Long id) {
        if (id == null)
            throw new AirportManagerServiceException("ID is null.");
        return airplaneDao.findById(id);
    }

    @Override
    public void updateAirplane(Airplane airplane) {
        if(airplane == null)
            throw new AirportManagerServiceException("Airplane is null.");
        try {
            airplaneDao.update(airplane);
        } catch (ObjectNotFoundException e) {
            throw new AirportManagerServiceException(e);
        } catch (JpaSystemException e) {
            throw new AirportManagerServiceException("Airplane with serial number " + airplane.getSerialNumber() + " already exists.");
        }
    }

    @Override
    public void removeAirplane(Long airplaneId) {
        if(airplaneId == null)
            throw new AirportManagerServiceException("Airplane ID is null.");
        try {
            var airplaneToDelete = airplaneDao.findById(airplaneId);
            var admin = adminDao.findAdminOfAirplane(airplaneId);
            admin.removeAirplane(airplaneToDelete);
            adminDao.update(admin);

            flightService.getFlightsByAirplane(airplaneToDelete).forEach(f -> flightService.removeFlight(f));

            airplaneDao.remove(airplaneId);
        } catch (ObjectNotFoundException e) {
            throw new AirportManagerServiceException(e);
        }
    }

    @Override
    public List<Airplane> findAllAvailableAirplanesForPeriod(LocalDateTime from, LocalDateTime to) {
        if (from == null)
            throw new AirportManagerServiceException("Parameter 'from' is null.");
        if (to == null)
            throw new AirportManagerServiceException("Parameter 'to' is null.");
        if (from.isAfter(to))
            throw new AirportManagerServiceException("Invalid period, from is after to");

        var flights = flightService.getFlightsForPeriod(from, to);
        var unavailableAirplanes = flights.stream().map(Flight::getAirplane).collect(Collectors.toSet());

        var allAirplanes = findAllAirplanes();

        var availableAirplanes = allAirplanes.stream().filter(a -> !unavailableAirplanes.contains(a)).distinct().collect(Collectors.toList());

        return availableAirplanes;
    }
}
