package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.exception.ChangeException;
import com.blithe.crm.exception.DeleteException;
import com.blithe.crm.exception.SaveException;
import com.blithe.crm.utils.DateTimeUtil;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.vo.ChartsVo;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.dao.CustomerDao;
import com.blithe.crm.workbench.dao.TranDao;
import com.blithe.crm.workbench.dao.TranHistoryDao;
import com.blithe.crm.workbench.dao.TranRemarkDao;
import com.blithe.crm.workbench.domain.Customer;
import com.blithe.crm.workbench.domain.Tran;
import com.blithe.crm.workbench.domain.TranHistory;
import com.blithe.crm.workbench.domain.TranRemark;
import com.blithe.crm.workbench.service.TranService;
import com.github.pagehelper.PageHelper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/1 20:21
 * Description:
 */

@Service
public class TranServiceImpl implements TranService {
    @Resource
    private TranDao tranDao;

    @Resource
    private TranHistoryDao historyDao;

    @Resource
    private CustomerDao customerDao;

    @Resource
    private TranRemarkDao remarkDao;

    @Override
    public PaginationVo<Tran> pageList(Integer pageNo, Integer pageSize, Tran tran) {
        PaginationVo<Tran> pv = new PaginationVo<>();
        pv.setTotal(tranDao.getTotal(tran));

        PageHelper.startPage(pageNo,pageSize);
        List<Tran> tranList = tranDao.selectTranListByCondition(tran);
        pv.setDataList(tranList);
        return pv;
    }

    @Override
    public List<TranHistory> getHistoryById(String tranId) {
        return historyDao.getHistoryListById(tranId);
    }

    @Override
    public List<Tran> getTranList() {
        return tranDao.getTranList();
    }

    @Override
    @Transactional
    public boolean save(Tran t) {
        /*
            交易添加业务：
                在做添加之前，参数t里面就少了一个项信息，就是客户的主键,cusomterId

                先处理客户相关的需求：
                    1）判断customerName，根据客户名称在客户表进行精确查询
                        如果有，则取出id封装的t对象中。
                        如果没有，则创建新的客户。
         */

        t.setCustomerId(addCustomer(t).getId());

        if(tranDao.save(t) != 1){
            throw new SaveException("交易添加失败");
        }
        addTranHistory(t);
        return true;
    }

    @Override
    public Tran detail(String id) {
        return tranDao.detail(id);
    }

    @Override
    @Transactional
    public boolean changeStage(Tran t) {
        boolean flag = tranDao.changeStage(t) == 1;
        if(!flag){
            throw new ChangeException("交易改变阶段失败！");
        }
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setStage(t.getStage());
        th.setTranId(t.getId());
        th.setMoney(t.getMoney());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        if(historyDao.save(th) != 1){
            throw new ChangeException("交易历史创建失败！");
        }
        return flag;
    }

    @Override
    public ChartsVo<Map<String, String>> getCharts() {
        ChartsVo<Map<String,String>> vo = new ChartsVo();
        vo.setTotal(tranDao.getTotalNum());
        vo.setDataList(tranDao.getCharts());
        return vo;
    }

    @Override
    public Tran edit(String id) {
        return tranDao.getInformation(id);
    }

    @Override
    @Transactional
    public boolean update(Tran t) {
        t.setCustomerId(addCustomer(t).getId());
        if(tranDao.update(t) != 1){
            throw new SaveException("交易添加失败");
        }
        addTranHistory(t);
        return true;
    }

    @Override
    @Transactional
    public boolean delete(String[] ids) {
        int length = ids.length;
        int count = 0;
        for(int i = 0 ; i < length ; i ++){
            historyDao.delete(ids[i]);
            count += tranDao.delete(ids[i]);
        }
        if(count != length){
            throw new DeleteException("删除失败");
        }
        return true;
    }

    @Override
    public List<TranRemark> showRemarkList(String id) {
        return remarkDao.getListById(id);
    }

    @Override
    public boolean saveRemark(TranRemark tr) {
        return remarkDao.save(tr) == 1;
    }

    @Override
    public boolean updateRemark(TranRemark tr) {
        return remarkDao.update(tr) == 1;
    }

    @Override
    public boolean deleteRemark(String id) {
        return remarkDao.delete(id) == 1;
    }


    private Customer addCustomer(Tran t){
        Customer customer = customerDao.getCustomerByName(t.getCustomerId());
        if(customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(t.getCustomerId());
            customer.setCreateTime(t.getCreateTime());
            customer.setCreateBy(t.getCreateBy());
            customer.setContactSummary(t.getContactSummary());
            customer.setNextContactTime(t.getNextContactTime());
            customer.setOwner(t.getOwner());
            // 添加客户
            if(customerDao.save(customer) != 1){
                throw new SaveException("客户添加失败");
            }
        }
        return customer;
    }

    private void addTranHistory(Tran t){
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy((t.getEditBy()!=""&&t.getEditBy()!=null?t.getEditBy():t.getCreateBy()));

        if(historyDao.save(th) != 1){
            throw new SaveException("交易历史添加失败");
        }
    }
}
