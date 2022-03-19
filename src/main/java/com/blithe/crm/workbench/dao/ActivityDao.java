package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.Activity;

import java.util.List;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/18 18:51
 * Description:
 */

public interface ActivityDao {
    // Activity test(@Param("id") String id);

    int save(Activity user);

    int getTotal(Activity activity);

    List<Activity> getActivityListByCondition(Activity activity);
}
