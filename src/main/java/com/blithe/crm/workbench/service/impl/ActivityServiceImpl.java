package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.dao.ActivityDao;
import com.blithe.crm.workbench.domain.Activity;
import com.blithe.crm.workbench.service.ActivityService;
import com.github.pagehelper.PageHelper;

import org.springframework.stereotype.Service;

import java.util.List;

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
}
