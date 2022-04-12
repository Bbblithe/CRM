package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.exception.ChangeException;
import com.blithe.crm.exception.DeleteException;
import com.blithe.crm.exception.SaveException;
import com.blithe.crm.setting.dao.UserDao;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.dao.ContactsDao;
import com.blithe.crm.workbench.dao.ContactsRemarkDao;
import com.blithe.crm.workbench.dao.CustomerDao;
import com.blithe.crm.workbench.domain.Contacts;
import com.blithe.crm.workbench.domain.ContactsRemark;
import com.blithe.crm.workbench.domain.Customer;
import com.blithe.crm.workbench.service.ContactsService;
import com.github.pagehelper.PageHelper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/5 20:44
 * Description:
 */

@Service
public class ContactsServiceImpl implements ContactsService {
    @Resource
    private ContactsDao contactsDao;

    @Resource
    private CustomerDao customerDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ContactsRemarkDao contactsRemarkDao;

    @Override
    public List<Contacts> getContactsListByName(String name) {
        return contactsDao.getContactsListByName(name);
    }

    @Override
    public PaginationVo<Contacts> pageList(Integer pageNo, Integer pageSize, Contacts c) {
        PaginationVo<Contacts> vo = new PaginationVo<>();
        vo.setTotal(contactsDao.getTotal(c));
        PageHelper.startPage(pageNo,pageSize);
        vo.setDataList(contactsDao.selectContactsListByCondition(c));
        return vo;
    }

    @Override
    @Transactional
    public boolean delete(String[] ids) {
        int count = 0;
        for(int i = 0 ; i < ids.length ; i ++){
            count += contactsDao.delete(ids[i]);
        }
        if(count != ids.length)
            throw new DeleteException("删除联系人出错！");
        return true;
    }

    @Override
    @Transactional
    public boolean save(Contacts contacts) {
        contacts.setCustomerId(addCustomer(contacts).getId());
        contactsDao.save(contacts);
        return true;
    }

    @Override
    public Map<String, Object> getUserListAndContacts(String id) {
        Map<String,Object> map = new HashMap<>();
        map.put("user",userDao.selectUserByContacts(id));
        map.put("userList",userDao.selectOtherUserByContacts(id));
        map.put("contacts",contactsDao.selectContactsById(id));
        return  map;
    }

    @Override
    @Transactional
    public boolean update(Contacts con) {
        con.setCustomerId(addCustomer(con).getId());
        if(contactsDao.update(con) !=1){
            throw new ChangeException("更新联系人出错了!");
        }
        return true;
    }

    @Override
    public Contacts getDetail(String id) {
        return contactsDao.getContactsById(id);
    }

    @Override
    public List<ContactsRemark> showRemarkList(String id) {
        return contactsRemarkDao.selectListById(id);
    }

    @Override
    public Map<String, Object> saveRemark(ContactsRemark cr) {
        Map<String,Object> map = new HashMap<>();
        map.put("cr",cr);
        map.put("success",contactsRemarkDao.save(cr));
        return map;
    }

    @Override
    public boolean deleteRemark(String id) {
        return contactsRemarkDao.delete(id) == 1;
    }

    @Override
    public Map<String, Object> updateRemark(ContactsRemark cr) {
        Map<String,Object> map = new HashMap<>();
        map.put("cr",cr);
        map.put("success",contactsRemarkDao.update(cr) == 1);
        return map;
    }

    private Customer addCustomer(Contacts contacts){
        Customer customer = customerDao.getCustomerByName(contacts.getCustomerId());
        if(customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(contacts.getCustomerId());
            customer.setCreateTime(contacts.getCreateTime());
            customer.setCreateBy(contacts.getCreateBy());
            customer.setContactSummary(contacts.getContactSummary());
            customer.setNextContactTime(contacts.getNextContactTime());
            customer.setOwner(contacts.getOwner());
            // 添加客户
            if(customerDao.save(customer) != 1){
                throw new SaveException("客户添加失败");
            }
        }
        return customer;
    }
}
