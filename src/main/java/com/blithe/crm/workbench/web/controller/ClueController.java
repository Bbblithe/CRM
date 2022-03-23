package com.blithe.crm.workbench.web.controller;

import com.blithe.crm.setting.domain.User;
import com.blithe.crm.setting.service.UserService;
import com.blithe.crm.utils.DateTimeUtil;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Clue;
import com.blithe.crm.workbench.service.ClueService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
}
