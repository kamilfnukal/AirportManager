package cz.fi.muni.pa165.airport_manager.facade;

import cz.fi.muni.pa165.airport_manager.dto.AirportDto;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.service.AirportService;
import cz.fi.muni.pa165.airport_manager.service.BeanMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Pavel Sklenar
 * This class represents implementation of AirportFacade
 */

@Service
public class AirportFacadeImpl implements AirportFacade {

    @Autowired
    private AirportService airportService;

    @Autowired
    private BeanMappingService beanMappingService;

    @Override
    public void createAirport(AirportDto airport, String email) {
        if(airport == null)
            throw new IllegalArgumentException("Airport DTO is null");
        if(email == null)
            throw new IllegalArgumentException("Email is null");
        var mappedAirplane = beanMappingService.mapTo(airport, Airport.class);
        airportService.createAirport(mappedAirplane, email);
    }

    @Override
    public List<AirportDto> findAllAirports() {
        return beanMappingService.mapTo(airportService.findAllAirports(), AirportDto.class);
    }

    @Override
    public AirportDto findAirportById(Long id) {
        if (id == null) throw new IllegalArgumentException("Id is null");

        var airport = airportService.findAirportById(id);
        return airport == null ? null : beanMappingService.mapTo(airport, AirportDto.class);
    }

    @Override
    public void updateAirport(AirportDto airport) {
        if(airport == null)
            throw new IllegalArgumentException("Airport DTO is null");
        var mappedAirplane = beanMappingService.mapTo(airport, Airport.class);
        airportService.updateAirport(mappedAirplane);
    }

    @Override
    public void removeAirport(Long airportId) {
        airportService.removeAirport(airportId);
    }

}
