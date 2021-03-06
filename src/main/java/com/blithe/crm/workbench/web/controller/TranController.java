package com.blithe.crm.workbench.web.controller;

import com.blithe.crm.setting.domain.User;
import com.blithe.crm.setting.service.UserService;
import com.blithe.crm.utils.DateTimeUtil;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.vo.ChartsVo;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Activity;
import com.blithe.crm.workbench.domain.Contacts;
import com.blithe.crm.workbench.domain.Tran;
import com.blithe.crm.workbench.domain.TranHistory;
import com.blithe.crm.workbench.domain.TranRemark;
import com.blithe.crm.workbench.service.ActivityService;
import com.blithe.crm.workbench.service.ContactsService;
import com.blithe.crm.workbench.service.CustomerService;
import com.blithe.crm.workbench.service.TranService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
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
    public ModelAndView getUser(String id){
        ModelAndView mv = new ModelAndView();
        List<User> userList = userService.getUserList();
        mv.addObject("userList",userList);
        mv.addObject("path",id);
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
                             String contactSummary, String nextContactTime,String path, HttpServletRequest request){
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
        t.setCustomerId(customerName);

        boolean flag = tranService.save(t);

        if(flag){
            // ?????????????????????????????????????????????
            if(path.equals("") || path == null) {
                mv.setViewName("redirect:/workbench/transaction/index.jsp");
            }else{
                mv.setViewName("redirect:/workbench/contacts/detail.do?id="+path);
            }
        }
        return mv;
    }

    @RequestMapping("update.do")
    public ModelAndView update(String id,String name, String money, String expectedDate, String stage, String source, String owner,
                             String customerName, String type, String activityId, String contactsId, String description,
                             String contactSummary, String nextContactTime, HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        Tran t = new Tran();
        t.setEditTime(editTime);
        t.setEditBy(editBy);
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
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);
        t.setCustomerId(customerName);

        tranService.update(t);
        mv.setViewName("/workbench/transaction/index.jsp");
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

    @RequestMapping("getHistoryListById.do")
    @ResponseBody
    public List<TranHistory> getHistoryById(String tranId,HttpServletRequest request){
        List<TranHistory> tranHistories = tranService.getHistoryById(tranId);
        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        for(TranHistory th : tranHistories){
            String stage = th.getStage();
            th.setPossibility(pMap.get(stage));
        }
        return tranHistories;
    }

    @RequestMapping("changeStage.do")
    @ResponseBody
    public Map<String,Object> changeStage(String id,String stage,String money,String expectedDate,HttpServletRequest request){
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);

        Map<String,Object> map = new HashMap<>();
        boolean flag = tranService.changeStage(t);
        map.put("success",flag);
        map.put("t",t);
        map.put("possibility",pMap.get(stage));
        return map;
    }

    @RequestMapping("getCharts.do")
    @ResponseBody
    public ChartsVo<Map<String,String>> getCharts(){
        return tranService.getCharts();
    }

    @RequestMapping("edit.do")
    public ModelAndView modify(String id){
        ModelAndView mv = new ModelAndView();
        Tran t = tranService.edit(id);
        List<User> userList = userService.getOtherUserList(t.getOwner());
        User user = userService.getUserById(t.getOwner());
        mv.addObject("t",t);
        mv.addObject("userList",userList);
        mv.addObject("user",user);
        mv.setViewName("/workbench/transaction/edit.jsp");
        return mv;
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public boolean deleteTranAndHistoryTran(HttpServletRequest request){
        String[] ids = request.getParameterValues("id");
        return tranService.delete(ids);
    }

    @RequestMapping("showRemarkList.do")
    @ResponseBody
    public List<TranRemark> showRemark(String id){
        return tranService.showRemarkList(id);
    }

    @RequestMapping("saveRemark.do")
    @ResponseBody
    public Map<String,Object> saveRemark(String noteContent,String tranId,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        TranRemark tr = new TranRemark();
        tr.setId(UUIDUtil.getUUID());
        tr.setCreateTime(DateTimeUtil.getSysTime());
        tr.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        tr.setTranId(tranId);
        tr.setNoteContent(noteContent);
        tr.setEditFlag("0");
        map.put("success",tranService.saveRemark(tr));
        map.put("tr",tr);
        return map;
    }

    @RequestMapping("updateRemark.do")
    @ResponseBody
    public Map<String,Object> updateRemark(String id,String noteContent,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        TranRemark tr = new TranRemark();
        tr.setId(id);
        tr.setEditFlag("1");
        tr.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        tr.setEditTime(DateTimeUtil.getSysTime());
        tr.setNoteContent(noteContent);
        map.put("success",tranService.updateRemark(tr));
        map.put("tr",tr);
        return map;
    }

    @RequestMapping("deleteRemark.do")
    @ResponseBody
    public boolean deleteRemark(String id){
        return tranService.deleteRemark(id);
    }
}
