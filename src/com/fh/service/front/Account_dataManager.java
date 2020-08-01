package com.fh.service.front;

import java.util.List;
import com.fh.entity.Page;
import com.fh.util.PageData;

/** 
 * 说明： 用户资料表接口
 * 创建人：
 * 创建时间：2020-05-21
 * @version
 */
public interface Account_dataManager{

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

	/**根据用户ID修改
	 * @param pd
	 * @throws Exception
	 */
	void editByUserId(PageData pd)throws Exception;

	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
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
	
	/**通过用户资料获取数据判断是否重复
	 * @param pd
	 * @throws Exception
	 */
	PageData findByUserData(PageData pd)throws Exception;


	/**通过用户id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findByUserId(PageData pd)throws Exception;

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

