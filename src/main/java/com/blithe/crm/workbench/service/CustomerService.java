package com.blithe.crm.workbench.service;

import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/5 20:46
 * Description:
 */

public interface CustomerService {
    List<String> getCustomerName(String name);

    PaginationVo<Customer> getPageList(Customer customer, Integer pageNo, Integer pageSize);

    boolean save(Customer customer);

    Map<String, Object> getUserListAndCustomer(String id);

    boolean update(Customer c);

    boolean delete(String[] ids);

    Customer getCustomer(String id);
}
