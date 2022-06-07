package cz.fi.muni.pa165.airport_manager.facade;

import cz.fi.muni.pa165.airport_manager.dto.AirplaneDto;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.service.AirplaneService;
import cz.fi.muni.pa165.airport_manager.service.BeanMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pavel Sklenar
 * This class represents implementation of AirplaneFacade
 */

@Service
public class AirplaneFacadeImpl implements AirplaneFacade {

    @Autowired
    private AirplaneService airplaneService;

    @Autowired
    private BeanMappingService beanMappingService;

    @Override
    public void createAirplane(AirplaneDto airplane, String email) {
        if(airplane == null)
            throw new IllegalArgumentException("Airplane DTO is null");
        if(email == null)
            throw new IllegalArgumentException("Email is null");
        var mappedAirplane = beanMappingService.mapTo(airplane, Airplane.class);
        airplaneService.createAirplane(mappedAirplane, email);
    }

    @Override
    public List<AirplaneDto> findAllAirplanes() {
        return beanMappingService.mapTo(airplaneService.findAllAirplanes(), AirplaneDto.class);
    }

    @Override
    public AirplaneDto findAirplaneById(Long id) {
        if (id == null) throw new IllegalArgumentException("Id is null");

        var airplane = airplaneService.findAirplaneById(id);
        return airplane == null ? null : beanMappingService.mapTo(airplane, AirplaneDto.class);
    }

    @Override
    public void updateAirplane(AirplaneDto airplane) {
        if(airplane == null)
            throw new IllegalArgumentException("Airplane DTO is null");
        var mappedAirplane = beanMappingService.mapTo(airplane, Airplane.class);
        airplaneService.updateAirplane(mappedAirplane);
    }

    @Override
    public void removeAirplane(Long airplaneId) {
        airplaneService.removeAirplane(airplaneId);
    }

    @Override
    public List<AirplaneDto> findAllAvailableAirplanesForPeriod(LocalDateTime from, LocalDateTime to) {
        var availableAirplanes = airplaneService.findAllAvailableAirplanesForPeriod(from, to);
        return beanMappingService.mapTo(availableAirplanes, AirplaneDto.class);
    }

}
