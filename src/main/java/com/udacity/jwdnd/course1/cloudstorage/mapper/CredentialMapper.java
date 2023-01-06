package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE url = #{url}")
    Credential getCredential(String url);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    List<Credential> retrieveAllCredentials(Integer userid);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{url}, " +
            "#{username}, #{key}, #{password}, #{userid} )")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    Integer insertCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    void deleteCredential(Integer credentialid);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password = #{password} WHERE credentialid = #{credentialid} ")
    void updateCredential(Integer credentialid, String url, String username, String key, String password, Integer userid);
}
