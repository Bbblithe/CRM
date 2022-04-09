package com.blithe.crm.vo;

import java.util.List;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/9 15:21
 * Description:
 */

public class ChartsVo<T> {
    private int total;
    private List<T> dataList;

    public ChartsVo(int total, List<T> dataList) {
        this.total = total;
        this.dataList = dataList;
    }

    public ChartsVo() {
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
