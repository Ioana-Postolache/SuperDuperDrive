package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialListService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class CredentialController {
    private final UserService userService;
    private final CredentialListService credentialListService;

    public CredentialController(UserService userService, CredentialListService credentialListService) {
        this.userService = userService;
        this.credentialListService = credentialListService;
    }

    @PostMapping("/credentials")
    public RedirectView createOrUpdateCredential(Authentication authentication, Credential credentialForm, RedirectAttributes redirectAttributes) {
        int userId = userService.getUserId(authentication.getName());
        credentialForm.setUserId(userId);
        if (credentialForm.getCredentialId() == null) {
            this.credentialListService.addCredential(credentialForm);
            redirectAttributes.addFlashAttribute("credentialSuccess", "Credential successfully created.");
        } else {
            this.credentialListService.updateCredential(credentialForm);
            redirectAttributes.addFlashAttribute("credentialSuccess", "Credential successfully updated.");
        }
        credentialForm.setCredentialId(null);
        credentialForm.setPassword("");
        credentialForm.setUrl("");
        credentialForm.setUsername("");
        redirectAttributes.addFlashAttribute("activeTab", "credentials");
        return new RedirectView("home");
    }

    @GetMapping(value = "/credentials/delete/{credentialId}")
    public RedirectView deleteCredential(Authentication authentication, @PathVariable int credentialId, RedirectAttributes redirectAttributes) {
        int userId = userService.getUserId(authentication.getName());
        boolean wasCredentialDeleted = credentialListService.deleteCredential(userId, credentialId) > 0;
        if (wasCredentialDeleted) {
            redirectAttributes.addFlashAttribute("credentialSuccess", "Credential successfully deleted.");
        } else {
            redirectAttributes.addFlashAttribute("credentialError", "Credential couldn't be deleted.");
        }
        redirectAttributes.addFlashAttribute("activeTab", "credentials");
        return new RedirectView("/home");
    }
}
