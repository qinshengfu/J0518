package com.fh.service.front.impl;

import java.util.List;
import javax.annotation.Resource;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.fh.controller.front.BaseFrontController;
import com.fh.entity.MemUser;
import com.fh.service.record.Usdt_recordManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.front.AccountManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 说明： 用户表
 * 创建人：
 * 创建时间：2020-05-21
 */
@Service("accountService")
@CacheConfig(cacheNames = "J0518_AccountService")
public class AccountService implements AccountManager {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    /**
     * usdt钱包记录
     */
    @Resource(name = "usdt_recordService")
    public Usdt_recordManager usdt_recordService;

    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void save(PageData pd) throws Exception {
        // 保存用户基础信息
        dao.save("AccountMapper.save", pd);
        // 取刚注册用户的ID
        String userId = pd.get("ACCOUNT_ID").toString();
        // 新增用户资料信息
        pd.put("USER_ID", userId);
        pd.put("FULL_NAME", "");
        pd.put("FRONT_OF_ID", "");
        pd.put("BACK_OF_ID", "");
        pd.put("WALLET_ADDRESS", "");
        pd.put("ID_CARD_NO", "");
        pd.put("USER_PORTRAIT", "");
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
        dao.delete("AccountMapper.delete", pd);
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
        pd.put("GMT_MODIFIED", DateUtil.now());
        dao.update("AccountMapper.edit", pd);
    }

    /**
     * 增加推荐人数和团队数量
     *
     * @param user
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateReAndTeamNumber(PageData user) throws Exception {
        if ("10000".equals(user.getString("ACCOUNT_ID"))) {
            user.put("RE_PATH", user.getString("ACCOUNT_ID"));
        } else {
            user.put("RE_PATH", user.getString("RE_PATH") + "," + user.getString("ACCOUNT_ID"));
        }
        dao.update("AccountMapper.addRecommendQuantity", user);
        dao.update("AccountMapper.addTeamAmount", user);
    }

    /**
     * 更新usdt钱包余额
     *
     * @param pd
     * @param isAdd true：增加、false:减少
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateUsdt(PageData pd, Boolean isAdd) throws Exception {
        if (isAdd) {
            dao.update("AccountMapper.addUsdt", pd);
        } else {
            dao.update("AccountMapper.reduceUsdt", pd);
        }
    }

    /**
     * 更新商城积分余额
     *
     * @param pd
     * @param isAdd true：增加、false:减少
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateIntegral(PageData pd, Boolean isAdd) throws Exception {
        if (isAdd) {
            dao.update("AccountMapper.addIntegral", pd);
        } else {
            dao.update("AccountMapper.reduceIntegral", pd);
        }
    }

    /**
     * 更新金额钱包余额
     *
     * @param pd
     * @param isAdd true：增加、false:减少
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateMoney(PageData pd, Boolean isAdd) throws Exception {
        if (isAdd) {
            dao.update("AccountMapper.addMoney", pd);
        } else {
            dao.update("AccountMapper.reduceMoney", pd);
        }
    }

    /**
     * 更新货币互换
     *
     * @param pd
     * @param isUsdt true：USDT兑换金额、false:金额兑换USDT     *
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateMoneySwap(PageData pd, Boolean isUsdt) throws Exception {
        // 兑换比例
        double base = Convert.toDouble(pd.get("base"));
        double multiple = Convert.toDouble(pd.get("multiple"));
        // 兑换数量
        double num = Convert.toDouble(pd.get("num"));
        pd.put("money", num);
        if (isUsdt) {
            // 得到的金额数量
            double money = NumberUtil.mul(NumberUtil.div(num, base), multiple);
            // 先减少USDT
            dao.update("AccountMapper.reduceUsdt", pd);
            // 增加金额
            pd.put("money", money);
            dao.update("AccountMapper.addMoney", pd);
            // 创建usdt记录
            pd.put("PHONE", pd.getString("phone"));
            pd.put("USER_ID", pd.getString("ACCOUNT_ID"));
            pd.put("MONEY", num);
            pd.put("STATUS", "已完成");
            pd.put("CHARGE", 0);
            pd.put("ACTUAL_RECEIPT", money);
            pd.put("HE_PHONE", "");
            pd.put("HE_USER_ID", "");
            pd.put("WALLET_ADDRESS", "兑换比例 " + base + ":" + multiple);
            pd.put("DATA_TYPE", "USDT兑换金额");
            pd.put("VOUCHER_PATH", "");
            pd.put("TAG", "");
            usdt_recordService.save(pd);
        } else {
            // 得到的USDT数量
            double usdt = NumberUtil.mul(NumberUtil.div(num, base), multiple);
            // 先减少金额
            dao.update("AccountMapper.reduceMoney", pd);
            // 增加usdt
            pd.put("money", usdt);
            dao.update("AccountMapper.addUsdt", pd);
            // 创建usdt记录
            pd.put("PHONE", pd.getString("phone"));
            pd.put("USER_ID", pd.getString("ACCOUNT_ID"));
            pd.put("MONEY", num);
            pd.put("STATUS", "已完成");
            pd.put("CHARGE", 0);
            pd.put("ACTUAL_RECEIPT", usdt);
            pd.put("HE_PHONE", "");
            pd.put("HE_USER_ID", "");
            pd.put("WALLET_ADDRESS", "兑换比例 " + base + ":" + multiple);
            pd.put("DATA_TYPE", "金额兑换USDT");
            pd.put("VOUCHER_PATH", "");
            pd.put("TAG", "");
            usdt_recordService.save(pd);
        }
    }

    /**
     * 用户转账
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateTransfer(PageData pd) throws Exception {
        // 转账数量
        double num = Convert.toDouble(pd.get("money"));
        String myUserId = pd.getString("ACCOUNT_ID");
        // 先减少转出用户USDT
        dao.update("AccountMapper.reduceUsdt", pd);
        PageData map = new PageData();
        // 创建转出用户usdt记录
        map.put("PHONE", pd.get("PHONE"));
        map.put("USER_ID", pd.get("ACCOUNT_ID"));
        map.put("MONEY", num);
        map.put("STATUS", "已完成");
        map.put("CHARGE", 0);
        map.put("ACTUAL_RECEIPT", num);
        map.put("HE_PHONE", pd.get("HE_PHONE"));
        map.put("HE_USER_ID", pd.get("HE_USER_ID"));
        map.put("WALLET_ADDRESS", "转给" + pd.get("HE_PHONE"));
        map.put("DATA_TYPE", "转账");
        map.put("VOUCHER_PATH", "");
        map.put("TAG", "-");
        usdt_recordService.save(map);
        // 给转入用户加钱
        pd.put("ACCOUNT_ID", pd.get("HE_USER_ID"));
        dao.update("AccountMapper.addUsdt", pd);
        // 创建usdt记录
        map.put("PHONE", pd.get("HE_PHONE"));
        map.put("USER_ID", pd.get("HE_USER_ID"));
        map.put("MONEY", num);
        map.put("STATUS", "已完成");
        map.put("CHARGE", 0);
        map.put("ACTUAL_RECEIPT", num);
        map.put("HE_PHONE", pd.get("PHONE"));
        map.put("HE_USER_ID", myUserId);
        map.put("WALLET_ADDRESS", "来自" + pd.get("PHONE"));
        map.put("DATA_TYPE", "转账");
        map.put("VOUCHER_PATH", "");
        map.put("TAG", "+");
        usdt_recordService.save(map);
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
        return (List<PageData>) dao.findForList("AccountMapper.datalistPage", page);
    }

    /**
     * 列表（财务汇总）
     *
     * @param page
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> listByFinancialSummary(Page page) throws Exception {
        return (List<PageData>) dao.findForList("AccountMapper.listByFinancialSummary", page);
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
        return (List<PageData>) dao.findForList("AccountMapper.listAll", pd);
    }

    /**
     * 列表(按照接点关系查所有上级)
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> listByRePath(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("AccountMapper.listByRePath", pd);
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
        return (PageData) dao.findForObject("AccountMapper.findById", pd);
    }

    /**
     * 通过手机号获取数据
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData findByPhone(PageData pd) throws Exception {
        return (PageData) dao.findForObject("AccountMapper.findByPhone", pd);
    }

    /**
     * 通过手机号和密码获取数据
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public MemUser findByPhoneAndPass(PageData pd) throws Exception {
        return (MemUser) dao.findForObject("AccountMapper.findByPhoneAndPass", pd);
    }

    /**
     * 清空数据保留顶点用户
     *
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void deleteAll() throws Exception {
        dao.delete("AccountMapper.deleteAllData", null);
        dao.delete("Account_dataMapper.deleteAllData", null);
    }

    /**
     * 重置帐户
     *
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void resetAccount() throws Exception {
        dao.delete("AccountMapper.resetAccount", null);
    }

}

