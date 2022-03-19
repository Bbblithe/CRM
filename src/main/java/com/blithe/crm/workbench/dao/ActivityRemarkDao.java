package com.blithe.crm.workbench.dao;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/18 20:15
 * Description:
 */

public interface ActivityRemarkDao {
    int getCountByIds(String[] ids);

    int deleteByIds(String[] ids);
}
