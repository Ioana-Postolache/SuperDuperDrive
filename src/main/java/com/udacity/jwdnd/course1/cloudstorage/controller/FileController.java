package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.FileListService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@Controller
public class FileController {
    private final UserService userService;
    private final FileListService fileListService;

    public FileController(UserService userService, FileListService fileListService) {
        this.userService = userService;
        this.fileListService = fileListService;
    }

    @GetMapping("/files")
    public String getHomePage(Authentication authentication, Model model) {
        int userId = userService.getUser(authentication.getName()).getUserId();
        model.addAttribute("files", this.fileListService.getFiles(userId));
        return "home";
    }

    @GetMapping("/files/{fileName}")
    public @ResponseBody
    ResponseEntity<byte[]> getFile(Authentication authentication, @PathVariable String fileName) {
        int userId = userService.getUser(authentication.getName()).getUserId();
        File file = this.fileListService.getFile(userId, fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file.getFileData());
    }
//    // option using param
//    @GetMapping("/files")
//    public @ResponseBody
//    byte[] getFile(Authentication authentication, @RequestParam("fileName") String fileName) {
//        int userId = userService.getUser(authentication.getName()).getUserId();
//        File file = this.fileListService.getFile(userId, fileName);
//        return file.getFileData();
//    }

    @PostMapping("/files")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile uploadedFile, Model model) throws IOException {
        int userId = userService.getUser(authentication.getName()).getUserId();
        String fileName = uploadedFile.getOriginalFilename();

        if(uploadedFile.getSize() > 2097152){
            model.addAttribute("fileError", "File exceeds its maximum permitted size of 1048576 bytes..");
        }
        else if (this.fileListService.doesFileAlreadyExist(userId, fileName)) {
            model.addAttribute("fileError", "File already exists.");
        } else {
            File newFile = new File();
            newFile.setFileName(fileName);
            newFile.setContentType(uploadedFile.getContentType());
            newFile.setFileSize(uploadedFile.getSize());
            newFile.setUserId(userId);
            newFile.setFileData(uploadedFile.getInputStream().readAllBytes());
            model.addAttribute("fileSuccess", "File successfully added.");
            this.fileListService.addFile(newFile);
        }
        model.addAttribute("files", this.fileListService.getFiles(userId));
        return "home";
    }

    @GetMapping(value = "/files/delete/{fileName}")
    public String deleteFile(Authentication authentication, @PathVariable String fileName, Model model) {
        int userId = userService.getUser(authentication.getName()).getUserId();
        boolean wasFileDeleted = fileListService.deleteFile(userId, fileName) > 0;
        if (wasFileDeleted) {
            model.addAttribute("fileSuccess", "File successfully deleted.");
        } else {
            model.addAttribute("fileError", "File couldn't be deleted.");
        }
        model.addAttribute("files", this.fileListService.getFiles(userId));
        return "home";
    }

}
