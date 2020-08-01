package com.fh.service.front.impl;

import java.util.List;
import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrSpliter;
import org.jsoup.helper.DataUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.front.User_addrManager;

/**
 * 说明： 用户收货地址表
 * 创建人：
 * 创建时间：2020-05-21
 */
@Service("user_addrService")
@CacheConfig(cacheNames = "J0518_User_addrService")
public class User_addrService implements User_addrManager {

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
        // 判断是否为默认地址
        if ("1".equals(pd.get("COMMON_ADDR").toString())) {
            PageData map = new PageData();
            map.put("USER_ID", pd.get("USER_ID"));
            map.put("COMMON_ADDR", 0);
            // 把其他地址全部改为普通
            dao.update("User_addrMapper.editByUserId", map);
        }
        // 切割省市区
        List<String> region = StrSpliter.split(pd.getString("region"), ",", 0, true, true);
        pd.put("GMT_CREATE", DateUtil.now());
        pd.put("GMT_MODIFIED", DateUtil.now());
        pd.put("PROVINCE_ID", "");
        pd.put("PROVINCE", region.get(0));
        pd.put("CITY_ID", "");
        pd.put("CITY", region.get(1));
        pd.put("AREA_ID", "");
        pd.put("AREA", region.get(2));
        pd.put("POST_CODE", "");
        // 详细地址
        pd.put("ADDR", pd.get("detailedAddress"));
        pd.put("STATUS", "1");
        pd.put("VERSION", "");
        dao.save("User_addrMapper.save", pd);
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
        dao.delete("User_addrMapper.delete", pd);
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
        // 判断是否为默认地址
        if ("1".equals(pd.get("COMMON_ADDR").toString())) {
            PageData map = new PageData();
            map.put("USER_ID", pd.get("USER_ID"));
            map.put("COMMON_ADDR", 0);
            // 把其他地址全部改为普通
            dao.update("User_addrMapper.editByUserId", map);
        }
        // 切割省市区
        List<String> region = StrSpliter.split(pd.getString("region"), ",", 0, true, true);
        pd.remove("region");
        pd.put("GMT_MODIFIED", DateUtil.now());
        pd.put("PROVINCE", region.get(0));
        pd.put("CITY", region.get(1));
        pd.put("AREA", region.get(2));
        dao.update("User_addrMapper.edit", pd);
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
        return (List<PageData>) dao.findForList("User_addrMapper.datalistPage", page);
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
        return (List<PageData>) dao.findForList("User_addrMapper.listAll", pd);
    }

    /**
     * 列表(根据用户ID查询)
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> listByUserId(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("User_addrMapper.listByUserId", pd);
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
        return (PageData) dao.findForObject("User_addrMapper.findById", pd);
    }

    /**
     * 通过用户ID获取默认地址
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData findByUserIdAndCommon(PageData pd) throws Exception {
        return (PageData) dao.findForObject("User_addrMapper.findByUserIdAndCommon", pd);
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
        dao.delete("User_addrMapper.deleteAll", ArrayDATA_IDS);
    }

}

