package com.blithe.crm.setting.service.impl;

import com.blithe.crm.setting.dao.DicTypeDao;
import com.blithe.crm.setting.dao.DicValueDao;
import com.blithe.crm.setting.service.DicService;

import javax.annotation.Resource;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/21 16:19
 * Description:
 */

class DicServiceImpl implements DicService {
    @Resource
    DicTypeDao dicTypeDao;

    @Resource
    DicValueDao dicValueDao;

}
