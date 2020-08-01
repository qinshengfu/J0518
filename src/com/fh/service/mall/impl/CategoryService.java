package com.fh.service.mall.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.mall.CategoryManager;

/** 
 * 说明： 产品类目
 * 创建人：
 * 创建时间：2020-05-21
 * @version
 */
@Service("categoryService")
@CacheConfig(cacheNames = "J0518_CategoryService")
public class CategoryService implements CategoryManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void save(PageData pd)throws Exception{
		dao.save("CategoryMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void delete(PageData pd)throws Exception{
		dao.delete("CategoryMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void edit(PageData pd)throws Exception{
		dao.update("CategoryMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("CategoryMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("CategoryMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("CategoryMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("CategoryMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

