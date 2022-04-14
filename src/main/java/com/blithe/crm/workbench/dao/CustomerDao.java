package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);

    List<String> getCustomerName(String name);

    int getTotalByName(Customer customer);

    List<Customer> getDataListByName(Customer customer);

    Customer getCustomerById(String id);

    int update(Customer c);

    int delete(String id);
}
