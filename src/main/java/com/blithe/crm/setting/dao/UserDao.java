package com.blithe.crm.setting.dao;

import com.blithe.crm.setting.domain.User;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/17 10:36
 * Description:
 */

public interface UserDao {
    User login(@Param("loginAct") String loginAct,@Param("loginPwd") String loginPwd);

    List<User> selectUserList();

    User selectUserByA(String id);

    List<User> selectOtherUsersByA(String id);

    List<User> selectOtherUsersByC(String id);

    User selectUserByC(String id);

    List<User> selectOtherUsersById(String owner);

    User getUserById(String owner);

    User selectUserByContacts(String id);

    List<User> selectOtherUserByContacts(String id);

    // int selectUser(@Param("loginAct")String loginAct, @Param("loginPwd") String loginPwd);
}
