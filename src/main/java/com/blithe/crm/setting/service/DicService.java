package com.blithe.crm.setting.service;

import com.blithe.crm.setting.domain.DicValue;

import java.util.List;
import java.util.Map;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/21 16:19
 * Description:
 */

public interface DicService {
    Map<String, List<DicValue>> selectAll();
}
