package cz.fi.muni.pa165.airport_manager.controllers;

import cz.fi.muni.pa165.airport_manager.dto.AirportDto;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.facade.AirportFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.security.Principal;

/**
 * @author Kamil Fňukal
 * Controller which is used to manage Airport entity
 */
@Controller
@RequestMapping("/airport")
public class AirportController {

    private final static Logger log = LoggerFactory.getLogger(AirportController.class);

    @Autowired
    private AirportFacade airportFacade;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("airports", airportFacade.findAllAirports());
        return "airport/list";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        var airport = new AirportDto();
        model.addAttribute("airport", airport);
        model.addAttribute("action", "create");
        return "airport/new";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("airport") AirportDto formBean, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Principal user) {
        log.debug("create(airportCreate={})", formBean);

        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                log.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                log.trace("FieldError: {}", fe);
            }
            model.addAttribute("action", "create");
            return "airport/new";
        }

        try {
            airportFacade.createAirport(formBean, user.getName());
            redirectAttributes.addFlashAttribute("alert_success", "Airport was created successfully.");
            model.addAttribute("airports", airportFacade.findAllAirports());
            return "redirect:list";

        } catch (AirportManagerServiceException exception) {
            model.addAttribute("unique_error", exception.getMessage());
            model.addAttribute("action", "create");
            return "airport/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.debug("edit({})", id);

        var airport = airportFacade.findAirportById(id);
        model.addAttribute("airport", airport);
        model.addAttribute("action", "update");
        return "airport/new";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("airport") AirportDto formBean, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
        log.debug("update(airportUpdate={})", formBean);

        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                log.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                log.trace("FieldError: {}", fe);
            }
            model.addAttribute("action", "update");
            return "airport/new";
        }

        try {
            airportFacade.updateAirport(formBean);
            redirectAttributes.addFlashAttribute("alert_success", "Airport was updated successfully");
            model.addAttribute("airports", airportFacade.findAllAirports());
            return "redirect:list";

        } catch (AirportManagerServiceException exception) {
            model.addAttribute("unique_error", exception.getMessage());
            model.addAttribute("airport", formBean);
            model.addAttribute("action", "update");
            return "airport/new";
        }
    }

    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable long id, RedirectAttributes redirectAttributes) {
        log.debug("delete({})", id);
        try {
            airportFacade.removeAirport(id);
            redirectAttributes.addFlashAttribute("alert_success", "Airport [id: " + id + "] was deleted.");
        } catch (Exception ex) {
            log.error("airport " + id + " cannot be deleted");
            log.error(NestedExceptionUtils.getMostSpecificCause(ex).getMessage());
            redirectAttributes.addFlashAttribute("alert_danger", "Airport [id: " + id + "] cannot be deleted.");
        }
        return "redirect:../list";
    }
}
