package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<File> getAllFilesByUser(int userId);

    @Select("SELECT * FROM FILES WHERE userId = #{userId} and fileName = #{fileName}")
    File getFileByFileName(int userId, String fileName);

    @Delete("Delete FROM FILES WHERE userId = #{userId} and fileName = #{fileName}")
    int deleteFileByFileName(int userId, String fileName);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId},  #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);
}
