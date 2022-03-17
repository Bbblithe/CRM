package com.blithe.crm.setting.domain;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/17 10:31
 * Description:
 */

public class User {
    /*
        关于字符串中表现的日期及时间
        我们在市场上常用的有两种方式
        日期：年月日
            yyyy-MM-dd 10位字符串
        日期 + 时间 ： 年月日时分秒
            yyyy-MM-dd HH:mm:ss 19位字符串
     */


    private String id; // 编号 主键
    private String loginAct; //  登陆账号
    private String name; // 用户的真实姓名
    private String loginPwd; // 登陆密码
    private String email; // 用户邮箱
    private String expireTime; // 失效时间
    private String lockState; // 锁定状态 0表示锁定，1表示启用
    private String deptno; // 部门编号
    private String allowIps; // 允许访问的ip地址
    private String createTime; // 创建时间
    private String createBy; // 创建人
    private String editeTime; // 编辑修改时间
    private String editBy; // 修改人

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginAct() {
        return loginAct;
    }

    public void setLoginAct(String loginAct) {
        this.loginAct = loginAct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getLockState() {
        return lockState;
    }

    public void setLockState(String lockState) {
        this.lockState = lockState;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getAllowIps() {
        return allowIps;
    }

    public void setAllowIps(String allowIps) {
        this.allowIps = allowIps;
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

    public String getEditeTime() {
        return editeTime;
    }

    public void setEditeTime(String editeTime) {
        this.editeTime = editeTime;
    }

    public String getEditBy() {
        return editBy;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }
}
