package com.blithe.crm.workbench.web.controller;

import com.blithe.crm.setting.domain.User;
import com.blithe.crm.setting.service.UserService;
import com.blithe.crm.utils.DateTimeUtil;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Activity;
import com.blithe.crm.workbench.domain.Contacts;
import com.blithe.crm.workbench.domain.Tran;
import com.blithe.crm.workbench.service.ActivityService;
import com.blithe.crm.workbench.service.ContactsService;
import com.blithe.crm.workbench.service.CustomerService;
import com.blithe.crm.workbench.service.TranService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/1 20:22
 * Description:
 */

@Controller
@RequestMapping("workbench/transaction")
public class TranController {

    @Resource
    private TranService tranService;

    @Resource
    private UserService userService;

    @Resource
    private ActivityService activityService;

    @Resource
    private ContactsService contactsService;

    @Resource
    private CustomerService customerService;

    @RequestMapping("add.do")
    public ModelAndView getUser(){
        ModelAndView mv = new ModelAndView();
        List<User> userList = userService.getUserList();
        mv.addObject("userList",userList);
        mv.setViewName("/workbench/transaction/save.jsp");
        return mv;
    }

    @RequestMapping("pageList.do")
    @ResponseBody
    public PaginationVo<Tran> getPageList(Integer pageNo, Integer pageSize, String source, String owner
            , String stage, String name, String customerName, String type, String contactName){
        Tran tran = new Tran();
        tran.setSource(source);
        tran.setOwner(owner);
        tran.setName(name);
        tran.setStage(stage);
        tran.setType(type);
        tran.setContactsId(contactName);
        tran.setCustomerId(customerName);
        return tranService.pageList(pageNo,pageSize,tran);
    }

    @RequestMapping("associateActivity.do")
    @ResponseBody
    public List<Activity> getActivityListByName(String name){
        return activityService.getActivityListByName(name);
    }


    @RequestMapping("associateContacts.do")
    @ResponseBody
    public List<Contacts> getContactsListByName(String name){
        return contactsService.getContactsListByName(name);
    }

    @RequestMapping("getCustomerName.do")
    @ResponseBody
    public List<String> getCustomerName(String name){
        return customerService.getCustomerName(name);
    }

    @RequestMapping("save.do")
    public ModelAndView save(String name, String money, String expectedDate, String stage, String source, String owner,
                             String customerName, String type, String activityId, String contactsId, String description,
                             String contactSummary, String nextContactTime, HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        Tran t = new Tran();
        String id = UUIDUtil.getUUID();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setActivityId(activityId);
        t.setSource(source);
        t.setContactsId(contactsId);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        boolean flag = tranService.save(t,customerName);

        if(flag){
            // 如果添加交易成功，跳转到列表页
            mv.setViewName("redirect:/workbench/transaction/index.jsp");
        }
        return mv;
    }

    @RequestMapping("detail.do")
    public ModelAndView getDetail(String id,HttpServletRequest request){
        ModelAndView mv =  new ModelAndView();
        Tran t = tranService.detail(id);
        String stage = t.getStage();
        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        mv.addObject("possibility",pMap.get(stage));
        mv.addObject("t",t);
        mv.setViewName("/workbench/transaction/detail.jsp");
        return mv;
    }
}
