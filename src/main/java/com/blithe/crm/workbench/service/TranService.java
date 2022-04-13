package com.blithe.crm.workbench.service;

import com.blithe.crm.vo.ChartsVo;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Tran;
import com.blithe.crm.workbench.domain.TranHistory;
import com.blithe.crm.workbench.domain.TranRemark;

import java.util.List;
import java.util.Map;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/1 20:21
 * Description:
 */

public interface TranService {
    PaginationVo<Tran> pageList(Integer pageNo, Integer pageSize, Tran tran);

    boolean save(Tran t);

    Tran detail(String id);

    List<TranHistory> getHistoryById(String tranId);

    boolean changeStage(Tran t);

    ChartsVo<Map<String, String>> getCharts();

    Tran edit(String id);

    boolean update(Tran t);

    boolean delete(String[] ids);

    List<TranRemark> showRemarkList(String id);

    boolean saveRemark(TranRemark tr);

    boolean updateRemark(TranRemark tr);

    boolean deleteRemark(String id);

    List<Tran> getTranList();
}
