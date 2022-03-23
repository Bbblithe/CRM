package com.blithe.crm.workbench.service;

import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Clue;

import java.util.Map;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/21 16:08
 * Description:
 */

public interface ClueService {
    boolean save(Clue clue);

    PaginationVo<Clue> pageList(Integer pageNo, Integer pageSize, Clue clue);

    Boolean delete(String[] ids);

    Map<String, Object> getUserListAndClue(String id);

    boolean update(Clue clue);

    Clue getDetail(String id);
}
