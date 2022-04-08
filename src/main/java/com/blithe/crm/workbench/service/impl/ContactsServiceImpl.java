package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.workbench.dao.ContactsDao;
import com.blithe.crm.workbench.domain.Contacts;
import com.blithe.crm.workbench.service.ContactsService;

import org.springframework.stereotype.Service;

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

    @Override
    public List<Contacts> getContactsListByName(String name) {
        return contactsDao.getContactsListByName(name);
    }
}
