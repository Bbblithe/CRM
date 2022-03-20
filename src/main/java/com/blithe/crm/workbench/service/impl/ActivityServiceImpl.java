package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.setting.dao.UserDao;
import com.blithe.crm.setting.domain.User;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.dao.ActivityDao;
import com.blithe.crm.workbench.dao.ActivityRemarkDao;
import com.blithe.crm.workbench.domain.Activity;
import com.blithe.crm.workbench.service.ActivityService;
import com.github.pagehelper.PageHelper;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/18 18:57
 * Description:
 */

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityDao dao;
    @Resource
    private ActivityRemarkDao remarkDao;
    @Resource
    private UserDao userDao;

    // @Override
    // public Activity test(String id){
    //     return dao.test(id);
    // }


    @Override
    public boolean save(Activity user) {
        // 添加dao逻辑，应当根据结果抛出异常，没成功就抛出异常
        return dao.save(user) == 1;
    }

    @Override
    public PaginationVo<Activity> pageList(Activity activity,int pageNo,int pageSize) {

        // 取得total
        int total = dao.getTotal(activity);

        // 取得dataList
        PageHelper.startPage(pageNo,pageSize);
        List<Activity> list = dao.getActivityListByCondition(activity);

        PaginationVo<Activity> vo = new PaginationVo<>(total,list);
        // 将total和dataList封装在vo中，将vo返回
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        // 查询出需要删除的备注的数量
        int count1 = remarkDao.getCountByIds(ids);

        // 删除备注，返回收到影响的的记录条数（实际删除的数量）
        int count2 = remarkDao.deleteByIds(ids);

        flag = count1==count2;

        // 删除市场活动
        int count3 = dao.delete(ids);
        flag = count3 == ids.length;

        return flag;
    }

    @Override
    public Map<String,Object> getUserListAndActivity(String id) {
        User user = userDao.selectUser(id);
        List<User> users = userDao.selectOtherUsers(id);

        Activity activity = dao.selectActivityById(id);
        Map<String,Object> map= new HashMap<>();
        map.put("user",user);
        map.put("userList",users);
        map.put("activity",activity);
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        return dao.update(activity) == 1;
    }
}
