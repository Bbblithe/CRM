package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory tranHistory);

    List<TranHistory> getHistoryListById(String tranId);
}
