package com.fh.service.mall;

import java.util.List;

import com.fh.entity.Page;
import com.fh.util.PageData;
import org.springframework.transaction.annotation.Transactional;

/**
 * 说明： 订单表接口
 * 创建人：
 * 创建时间：2020-05-21
 */
public interface OrderManager {

    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    void save(PageData pd) throws Exception;

    /**
     * 生成订单
     *
     * @param pd
     * @throws Exception
     */
    void saveGenerateOrder(PageData pd) throws Exception;

    /**
     * 从购物车下单生成订单
     *
     * @param pd
     * @throws Exception
     */
    void saveCartGenerateOrder(PageData pd, List<PageData> cartList) throws Exception;

    /**
     * 删除
     *
     * @param pd
     * @throws Exception
     */
    public void delete(PageData pd) throws Exception;

    /**
     * 修改
     *
     * @param pd
     * @throws Exception
     */
    void edit(PageData pd) throws Exception;

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    public List<PageData> list(Page page) throws Exception;

    /**
     * 列表(全部)
     *
     * @param pd
     * @throws Exception
     */
    List<PageData> listAll(PageData pd) throws Exception;


    /**
     * 列表(根据用户ID查询全部)
     *
     * @param pd
     * @throws Exception
     */
    List<PageData> listAllByUserId(PageData pd) throws Exception;

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    PageData findById(PageData pd) throws Exception;

    /**
     * 通过id连表查询
     *
     * @param pd
     * @throws Exception
     */
    List<PageData> listByIdLinkedTable(PageData pd) throws Exception;

    /**
     * 批量删除
     *
     * @param ArrayDATA_IDS
     * @throws Exception
     */
    public void deleteAll(String[] ArrayDATA_IDS) throws Exception;

}

