package com.fh.service.record;

import java.util.List;

import com.fh.entity.Page;
import com.fh.util.PageData;

/**
 * 说明： usdt钱包记录接口
 * 创建人：
 * 创建时间：2020-05-21
 */
public interface Usdt_recordManager {

    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    public void save(PageData pd) throws Exception;

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
     * 充值通过审核/驳回
     *
     * @param pd
     * @param isSuccess true 审核 、 false驳回
     * @throws Exception
     */
    void rechargeEdit(PageData pd, Boolean isSuccess) throws Exception;

    /**
     * 提现通过审核/驳回
     *
     * @param pd
     * @param isSuccess true 审核 、 false驳回
     * @throws Exception
     */
    void withdrawalEdit(PageData pd, Boolean isSuccess) throws Exception;

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    List<PageData> list(Page page) throws Exception;

    /**
     * 根据类型分页列表
     *
     * @param page
     * @throws Exception
     */
    List<PageData> listByType(Page page) throws Exception;

    /**
     * 列表(全部)
     *
     * @param pd
     * @throws Exception
     */
    List<PageData> listAll(PageData pd) throws Exception;

    /**
     * 列表(获取货币互换记录)
     *
     * @param pd
     * @throws Exception
     */
    List<PageData> listByMoneySwapAndUserId(PageData pd) throws Exception;

    /**
     * 列表(按照用户Id根据数据类型获取)
     *
     * @param pd
     * @throws Exception
     */
    List<PageData> listByDataTypeOnUserId(PageData pd) throws Exception;

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    public PageData findById(PageData pd) throws Exception;

    /**
     * 批量删除
     *
     * @param ArrayDATA_IDS
     * @throws Exception
     */
    public void deleteAll(String[] ArrayDATA_IDS) throws Exception;

}

