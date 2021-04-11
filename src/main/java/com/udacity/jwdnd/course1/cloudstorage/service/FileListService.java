package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileListService {
    private FileMapper fileMapper;

    public FileListService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public void addFile(File file) {
        fileMapper.insert(file);
    }

    public List<File> getFiles(int userId) {
        return fileMapper.getAllFilesByUser(userId);
    }

    public File getFile(int userId, String fileName) {
        return fileMapper.getFileByFileName(userId, fileName);
    }

    public int deleteFile(int userId, String fileName) {
        return fileMapper.deleteFileByFileName(userId, fileName);
    }
}
