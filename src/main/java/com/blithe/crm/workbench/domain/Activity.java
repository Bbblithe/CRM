package com.blithe.crm.workbench.domain;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/18 18:36
 * Description:
 */

public class Activity {

    private String id; // 主键
    private String owner; // 所有者 外建 关联tbl_user表
    private String name; // 市场活动的名称
    private String startDate; // 开始日期 年月日
    private String endDate; // 结束日期 年月日
    private String cost; // 花费
    private String description; // 描述
    private String createTime; //创建时间 年月日 时分秒
    private String createBy; // 创建人
    private String editTime; // 编辑修改时间 年月日 时分秒
    private String editBy; // 修改人

    public Activity() {
    }

    public Activity(String id, String owner, String name, String startDate, String endDate, String cost, String description, String createTime, String createBy, String editTime, String editBy) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cost = cost;
        this.description = description;
        this.createTime = createTime;
        this.createBy = createBy;
        this.editTime = editTime;
        this.editBy = editBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getEditBy() {
        return editBy;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", cost='" + cost + '\'' +
                ", description='" + description + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createBy='" + createBy + '\'' +
                ", editTime='" + editTime + '\'' +
                ", editBy='" + editBy + '\'' +
                '}';
    }
}
