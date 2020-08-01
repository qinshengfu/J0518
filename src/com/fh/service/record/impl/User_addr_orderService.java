package com.fh.service.record.impl;

import java.util.List;
import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.record.User_addr_orderManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** 
 * 说明： 用户订单配送地址表
 * 创建人：
 * 创建时间：2020-05-21
 * @version
 */
@Service("user_addr_orderService")
@CacheConfig(cacheNames = "J0518_User_addr_orderService")
public class User_addr_orderService implements User_addr_orderManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void save(PageData pd)throws Exception{
		pd.put("GMT_CREATE", DateUtil.now());    //创建时间
		pd.put("GMT_MODIFIED", DateUtil.now());    //更新时间
		pd.put("VERSION", "0");	//版本号
		pd.put("PROVINCE_ID", "");
		pd.put("CITY_ID", "");
		pd.put("AREA_ID", "");
		pd.put("POST_CODE", "");
		dao.save("User_addr_orderMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void delete(PageData pd)throws Exception{
		dao.delete("User_addr_orderMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void edit(PageData pd)throws Exception{
		dao.update("User_addr_orderMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("User_addr_orderMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("User_addr_orderMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("User_addr_orderMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("User_addr_orderMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

