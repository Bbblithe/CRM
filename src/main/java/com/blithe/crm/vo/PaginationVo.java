package com.blithe.crm.vo;

import java.util.List;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/19 10:00
 * Description:
 */

public class PaginationVo<T> {
    private int total;
    private List<T> dataList;

    public PaginationVo() {
    }

    public PaginationVo(int total, List<T> dataList) {
        this.total = total;
        this.dataList = dataList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "PaginationVo{" +
                "total=" + total +
                ", dataList=" + dataList +
                '}';
    }
}
