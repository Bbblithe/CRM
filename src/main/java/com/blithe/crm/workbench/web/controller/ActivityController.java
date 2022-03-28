package com.blithe.crm.workbench.web.controller;

import com.blithe.crm.setting.domain.User;
import com.blithe.crm.setting.service.UserService;
import com.blithe.crm.utils.DateTimeUtil;
import com.blithe.crm.utils.PrintJson;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Activity;
import com.blithe.crm.workbench.domain.ActivityRemark;
import com.blithe.crm.workbench.service.ActivityService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/18 18:58
 * Description:
 */

@Controller
@RequestMapping("workbench/activity")
public class ActivityController {
    @Resource
    private ActivityService activityService;
    @Resource
    private UserService userService;

    // @RequestMapping("/test.do")
    // public ModelAndView test(String id){
    //     Activity activity = activityService.test(id);
    //     System.out.println("执行方法咯" + activity);
    //     ModelAndView mv = new ModelAndView();
    //     return mv;
    // }

    @RequestMapping("/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        return userService.getUserList();
    }

    @RequestMapping("/save.do")
    public void getActivityList(HttpServletResponse response,HttpServletRequest request, String owner, String name, String startDate,
                                String endDate, String cost, String description){
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        boolean success = activityService.save(new Activity(id,owner,name,startDate,endDate,cost,description,createTime,
                ((User)request.getSession().getAttribute("user")).getName(),"",""
                ));

        PrintJson.printJsonFlag(response,success);
    }

    @RequestMapping("/pageList.do")
    @ResponseBody
    public PaginationVo<Activity> getPageActivity(HttpServletResponse response,int pageNo, int pageSize,String name,String owner,
                                String startDate,String endDate){

        Activity activity = new Activity();
        activity.setName(name);
        activity.setOwner(owner);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        PaginationVo<Activity> vo = activityService.pageList(activity,pageNo,pageSize);
        return vo;
        // Map<String,Object> map = new HashMap<>();
        // map.put("name",name);
        // map.put("owner",owner);
        // map.put("startDate",startDate);
        // map.put("endDate",endDate);
    }

    @RequestMapping("/delete.do")
    public void deleteActivity(HttpServletResponse response,HttpServletRequest request){
        String ids[] = request.getParameterValues("id");
        boolean flag = activityService.delete(ids);
        PrintJson.printJsonFlag(response,flag);
    }

    @RequestMapping("/deleteOne.do")
    public void deleteOneActivity(HttpServletResponse response,String id){
        boolean flag = activityService.deleteOne(id);
        PrintJson.printJsonFlag(response,flag);
    }

    @RequestMapping("/getUserListAndActivity.do")
    @ResponseBody
    public Map<String,Object> getUserListAndActivity(String id){
        Map<String,Object> map= activityService.getUserListAndActivity(id);
        // ListActivityVo<User> vo = activityService.getUserListAndActivity(id);
        return map;
    }

    /*
    public void getActivityList(HttpServletResponse response,HttpServletRequest request, String owner, String name, String startDate,
                                String endDate, String cost, String description){
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        boolean success = activityService.save(new Activity(id,owner,name,startDate,endDate,cost,description,createTime,
                ((User)request.getSession().getAttribute("user")).getName(),"",""
                ));

        PrintJson.printJsonFlag(response,success);
    }
     */

    @RequestMapping("/update.do")
    public void UpdateActivity(HttpServletResponse response,HttpServletRequest request,
                               String id,String owner,String name, String startDate,
                               String endDate, String cost, String description){
        String editTime = DateTimeUtil.getSysTime();
        Activity activity = new Activity();
        activity.setId(id);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setStartDate(startDate);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        boolean success = activityService.update(activity);
        PrintJson.printJsonFlag(response,success);
    }

    @RequestMapping("/detail.do")
    public ModelAndView showRemark(String id){
        ModelAndView mv = new ModelAndView();
        Activity a = activityService.detail(id);
        mv.addObject("activity",a);
        mv.setViewName("/workbench/activity/detail.jsp");
        return mv;
    }

    @RequestMapping("/getRemarkListByAId.do")
    @ResponseBody
    public List<ActivityRemark> getRemarkListById(String activityId){
        return activityService.selectActivityRemarkList(activityId);
    }

    @RequestMapping("/deleteRemark.do")
    @ResponseBody
    public boolean deleteRemark(HttpServletResponse response ,String id){
        return activityService.deleteRemark(id);
    }

    @RequestMapping("/saveRemark.do")
    @ResponseBody
    public Map<String,Object> saveRemark(HttpServletRequest request,String activityId,String noteContent){
        String id = UUIDUtil.getUUID();
        String createTime =  DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        ActivityRemark ar = new ActivityRemark();
        ar.setActivityId(activityId);
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setCreateTime(createTime);
        ar.setCreateBy(createBy);
        ar.setEditFlag(editFlag);

        boolean flag = activityService.saveRemark(ar);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);
        return map;
    }

    @RequestMapping("/updateRemark.do")
    @ResponseBody
    public Map<String,Object> updateRemark(HttpServletRequest request,String id,String noteContent){

        boolean flag = activityService.updateRemark(id,noteContent,
                ((User)request.getSession().getAttribute("user")).getName(),
                DateTimeUtil.getSysTime());
        ActivityRemark ar = activityService.selectAR(id);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);
        return map;
    }
}
