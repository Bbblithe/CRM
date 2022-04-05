package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.dao.TranDao;
import com.blithe.crm.workbench.dao.TranHistoryDao;
import com.blithe.crm.workbench.domain.Tran;
import com.blithe.crm.workbench.service.TranService;
import com.github.pagehelper.PageHelper;

import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/1 20:21
 * Description:
 */

@Service
public class TranServiceImpl implements TranService {
    @Resource
    private TranDao tranDao;

    @Resource
    private TranHistoryDao historyDao;

    @Override
    public PaginationVo<Tran> pageList(Integer pageNo, Integer pageSize, Tran tran) {
        PaginationVo<Tran> pv = new PaginationVo<>();
        pv.setTotal(tranDao.getTotal(tran));

        PageHelper.startPage(pageNo,pageSize);
        List<Tran> tranList = tranDao.selectTranListByCondition(tran);
        pv.setDataList(tranList);
        return pv;
    }
}
