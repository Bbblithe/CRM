package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.TranRemark;

import java.util.List;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/10 11:07
 * Description:
 */

public interface TranRemarkDao {
    List<TranRemark> getListById(String id);

    int save(TranRemark tr);

    int update(TranRemark tr);

    int delete(String id);
}
