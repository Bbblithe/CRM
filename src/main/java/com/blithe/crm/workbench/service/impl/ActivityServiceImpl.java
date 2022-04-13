package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.setting.dao.UserDao;
import com.blithe.crm.setting.domain.User;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.dao.ActivityDao;
import com.blithe.crm.workbench.dao.ActivityRemarkDao;
import com.blithe.crm.workbench.domain.Activity;
import com.blithe.crm.workbench.domain.ActivityRemark;
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

    @Override
    public List<Activity> getActivityListByName(String name) {
        return dao.selectActivityByName(name);
    }

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
        User user = userDao.selectUserByA(id);
        List<User> users = userDao.selectOtherUsersByA(id);

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

    @Override
    public Activity detail(String id) {
        return dao.getDetail(id);
    }

    @Override
    public boolean deleteOne(String id) {
        return dao.deleteOne(id);
    }

    @Override
    public List<ActivityRemark> selectActivityRemarkList(String id) {
        return remarkDao.selectActivityRemarkListById(id);
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {
        return remarkDao.saveRemark(ar) == 1;
    }

    @Override
    public boolean deleteRemark(String id) {
        return remarkDao.deleteById(id) == 1;
    }

    @Override
    public boolean updateRemark(String remarkId, String noteContent,String editBy,String editTime) {
        return remarkDao.updateRemark(remarkId,noteContent,editBy,editTime,"1") == 1;
    }

    @Override
    public ActivityRemark selectAR(String remarkId) {
        return remarkDao.selectAr(remarkId);
    }


    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        return dao.getActivityListByClueId(clueId);
    }

    @Override
    public List<Activity> selectActivityByNameAndNotAssociateByClueId(String name,String clueId) {
        return dao.selectActivityByNameAndNotAssociateByClueId(name,clueId);
    }

    @Override
    public List<Activity> selectActivityByNameAndNotAssociateByContactsId(String id, String name) {
        return dao.selectActivityByNameAndNotAssociateByContactsId(id,name);
    }

    @Override
    public List<Activity> selectActivityByIdAndAssociate(String id) {
        return dao.selectActivityByIdAndAssociate(id);
    }

    @Override
    public List<Activity> selectActivityByName(String aname) {
        return dao.selectActivityByName(aname);
    }
}
