package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.setting.dao.UserDao;
import com.blithe.crm.setting.domain.User;
import com.blithe.crm.utils.DateTimeUtil;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.dao.ClueActivityRelationDao;
import com.blithe.crm.workbench.dao.ClueDao;
import com.blithe.crm.workbench.dao.ClueRemarkDao;
import com.blithe.crm.workbench.dao.ContactsDao;
import com.blithe.crm.workbench.dao.ContactsRemarkDao;
import com.blithe.crm.workbench.dao.CustomerDao;
import com.blithe.crm.workbench.dao.CustomerRemarkDao;
import com.blithe.crm.workbench.dao.TranDao;
import com.blithe.crm.workbench.dao.TranHistoryDao;
import com.blithe.crm.workbench.domain.Clue;
import com.blithe.crm.workbench.domain.ClueActivityRelation;
import com.blithe.crm.workbench.domain.ClueRemark;
import com.blithe.crm.workbench.domain.Contacts;
import com.blithe.crm.workbench.domain.Customer;
import com.blithe.crm.workbench.domain.Tran;
import com.blithe.crm.workbench.service.ClueService;
import com.github.pagehelper.PageHelper;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/21 16:08
 * Description:
 */

@Service
public class ClueServiceImpl implements ClueService {
    @Resource
    private UserDao userDao;

    // 线索相关表
    @Resource
    private ClueDao clueDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;
    @Resource
    private ClueRemarkDao clueRemarkDao;

    // 客户相关表
    @Resource
    private CustomerDao customerDao;
    @Resource
    private CustomerRemarkDao customerRemarkDao;

    // 联系人相关表
    @Resource
    private ContactsDao contactsDao;
    @Resource
    private ContactsRemarkDao contactsRemarkDao;

    // 交易相关表
    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;

    @Override
    public boolean save(Clue clue) {
        return clueDao.save(clue) == 1;
    }

    @Override
    public PaginationVo<Clue> pageList(Integer pageNo, Integer pageSize, Clue clue) {
        int total = clueDao.getTotal(clue);

        PageHelper.startPage(pageNo,pageSize);
        List<Clue> clueList = clueDao.selectClueListByCondition(clue);
        PaginationVo<Clue> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(clueList);
        return vo;
    }

    @Override
    public Boolean delete(String[] ids) {
        clueDao.disconnect(ids);
        return clueDao.deleteClues(ids) == ids.length;
    }

    @Override
    public Map<String, Object> getUserListAndClue(String id) {
        User user = userDao.selectUserByC(id);
        List<User> userList = userDao.selectOtherUsersByC(id);

        Clue clue = clueDao.selectClue(id);
        Map<String,Object> map = new HashMap<>();
        map.put("user",user);
        map.put("userList",userList);
        map.put("clue",clue);
        return map;
    }

    @Override
    public boolean update(Clue clue) {
        return clueDao.update(clue) == 1;
    }

    @Override
    public Clue getDetail(String id) {
        return clueDao.detail(id);
    }

    @Override
    public boolean unband(String id) {
        return clueActivityRelationDao.unband(id) == 1;
    }

    @Override
    public boolean bund(String clueId, String[] ids) {
        boolean flag = true;
        for(String id:ids){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(clueId);
            car.setActivityId(id);

            flag = clueActivityRelationDao.bund(car)==1;
        }
        return flag;
    }

    @Override
    public boolean deleteClueById(String id) {
        String[] ids = new String[1];
        ids[0] = id;
        clueDao.disconnect(ids);
        int count2 = clueDao.deleteClues(ids);
        return count2 == 1;
    }

    @Override
    public List<ClueRemark> showRemarkList(String id) {
        return clueRemarkDao.showRemarkList(id);
    }

    @Override
    public Map<String, Object> saveRemark(ClueRemark cr) {
        Map<String,Object> map = new HashMap<>();
        map.put("success",clueRemarkDao.saveRemark(cr) == 1);
        return map;
    }

    @Override
    public Map<String, Object> updateRemark(ClueRemark cr) {
        Map<String,Object> map = new HashMap<>();
        map.put("success",clueRemarkDao.updateRemark(cr) == 1);
        return map;
    }

    @Override
    public boolean deleteRemark(String id) {
        return clueRemarkDao.deleteRemark(id) == 1;
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {
        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;
        // 1、通过线索id获取到线索对象（线索对象中封装了线索的信息）
        Clue c = clueDao.selectClue(clueId);

        // 2、通过线索对象提取客户消息，当客户不存在的时候，新建客户（根据公司的名称精确匹配，判断客户是否存在）
        String company = c.getCompany();
        Customer cus = customerDao.getCustomerByName(company);
        // 如果cus为null，说明以前没有这个客户，需要新建一个
        if(cus == null){
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setAddress(c.getAddress());
            cus.setWebsite(c.getWebsite());
            cus.setPhone(c.getPhone());
            cus.setOwner(c.getOwner());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setName(company);
            cus.setDescription(c.getDescription());
            cus.setCreateBy(createBy);
            cus.setCreateBy(c.getCreateBy());
            cus.setCreateTime(c.getCreateTime());
            cus.setContactSummary(c.getContactSummary());
            // 添加客户
            int count1 = customerDao.save(cus);
            if(count1 != 1){
                flag = false;
            }
        }

        // 经过第二部处理之后，客户的信息我们已经拥有了,在使用其他表的时候，如果使用到客户的id
        // 直接使用cus.getId();

        // 3、通过线索对象提取联系人信息，保存联系人
        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setSource(c.getSource());
        con.setOwner(c.getOwner());
        con.setNextContactTime(c.getNextContactTime());
        con.setMphone(c.getMphone());
        con.setJob(c.getJob());
        con.setFullname(c.getFullname());
        con.setEmail(c.getEmail());
        con.setDescription(c.getDescription());
        con.setCustomerId(cus.getId());
        con.setCreateTime(createTime);
        con.setCreateBy(createBy);
        con.setContactSummary(c.getContactSummary());
        con.setAppellation(c.getAppellation());
        con.setAddress(c.getAddress());
        // 添加联系人
        int count2 = contactsDao.save(con);
        if(count2 != 1){
            flag = false;
        }
        // 经过第三步处理，联系人的信息我们已经拥有了，将来在处理其他表的时候，如果要使用到联系人的id
        // 直接使用cus.getId();
        return flag;
    }
}
