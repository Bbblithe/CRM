package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {
    List<ClueRemark> showRemarkList(String id);

    int saveRemark(ClueRemark cr);

    int updateRemark(ClueRemark cr);

    int deleteRemark(String id);
}
