package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.exception.DeleteException;
import com.blithe.crm.exception.SaveException;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.dao.ContactsDao;
import com.blithe.crm.workbench.dao.CustomerDao;
import com.blithe.crm.workbench.domain.Contacts;
import com.blithe.crm.workbench.domain.Customer;
import com.blithe.crm.workbench.service.ContactsService;
import com.github.pagehelper.PageHelper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public boolean save(Contacts contacts) {
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
        contacts.setCustomerId(customer.getId());
        contactsDao.save(contacts);
        return true;
    }
}
