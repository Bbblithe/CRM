package com.blithe.crm.setting.service.impl;

import com.blithe.crm.exception.LoginException;
import com.blithe.crm.setting.dao.UserDao;
import com.blithe.crm.setting.domain.User;
import com.blithe.crm.setting.service.UserService;
import com.blithe.crm.utils.DateTimeUtil;

import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/17 10:38
 * Description:
 */

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao dao;

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        User user = dao.login(loginAct,loginPwd);
        if(user == null){
            throw new LoginException("账号密码错误！");
        }
        // 如果程序能够执行到此，说明账号密码正确
        // 需要继续向下验证ip等消息
        String expireTime = user.getExpireTime();
        if(expireTime.compareTo(DateTimeUtil.getSysTime()) < 0){
            throw new LoginException("账号失效，请联系管理员重制时间！");
        }

        // 判断锁定状态
        if("0".equals(user.getLockState())){
            throw new LoginException("账号已被锁定，请联系管理员解锁状态！");
        }
        if(!user.getAllowIps().contains(ip)){
            throw new LoginException("ip地址受限");
        }

        return user;
    }

    // @Override
    // public int loginUser(String loginAct, String loginPwd) {
    //     return dao.selectUser(loginAct,loginPwd);
    // }


    @Override
    public List<User> getUserList() {
        return dao.selectUserList();
    }
}
