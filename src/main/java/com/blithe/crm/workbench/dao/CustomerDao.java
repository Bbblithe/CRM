package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);

    String getIdByName(String customerName);
}
