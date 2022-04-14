package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.exception.DeleteException;
import com.blithe.crm.setting.dao.UserDao;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.dao.CustomerDao;
import com.blithe.crm.workbench.domain.Customer;
import com.blithe.crm.workbench.service.CustomerService;
import com.github.pagehelper.PageHelper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/5 20:47
 * Description:
 */

@Service
public class CustomerServiceImpl implements CustomerService {
    @Resource
    private CustomerDao customerDao;

    @Resource
    private UserDao userDao;

    @Override
    public List<String> getCustomerName(String name) {
        return customerDao.getCustomerName(name);
    }

    @Override
    public PaginationVo<Customer> getPageList(Customer customer, Integer pageNo, Integer pageSize) {
        PaginationVo<Customer> vo = new PaginationVo<>();
        vo.setTotal(customerDao.getTotalByName(customer));
        PageHelper.startPage(pageNo,pageSize);
        vo.setDataList(customerDao.getDataListByName(customer));
        return vo;
    }

    @Override
    public boolean save(Customer customer) {
        return customerDao.save(customer) == 1;
    }

    @Override
    public Map<String, Object> getUserListAndCustomer(String id) {
        Map<String,Object> map = new HashMap<>();
        map.put("user",userDao.selectUserByCustomerId(id));
        map.put("userList",userDao.selectOtherUserByContacts(id));
        map.put("customer",customerDao.getCustomerById(id));
        return map;
    }

    @Override
    public boolean update(Customer c) {
        return customerDao.update(c) == 1;
    }

    @Override
    @Transactional
    public boolean delete(String[] ids) {
        int length = ids.length;
        int count = 0;
        for(int i = 0 ; i < length ; i ++){
            count += customerDao.delete(ids[i]);
        }
        if(count != length){
            throw new DeleteException("删除顾客失败！");
        }
        return true;
    }
}
