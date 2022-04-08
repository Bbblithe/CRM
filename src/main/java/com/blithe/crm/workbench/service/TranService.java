package com.blithe.crm.workbench.service;

import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Tran;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/1 20:21
 * Description:
 */

public interface TranService {
    PaginationVo<Tran> pageList(Integer pageNo, Integer pageSize, Tran tran);

    boolean save(Tran t, String customerName);

    Tran detail(String id);
}
