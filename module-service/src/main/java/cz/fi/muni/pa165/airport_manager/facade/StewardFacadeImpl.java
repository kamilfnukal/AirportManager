package cz.fi.muni.pa165.airport_manager.facade;

import cz.fi.muni.pa165.airport_manager.dto.StewardDto;
import cz.fi.muni.pa165.airport_manager.entity.Steward;
import cz.fi.muni.pa165.airport_manager.service.BeanMappingService;
import cz.fi.muni.pa165.airport_manager.service.StewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Matej Michálek
 * This class represents implementation of StewardFacade
 */

@Service
public class StewardFacadeImpl implements StewardFacade {

    @Autowired
    private StewardService stewardService;

    @Autowired
    private BeanMappingService beanMappingService;

    @Override
    public void createSteward(StewardDto steward, String email) {
        if (steward == null)
            throw new IllegalArgumentException("Steward DTO is null");
        if(email == null)
            throw new IllegalArgumentException("Email is null");
        var mappedSteward = beanMappingService.mapTo(steward, Steward.class);
        stewardService.createSteward(mappedSteward, email);
    }

    @Override
    public List<StewardDto> findAllStewards() {
        return beanMappingService.mapTo(stewardService.findAllStewards(), StewardDto.class);
    }

    @Override
    public StewardDto findStewardById(Long id) {
        if (id == null) throw new IllegalArgumentException("Id is null");

        var steward = stewardService.findStewardById(id);
        return steward == null ? null : beanMappingService.mapTo(steward, StewardDto.class);
    }

    @Override
    public void updateSteward(StewardDto steward) {
        if (steward == null)
            throw new IllegalArgumentException("Steward DTO is null");
        var mappedSteward = beanMappingService.mapTo(steward, Steward.class);
        stewardService.updateSteward(mappedSteward);
    }

    @Override
    public void removeSteward(Long stewardId) {
        stewardService.removeSteward(stewardId);
    }

    @Override
    public List<StewardDto> findAllAvailableStewardsForPeriod(LocalDateTime from, LocalDateTime to) {
        var availableStewards = stewardService.findAllAvailableStewardsForPeriod(from, to);
        return beanMappingService.mapTo(availableStewards, StewardDto.class);
    }

}
