package com.blithe.crm.setting.service;

import com.blithe.crm.exception.LoginException;
import com.blithe.crm.setting.domain.User;

import java.util.List;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/17 10:38
 * Description:
 */

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
    // int loginUser(String loginAct,String loginPwd);
}
