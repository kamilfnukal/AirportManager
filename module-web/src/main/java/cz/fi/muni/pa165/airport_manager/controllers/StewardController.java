package cz.fi.muni.pa165.airport_manager.controllers;

import cz.fi.muni.pa165.airport_manager.dto.StewardDto;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.facade.StewardFacade;
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
 * @author Martin Kalina
 * Controller which is used to manage Steward entity
 */

@Controller
@RequestMapping("/steward")
public class StewardController {

    private final static Logger log = LoggerFactory.getLogger(StewardController.class);

    @Autowired
    private StewardFacade stewardFacade;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("stewards", stewardFacade.findAllStewards());
        return "steward/list";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model){
        StewardDto steward = new StewardDto();
        model.addAttribute("steward", steward);
        model.addAttribute("action", "create");
        return "steward/new";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("steward") StewardDto formBean, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Principal user) {
        log.debug("create(stewardCreate={})", formBean);

        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                log.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                log.trace("FieldError: {}", fe);
            }
            model.addAttribute("action", "create");
            model.addAttribute("stewards", stewardFacade.findAllStewards());
            return "steward/new";
        }

        try {
            stewardFacade.createSteward(formBean, user.getName());
            redirectAttributes.addFlashAttribute("alert_success", "Steward was created successfully.");

            model.addAttribute("stewards", stewardFacade.findAllStewards());
            return "redirect:list";
        }
        catch (AirportManagerServiceException exception) {
            model.addAttribute("stewards", stewardFacade.findAllStewards());
            model.addAttribute("action", "create");
            model.addAttribute("unique_error", exception.getMessage());
            return "steward/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.debug("edit({})", id);

        var steward = stewardFacade.findStewardById(id);
        model.addAttribute("steward", steward);
        model.addAttribute("action", "update");
        return "steward/new";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("steward") StewardDto formBean, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
        log.debug("update(stewardUpdate={})", formBean);

        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                log.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                log.trace("FieldError: {}", fe);
            }
            model.addAttribute("action", "update");
            return "steward/new";
        }

        try {
            stewardFacade.updateSteward(formBean);
            redirectAttributes.addFlashAttribute("alert_success", "Steward was updated successfully.");
            model.addAttribute("stewards", stewardFacade.findAllStewards());
            return "redirect:list";

        } catch (AirportManagerServiceException exception) {
            model.addAttribute("steward", formBean);
            model.addAttribute("action", "update");
            model.addAttribute("unique_error", exception.getMessage());
            return "steward/new";
        }
    }

    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            stewardFacade.removeSteward(id);
            redirectAttributes.addFlashAttribute("alert_success", "Steward [id: " + id + "] was deleted.");
        } catch (Exception ex) {
            log.error("steward "+id+" cannot be deleted");
            log.error(NestedExceptionUtils.getMostSpecificCause(ex).getMessage());
            redirectAttributes.addFlashAttribute("alert_danger", "Steward [id: " + id + "] cannot be deleted.");
        }
        return "redirect:../list";
    }

}
