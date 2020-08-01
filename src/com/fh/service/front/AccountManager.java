package com.fh.service.front;

import java.util.List;

import com.fh.entity.MemUser;
import com.fh.entity.Page;
import com.fh.util.PageData;
import org.springframework.transaction.annotation.Transactional;

/**
 * 说明： 用户表接口
 * 创建人：
 * 创建时间：2020-05-21
 */
public interface AccountManager {

    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    void save(PageData pd) throws Exception;

    /**
     * 删除
     *
     * @param pd
     * @throws Exception
     */
    void delete(PageData pd) throws Exception;

    /**
     * 修改
     *
     * @param pd
     * @throws Exception
     */
    void edit(PageData pd) throws Exception;

    /**
     * 增加推荐人数和团队数量
     *
     * @param user
     * @throws Exception
     */
    void updateReAndTeamNumber(PageData user) throws Exception;

    /**
     * 更新usdt钱包余额
     *
     * @param pd
     * @param isAdd true：增加、false:减少
     * @throws Exception
     */
    void updateUsdt(PageData pd, Boolean isAdd) throws Exception;

    /**
     * 更新商城积分余额
     *
     * @param pd
     * @param isAdd true：增加、false:减少
     * @throws Exception
     */
    void updateIntegral(PageData pd, Boolean isAdd) throws Exception;

    /**
     * 更新金额钱包余额
     *
     * @param pd
     * @param isAdd true：增加、false:减少
     * @throws Exception
     */
    void updateMoney(PageData pd, Boolean isAdd) throws Exception;

    /**
     * 更新货币互换
     *
     * @param pd
     * @param isUsdt true：USDT兑换金额、false:金额兑换USDT
     * @throws Exception
     */
    void updateMoneySwap(PageData pd, Boolean isUsdt) throws Exception;

    /**
     * 用户转账
     *
     * @param pd
     * @throws Exception
     */
    void updateTransfer(PageData pd ) throws Exception;

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    List<PageData> list(Page page) throws Exception;

    /**
     * 列表(财务汇总)
     *
     * @param page
     * @throws Exception
     */
    List<PageData> listByFinancialSummary(Page page) throws Exception;

    /**
     * 列表(全部)
     *
     * @param pd
     * @throws Exception
     */
    List<PageData> listAll(PageData pd) throws Exception;

    /**
     * 列表(按照接点关系查所有上级)
     *
     * @param pd
     * @throws Exception
     */
    List<PageData> listByRePath(PageData pd) throws Exception;

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    PageData findById(PageData pd) throws Exception;

    /**
     * 通过手机号获取数据
     *
     * @param pd
     * @throws Exception
     */
    PageData findByPhone(PageData pd) throws Exception;

    /**
     * 通过手机号和密码获取数据
     *
     * @param pd
     * @throws Exception
     */
    MemUser findByPhoneAndPass(PageData pd) throws Exception;

    /**
     * 清空数据保留顶点用户
     *
     * @throws Exception
     */
    void deleteAll() throws Exception;

    /**
     * 重置帐户
     *
     * @throws Exception
     */
    void resetAccount() throws Exception;

}

