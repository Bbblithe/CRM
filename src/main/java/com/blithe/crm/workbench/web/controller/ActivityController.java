package com.blithe.crm.workbench.web.controller;

import com.blithe.crm.setting.domain.User;
import com.blithe.crm.setting.service.UserService;
import com.blithe.crm.utils.DateTimeUtil;
import com.blithe.crm.utils.PrintJson;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.workbench.domain.Activity;
import com.blithe.crm.workbench.service.ActivityService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
}