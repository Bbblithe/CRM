package com.blithe.crm.setting.service.impl;

import com.blithe.crm.setting.dao.DicTypeDao;
import com.blithe.crm.setting.dao.DicValueDao;
import com.blithe.crm.setting.domain.DicType;
import com.blithe.crm.setting.domain.DicValue;
import com.blithe.crm.setting.service.DicService;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/21 16:19
 * Description:
 */

@Service
public class DicServiceImpl implements DicService {
    @Resource
    private DicTypeDao dicTypeDao ;

    @Resource
    private DicValueDao dicValueDao ;


    @Override
    public Map<String, List<DicValue>> selectAll() {
        // 将字典类型列表取出
        List<DicType> list = dicTypeDao.getTypeList();

        Map<String,List<DicValue>> map = new HashMap<>();
        // 将字典类型列表遍历
        for(DicType dicType : list){
            // 取得每一个类型的value，并存在List之中
            List<DicValue> values = dicValueDao.getValues(dicType.getCode());
            map.put(dicType.getCode(),values);
        }
        return map;
    }
}
