package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.service.FileListService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/home")
public class HomeController {
    private final UserService userService;
    private final FileListService fileListService;

    public HomeController(UserService userService, FileListService fileListService) {
        this.userService = userService;
        this.fileListService = fileListService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication, Model model) {
        int userId = userService.getUser(authentication.getName()).getUserId();
        model.addAttribute("files", this.fileListService.getFiles(userId));
        return "home";
    }

}
