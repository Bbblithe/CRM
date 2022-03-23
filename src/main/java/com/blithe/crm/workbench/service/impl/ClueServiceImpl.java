package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.setting.dao.UserDao;
import com.blithe.crm.setting.domain.User;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.dao.ClueDao;
import com.blithe.crm.workbench.domain.Clue;
import com.blithe.crm.workbench.service.ClueService;
import com.github.pagehelper.PageHelper;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/21 16:08
 * Description:
 */

@Service
public class ClueServiceImpl implements ClueService {
    @Resource
    private UserDao userDao;

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

    @Override
    public Boolean delete(String[] ids) {
        return clueDao.deleteClues(ids) == ids.length;
    }

    @Override
    public Map<String, Object> getUserListAndClue(String id) {
        User user = userDao.selectUserByC(id);
        List<User> userList = userDao.selectOtherUsersByC(id);

        Clue clue = clueDao.selectClue(id);
        Map<String,Object> map = new HashMap<>();
        map.put("user",user);
        map.put("userList",userList);
        map.put("clue",clue);
        return map;
    }

    @Override
    public boolean update(Clue clue) {
        return clueDao.update(clue) == 1;
    }

    @Override
    public Clue getDetail(String id) {
        return clueDao.detail(id);
    }
}
