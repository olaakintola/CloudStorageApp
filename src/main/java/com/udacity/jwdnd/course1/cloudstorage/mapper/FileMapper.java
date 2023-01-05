package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import org.apache.ibatis.annotations.*;

import java.io.InputStream;
import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    UserFile getUserFile(Integer fileId);

    @Select("SELECT * FROM FILES WHERE userid =#{userid}")
    List<UserFile> retrieveAllUserFiles(Integer userid);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{filename}, " +
            "#{contenttype}, #{filesize}, #{userid}, #{filedata} )")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insertUserFile(UserFile userFile);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    void deleteUserFile(Integer fileId);

    @Update("UPDATE FILES SET filename = #{filename}, contenttype = #{contenttype}, filesize = #{filesize},filedata = #{filedata} " +
            "WHERE fileId = #{fileId}")
    void updateUserFile(Integer fileId, String filename, String contenttype, String filesize, InputStream filedata, Integer userid);
}
