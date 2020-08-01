package com.fh.service.share;

import java.util.List;

import com.fh.entity.Page;
import com.fh.util.PageData;

/**
 * 说明： 股票商品表接口
 * 创建人：
 * 创建时间：2020-05-21
 */
public interface Shares_prodManager {

    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    public void save(PageData pd) throws Exception;

    /**
     * 购买股票商品
     *
     * @param pd
     * @throws Exception
     */
    void updateBuyStock(PageData pd) throws Exception;

    /**
     * 删除
     *
     * @param pd
     * @throws Exception
     */
    public void delete(PageData pd) throws Exception;

    /**
     * 修改
     *
     * @param pd
     * @throws Exception
     */
    void edit(PageData pd) throws Exception;

    /**
     * 更新股票商品库存
     *
     * @param pd    商品id和num 数量
     * @param isAdd true 增加/false
     * @throws Exception
     */
    void updateStockProd(PageData pd, boolean isAdd) throws Exception;

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    public List<PageData> list(Page page) throws Exception;

    /**
     * 列表(全部)
     *
     * @param pd
     * @throws Exception
     */
    public List<PageData> listAll(PageData pd) throws Exception;

    /**
     * 列表(查询库存大于0的)
     *
     * @param pd
     * @throws Exception
     */
    List<PageData> listAllByNum(PageData pd) throws Exception;

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    PageData findById(PageData pd) throws Exception;

    /**
     * 通过id获取当日成交量
     *
     * @param pd
     * @throws Exception
     */
    PageData getDayVolumeById(PageData pd) throws Exception;

    /**
     * 通过id获取当日交易额
     *
     * @param pd
     * @throws Exception
     */
    PageData getDayTradingById(PageData pd) throws Exception;

    /**
     * 批量删除
     *
     * @param ArrayDATA_IDS
     * @throws Exception
     */
    public void deleteAll(String[] ArrayDATA_IDS) throws Exception;

}

