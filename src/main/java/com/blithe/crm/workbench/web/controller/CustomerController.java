package com.blithe.crm.workbench.web.controller;

import com.blithe.crm.setting.domain.User;
import com.blithe.crm.setting.service.UserService;
import com.blithe.crm.utils.DateTimeUtil;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Customer;
import com.blithe.crm.workbench.service.CustomerService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/14 18:46
 * Description:
 */

@Controller
@RequestMapping("workbench/customer")
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @Resource
    private UserService userService;

    @RequestMapping("pageList.do")
    @ResponseBody
    public PaginationVo<Customer> getPageList(Integer pageNo, Integer pageSize, String owner, String website, String name, String phone){
        Customer customer = new Customer();
        customer.setOwner(owner);
        customer.setName(name);
        customer.setWebsite(website);
        customer.setPhone(phone);
        return customerService.getPageList(customer,pageNo,pageSize);
    }

    @RequestMapping("getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        return userService.getUserList();
    }

    @RequestMapping("save.do")
    @ResponseBody
    public boolean saveCustomer(String name, String website, String owner, HttpServletRequest request,
                                String phone, String description, String contactSummary,
                                String nextContactTime, String address){
        Customer customer = new Customer();
        customer.setId(UUIDUtil.getUUID());
        customer.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        customer.setCreateTime(DateTimeUtil.getSysTime());
        customer.setPhone(phone);
        customer.setWebsite(website);
        customer.setName(name);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setNextContactTime(nextContactTime);
        customer.setDescription(description);
        customer.setOwner(owner);
        customer.setContactSummary(contactSummary);
        return customerService.save(customer);
    }

    @RequestMapping("getUserListAndCustomer.do")
    @ResponseBody
    public Map<String,Object> getUserListAndCustomer(String id){
        return customerService.getUserListAndCustomer(id);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public boolean update(String id,String owner,String website,String description,String contactSummary,
            String nextContactTime,String address,String phone,String name,HttpServletRequest request){
        Customer c = new Customer();
        c.setId(id);
        c.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        c.setEditTime(DateTimeUtil.getSysTime());
        c.setName(name);
        c.setOwner(owner);
        c.setDescription(description);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setContactSummary(contactSummary);
        return customerService.update(c);
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public boolean delete(HttpServletRequest request){
        String[] ids = request.getParameterValues("id");
        return customerService.delete(ids);
    }
}
