package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.FileListService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
public class FileController {
    private final UserService userService;
    private final FileListService fileListService;

    public FileController(UserService userService, FileListService fileListService) {
        this.userService = userService;
        this.fileListService = fileListService;
    }

    // for download file
    @GetMapping("/files/{fileName}")
    public @ResponseBody
    ResponseEntity<byte[]> getFile(Authentication authentication, @PathVariable String fileName) {
        int userId = userService.getUserId(authentication.getName());
        File file = this.fileListService.getFile(userId, fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file.getFileData());
    }

    @PostMapping("/files")
    public RedirectView uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile uploadedFile, RedirectAttributes redirectAttributes) throws IOException {
        int userId = userService.getUserId(authentication.getName());
        String fileName = uploadedFile.getOriginalFilename();

        if(uploadedFile.getSize() > 2097152){
            redirectAttributes.addFlashAttribute("fileError", "File exceeds its maximum permitted size of 1048576 bytes..");
        }
        else if (this.fileListService.doesFileAlreadyExist(userId, fileName)) {
            redirectAttributes.addFlashAttribute("fileError", "File already exists.");
        } else {
            File newFile = new File();
            newFile.setFileName(fileName);
            newFile.setContentType(uploadedFile.getContentType());
            newFile.setFileSize(uploadedFile.getSize());
            newFile.setUserId(userId);
            newFile.setFileData(uploadedFile.getInputStream().readAllBytes());
            redirectAttributes.addFlashAttribute("fileSuccess", "File successfully added.");
            this.fileListService.addFile(newFile);
        }
        redirectAttributes.addFlashAttribute("activeTab", "files");
        return new RedirectView("home");
    }

    @GetMapping(value = "/files/delete/{fileName}")
    public RedirectView deleteFile(Authentication authentication, @PathVariable String fileName, RedirectAttributes redirectAttributes) {
        int userId = userService.getUserId(authentication.getName());
        boolean wasFileDeleted = fileListService.deleteFile(userId, fileName) > 0;
        if (wasFileDeleted) {
            redirectAttributes.addFlashAttribute("fileSuccess", "File successfully deleted.");
        } else {
            redirectAttributes.addFlashAttribute("fileError", "File couldn't be deleted.");
        }
        redirectAttributes.addFlashAttribute("activeTab", "files");
        // without the "/" we were getting redirected relative to the current ServletContext
        // instead of relative to the web server
        return new RedirectView("/home");
    }
}
