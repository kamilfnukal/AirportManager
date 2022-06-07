package cz.fi.muni.pa165.airport_manager.controllers;

import cz.fi.muni.pa165.airport_manager.dto.DatesDto;
import cz.fi.muni.pa165.airport_manager.dto.FlightCreateDto;
import cz.fi.muni.pa165.airport_manager.dto.FlightDto;
import cz.fi.muni.pa165.airport_manager.dto.StewardDto;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.facade.AirplaneFacade;
import cz.fi.muni.pa165.airport_manager.facade.AirportFacade;
import cz.fi.muni.pa165.airport_manager.facade.FlightFacade;
import cz.fi.muni.pa165.airport_manager.facade.StewardFacade;
import cz.fi.muni.pa165.airport_manager.forms.DatesDTOValidator;
import cz.fi.muni.pa165.airport_manager.forms.FlightCreateDTOValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * @author Kamil Fňukal
 * Controller which is used to manage Flight entity
 */
@Controller
@RequestMapping("/flight")
public class FlightController {

    private final static Logger log = LoggerFactory.getLogger(FlightController.class);

    @Autowired
    private FlightFacade flightFacade;

    @Autowired
    private AirportFacade airportFacade;

    @Autowired
    private StewardFacade stewardFacade;

    @Autowired
    private AirplaneFacade airplaneFacade;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("flights", flightFacade.findAllFlights());
        return "flight/list";
    }

    @GetMapping("/dates")
    public String showDatesCreateFrom(Model model){
        var dates = new DatesDto();
        model.addAttribute("dates", dates);
        model.addAttribute("action", "create");
        return "flight/dates";
    }

    @PostMapping("/dates")
    public String createDates(@Valid @ModelAttribute("dates") DatesDto formBean,
                BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                log.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                log.trace("FieldError: {}", fe);
            }

            model.addAttribute("action", "create");
            return "flight/dates";
        }

        return showCreationForm(model, formBean);
    }

    @GetMapping("/new")
    public String showCreationForm(Model model, DatesDto dates) {
        var flight = new FlightCreateDto();
        flight.setDepartureTime(dates.getDepartureTime());
        flight.setArrivalTime(dates.getArrivalTime());
        model.addAttribute("flight", flight);
        model.addAttribute("airports", airportFacade.findAllAirports());
        model.addAttribute("all_stewards", stewardFacade.findAllAvailableStewardsForPeriod(dates.getDepartureTime(), dates.getArrivalTime()));
        model.addAttribute("airplanes", airplaneFacade.findAllAvailableAirplanesForPeriod(dates.getDepartureTime(), dates.getArrivalTime()));
        model.addAttribute("action", "create");
        return "flight/new";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("flight") FlightCreateDto formBean, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Principal user) {
        log.debug("create(flightCreate={})", formBean);

        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                log.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                log.trace("FieldError: {}", fe);
            }
            model.addAttribute("airports", airportFacade.findAllAirports());
            model.addAttribute("all_stewards", stewardFacade.findAllStewards());
            model.addAttribute("airplanes", airplaneFacade.findAllAirplanes());
            model.addAttribute("action", "create");
            return "flight/new";
        }

        try {
            flightFacade.createFlight(formBean, user.getName());
            redirectAttributes.addFlashAttribute("alert_success", "Airplane was created successfully");
            model.addAttribute("flights", flightFacade.findAllFlights());
            return "redirect:list";

        } catch (AirportManagerServiceException exception) {
            redirectAttributes.addFlashAttribute("alert_danger", exception.getMessage());
            model.addAttribute("airplane", formBean);
            model.addAttribute("airports", airportFacade.findAllAirports());
            model.addAttribute("all_stewards", stewardFacade.findAllStewards());
            model.addAttribute("airplanes", airplaneFacade.findAllAirplanes());
            model.addAttribute("action", "create");
            return "redirect:new";
        }
    }

    @GetMapping("/{id}/dates")
    public String showDatesEditForm(@PathVariable Long id, Model model) {
        log.debug("datesEdit({})", id);

        var flight = flightFacade.findFlightById(id);
        var dates = new DatesDto(flight.getDepartureTime(), flight.getArrivalTime());

        model.addAttribute("flightId", id);
        model.addAttribute("dates", dates);
        model.addAttribute("action", "update");

        return "flight/update_dates";
    }

    @PostMapping("/{id}/dates")
    public String updateDates(@PathVariable Long id, @Valid @ModelAttribute("dates") DatesDto formBean,
                              BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                log.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                log.trace("FieldError: {}", fe);
            }

            model.addAttribute("flightId", id);
            model.addAttribute("action", "update");
            return "flight/update_dates";
        }

        return showEditForm(id, model, formBean);
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, DatesDto dates) {
        log.debug("datesEdit({})", id);

        var oldFlight = flightFacade.findFlightById(id);
        var datesHaveChanged = !oldFlight.getDepartureTime().isEqual(dates.getDepartureTime()) || !oldFlight.getArrivalTime().isEqual(dates.getArrivalTime());

        var stewardIds = new HashSet<>(oldFlight.getStewards().stream().map(StewardDto::getId).collect(Collectors.toSet()));

        var flight = new FlightCreateDto(oldFlight.getId(), dates.getDepartureTime(), dates.getArrivalTime(),
                oldFlight.getOrigin().getId(), oldFlight.getDestination().getId(),
                oldFlight.getAirplane().getId(), stewardIds);

        var availableStewards = new HashSet<>(stewardFacade.findAllAvailableStewardsForPeriod(dates.getDepartureTime(), dates.getArrivalTime()));
        if(!datesHaveChanged) availableStewards.addAll(oldFlight.getStewards().stream().map(s -> stewardFacade.findStewardById(s.getId())).collect(Collectors.toList()));

        var availableAirplanes = airplaneFacade.findAllAvailableAirplanesForPeriod(dates.getDepartureTime(), dates.getArrivalTime());
        if(!datesHaveChanged) availableAirplanes.add(airplaneFacade.findAirplaneById(oldFlight.getAirplane().getId()));

        model.addAttribute("flight", flight);
        model.addAttribute("airports", airportFacade.findAllAirports());
        model.addAttribute("all_stewards", availableStewards);
        model.addAttribute("airplanes", availableAirplanes);
        model.addAttribute("action", "update");
        return "flight/edit";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("flight") FlightCreateDto formBean, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
        log.debug("update(flightUpdate={})", formBean);

        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                log.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                log.trace("FieldError: {}", fe);
            }

            model.addAttribute("airports", airportFacade.findAllAirports());
            model.addAttribute("all_stewards", stewardFacade.findAllAvailableStewardsForPeriod(formBean.getDepartureTime(), formBean.getArrivalTime()));
            model.addAttribute("airplanes", airplaneFacade.findAllAvailableAirplanesForPeriod(formBean.getDepartureTime(), formBean.getArrivalTime()));
            model.addAttribute("action", "update");
            return "flight/new";
        }

        flightFacade.updateFlight(formBean);
        redirectAttributes.addFlashAttribute("alert_success", "Flight was updated successfully.");

        model.addAttribute("flights", flightFacade.findAllFlights());
        return "redirect:list";
    }

    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable long id, RedirectAttributes redirectAttributes) {
        FlightDto flight = flightFacade.findFlightById(id);
        log.debug("delete({})", id);
        try {
            flightFacade.removeFlight(flight);
            redirectAttributes.addFlashAttribute("alert_success", "Flight [id: " + flight.getId() + "] was deleted.");
        } catch (Exception ex) {
            log.error("flight "+id+" cannot be deleted");
            log.error(NestedExceptionUtils.getMostSpecificCause(ex).getMessage());
            redirectAttributes.addFlashAttribute("alert_danger", "Flight [id: " + flight.getId() + "] cannot be deleted.");
        }
        return "redirect:../list";
    }

    /**
     * Spring Validator added to JSR-303 Validator for this @Controller only, in order to apply special validation while creating/updating flight.
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if (binder.getTarget() instanceof FlightCreateDto) {
            binder.addValidators(new FlightCreateDTOValidator());
        } else if (binder.getTarget() instanceof DatesDto) {
            binder.addValidators(new DatesDTOValidator());
        }
    }
}
