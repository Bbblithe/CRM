package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.ClueActivityRelation;

public interface ClueActivityRelationDao {

    int unband(String id);

    int bund(ClueActivityRelation clueActivityRelation);
}
