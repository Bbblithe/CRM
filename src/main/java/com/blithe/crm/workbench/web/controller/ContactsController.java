package com.blithe.crm.workbench.web.controller;

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
}
