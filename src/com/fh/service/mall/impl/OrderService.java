package com.fh.service.mall.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.fh.service.front.AccountManager;
import com.fh.service.mall.Order_itemManager;
import com.fh.service.mall.ProdManager;
import com.fh.service.record.Money_recordManager;
import com.fh.service.record.Points_recordManager;
import com.fh.service.record.User_addr_orderManager;
import com.fh.util.Tools;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.mall.OrderManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 说明： 订单表
 * 创建人：
 * 创建时间：2020-05-21
 */
@Service("orderService")
@CacheConfig(cacheNames = "J0518_OrderService")
public class OrderService implements OrderManager {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    /**
     * 用户订单配送地址
     */
    @Resource(name = "user_addr_orderService")
    private User_addr_orderManager user_addr_orderService;
    /**
     * 商品表
     */
    @Resource(name = "prodService")
    private ProdManager prodService;
    /**
     * 用户表
     */
    @Resource(name = "accountService")
    private AccountManager accountService;
    /**
     * 金额钱包记录
     */
    @Resource(name = "money_recordService")
    private Money_recordManager money_recordService;
    /**
     * 商城积分记录
     */
    @Resource(name = "points_recordService")
    private Points_recordManager points_recordService;
    /** 订单项 */
    @Resource(name = "order_itemService")
    private Order_itemManager order_itemService;


    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void save(PageData pd) throws Exception {
        pd.put("GMT_CREATE", DateUtil.now());    //创建时间
        pd.put("GMT_MODIFIED", DateUtil.now());    //更新时间
        pd.put("SHOP_ID", "");    //店铺id
        pd.put("TOTAL", pd.get("PRODUCT_TOTAL_AMOUNT"));    //总值
        pd.put("ACTUAL_TOTAL", pd.get("PRODUCT_TOTAL_AMOUNT"));    //实际总值
        pd.put("PAY_TYPE", pd.get("payType"));    //支付方式 0：usdt 1：商城积分 2：金额
        pd.put("REMARKS", "");    //订单备注
        pd.put("STATUS", "待发货");    //订单状态
        pd.put("DVY_FLOW_ID", "");    //物流单号
        pd.put("FREIGHT_AMOUNT", "0");    //订单运费
        pd.put("PRODUCT_SUM", pd.get("PROD_COUNT"));    //订单商品总数
        pd.put("PAY_TIME", DateUtil.now());    //付款时间
        pd.put("DVY_TIME", "");    //发货时间
        pd.put("FINALLY_TIME", "");    //完成时间
        pd.put("CANCEL_TIME", "");    //取消时间
        pd.put("IS_PAYED", "1");    //是否已经支付，1：已支付，0：未支付
        pd.put("DELETE_STATUS", "0");    //删除状态，0：没有删除， 1：回收站， 2：永久删除
        pd.put("REFUND_STS", "0");    //申请退款：0:默认,1:在处理,2:处理完成
        pd.put("REDUCE_AMOUNT", "0");    //优惠总额
        pd.put("ORDER_TYPE", "");    //订单类型
        pd.put("CLOSE_TYPE", "");    //订单关闭原因
        dao.save("OrderMapper.save", pd);
    }

    /**
     * 生成订单
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void saveGenerateOrder(PageData pd) throws Exception {
        PageData map = new PageData();
        // 先把订单项的时间更新
        map.put("GMT_MODIFIED", DateUtil.now());    //更新时间
        map.put("ORDER_ITEM_ID", pd.get("ORDER_ITEM_ID"));
        dao.update("Order_itemMapper.edit", map);
        // 创建用户订单地址
        pd.put("ADDR_ID", pd.get("USER_ADDR_ID"));
        user_addr_orderService.save(pd);
        String addrOrderId = pd.get("USER_ADDR_ORDER_ID").toString();
        // 创建订单
        pd.put("ADDR_ORDER_ID", addrOrderId);
        this.save(pd);
        // 减库存
        pd.put("tag", "-");
        pd.put("num", pd.get("PROD_COUNT"));
        prodService.updateInventory(pd);
        // 根据支付类型扣除对应金额 1:积分、0：钱包余额 并创建钱包消费记录
        pd.put("money", pd.get("PRODUCT_TOTAL_AMOUNT"));
        pd.put("USER_ID", pd.get("ACCOUNT_ID"));
        pd.put("MONEY", pd.get("PRODUCT_TOTAL_AMOUNT"));    //金额
        pd.put("CHARGE", "0");    //手续费
        pd.put("ACTUAL_RECEIPT", pd.get("PRODUCT_TOTAL_AMOUNT"));    //实际到账
        pd.put("STATUS", "已完成");    //状态
        pd.put("HE_PHONE", "");    //对方手机号
        pd.put("HE_USER_ID", "");    //对方用户ID
        pd.put("TAG", "-"); // + 或 -
        pd.put("DATA_TYPE", "购买商品"); // + 或 -
        if ("1".equals(pd.getString("payType"))) {
            accountService.updateIntegral(pd, false);
            points_recordService.save(pd);
        } else {
            accountService.updateMoney(pd, false);
            money_recordService.save(pd);
        }
    }

    /**
     * 从购物车下单生成订单
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void saveCartGenerateOrder(PageData pd, List<PageData> cartList) throws Exception {
        // 创建用户订单地址
        pd.put("ADDR_ID", pd.get("USER_ADDR_ID"));
        user_addr_orderService.save(pd);
        String addrOrderId = pd.get("USER_ADDR_ORDER_ID").toString();
        // 定义订单产品名称 以,号分割
        StringBuilder prodName = new StringBuilder();
        // 循环创建订单项,以订单号关联，一笔订单对应多个订单项
        for (PageData prod : cartList) {
            double productTotalAmount = NumberUtil.mul(prod.get("BASKET_COUNT").toString(), prod.get("PRICE").toString()).doubleValue();
            prodName.append(prod.get("PROD_NAME")).append(",");
            prod.put("ORDER_NUMBER", pd.get("ORDER_NUMBER"));
            prod.put("PRODUCT_TOTAL_AMOUNT", productTotalAmount);
            prod.put("PROD_COUNT", prod.get("BASKET_COUNT"));
            // 加入购物车时间
            prod.put("BASKET_DATE", prod.getString("GMT_CREATE"));
            prod.put("GMT_MODIFIED", DateUtil.now());
            // 创建订单项
            order_itemService.save(prod);
            // 减库存
            prod.put("tag", "-");
            prod.put("num", prod.get("BASKET_COUNT"));
            prodService.updateInventory(prod);
            // 删除购物车产品
            dao.delete("BasketMapper.delete", prod);
        }
        pd.put("PROD_NAME", prodName.toString());
        // 创建订单
        pd.put("ADDR_ORDER_ID", addrOrderId);
        this.save(pd);
        // 根据支付类型扣除对应金额 1:积分、0：钱包余额 并创建钱包消费记录
        pd.put("money", pd.get("PRODUCT_TOTAL_AMOUNT"));
        pd.put("MONEY", pd.get("PRODUCT_TOTAL_AMOUNT"));    //金额
        pd.put("CHARGE", "0");    //手续费
        pd.put("ACTUAL_RECEIPT", pd.get("PRODUCT_TOTAL_AMOUNT"));    //实际到账
        pd.put("STATUS", "已完成");    //状态
        pd.put("HE_PHONE", "");    //对方手机号
        pd.put("HE_USER_ID", "");    //对方用户ID
        pd.put("TAG", "-"); // + 或 -
        pd.put("DATA_TYPE", "购买商品");
        if ("1".equals(pd.getString("payType"))) {
            accountService.updateIntegral(pd, false);
            points_recordService.save(pd);
        } else {
            accountService.updateMoney(pd, false);
            money_recordService.save(pd);
        }


    }

    /**
     * 删除
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void delete(PageData pd) throws Exception {
        dao.delete("OrderMapper.delete", pd);
    }

    /**
     * 修改
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void edit(PageData pd) throws Exception {
        dao.update("OrderMapper.edit", pd);
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) dao.findForList("OrderMapper.datalistPage", page);
    }

    /**
     * 列表(全部)
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> listAll(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("OrderMapper.listAll", pd);
    }

    /**
     * 列表(根据用户ID查询全部)
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> listAllByUserId(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("OrderMapper.listAllByUserIdAndStatus", pd);
    }

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("OrderMapper.findById", pd);
    }

    /**
     * 通过id连表查询
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> listByIdLinkedTable(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("OrderMapper.listByIdLinkedTable", pd);
    }

    /**
     * 批量删除
     *
     * @param ArrayDATA_IDS
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
        dao.delete("OrderMapper.deleteAll", ArrayDATA_IDS);
    }

}

