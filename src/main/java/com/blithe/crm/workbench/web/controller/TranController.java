package com.blithe.crm.workbench.web.controller;

import com.blithe.crm.setting.domain.User;
import com.blithe.crm.setting.service.UserService;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Tran;
import com.blithe.crm.workbench.service.ContactsService;
import com.blithe.crm.workbench.service.CustomerService;
import com.blithe.crm.workbench.service.TranService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.annotation.Resource;

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
        tran.setContactsId(contactsService.getIdByName(contactName));
        tran.setCustomerId(customerService.getIdByName(customerName));
        return tranService.pageList(pageNo,pageSize,tran);
    }
}
