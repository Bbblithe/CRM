package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.ContactsActivityRelation;

import org.apache.ibatis.annotations.Param;

public interface ContactsActivityRelationDao {

    int save(ContactsActivityRelation contactsActivityRelation);

    int unband(@Param("id") String id,@Param("conId")String conId);

    int bund(ContactsActivityRelation car);
}
