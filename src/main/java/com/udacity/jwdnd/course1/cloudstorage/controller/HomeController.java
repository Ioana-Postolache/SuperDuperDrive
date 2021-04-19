package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Controller
@RequestMapping("/home")
public class HomeController {
    private final UserService userService;
    private final FileListService fileListService;
    private final NoteListService noteListService;
    private final CredentialListService credentialListService;
    private final EncryptionService encryptionService;

    public HomeController(UserService userService, FileListService fileListService, NoteListService noteListService, CredentialListService credentialListService, EncryptionService encryptionService) {
        this.userService = userService;
        this.fileListService = fileListService;
        this.noteListService = noteListService;
        this.credentialListService = credentialListService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication, Note noteForm, Credential credentialForm, Model model, HttpServletRequest request) {
        int userId = userService.getUserId(authentication.getName());
        model.addAttribute("files", this.fileListService.getFiles(userId));
        model.addAttribute("notes", this.noteListService.getNotes(userId));
        model.addAttribute("credentials", this.credentialListService.getCredentials(userId));
        model.addAttribute("noteForm", noteForm);
        model.addAttribute("credentialForm", credentialForm);
        model.addAttribute("encryptionService", encryptionService);
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            String activeTab = (String) inputFlashMap.get("activeTab");
            model.addAttribute("activeTab", activeTab);
            switch (activeTab) {
                case "files":
                    model.addAttribute("fileError", inputFlashMap.get("fileError"));
                    model.addAttribute("fileSuccess", inputFlashMap.get("fileSuccess"));
                    break;
                case "notes":
                    model.addAttribute("noteError", inputFlashMap.get("noteError"));
                    model.addAttribute("noteSuccess", inputFlashMap.get("noteSuccess"));
                    break;
                case "credentials":
                    model.addAttribute("credentialError", inputFlashMap.get("credentialError"));
                    model.addAttribute("credentialSuccess", inputFlashMap.get("credentialSuccess"));
                    break;
            }
        } else {
            model.addAttribute("activeTab", "files");
        }
        return "home";
    }

}
