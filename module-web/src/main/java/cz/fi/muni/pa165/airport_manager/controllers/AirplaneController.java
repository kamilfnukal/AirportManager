package cz.fi.muni.pa165.airport_manager.controllers;

import cz.fi.muni.pa165.airport_manager.dto.AirplaneDto;
import cz.fi.muni.pa165.airport_manager.enums.AirplaneManufacturer;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.facade.AirplaneFacade;
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
 * @author Pavel Sklenar
 * Controller which is used to manage Airplane entity
 */
@Controller
@RequestMapping("/airplane")
public class AirplaneController {

    private final static Logger log = LoggerFactory.getLogger(AirplaneController.class);

    @Autowired
    private AirplaneFacade airplaneFacade;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("airplanes", airplaneFacade.findAllAirplanes());
        return "airplane/list";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        var airplane = new AirplaneDto();
        model.addAttribute("airplane", airplane);
        model.addAttribute("manufacturers", AirplaneManufacturer.values());
        model.addAttribute("action", "create");
        return "airplane/new";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("airplane") AirplaneDto formBean, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Principal user) {
        log.debug("create(airplaneCreate={})", formBean);

        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                log.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                log.trace("FieldError: {}", fe);
            }
            model.addAttribute("manufacturers", AirplaneManufacturer.values());
            model.addAttribute("action", "create");
            return "airplane/new";
        }

        try {
            airplaneFacade.createAirplane(formBean, user.getName());
            redirectAttributes.addFlashAttribute("alert_success", "Airplane was created successfully.");
            model.addAttribute("airplanes", airplaneFacade.findAllAirplanes());
            return "redirect:list";

        } catch (AirportManagerServiceException exception) {
            model.addAttribute("unique_error", exception.getMessage());
            model.addAttribute("manufacturers", AirplaneManufacturer.values());
            model.addAttribute("action", "create");
            return "airplane/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.debug("edit({})", id);

        var airplane = airplaneFacade.findAirplaneById(id);
        model.addAttribute("airplane", airplane);
        model.addAttribute("manufacturers", AirplaneManufacturer.values());
        model.addAttribute("action", "update");
        return "airplane/new";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("airplane") AirplaneDto formBean, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
        log.debug("update(airplaneUpdate={})", formBean);

        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                log.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                log.trace("FieldError: {}", fe);
            }
            model.addAttribute("manufacturers", AirplaneManufacturer.values());
            model.addAttribute("action", "update");
            return "airplane/new";
        }

        try {
            airplaneFacade.updateAirplane(formBean);
            redirectAttributes.addFlashAttribute("alert_success", "Airplane was updated successfully.");
            model.addAttribute("airplanes", airplaneFacade.findAllAirplanes());
            return "redirect:list";

        } catch (AirportManagerServiceException exception) {
            model.addAttribute("unique_error", exception.getMessage());
            model.addAttribute("airplane", formBean);
            model.addAttribute("manufacturers", AirplaneManufacturer.values());
            model.addAttribute("action", "update");
            return "airplane/new";
        }
    }

    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable long id, RedirectAttributes redirectAttributes) {
        log.debug("delete({})", id);
        try {
            airplaneFacade.removeAirplane(id);
            redirectAttributes.addFlashAttribute("alert_success", "Airplane [id: " + id + "] was deleted.");
        } catch (Exception ex) {
            log.error("airplane "+id+" cannot be deleted");
            log.error(NestedExceptionUtils.getMostSpecificCause(ex).getMessage());
            redirectAttributes.addFlashAttribute("alert_danger", "Airplane [id: " + id + "] cannot be deleted.");
        }
        return "redirect:../list";
    }
}
