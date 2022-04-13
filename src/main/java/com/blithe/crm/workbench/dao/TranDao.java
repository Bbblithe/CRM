package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    int getTotal(Tran tran);

    List<Tran> selectTranListByCondition(Tran tran);

    Tran detail(String id);

    int changeStage(Tran t);

    List<Map<String, String>> getCharts();

    int getTotalNum();

    Tran getInformation(String id);

    int update(Tran t);

    int delete(String id);

    List<Tran> getTranList();
}
