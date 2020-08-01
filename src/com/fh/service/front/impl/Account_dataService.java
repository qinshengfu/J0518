package com.fh.service.front.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.front.Account_dataManager;


/**
 * 说明： 用户资料表
 * 创建人：
 * 创建时间：2020-05-21
 */
@Service("account_dataService")
@CacheConfig(cacheNames = "J0518_AccountService")
public class Account_dataService implements Account_dataManager {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void save(PageData pd) throws Exception {
        dao.save("Account_dataMapper.save", pd);
    }

    /**
     * 删除
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void delete(PageData pd) throws Exception {
        dao.delete("Account_dataMapper.delete", pd);
    }

    /**
     * 修改
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void edit(PageData pd) throws Exception {
        dao.update("Account_dataMapper.edit", pd);
    }

    /**
     * 根据用户ID修改
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void editByUserId(PageData pd) throws Exception {
        dao.update("Account_dataMapper.editByUserId", pd);
        // 更新用户为已实名
        PageData map = new PageData();
        map.put("ACCOUNT_ID", pd.get("USER_ID"));
        map.put("IS_REAL", 1);
        dao.update("AccountMapper.edit", map);
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) dao.findForList("Account_dataMapper.datalistPage", page);
    }

    /**
     * 列表(全部)
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> listAll(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("Account_dataMapper.listAll", pd);
    }

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("Account_dataMapper.findById", pd);
    }

    /**
     * 通过用户资料获取数据判断是否重复
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData findByUserData(PageData pd) throws Exception {
        return (PageData) dao.findForObject("Account_dataMapper.findByUserData", pd);
    }

    /**
     * 通过UserId获取数据
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData findByUserId(PageData pd) throws Exception {
        return (PageData) dao.findForObject("Account_dataMapper.findByUserId", pd);
    }

    /**
     * 批量删除
     *
     * @param ArrayDATA_IDS
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
        dao.delete("Account_dataMapper.deleteAll", ArrayDATA_IDS);
    }

}

