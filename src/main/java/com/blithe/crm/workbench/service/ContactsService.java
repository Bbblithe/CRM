package com.blithe.crm.workbench.service;

import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Contacts;
import com.blithe.crm.workbench.domain.ContactsRemark;

import java.util.List;
import java.util.Map;

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

    Map<String, Object> getUserListAndContacts(String id);

    boolean update(Contacts con);

    Contacts getDetail(String id);

    List<ContactsRemark> showRemarkList(String id);

    Map<String, Object> saveRemark(ContactsRemark cr);

    boolean deleteRemark(String id);

    Map<String, Object> updateRemark(ContactsRemark cr);

    boolean unband(String id, String conId);

    boolean bund(String contactsId, String[] ids);
}
