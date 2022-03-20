package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.ActivityRemark;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/18 20:15
 * Description:
 */

public interface ActivityRemarkDao {
    int getCountByIds(String[] ids);

    int deleteByIds(String[] ids);

    List<ActivityRemark> selectActivityRemarkListById(String id);

    int deleteById(String id);

    int saveRemark(ActivityRemark ar);

    // 这里没有选择封装成对象，巩固一下注解@Parma()
    int updateRemark(@Param("remarkId")String remarkId, @Param("noteContent")String noteContent ,
                     @Param("editBy") String editBy, @Param("editTime") String editTime,
                     @Param("editFlag") String editFlag);

    ActivityRemark selectAr(String remarkId);
}
