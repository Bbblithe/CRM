package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.dao.ClueDao;
import com.blithe.crm.workbench.domain.Clue;
import com.blithe.crm.workbench.service.ClueService;
import com.github.pagehelper.PageHelper;

import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/21 16:08
 * Description:
 */

@Service
public class ClueServiceImpl implements ClueService {
    @Resource
    private ClueDao clueDao;

    @Override
    public boolean save(Clue clue) {
        return clueDao.save(clue) == 1;
    }

    @Override
    public PaginationVo<Clue> pageList(Integer pageNo, Integer pageSize, Clue clue) {
        int total = clueDao.getTotal(clue);

        PageHelper.startPage(pageNo,pageSize);
        List<Clue> clueList = clueDao.selectClueListByCondition(clue);
        PaginationVo<Clue> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(clueList);
        return vo;
    }
}
