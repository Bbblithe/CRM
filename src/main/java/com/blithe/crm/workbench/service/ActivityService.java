package com.blithe.crm.workbench.service;

import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Activity;
import com.blithe.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/18 18:57
 * Description:
 */

public interface ActivityService {
    boolean save(Activity user);

    PaginationVo<Activity> pageList(Activity activity,int pageNo,int pageSize);

    boolean delete(String[] ids);

    boolean deleteOne(String id);

    Map<String,Object> getUserListAndActivity(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> selectActivityRemarkList(String id);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean updateRemark(String remarkId, String noteContent,String editBy,String editTime);

    ActivityRemark selectAR(String remarkId);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> selectActivityByNameAndNotAssociateByClueId(String name,String clueId);

    // Activity test(String id);
}
