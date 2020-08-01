package com.fh.service.share.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.fh.entity.MemUser;
import com.fh.entity.result.R;
import com.fh.service.front.AccountManager;
import com.fh.service.front.User_addrManager;
import com.fh.service.record.Money_recordManager;
import com.fh.service.record.Usdt_recordManager;
import com.fh.util.Const;
import com.fh.util.Jurisdiction;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.share.Shares_orderManager;

/** 
 * 说明： 股票订单表
 * 创建人：
 * 创建时间：2020-06-08
 * @version
 */
@Service("shares_orderService")
@CacheConfig(cacheNames = "J0518_Shares_orderService")
public class Shares_orderService implements Shares_orderManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**
	 * 用户表
	 */
	@Resource(name = "accountService")
	private AccountManager accountService;
	/**
	 * 金额钱包记录
	 */
	@Resource(name="money_recordService")
	private Money_recordManager money_recordService;

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void save(PageData pd)throws Exception{
		pd.put("GMT_CREATE", DateUtil.now());
		dao.save("Shares_orderMapper.save", pd);
	}

	/**新增买入订单
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void saveBuyOrder(PageData pd)throws Exception{
		// 创建买入订单>扣除用户钱包>创建钱包消费记录
		pd.put("SYS_CONFIG_ID", "1");
		PageData param = (PageData)dao.findForObject("Sys_configMapper.findById", pd);
		int lockedDay = Convert.toInt(param.get("LOCKED_DAY"));
		// T+1规则，当前买入，次日才能卖出。所以用现在时间偏移一天
		pd.put("GMT_MODIFIED", DateUtil.offsetDay(new Date(), lockedDay).toString());
		pd.put("STATUS", "交易中");
		pd.put("TYPE", "买入");
		pd.put("REMAINDER", pd.get("TOTAL"));
		pd.put("ORI_PRICE", pd.get("PRICE"));
		pd.put("HE_PHONE", "");
		pd.put("EVENT", "等待系统匹配");
		pd.put("MAIN_ORDER_ID", "");
		this.save(pd);
		// 减用户金额
		accountService.updateMoney(pd, false);
		// 创建钱包消费记录
		pd.put("USER_ID", pd.get("ACCOUNT_ID"));
		pd.put("MONEY", pd.get("money"));
		pd.put("STATUS", "已完成");
		pd.put("CHARGE", 0);
		pd.put("ACTUAL_RECEIPT", pd.get("money"));
		pd.put("HE_PHONE", "");
		pd.put("HE_USER_ID", "");
		pd.put("TAG", "-");
		pd.put("DATA_TYPE", "发布买入");
		money_recordService.save(pd);
	}

	/**新增卖出订单
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void saveSellOrder(PageData pd)throws Exception{
		// 扣库存
		pd.put("num", pd.get("TOTAL"));
		this.updateOrderStock(pd, false);
		// 创建卖出订单
		pd.put("GMT_MODIFIED", DateUtil.now());
		pd.put("STATUS", "交易中");
		pd.put("TYPE", "卖出");
		pd.put("REMAINDER", pd.get("TOTAL"));
		pd.put("ORI_PRICE", pd.get("PRICE"));
		pd.put("HE_PHONE", "");
		pd.put("EVENT", "等待系统匹配");
		pd.put("MAIN_ORDER_ID", pd.get("SHARES_ORDER_ID"));
		this.save(pd);
	}

	/**删除
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void delete(PageData pd)throws Exception{
		dao.delete("Shares_orderMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void edit(PageData pd)throws Exception{
		dao.update("Shares_orderMapper.edit", pd);
	}

	/**修改订单库存
	 * @param pd
	 * @param isAdd true增加、false减少
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void updateOrderStock(PageData pd, Boolean isAdd)throws Exception{
		if (isAdd) {
			dao.update("Shares_orderMapper.addOrderStock", pd);
		} else {
			dao.update("Shares_orderMapper.lessOrderStock", pd);
		}
	}

	/**取消订单
	 * @param pd
	 * @param isBuy true:取消买入、false:取消卖出
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public R updateCancelOrder(PageData pd, Boolean isBuy)throws Exception{
		MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
		PageData map = new PageData();
		// 更新状态
		map.put("SHARES_ORDER_ID", pd.get("SHARES_ORDER_ID"));
		map.put("GMT_MODIFIED", DateUtil.now());
		map.put("STATUS", "已取消");
		map.put("EVENT", "取消订单");
		this.edit(map);
		// 原价、 这笔订单库存
		double oriPrice = Convert.toDouble(pd.get("ORI_PRICE"));
		int remainder = Convert.toInt(pd.get("REMAINDER"));
		if (isBuy) {
			// 计算总价
			double totalAmount = NumberUtil.mul(oriPrice, remainder);
			map.put("ACCOUNT_ID", user.getACCOUNT_ID());
			map.put("money", totalAmount);
			// 增加用户金额
			accountService.updateMoney(map, true);
			// 创建钱包消费记录
			map.put("PHONE", user.getPHONE());
			map.put("USER_ID", user.getACCOUNT_ID());
			map.put("MONEY", totalAmount);
			map.put("STATUS", "已完成");
			map.put("CHARGE", 0);
			map.put("ACTUAL_RECEIPT", totalAmount);
			map.put("HE_PHONE", "");
			map.put("HE_USER_ID", "");
			map.put("TAG", "+");
			map.put("DATA_TYPE", "取消买入订单");
			money_recordService.save(map);
		} else {
			// 给主订单增加库存
			map.put("SHARES_ORDER_ID", pd.get("MAIN_ORDER_ID"));
			map.put("num", remainder);
			this.updateOrderStock(map, true);
		}
		return  R.ok();
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("Shares_orderMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Shares_orderMapper.listAll", pd);
	}

	/**列表(根据用户手机号查询所有持有股票)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listByPhone(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Shares_orderMapper.listByPhone", pd);
	}

	/**列表(买入/卖出订单各一条)
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listBuyAndSell(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Shares_orderMapper.listBuyAndSell", pd);
	}

	/**列表(获取最近10个买卖订单)
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listByBuyAndSellOrder(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Shares_orderMapper.listByBuyAndSellOrder", pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Shares_orderMapper.findById", pd);
	}

	/**通过手机号获取个人持有股票商品数量
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData findByPhoneSharesHeldNum(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Shares_orderMapper.findByPhoneSharesHeldNum", pd);
	}

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("Shares_orderMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

