package com.fh.service.record.impl;

import java.util.List;
import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import com.fh.service.record.User_addr_orderManager;
import com.fh.service.share.Shares_orderManager;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.record.Delivery_recordManager;

/**
 * 说明： 提货记录表
 * 创建人：
 * 创建时间：2020-05-21
 */
@Service("delivery_recordService")
@CacheConfig(cacheNames = "J0518_Delivery_recordService")
public class Delivery_recordService implements Delivery_recordManager {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    /**
     * 股票订单记录
     */
    @Resource(name="shares_orderService")
    private Shares_orderManager shares_orderService;
    /**
     * 用户订单配送地址
     */
    @Resource(name = "user_addr_orderService")
    private User_addr_orderManager user_addr_orderService;

    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void save(PageData pd) throws Exception {
        // 先减库存
        shares_orderService.updateOrderStock(pd, false);
        // 创建收货地址
        pd.put("ADDR_ID", pd.get("USER_ADDR_ID"));
        user_addr_orderService.save(pd);
        String userAddrOrderId = pd.get("USER_ADDR_ORDER_ID").toString();
        // 创建提货记录
        pd.put("GMT_CREATE", DateUtil.now());
        pd.put("GMT_MODIFIED", DateUtil.now());
        pd.put("PROD_NUM", pd.get("num"));    //商品数量
        pd.put("CURRENT_PRICE", pd.get("PRICE"));    //当前价格
        pd.put("ADDR_ORDER_ID", userAddrOrderId);    //用户订单地址Id
        pd.put("FINALLY_TIME", "");    //完成时间
        pd.put("DVY_TIME", "");    //发货时间
        pd.put("STATUS", "待发货");
        pd.put("DVY_FLOW_ID", "");
        dao.save("Delivery_recordMapper.save", pd);
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
        dao.delete("Delivery_recordMapper.delete", pd);
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
        dao.update("Delivery_recordMapper.edit", pd);
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
        return (List<PageData>) dao.findForList("Delivery_recordMapper.datalistPage", page);
    }

    /**
     * 根据用户ID获取提货记录
     *
     * @param page
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> listAllByUserId(PageData page) throws Exception {
        return (List<PageData>) dao.findForList("Delivery_recordMapper.listAllByUserId", page);
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
        return (List<PageData>) dao.findForList("Delivery_recordMapper.listAll", pd);
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
        return (PageData) dao.findForObject("Delivery_recordMapper.findById", pd);
    }

    /**
     * 通过id连表查询
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData findByIdLinkedTable(PageData pd) throws Exception {
        return (PageData) dao.findForObject("Delivery_recordMapper.findByIdLinkedTable", pd);
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
        dao.delete("Delivery_recordMapper.deleteAll", ArrayDATA_IDS);
    }

}

