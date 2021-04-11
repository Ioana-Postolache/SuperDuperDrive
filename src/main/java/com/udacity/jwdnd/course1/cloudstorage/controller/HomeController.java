package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.FileListService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final UserService userService;
    private FileListService fileListService;

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

    @PostMapping
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile fileUpload, Model model) {
        File file = new File();
        int userId = userService.getUser(authentication.getName()).getUserId();
        file.setUserId(userId);
        this.fileListService.addFile(file);
        model.addAttribute("files", this.fileListService.getFiles(userId));
        return "home";
    }

}
