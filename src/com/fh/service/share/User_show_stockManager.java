package com.fh.service.share;

import java.util.List;
import com.fh.entity.Page;
import com.fh.util.PageData;

/** 
 * 说明： 用户自选股票接口
 * 创建人：
 * 创建时间：2020-06-04
 * @version
 */
public interface User_show_stockManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd)throws Exception;

	/**列表(根据用户ID连表查询)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listByUserId(PageData pd)throws Exception;

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd)throws Exception;

	/**通过用户id和股票ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByUserIdAndStockId(PageData pd)throws Exception;

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

