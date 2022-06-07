package cz.fi.muni.pa165.airport_manager.controllers;

import cz.fi.muni.pa165.airport_manager.dto.ManagerDto;
import cz.fi.muni.pa165.airport_manager.facade.ManagerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.security.Principal;

/**
 * @author Pavel Sklenar
 * Controller which is used to manage login for Manager entity
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerFacade managerFacade;

    @GetMapping("/m_profile")
    public String showManagerProfile(Model model, Principal user) {
        model.addAttribute("user", managerFacade.findByEmail(user.getName()));
        return "profile";
    }

    @PostMapping("/m_update")
    public String update(@Valid @ModelAttribute("user") ManagerDto formBean, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Principal user) {
        ManagerDto manager = managerFacade.findByEmail(user.getName());
        if (bindingResult.hasErrors()) {
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
            }
            return "profile";
        }

        formBean.setPassword(manager.getPassword());
        formBean.setEmail(manager.getEmail());
        managerFacade.updateManager(formBean);
        redirectAttributes.addFlashAttribute("alert_success", "Manager was updated successfully");
        return "redirect:m_profile";
    }

    @PostMapping("/m_update_password")
    public String updatePassword(@Valid @ModelAttribute("user") ManagerDto formBean, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Principal user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        ManagerDto manager = managerFacade.findByEmail(user.getName());
        model.addAttribute("user", manager);
        formBean.setFirstName(manager.getFirstName());
        formBean.setLastName(manager.getLastName());
        formBean.setPhone(manager.getPhone());
        formBean.setEmail(manager.getEmail());
        formBean.setPassword(encoder.encode(formBean.getPassword()));
        managerFacade.updateManager(formBean);
        redirectAttributes.addFlashAttribute("alert_success", "Manager password was updated successfully");
        return "redirect:m_profile";
    }

}