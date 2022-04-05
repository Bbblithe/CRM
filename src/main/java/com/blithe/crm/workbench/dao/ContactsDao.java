package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.Contacts;

public interface ContactsDao {

    int save(Contacts con);

    String getIdByName(String contactName);
}
