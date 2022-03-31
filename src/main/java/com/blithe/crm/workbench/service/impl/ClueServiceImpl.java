package com.blithe.crm.workbench.service.impl;

import com.blithe.crm.exception.ConvertException;
import com.blithe.crm.setting.dao.UserDao;
import com.blithe.crm.setting.domain.User;
import com.blithe.crm.utils.DateTimeUtil;
import com.blithe.crm.utils.UUIDUtil;
import com.blithe.crm.vo.PaginationVo;
import com.blithe.crm.workbench.dao.ClueActivityRelationDao;
import com.blithe.crm.workbench.dao.ClueDao;
import com.blithe.crm.workbench.dao.ClueRemarkDao;
import com.blithe.crm.workbench.dao.ContactsActivityRelationDao;
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
import com.blithe.crm.workbench.domain.ContactsActivityRelation;
import com.blithe.crm.workbench.domain.ContactsRemark;
import com.blithe.crm.workbench.domain.Customer;
import com.blithe.crm.workbench.domain.CustomerRemark;
import com.blithe.crm.workbench.domain.Tran;
import com.blithe.crm.workbench.domain.TranHistory;
import com.blithe.crm.workbench.service.ClueService;
import com.github.pagehelper.PageHelper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao;

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

    @Transactional
    @Override
    public boolean convert(String clueId, Tran t, String createBy) {
        String createTime = DateTimeUtil.getSysTime();
        boolean flag = false;
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
                throw new ConvertException("客户为空");
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
            throw new ConvertException("联系人添加失败");
        }
        // 经过第三步处理，联系人的信息我们已经拥有了，将来在处理其他表的时候，如果要使用到联系人的id
        // 直接使用cus.getId();

        // 4、线索备注转换到客户备注以及联系人备注
        // 查询出与该线索关联的备注信息列表
        List<ClueRemark> clueRemarkList = clueRemarkDao.showRemarkList(clueId);
        for(ClueRemark clueRemark : clueRemarkList){
            // 取出备注信息（主要转换到客户备注和联系人备注的就是这个备注信息）
            String noteContent = clueRemark.getNoteContent();

            // 创建客户备注对象，添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(DateTimeUtil.getSysTime());
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            int count3 =  customerRemarkDao.save(customerRemark);
            if(count3 == 0){
                throw new ConvertException("客户备注添加失败");
            }

            // 创建联系人备注对象，添加联系人
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            int count4 =  contactsRemarkDao.save(contactsRemark);
            if(count4 == 0){
                throw new ConvertException("联系人备注添加失败");
            }
        }

        // 5、"线索和市场活动"的关系转换到"联系人和市场活动"的关系
        // 要查询与该条线索关联的市场活动，查询到与市场活动的关联关系列表
        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationDao.getListByClueId(clueId);
        // 遍历出每一条与市场活动关联的关系记录
        for(ClueActivityRelation clueActivityRelation : clueActivityRelations){
            // 从每一条遍历出来的记录中取出关联的市场活动id
            String activityId = clueActivityRelation.getActivityId();

            // 创建联系人与市场活动的关联关系对象，让第三步生成的联系人与市场活动做关联
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(con.getId());
            // 添加联系人与市场活动的关联关系
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if(count5 == 0){
                throw new ConvertException("联系人与市场活动关联添加失败");
            }
        }

        // 6、1）如果有创建交易需求，创建一条交易
        if(t != null){
            /*
                t对象在controller中已经封装好的信息如下
                    id,money,name,expectedDate,stage,activityId,createBy
                    ,createTime
                通过第一步生成的c对象，取出一些信息，继续完善对t对象的封装
             */
            t.setSource(c.getSource());
            t.setOwner(c.getOwner());
            t.setNextContactTime(c.getNextContactTime());
            t.setDescription(c.getDescription());
            t.setCustomerId(cus.getId());
            t.setContactsId(con.getId());
            t.setContactSummary(c.getContactSummary());
            int count6 = tranDao.save(t);
            if(count6 == 0){
                throw new ConvertException("交易请求添加失败！");
            }

            // 2） 如果创建了交易，则创建一条交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(DateTimeUtil.getSysTime());
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());
            tranHistory.setTranId(t.getId());

            // 添加交易历史
            int count7 = tranHistoryDao.save(tranHistory);
            if(count7 != 1){
                throw new ConvertException("交易历史添加失败");
            }
        }

        // 7、删除线索备注
        for(ClueRemark clueRemark : clueRemarkList){
            int count8 = clueRemarkDao.deleteRemark(clueRemark.getId());
            if(count8 != 1){
                throw new ConvertException("线索删除失败");
            }
        }

        // 8、删除线索和市场活动的关系
        for(ClueActivityRelation clueActivityRelation : clueActivityRelations){
            int count9 = clueActivityRelationDao.delete(clueActivityRelation);
            if(count9 == 0){
                throw new ConvertException("线索和市场活动删除失败");
            }
        }

        // 9、删除线索
        String[] cs = new String[1];
        cs[0] = clueId;
        int count10 =clueDao.deleteClues(cs);
        if(count10 != 1){
            throw new ConvertException("线索删除失败");
        }
        flag = true;
        return flag;
    }
}
