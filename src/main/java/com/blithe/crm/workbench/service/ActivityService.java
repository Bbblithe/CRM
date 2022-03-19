package com.blithe.crm.workbench.service;

import com.blithe.crm.setting.domain.User;
import com.blithe.crm.vo.ListActivityVo;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Activity;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/18 18:57
 * Description:
 */

public interface ActivityService {
    boolean save(Activity user);

    PaginationVo<Activity> pageList(Activity activity,int pageNo,int pageSize);

    boolean delete(String[] ids);

    ListActivityVo<User> getUserListAndActivity(String id);

    boolean update(Activity activity);

    // Activity test(String id);
}
