package com.blithe.crm.workbench.web.controller;

import com.blithe.crm.setting.domain.User;
import com.blithe.crm.utils.DateTimeUtil;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Contacts;
import com.blithe.crm.workbench.service.ContactsService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/10 19:29
 * Description:
 */

@Controller
@RequestMapping("workbench/contacts")
public class ContactsController {
   @Resource
   private ContactsService contactsService;

   @RequestMapping("pageList.do")
   @ResponseBody
   public PaginationVo<Contacts> getPageList(Integer pageNo, Integer pageSize, String source, String owner,
                                   String fullname, String customerName, String birth){
      Contacts c = new Contacts();
      c.setSource(source);
      c.setOwner(owner);
      c.setFullname(fullname);
      c.setCustomerId(customerName);
      c.setBirth(birth);
      return contactsService.pageList(pageNo,pageSize,c);
   }

   @RequestMapping("delete.do")
   @ResponseBody
   public boolean delete(HttpServletRequest request){
      String[] ids = request.getParameterValues("id");
      return contactsService.delete(ids);
   }

   @RequestMapping("save.do")
   @ResponseBody
   public boolean save(String fullname, String appellation,String owner,String job, String customerName,
        String email, String mphone, String source, String description, String contactSummary, String nextContactTime,
        String birth, String address,HttpServletRequest request){
      Contacts contacts = new Contacts();
      contacts.setId(UUIDUtil.getUUID());
      contacts.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
      contacts.setCreateTime(DateTimeUtil.getSysTime());
      contacts.setBirth(birth);
      contacts.setAddress(address);
      contacts.setMphone(mphone);
      contacts.setNextContactTime(nextContactTime);
      contacts.setAppellation(appellation);
      contacts.setEmail(email);
      contacts.setJob(job);
      contacts.setFullname(fullname);
      contacts.setOwner(owner);
      contacts.setCustomerId(customerName);
      contacts.setSource(source);
      contacts.setDescription(description);
      contacts.setContactSummary(contactSummary);
      return contactsService.save(contacts);
   }

}