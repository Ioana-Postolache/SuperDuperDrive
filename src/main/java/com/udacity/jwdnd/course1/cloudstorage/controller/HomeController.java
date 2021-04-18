package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
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

    public HomeController(UserService userService, FileListService fileListService, NoteListService noteListService) {
        this.userService = userService;
        this.fileListService = fileListService;
        this.noteListService = noteListService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication, NoteForm noteForm, Model model, HttpServletRequest request) {
        int userId = userService.getUser(authentication.getName()).getUserId();
        model.addAttribute("files", this.fileListService.getFiles(userId));
        model.addAttribute("notes", this.noteListService.getNotes(userId));
        model.addAttribute("noteForm",noteForm);
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            String activeTab = (String) inputFlashMap.get("activeTab");
            model.addAttribute("activeTab", activeTab);
            if(activeTab.equals("files")){
                model.addAttribute("fileError", inputFlashMap.get("fileError"));
                model.addAttribute("fileSuccess", inputFlashMap.get("fileSuccess"));
            } else if(activeTab.equals("notes")){
                model.addAttribute("noteError", inputFlashMap.get("noteError"));
                model.addAttribute("noteSuccess", inputFlashMap.get("noteSuccess"));
            }

        } else {
            model.addAttribute("activeTab", "files");
        }
        return "home";
    }

}
