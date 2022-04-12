package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.ContactsRemark;

import java.util.List;

public interface ContactsRemarkDao {

    int save(ContactsRemark customerRemark);

    List<ContactsRemark> selectListById(String id);

    int delete(String id);

    int update(ContactsRemark cr);
}
