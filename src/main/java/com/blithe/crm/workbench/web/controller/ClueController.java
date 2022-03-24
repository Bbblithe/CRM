package com.blithe.crm.workbench.web.controller;

import com.blithe.crm.setting.domain.User;
import com.blithe.crm.setting.service.UserService;
import com.blithe.crm.utils.DateTimeUtil;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Activity;
import com.blithe.crm.workbench.domain.Clue;
import com.blithe.crm.workbench.service.ActivityService;
import com.blithe.crm.workbench.service.ClueService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/21 16:09
 * Description:
 */

@Controller
@RequestMapping("workbench/clue")
 class ClueController {

    @Resource
    UserService userService;

    @Resource
    ClueService clueService;

    @Resource
    ActivityService activityService;

    @RequestMapping("getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        return userService.getUserList();
    }

    @RequestMapping("save.do")
    @ResponseBody
    public boolean save(String fullname, String appellation, String owner, String company
    , String job, String email, String phone, String website, String mphone, String state, String source
    , String description, String contactSummary, String nextContactTime
    , String address, HttpServletRequest request){
        String id = UUIDUtil.getUUID();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        Clue clue = new Clue();
        clue.setAddress(address);
        clue.setAppellation(appellation);
        clue.setCompany(company);
        clue.setContactSummary(contactSummary);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setNextContactTime(nextContactTime);
        clue.setDescription(description);
        clue.setId(id);
        clue.setJob(job);
        clue.setFullname(fullname);
        clue.setOwner(owner);
        clue.setMphone(mphone);
        clue.setWebsite(website);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setState(state);
        clue.setSource(source);

        return clueService.save(clue);
    }

    @RequestMapping("pageList.do")
    @ResponseBody
    public PaginationVo<Clue> pageList(Integer pageNo, Integer pageSize,
            String fullname,String company,String mphone,String phone,
            String source,String owner, String state){
        Clue clue = new Clue();
        clue.setSource(source);
        clue.setState(state);
        clue.setPhone(phone);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setFullname(fullname);
        clue.setMphone(mphone);
        return clueService.pageList(pageNo,pageSize,clue);
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public Boolean deleteClue(HttpServletRequest request){
        String[] ids = request.getParameterValues("id");
        return clueService.delete(ids);
    }

    @RequestMapping("getUserListAndClue.do")
    @ResponseBody
    public Map<String,Object> getUserListAndClueList(String id){
        return clueService.getUserListAndClue(id);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public boolean updateClue(String company,String id,String fullname,String appellation, String owner,
            String job,String email,String phone,String source,String state,String website,String description,
            String contactSummary,String nextContactTime,String address,String mphone,HttpServletRequest request){
        Clue clue = new Clue();
        clue.setMphone(mphone);
        clue.setCompany(company);
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAddress(address);
        clue.setAppellation(appellation);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setState(state);
        clue.setSource(source);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setWebsite(website);
        clue.setOwner(owner);
        clue.setDescription(description);
        clue.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        clue.setEditTime(DateTimeUtil.getSysTime());
        return clueService.update(clue);
    }

    @RequestMapping("detail.do")
    public ModelAndView showDetail(String id){
        ModelAndView mv = new ModelAndView();
        mv.addObject("clue",clueService.getDetail(id));
        mv.setViewName("/workbench/clue/detail.jsp");
        return mv;
    }

    @RequestMapping("getActivityListByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByClueId(String clueId){
        return activityService.getActivityListByClueId(clueId);
    }

    @RequestMapping("unband.do")
    @ResponseBody
    public boolean unband(String id){
        return clueService.unband(id);
    }
}
