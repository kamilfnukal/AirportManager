package cz.fi.muni.pa165.airport_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author Pavel Sklenar
 * Application boot class
 */
@SpringBootApplication
@Import({AirportManagerSampleDataConfiguration.class})
public class AirportManagerApplicationRest {

    public static void main(String[] args) {
        System.setProperty("server.servlet.context-path", "/pa165");
        System.setProperty("server.port", "9090");
        System.setProperty("server.error.include-stacktrace", "NEVER");
        SpringApplication.run(AirportManagerApplicationRest.class, args);
    }

}
