package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.workbench.dao.CustomerDao;
import com.blithe.crm.workbench.service.CustomerService;

import org.springframework.stereotype.Service;

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

    @Override
    public String getIdByName(String customerName) {
        return customerDao.getIdByName(customerName);
    }
}
