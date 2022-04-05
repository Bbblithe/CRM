package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.Tran;

import java.util.List;

public interface TranDao {

    int save(Tran t);

    int getTotal(Tran tran);

    List<Tran> selectTranListByCondition(Tran tran);
}
