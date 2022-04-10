package com.blithe.crm.workbench.service;

import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Contacts;

import java.util.List;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/5 20:43
 * Description:
 */

public interface ContactsService {
    List<Contacts> getContactsListByName(String name);

    PaginationVo<Contacts> pageList(Integer pageNo, Integer pageSize, Contacts c);

    boolean delete(String[] ids);

    boolean save(Contacts contacts);
}
