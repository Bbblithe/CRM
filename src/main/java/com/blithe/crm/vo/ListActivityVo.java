package com.blithe.crm.vo;

import com.blithe.crm.setting.domain.User;
import com.blithe.crm.workbench.domain.Activity;

import java.util.List;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/19 19:09
 * Description:
 */

public class ListActivityVo<T> {
    private User user;
    private List<T> userList;
    private Activity activity;

    public ListActivityVo() {
    }

    public ListActivityVo(User user,List<T> userList, Activity activity) {
        this.user = user;
        this.userList = userList;
        this.activity = activity;
    }

    public List<T> getUserList() {
        return userList;
    }

    public void setUserList(List<T> userList) {
        this.userList = userList;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
