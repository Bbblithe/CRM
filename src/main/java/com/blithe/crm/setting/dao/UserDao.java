package com.blithe.crm.setting.dao;

import com.blithe.crm.setting.domain.User;

import org.apache.ibatis.annotations.Param;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/17 10:36
 * Description:
 */

public interface UserDao {
    User login(@Param("loginAct") String loginAct,@Param("loginPwd") String loginPwd);
    // int selectUser(@Param("loginAct")String loginAct, @Param("loginPwd") String loginPwd);
}
