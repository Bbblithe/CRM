package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.Clue;

import java.util.List;

public interface ClueDao {
    int save(Clue clue);

    int getTotal(Clue clue);

    List<Clue> selectClueListByCondition(Clue clue);
}
