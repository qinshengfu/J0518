package com.fh.service.mall.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import com.fh.service.mall.SkuManager;
import com.fh.util.Tools;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.mall.BasketManager;

/**
 * 说明： 购物车
 * 创建人：
 * 创建时间：2020-05-21
 */
@Service("basketService")
@CacheConfig(cacheNames = "J0518_ProdService")
public class BasketService implements BasketManager {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    /**
     * 单品SKU
     */
    @Resource(name = "skuService")
    private SkuManager skuService;


    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void save(PageData pd) throws Exception {
        // 根据用户ID和商品ID查询购物车是否有此商品
        PageData cartProd = (PageData) dao.findForObject("BasketMapper.findByUserIdAndProdId", pd);
        if (cartProd == null) {
            // 创建单品SKU
            pd.put("STOCKS", pd.get("num"));    //商品在付款减库存的状态下，该sku上未付款的订单数量
            pd.put("ACTUAL_STOCKS", pd.get("TOTAL_STOCKS"));    //实际库存
            pd.put("PROD_NAME", pd.getString("PROD_NAME"));    //商品名称
            skuService.save(pd);
            // 添加到购物车
            pd.put("SHOP_ID", "");    //店铺id
            pd.put("BASKET_COUNT", pd.get("num"));    //购物车产品个数
            dao.save("BasketMapper.save", pd);
        } else {
            // 在购物车原有商品基础上增加数量
            pd.put("BASKET_ID", cartProd.get("BASKET_ID"));
            dao.update("BasketMapper.addCartProdNum", pd);
        }
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
        dao.delete("BasketMapper.delete", pd);
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
        dao.update("BasketMapper.edit", pd);
    }

    /**
     * 更新购物车商品数量
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updataCartProdNum(PageData pd) throws Exception {
        if ("+".equals(pd.getString("tag"))){
            dao.update("BasketMapper.addCartProdNum", pd);
        } else {
            dao.update("BasketMapper.lessCartProdNum", pd);
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
        return (List<PageData>) dao.findForList("BasketMapper.datalistPage", page);
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
        return (List<PageData>) dao.findForList("BasketMapper.listAll", pd);
    }

    /**
     * 列表(根据用户Id连表查询)
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> listAllByUserId(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("BasketMapper.listAllByUserId", pd);
    }

    /**
     * 列表(根据购物车Id连表查询)
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> listByCartIdList(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("BasketMapper.listByCartIdList", pd);
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
        return (PageData) dao.findForObject("BasketMapper.findById", pd);
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
        dao.delete("BasketMapper.deleteAll", ArrayDATA_IDS);
    }

}

