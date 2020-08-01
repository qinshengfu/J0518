package com.fh.service.share;

import java.util.List;
import com.fh.entity.Page;
import com.fh.entity.result.R;
import com.fh.util.PageData;

/** 
 * 说明： 股票订单表接口
 * 创建人：
 * 创建时间：2020-06-08
 * @version
 */
public interface Shares_orderManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd)throws Exception;

	/**新增买入订单
	 * @param pd
	 * @throws Exception
	 */
	void saveBuyOrder(PageData pd)throws Exception;

	/**新增卖出订单
	 * @param pd
	 * @throws Exception
	 */
	void saveSellOrder(PageData pd)throws Exception;

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

	/**修改订单库存
	 * @param pd
	 * @param isAdd true增加、false减少
	 * @throws Exception
	 */
	void updateOrderStock(PageData pd, Boolean isAdd)throws Exception;

	/**取消订单
	 * @param pd
	 * @param isBuy true:取消买入、false:取消卖出
	 * @throws Exception
	 */
	R updateCancelOrder(PageData pd, Boolean isBuy)throws Exception;

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

	/**列表(根据用户手机号查询所有持有股票)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listByPhone(PageData pd)throws Exception;

	/**列表(买入/卖出订单各一条)
	 * @throws Exception
	 */
	List<PageData> listBuyAndSell(PageData pd)throws Exception;

	/**列表(获取最近10个买卖订单)
	 * @throws Exception
	 */
	List<PageData> listByBuyAndSellOrder(PageData pd)throws Exception;

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd)throws Exception;

	/**通过手机号获取个人持有股票商品数量
	 * @param pd
	 * @throws Exception
	 */
	PageData findByPhoneSharesHeldNum(PageData pd)throws Exception;

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

