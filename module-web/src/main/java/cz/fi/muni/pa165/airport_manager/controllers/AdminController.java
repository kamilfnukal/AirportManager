package cz.fi.muni.pa165.airport_manager.controllers;

import cz.fi.muni.pa165.airport_manager.dto.AdminDto;
import cz.fi.muni.pa165.airport_manager.facade.AdminFacade;
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
 * Controller which is used to manage login for Admin entity
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminFacade adminFacade;

    @GetMapping("/a_profile")
    public String showAdminProfile(Model model, Principal user) {
        model.addAttribute("user", adminFacade.findByEmail(user.getName()));
        return "profile";
    }

    @PostMapping("/a_update")
    public String update(@Valid @ModelAttribute("user") AdminDto formBean, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Principal user) {
        AdminDto admin = adminFacade.findByEmail(user.getName());
        if (bindingResult.hasErrors()) {
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
            }
            return "profile";
        }
        formBean.setPassword(admin.getPassword());
        formBean.setEmail(admin.getEmail());
        adminFacade.updateAdmin(formBean);
        redirectAttributes.addFlashAttribute("alert_success", "Admin was updated successfully.");

        return "redirect:a_profile";
    }

    @PostMapping("/a_update_password")
    public String updatePassword(@Valid @ModelAttribute("user") AdminDto formBean, BindingResult bindingResult,
                                 Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Principal user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AdminDto admin = adminFacade.findByEmail(user.getName());
        model.addAttribute("user", admin);
        formBean.setFirstName(admin.getFirstName());
        formBean.setLastName(admin.getLastName());
        formBean.setPhone(admin.getPhone());
        formBean.setEmail(admin.getEmail());
        formBean.setPassword(encoder.encode(formBean.getPassword()));
        adminFacade.updateAdmin(formBean);
        redirectAttributes.addFlashAttribute("alert_success", "Admin password was updated successfully.");
        return "redirect:a_profile";
    }

}
