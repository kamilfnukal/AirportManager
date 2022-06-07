package cz.fi.muni.pa165.airport_manager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Pavel Sklenar
 * Controller which is used to manage login for manager and admins users
 */
@Controller
public class LoginController {

    @GetMapping("/")
    public String loginBase() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

}
