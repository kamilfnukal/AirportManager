package cz.fi.muni.pa165.airport_manager.config;

import cz.fi.muni.pa165.airport_manager.PersistenceAirportManagerContext;
import cz.fi.muni.pa165.airport_manager.facade.StewardFacadeImpl;
import cz.fi.muni.pa165.airport_manager.service.StewardServiceImpl;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collections;

/**
 * @author Matej Michálek
 */

@Configuration
@Import(PersistenceAirportManagerContext.class)
@ComponentScan(basePackageClasses = {StewardServiceImpl.class, StewardFacadeImpl.class})
public class ServiceConfiguration {

    @Bean
    public Mapper dozer() {
        var dozer = new DozerBeanMapper();
        // support library for missing Java8 type mappers (https://github.com/GeBeater/dozer-jdk8-support)
        dozer.setMappingFiles(Collections.singletonList("dozerJdk8Converters.xml"));
        return dozer;
    }
}