package com.blithe.crm.setting.dao;

import com.blithe.crm.setting.domain.DicValue;

import java.util.List;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/21 16:16
 * Description:
 */

public interface DicValueDao {
    List<DicValue> getValues(String code);
}
