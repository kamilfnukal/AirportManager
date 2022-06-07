package cz.fi.muni.pa165.airport_manager.rest.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * root URL for Steward Controller
 * @author Martin Kalina
 */

@Controller
@RequestMapping("/rest")
public class MainController {

    @GetMapping()
    public String showIndex(){
        return "index.html";
    }
}
