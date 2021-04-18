package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getAllNotesByUser(int userId);

    @Select("SELECT * FROM NOTES WHERE userId = #{userId} and notetitle = #{noteTitle}")
    Note getNoteByNoteName(int userId, String noteTitle);

    @Delete("Delete FROM NOTES WHERE userId = #{userId} and notetitle = #{noteTitle}")
    int deleteNoteByNoteName(int userId, String noteTitle);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) " +
            "VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);
}

