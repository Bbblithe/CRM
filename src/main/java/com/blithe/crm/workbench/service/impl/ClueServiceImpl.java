package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.setting.dao.UserDao;
import com.blithe.crm.setting.domain.User;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.dao.ClueActivityRelationDao;
import com.blithe.crm.workbench.dao.ClueDao;
import com.blithe.crm.workbench.dao.ClueRemarkDao;
import com.blithe.crm.workbench.domain.Clue;
import com.blithe.crm.workbench.domain.ClueActivityRelation;
import com.blithe.crm.workbench.domain.ClueRemark;
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

    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;

    @Resource
    private ClueRemarkDao clueRemarkDao;

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
        clueDao.disconnect(ids);
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

    @Override
    public boolean unband(String id) {
        return clueActivityRelationDao.unband(id) == 1;
    }

    @Override
    public boolean bund(String clueId, String[] ids) {
        boolean flag = true;
        for(String id:ids){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(clueId);
            car.setActivityId(id);

            flag = clueActivityRelationDao.bund(car)==1;
        }
        return flag;
    }

    @Override
    public boolean deleteClueById(String id) {
        String[] ids = new String[1];
        ids[0] = id;
        clueDao.disconnect(ids);
        int count2 = clueDao.deleteClues(ids);
        return count2 == 1;
    }

    @Override
    public List<ClueRemark> showRemarkList(String id) {
        return clueRemarkDao.showRemarkList(id);
    }

    @Override
    public Map<String, Object> saveRemark(ClueRemark cr) {
        Map<String,Object> map = new HashMap<>();
        map.put("success",clueRemarkDao.saveRemark(cr) == 1);
        return map;
    }

    @Override
    public Map<String, Object> updateRemark(ClueRemark cr) {
        Map<String,Object> map = new HashMap<>();
        map.put("success",clueRemarkDao.updateRemark(cr) == 1);
        return map;
    }

    @Override
    public boolean deleteRemark(String id) {
        return clueRemarkDao.deleteRemark(id) == 1;
    }
}
