package cz.fi.muni.pa165.airport_manager.service;

import cz.fi.muni.pa165.airport_manager.dao.AdminDao;
import cz.fi.muni.pa165.airport_manager.dao.AirportDao;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Pavel Sklenar
 * This class represents service layer for Airport entity.
 */

@Service
public class AirportServiceImpl implements AirportService {

    @Autowired
    private AirportDao airportDao;
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private FlightService flightService;


    @Override
    public void createAirport(Airport airport, String email) {
        if(airport == null)
            throw new AirportManagerServiceException("Airport is null.");
        if(email == null)
            throw new AirportManagerServiceException("Email is null.");
        try {
            airportDao.create(airport);

            var admin = adminDao.findByEmail(email);
            admin.addAirport(airport);
            adminDao.update(admin);
        } catch (JpaSystemException e) {
            throw new AirportManagerServiceException("Airport with IATA code " + airport.getCode() + " already exists.");
        }
    }

    @Override
    public List<Airport> findAllAirports() {
        return airportDao.findAll();
    }

    @Override
    public Airport findAirportById(Long id) {
        if (id == null)
            throw new AirportManagerServiceException("ID is null.");
        return airportDao.findById(id);
    }

    @Override
    public void updateAirport(Airport airport) {
        if(airport == null)
            throw new AirportManagerServiceException("Airport is null.");
        try {
            airportDao.update(airport);
        } catch (ObjectNotFoundException e) {
            throw new AirportManagerServiceException(e);
        } catch (JpaSystemException e) {
            throw new AirportManagerServiceException("Airport with IATA code " + airport.getCode() + " already exists.");
        }
    }

    @Override
    public void removeAirport(Long airportId) {
        if(airportId == null)
            throw new AirportManagerServiceException("Airport ID is null.");
        try {
            var airportToDelete = airportDao.findById(airportId);
            var admin = adminDao.findAdminOfAirport(airportId);
            admin.removeAirport(airportToDelete);
            adminDao.update(admin);

            flightService.getArrivalFlights(airportToDelete).forEach(f -> flightService.removeFlight(f));
            flightService.getDepartureFlights(airportToDelete).forEach(f -> flightService.removeFlight(f));

            airportDao.remove(airportId);
        } catch (ObjectNotFoundException e) {
            throw new AirportManagerServiceException(e);
        }
    }

}
