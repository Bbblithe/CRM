package com.blithe.crm.workbench.web.controller;

import com.blithe.crm.setting.domain.User;
import com.blithe.crm.utils.DateTimeUtil;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Contacts;
import com.blithe.crm.workbench.domain.ContactsRemark;
import com.blithe.crm.workbench.service.ContactsService;

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

   @RequestMapping("update.do")
   @ResponseBody
   public boolean update(String id, String fullname, String appellation, String owner, String job,
                         String email, String source, String description, String customerName,HttpServletRequest request,
                         String contactSummary, String nextContactTime, String address, String mphone, String birth){
      Contacts con = new Contacts();
      con.setCustomerId(customerName);
      con.setMphone(mphone);
      con.setAddress(address);
      con.setNextContactTime(nextContactTime);
      con.setBirth(birth);
      con.setDescription(description);
      con.setId(id);
      con.setFullname(fullname);
      con.setAppellation(appellation);
      con.setOwner(owner);
      con.setJob(job);
      con.setEmail(email);
      con.setSource(source);
      con.setContactSummary(contactSummary);
      con.setEditBy(((User)request.getSession().getAttribute("user")).getName());
      con.setEditTime(DateTimeUtil.getSysTime());
      return contactsService.update(con);
   }

   @RequestMapping("detail.do")
   public ModelAndView showDetail(String id){
      ModelAndView mv = new ModelAndView();
      mv.addObject("con",contactsService.getDetail(id));
      mv.setViewName("/workbench/contacts/detail.jsp");
      return mv;
   }

   @RequestMapping("showRemarkList.do")
   @ResponseBody
   public List<ContactsRemark> showRemark(String id){
      return contactsService.showRemarkList(id);
   }

   @RequestMapping("saveRemark.do")
   @ResponseBody
   public Map<String,Object> saveRemark(String contactsId,String noteContent,HttpServletRequest request){
      ContactsRemark cr = new ContactsRemark();
      cr.setId(UUIDUtil.getUUID());
      cr.setContactsId(contactsId);
      cr.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
      cr.setCreateTime(DateTimeUtil.getSysTime());
      cr.setNoteContent(noteContent);
      cr.setEditFlag("0");
      return contactsService.saveRemark(cr);
   }

   @RequestMapping("deleteRemark.do")
   @ResponseBody
   public boolean deleteRemark(String id){
      return contactsService.deleteRemark(id);
   }

   @RequestMapping("updateRemark.do")
   @ResponseBody
   public Map<String,Object> updateRemark(String id,String noteContent,HttpServletRequest request){
      ContactsRemark cr = new ContactsRemark();
      cr.setId(id);
      cr.setEditBy(((User)request.getSession().getAttribute("user")).getName());
      cr.setEditTime(DateTimeUtil.getSysTime());
      cr.setNoteContent(noteContent);
      cr.setEditFlag("1");
      return contactsService.updateRemark(cr);
   }

   @RequestMapping("getUserListAndContacts.do")
   @ResponseBody
   public Map<String,Object> getUserListAndContacts(String id){
      Map<String,Object> map =  contactsService.getUserListAndContacts(id);
      return map;
   }
}
