package com.fh.service.mall.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.fh.service.mall.Order_itemManager;
import com.fh.util.Tools;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.mall.ProdManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 说明： 商品表
 * 创建人：
 * 创建时间：2020-05-21
 */
@Service("prodService")
@CacheConfig(cacheNames = "J0518_ProdService")
public class ProdService implements ProdManager {

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
        dao.save("ProdMapper.save", pd);
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
        dao.delete("ProdMapper.delete", pd);
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
        dao.update("ProdMapper.edit", pd);
    }

    /**
     * 更新库存数量
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateInventory(PageData pd) throws Exception {
        PageData map = (PageData) dao.findForObject("ProdMapper.findById", pd);
        map.put("num", pd.get("num"));
        int version = Convert.toInt(map.get("VERSION"));
        int i;
        int j = 0;
        int count = 3;
        if ("+".equals(pd.getString("tag"))) {
            // 增加库存 用乐观锁 重试3次
            while (j < count) {
                map.put("VERSION", version + 1);
                i = (int) dao.update("ProdMapper.addInventory", map);
                if (i > 0) {
                    break;
                }
                version++;
                j++;
                if (j == count) {
                    throw new RuntimeException("增加库存失败");
                }
            }
        } else {
            // 减少库存 用乐观锁 重试3次
            while (j < count) {
                map.put("VERSION", version + 1);
                i = (int) dao.update("ProdMapper.lessInventory", map);
                if (i > 0) {
                    break;
                }
                version++;
                j++;
                if (j == count) {
                    throw new RuntimeException("减少库存失败");
                }
            }
        }
    }

    /**
     * 更新销售数量
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updataSoldNum(PageData pd) throws Exception {
        if ("+".equals(pd.getString("tag"))) {
            dao.update("ProdMapper.addSoldNum", pd);
        } else {
            dao.update("ProdMapper.lessSoldNum", pd);
        }
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
        return (List<PageData>) dao.findForList("ProdMapper.datalistPage", page);
    }

    /**
     * 前台查询列表
     *
     * @param page
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> frontDataListPage(Page page) throws Exception {
        return (List<PageData>) dao.findForList("ProdMapper.frontDataListPage", page);
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
        return (List<PageData>) dao.findForList("ProdMapper.listAll", pd);
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
        return (PageData) dao.findForObject("ProdMapper.findById", pd);
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
        dao.delete("ProdMapper.deleteAll", ArrayDATA_IDS);
    }

}

