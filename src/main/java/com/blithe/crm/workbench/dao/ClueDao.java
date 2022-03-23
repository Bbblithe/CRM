package com.blithe.crm.workbench.dao;

import com.blithe.crm.workbench.domain.Clue;

import java.util.List;

public interface ClueDao {
    int save(Clue clue);

    int getTotal(Clue clue);

    List<Clue> selectClueListByCondition(Clue clue);

    int deleteClues(String[] ids);

    Clue selectClue(String id);

    int update(Clue clue);
}
