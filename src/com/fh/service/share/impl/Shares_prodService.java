package com.fh.service.share.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.fh.service.front.AccountManager;
import com.fh.service.record.Money_recordManager;
import com.fh.service.share.Shares_orderManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.share.Shares_prodManager;

/**
 * 说明： 股票商品表
 * 创建人：
 * 创建时间：2020-05-21
 */
@Service("shares_prodService")
@CacheConfig(cacheNames = "J0518_Shares_prodService")
public class Shares_prodService implements Shares_prodManager {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    /**
     * 股票订单表表
     */
    @Resource(name="shares_orderService")
    private Shares_orderManager shares_orderService;
    /**
     * 用户表
     */
    @Resource(name = "accountService")
    private AccountManager accountService;
    /**
     * 金额钱包记录
     */
    @Resource(name = "money_recordService")
    private Money_recordManager money_recordService;

    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void save(PageData pd) throws Exception {
        dao.save("Shares_prodMapper.save", pd);
    }

    /**
     * 购买股票商品
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateBuyStock(PageData pd) throws Exception {
        // 扣库存》创建购买记录》减用户余额》创建钱包消费记录
        pd.put("num", pd.get("amount"));
        updateStockProd(pd, false);
        // 扣完库存后 判断余数是否 > 1
        PageData stockProd = this.findById(pd);
        int stockNum = Convert.toInt(stockProd.get("REMAINDER"));
        if (stockNum < 1) {
            stockProd.put("STATUS", "交易中");
            this.edit(stockProd);
        }
        // 根据股票商品ID和手机号 查询用户是否持有此商品
        PageData stockOrder = (PageData) dao.findForObject("Shares_orderMapper.findBySharesProdIdAndPhone", pd);
        if (stockOrder != null) {
            // 增加库存
            pd.put("SHARES_ORDER_ID", stockOrder.get("SHARES_ORDER_ID"));
            shares_orderService.updateOrderStock(pd, true);
            // 增加总数和修改价格
            dao.findForObject("Shares_orderMapper.addTotalNumber", pd);
        } else {
            pd.put("SYS_CONFIG_ID", "1");
            PageData param = (PageData)dao.findForObject("Sys_configMapper.findById", pd);
            int lockedDay = Convert.toInt(param.get("LOCKED_DAY"));
            // 创建购买记录 T+1规则，当前买入，次日才能卖出。所以用现在时间偏移一天
            pd.put("GMT_MODIFIED", DateUtil.offsetDay(new Date(), lockedDay).toString());
            pd.put("STATUS", "持仓");
            pd.put("TYPE", "买入");
            pd.put("ORI_PRICE", pd.get("OPEN_PRICE"));
            pd.put("HE_PHONE", "");
            pd.put("TOTAL", pd.get("amount"));
            pd.put("REMAINDER", pd.get("amount"));
            pd.put("HE_PHONE", "");
            pd.put("EVENT", "购买限量发售股票");
            pd.put("MAIN_ORDER_ID", "");
            shares_orderService.save(pd);
        }
        // 更新用户余额
        pd.put("money", pd.get("totalMoney"));
        accountService.updateMoney(pd, false);
        // 创建金额钱包消费记录
		pd.put("USER_ID", pd.get("ACCOUNT_ID"));	//用户ID
		pd.put("MONEY", pd.get("totalMoney"));	//金额
		pd.put("CHARGE", "0");	//手续费
		pd.put("STATUS", "已完成");
		pd.put("ACTUAL_RECEIPT", pd.get("totalMoney"));	//实际到账
		pd.put("HE_PHONE", "");	//对方手机号
		pd.put("HE_USER_ID", "");	//对方用户ID
		pd.put("TAG", "-");
		pd.put("DATA_TYPE", "购买股票");
		money_recordService.save(pd);
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
        dao.delete("Shares_prodMapper.delete", pd);
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
        dao.update("Shares_prodMapper.edit", pd);
    }

    /**
     * 更新股票商品库存
     *
     * @param pd    商品id和num 数量
     * @param isAdd true 增加/false
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateStockProd(PageData pd, boolean isAdd) throws Exception {
        if (isAdd) {
            dao.update("Shares_prodMapper.addStock", pd);
        } else {
            dao.update("Shares_prodMapper.lessStock", pd);
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
        return (List<PageData>) dao.findForList("Shares_prodMapper.datalistPage", page);
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
        return (List<PageData>) dao.findForList("Shares_prodMapper.listAll", pd);
    }

    /**
     * 列表(查询库存大于0的)
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> listAllByNum(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("Shares_prodMapper.listAllByNum", pd);
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
        return (PageData) dao.findForObject("Shares_prodMapper.findById", pd);
    }

    /**
     * 通过id获取当日成交量
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData getDayVolumeById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("Shares_prodMapper.getDayVolumeById", pd);
    }

    /**
     * 通过id获取当日交易额
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData getDayTradingById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("Shares_prodMapper.getDayTradingById", pd);
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
        dao.delete("Shares_prodMapper.deleteAll", ArrayDATA_IDS);
    }

}

