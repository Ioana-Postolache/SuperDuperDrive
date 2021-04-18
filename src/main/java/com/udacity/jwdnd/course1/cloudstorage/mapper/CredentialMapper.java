package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE userId = #{userId}")
    List<Credential> getAllCredentialsByUser(int userId);

    @Delete("Delete FROM CREDENTIALS WHERE userId = #{userId} and credentialId = #{credentialId}")
    int deleteCredentialByCredentialId(int userId, int credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password,userid) " +
            "VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credential credential);

    @Update("UPDATE CREDENTIALS set url=#{url}, username=#{username}, key=#{key}, password=#{password} " +
            "WHERE userid=#{userId} AND credentialid=#{credentialId}")
    int update(Credential credential);
}
