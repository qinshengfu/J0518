package com.fh.service.mall;

import java.util.List;
import com.fh.entity.Page;
import com.fh.util.PageData;
import org.springframework.transaction.annotation.Transactional;

/** 
 * 说明： 商品表接口
 * 创建人：
 * 创建时间：2020-05-21
 * @version
 */
public interface ProdManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd)throws Exception;

	/** 更新库存数量
	 * @param pd
	 * @throws Exception
	 */
	void updateInventory(PageData pd)throws Exception;

	/**更新销售数量
	 * @param pd
	 * @throws Exception
	 */
	void updataSoldNum(PageData pd)throws Exception;

	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> list(Page page)throws Exception;

	/**前台查询列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> frontDataListPage(Page page)throws Exception;

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

