package com.blithe.crm.workbench.service;

import com.blithe.crm.workbench.domain.Contacts;

import java.util.List;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/5 20:43
 * Description:
 */

public interface ContactsService {
    List<Contacts> getContactsListByName(String name);
}
