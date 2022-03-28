package com.blithe.crm.workbench.service;

import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.domain.Clue;
import com.blithe.crm.workbench.domain.ClueRemark;

import java.util.List;
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

    boolean unband(String id);

    boolean bund(String clueId, String[] ids);

    boolean deleteClueById(String id);

    List<ClueRemark> showRemarkList(String id);

    Map<String, Object> saveRemark(ClueRemark cr);

    Map<String, Object> updateRemark(ClueRemark cr);

    boolean deleteRemark(String id);
}
