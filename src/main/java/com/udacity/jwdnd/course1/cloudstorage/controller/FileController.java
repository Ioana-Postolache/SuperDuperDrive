package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.FileListService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileController {
    private final UserService userService;
    private FileListService fileListService;

    public FileController(UserService userService, FileListService fileListService) {
        this.userService = userService;
        this.fileListService = fileListService;
    }

//    @GetMapping("/files")
//    public String getHomePage(Authentication authentication,Model model) {
//        int userId = userService.getUser(authentication.getName()).getUserId();
//        model.addAttribute("files", this.fileListService.getFiles(userId));
//        return "home";
//    }

    @GetMapping("/files/{fileName}")
    public @ResponseBody
    byte[] getFile(Authentication authentication, @PathVariable String fileName, Model model) {
        int userId = userService.getUser(authentication.getName()).getUserId();
        File file = this.fileListService.getFile(userId, fileName);
        return file.getFileData();
    }

    @GetMapping("/files")
    public @ResponseBody
    byte[] getFile(Authentication authentication, @RequestParam("fileName") String fileName) {
        int userId = userService.getUser(authentication.getName()).getUserId();
        File file = this.fileListService.getFile(userId, fileName);
        return file.getFileData();
    }

    @PostMapping("/files")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile uploadedFile, Model model) throws IOException {
        File newFile = new File();
        newFile.setFileName(uploadedFile.getOriginalFilename());
        newFile.setContentType(uploadedFile.getContentType());
        newFile.setFileSize(uploadedFile.getSize());
        int userId = userService.getUser(authentication.getName()).getUserId();
        newFile.setUserId(userId);
        newFile.setFileData(uploadedFile.getInputStream().readAllBytes());
        this.fileListService.addFile(newFile);
        model.addAttribute("files", this.fileListService.getFiles(userId));
        return "home";
    }


    @GetMapping(value = "/files/delete/{fileName}")
    public String deleteFile(Authentication authentication, @PathVariable String fileName, Model model) {
        int userId = userService.getUser(authentication.getName()).getUserId();
        fileListService.deleteFile(userId, fileName);
//
//        if (!isRemoved) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }

        model.addAttribute("files", this.fileListService.getFiles(userId));
        return "home";
    }

}
