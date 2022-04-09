package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.exception.ChangeException;
import com.blithe.crm.exception.SaveException;
import com.blithe.crm.utils.DateTimeUtil;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.vo.ChartsVo;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.dao.CustomerDao;
import com.blithe.crm.workbench.dao.TranDao;
import com.blithe.crm.workbench.dao.TranHistoryDao;
import com.blithe.crm.workbench.domain.Customer;
import com.blithe.crm.workbench.domain.Tran;
import com.blithe.crm.workbench.domain.TranHistory;
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
    @Transactional
    public boolean save(Tran t, String customerName) {
        /*
            交易添加业务：
                在做添加之前，参数t里面就少了一个项信息，就是客户的主键,cusomterId

                先处理客户相关的需求：
                    1）判断customerName，根据客户名称在客户表进行精确查询
                        如果有，则取出id封装的t对象中。
                        如果没有，则创建新的客户。
         */
        Customer customer = customerDao.getCustomerByName(customerName);
        if(customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
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

        t.setCustomerId(customer.getId());

        if(tranDao.save(t) != 1){
            throw new SaveException("交易添加失败");
        }
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(t.getCreateTime());
        th.setCreateBy(t.getCreateBy());

        if(historyDao.save(th) != 1){
            throw new SaveException("交易历史添加失败");
        }
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
}
