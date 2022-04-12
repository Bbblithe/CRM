package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsDao {

    int save(Contacts con);

    List<Contacts> getContactsListByName(String name);

    int getTotal(Contacts c);

    List<Contacts> selectContactsListByCondition(Contacts c);

    int delete(String id);

    Contacts selectContactsById(String id);

    int update(Contacts con);

    Contacts getContactsById(String id);
}
