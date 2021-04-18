package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialListService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileListService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteListService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
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

    public HomeController(UserService userService, FileListService fileListService, NoteListService noteListService, CredentialListService credentialListService) {
        this.userService = userService;
        this.fileListService = fileListService;
        this.noteListService = noteListService;
        this.credentialListService = credentialListService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication, Note noteForm, Credential credentialForm, Model model, HttpServletRequest request) {
        int userId = userService.getUserId(authentication.getName());
        model.addAttribute("files", this.fileListService.getFiles(userId));
        model.addAttribute("notes", this.noteListService.getNotes(userId));
        model.addAttribute("credentials", this.credentialListService.getCredentials(userId));
        model.addAttribute("noteForm", noteForm);
        model.addAttribute("credentialForm", credentialForm);
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
