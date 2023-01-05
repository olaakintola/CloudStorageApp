package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES (#{username}, #{salt}, #{password}, #{firstname}, #{lastname})")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    Integer insertUser(User user);

    @Delete("DELETE FROM USERS WHERE userid = #{userid}")
    void deleteUser(Integer userid);

    @Update("UPDATE USERS SET username = #{username}, salt = #{salt}, password = #{password}, " +
            "firstname = #{firstname}, lastname = #{lastname} WHERE userid = #{userid}")
    void updateUser(Integer userid, String username, String salt, String password, String firstname, String lastname);

}
