package com.fh.service.record.impl;

import java.util.List;
import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import com.fh.service.front.AccountManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.record.Usdt_recordManager;
import org.springframework.transaction.annotation.Transactional;

/** 
 * 说明： usdt钱包记录
 * 创建人：
 * 创建时间：2020-05-21
 * @version
 */
@Service("usdt_recordService")
@CacheConfig(cacheNames = "J0518_Usdt_recordService")
public class Usdt_recordService implements Usdt_recordManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	/**
	 * 用户表
	 */
	@Resource(name = "accountService")
	public AccountManager accountService;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void save(PageData pd)throws Exception{
		pd.put("GMT_CREATE", DateUtil.now());
		pd.put("GMT_MODIFIED", DateUtil.now());
		pd.put("VERSION", 0);
		dao.save("Usdt_recordMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void delete(PageData pd)throws Exception{
		dao.delete("Usdt_recordMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void edit(PageData pd)throws Exception{
		dao.update("Usdt_recordMapper.edit", pd);
	}

	/**充值通过审核/驳回
	 * @param pd
	 * @param isSuccess true 审核 、 false驳回
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	@Transactional(rollbackFor = Exception.class)
	public void rechargeEdit(PageData pd, Boolean isSuccess)throws Exception{
		PageData map = new PageData();
		if (isSuccess) {
			// 把订单状态改为 已完成
			map.put("STATUS", "已完成");
			map.put("USDT_RECORD_ID", pd.get("USDT_RECORD_ID"));
			dao.update("Usdt_recordMapper.edit", map);
			// 给用户加钱
			map.put("money", pd.get("ACTUAL_RECEIPT"));
			map.put("ACCOUNT_ID", pd.get("USER_ID"));
			accountService.updateUsdt(map, true);
		} else {
			// 把订单状态改为 驳回
			map.put("STATUS", "驳回");
			map.put("USDT_RECORD_ID", pd.get("USDT_RECORD_ID"));
			dao.update("Usdt_recordMapper.edit", map);
		}
	}

	/**提现通过审核/驳回
	 * @param pd
	 * @param isSuccess true 审核 、 false驳回
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	@Transactional(rollbackFor = Exception.class)
	public void withdrawalEdit(PageData pd, Boolean isSuccess)throws Exception{
		PageData map = new PageData();
		if (isSuccess) {
			// 把订单状态改为 已完成
			map.put("STATUS", "已完成");
			map.put("USDT_RECORD_ID", pd.get("USDT_RECORD_ID"));
			dao.update("Usdt_recordMapper.edit", map);
		} else {
			// 把订单状态改为 驳回
			map.put("STATUS", "驳回");
			map.put("USDT_RECORD_ID", pd.get("USDT_RECORD_ID"));
			dao.update("Usdt_recordMapper.edit", map);
			// 给用户加钱
			map.put("money", pd.get("MONEY"));
			map.put("ACCOUNT_ID", pd.get("USER_ID"));
			accountService.updateUsdt(map, true);
		}
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("Usdt_recordMapper.datalistPage", page);
	}

	/**根据类型分页列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listByType(Page page)throws Exception{
		return (List<PageData>)dao.findForList("Usdt_recordMapper.listByTypePage", page);
	}

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Usdt_recordMapper.listAll", pd);
	}

	/**列表(获取货币互换记录)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listByMoneySwapAndUserId(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Usdt_recordMapper.listByMoneySwapAndUserId", pd);
	}

	/**列表(按照用户Id根据数据类型获取)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listByDataTypeOnUserId(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Usdt_recordMapper.listByDataTypeOnUserId", pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Usdt_recordMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("Usdt_recordMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

