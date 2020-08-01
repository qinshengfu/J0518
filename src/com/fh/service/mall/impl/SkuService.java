package com.fh.service.mall.impl;

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
import com.fh.service.mall.SkuManager;

/** 
 * 说明： 单品SKU表
 * 创建人：
 * 创建时间：2020-05-21
 * @version
 */
@Service("skuService")
@CacheConfig(cacheNames = "J0518_SkuService")
public class SkuService implements SkuManager{

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
		pd.put("PROPERTIES", "");    //销售属性组合字符串 格式是 p1:v1;p2:v2
		pd.put("SKU_NAME", "");    //sku名称
		pd.put("STATUS", "1");    //状态： 1 启用，0 禁用
		pd.put("IS_DELETE", "0");    //0 正常 1 已被删除
		pd.put("VERSION", "0");    //版本号
		dao.save("SkuMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void delete(PageData pd)throws Exception{
		dao.delete("SkuMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void edit(PageData pd)throws Exception{
		dao.update("SkuMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("SkuMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("SkuMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("SkuMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("SkuMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

