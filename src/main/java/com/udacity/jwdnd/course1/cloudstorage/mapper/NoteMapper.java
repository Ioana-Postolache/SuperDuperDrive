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

    @Delete("Delete FROM NOTES WHERE userId = #{userId} and noteid = #{noteId}")
    int deleteNoteByNoteId(int userId, int noteId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) " +
            "VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Update("UPDATE NOTES set notetitle=#{noteTitle}, notedescription=#{noteDescription} " +
            "WHERE userid=#{userId} AND noteid=#{noteId}")
    int update(Note note);
}

