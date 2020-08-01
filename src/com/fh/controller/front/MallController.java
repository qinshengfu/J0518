package com.fh.controller.front;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.text.StrSpliter;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.fh.annotation.CacheLock;
import com.fh.annotation.Limit;
import com.fh.entity.MemUser;
import com.fh.entity.Page;
import com.fh.entity.result.R;
import com.fh.service.mall.BasketManager;
import com.fh.service.mall.ChartManager;
import com.fh.service.mall.ProdManager;
import com.fh.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * 说明：商城操作相关
 * 创建人：Ajie
 * 创建时间：2020年5月22日17:50:08
 */
@Slf4j
@Api(tags = "商城操作")
@Controller
public class MallController extends BaseFrontController {

    /**
     * 雪花算法
     */
    private final Snowflake snowflake = new Snowflake(8L, 1L);

    /**
     * 商城轮播图
     */
    @Resource(name = "chartService")
    private ChartManager chartService;
    /**
     * 商品表
     */
    @Resource(name = "prodService")
    private ProdManager prodService;
    /**
     * 购物车
     */
    @Resource(name = "basketService")
    private BasketManager basketService;

    /**
     * 功能描述：访问【商城首页】
     *
     * @author Ajie
     * @date 2020年5月22日17:51:59
     */
    @RequestMapping(value = "release/to_mall")
    public ModelAndView toIndex() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/shop/index");
        mv.addObject("flag", "mall");
        return mv;
    }

    /**
     * 功能描述：访问【购物车】
     *
     * @author Ajie
     * @date 2020年5月22日17:51:59
     */
    @RequestMapping(value = "front/to_mallCart")
    public ModelAndView toCart() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/shop/cart");
        return mv;
    }

    /**
     * 功能描述：访问【分类页面】
     *
     * @author Ajie
     * @date 2020年5月22日17:51:59
     */
    @RequestMapping(value = "release/to_mallClassification")
    public ModelAndView toClassification() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/shop/classification");
        return mv;
    }

    /**
     * 功能描述：访问【我的】
     *
     * @author Ajie
     * @date 2020年5月22日17:51:59
     */
    @RequestMapping(value = "front/to_mallMine")
    public ModelAndView toMine() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/shop/mine");
        return mv;
    }

    /**
     * 功能描述：访问【商品详情】
     *
     * @author Ajie
     * @date 2020年5月22日17:51:59
     */
    @RequestMapping(value = "release/to_mallShopdetail/{id}")
    public ModelAndView toShopdetail(@PathVariable(value = "id") String id) throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd.put("PROD_ID", id);
        pd = prodService.findById(pd);
        mv.setViewName("front/shop/shopdetail");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 功能描述：访问【收货地址】
     *
     * @author Ajie
     * @date 2020年5月26日11:24:36
     */
    @RequestMapping(value = "front/to_mallAddress")
    public ModelAndView toAddress() {
        PageData pd = this.getPageData();
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/shop/address");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 功能描述：访问【新增收货地址】
     *
     * @author Ajie
     * @date 2020年5月26日11:24:36
     */
    @RequestMapping(value = "front/to_mallAddAddress")
    public ModelAndView toAddAddress() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/shop/add_address");
        return mv;
    }

    /**
     * 功能描述：访问【编辑收货地址】
     *
     * @author Ajie
     * @date 2020年5月26日11:24:36
     */
    @RequestMapping(value = "front/to_mallEditAddress/{id}")
    public ModelAndView toEditAddress(@PathVariable(value = "id") String id) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/shop/edit_address");
        mv.addObject("id", id);
        return mv;
    }

    /**
     * 功能描述：访问【订单详情-确认订单】
     *
     * @author Ajie
     * @date 2020年5月26日11:24:36
     */
    @RequestMapping(value = "front/to_mallOrderDetails/{id}")
    public ModelAndView toOrderDetails(@PathVariable(value = "id") String orderItemId) {
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        mv.setViewName("front/shop/orderpayment");
        mv.addObject("user", user);
        mv.addObject("pd", pd);
        mv.addObject("orderItemId", orderItemId);
        return mv;
    }

    /**
     * 功能描述：获取商城所有轮播图
     *
     * @author Ajie
     * @date 2020/5/25 0025
     */
    @RequestMapping(value = "release/mallChart", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "商城轮播图")
    public R mallChart() throws Exception {
        // 查询所有轮播图
        List<PageData> chartAll = chartService.listAll(new PageData());
        return R.ok().data("item", chartAll);
    }

    /**
     * 功能描述：新增收货地址
     *
     * @author Ajie
     * @date 2020年5月28日12:10:16
     */
    @RequestMapping(value = "front/addReceivingAddress", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "RECEIVER", value = "收货人姓名", required = true),
            @ApiImplicitParam(name = "MOBILE", value = "收货人手机号", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "region", value = "地区(省市区以【,】号分割)", example = "省,市,区", required = true),
            @ApiImplicitParam(name = "detailedAddress", value = "详细地址", required = true),
            @ApiImplicitParam(name = "isDefault", value = "是否默认 1:默认", dataType = "Integer"),
    })
    @Limit(name = "限制访问次数", key = "addReceivingAddress", prefix = "J0518", period = 60, count = 10)
    public R transferAccount() throws Exception {
        // 请求参数
        PageData pd = this.getPageData();
        for (Object key : pd.keySet()) {
            if (Tools.isEmpty(pd.getString(key))) {
                return R.parError();
            }
        }
        // 验证手机号格式
        if (!Tools.checkMobileNumber(pd.getString("MOBILE"))) {
            return R.error().message("手机号格式错误");
        }
        // 用户信息
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 检查是否默认地址
        int isDefault = "1".equals(pd.getString("isDefault")) ? 1 : 0;
        // 根据用户ID 查询收货地址 首次添加地址设置为默认地址
        pd.put("USER_ID", memUser.getACCOUNT_ID());
        List<PageData> addrList = user_addrService.listByUserId(pd);
        if (addrList.isEmpty()) {
            isDefault = 1;
        }
        pd.put("COMMON_ADDR", isDefault);
        // 保存新增地址
        pd.put("PHONE", memUser.getPHONE());
        user_addrService.save(pd);
        return R.ok().message("添加成功");
    }

    /**
     * 功能描述：获取当前登录用户收货地址列表
     *
     * @author Ajie
     * @date 2020年5月28日12:10:16
     */
    @RequestMapping(value = "front/getAddressList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("获取当前登录用户收货地址列表")
    public R getAddressList() throws Exception {
        PageData pd = new PageData();
        // 用户信息
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 根据用户ID 查询收货地址
        pd.put("USER_ID", memUser.getACCOUNT_ID());
        List<PageData> addrList = user_addrService.listByUserId(pd);
        return R.ok().data("item", addrList);
    }

    /**
     * 功能描述：根据ID获取收货地址
     *
     * @author Ajie
     * @date 2020年5月28日12:10:16
     */
    @RequestMapping(value = "front/getAddress", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据ID获取收货地址")
    @ApiImplicitParam(name = "USER_ADDR_ID", value = "收货地址ID", required = true, dataType = "Integer")
    public R getAddress() throws Exception {
        PageData pd = this.getPageData();
        pd = user_addrService.findById(pd);
        // 用户信息
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        if (!pd.getString("USER_ID").equals(memUser.getACCOUNT_ID())) {
            return R.error().message("只能查询自己地址");
        }
        return R.ok().data(pd);
    }

    /**
     * 功能描述：根据ID删除收货地址
     *
     * @author Ajie
     * @date 2020年5月28日12:10:16
     */
    @RequestMapping(value = "front/delAddress", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("根据ID删除收货地址")
    @ApiImplicitParam(name = "USER_ADDR_ID", value = "收货地址ID", required = true, dataType = "Integer")
    public R delAddress() throws Exception {
        PageData pd = this.getPageData();
        pd = user_addrService.findById(pd);
        if ("1".equals(pd.getString("COMMON_ADDR"))) {
            return R.error().message("默认地址无法删除");
        }
        // 用户信息
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        if (!pd.getString("USER_ID").equals(memUser.getACCOUNT_ID())) {
            return R.error().message("只能删除自己地址");
        }
        user_addrService.delete(pd);
        return R.ok().message("删除成功");
    }

    /**
     * 功能描述：修改用户收货地址
     *
     * @author Ajie
     * @date 2020年5月28日12:10:16
     */
    @RequestMapping(value = "front/editUserAddress", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("修改用户收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "RECEIVER", value = "收货人姓名", required = true),
            @ApiImplicitParam(name = "MOBILE", value = "收货人手机号", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "region", value = "地区(省市区以【,】号分割)", example = "省,市,区", required = true),
            @ApiImplicitParam(name = "ADDR", value = "详细地址", required = true),
            @ApiImplicitParam(name = "isDefault", value = "是否默认 1:默认", dataType = "Integer"),
            @ApiImplicitParam(name = "USER_ADDR_ID", value = "用户地址ID", dataType = "Integer"),
    })
    public R editAddress() throws Exception {
        // 请求参数
        PageData pd = this.getPageData();
        for (Object key : pd.keySet()) {
            if (Tools.isEmpty(pd.getString(key))) {
                return R.parError();
            }
        }
        // 验证手机号格式
        if (!Tools.checkMobileNumber(pd.getString("MOBILE"))) {
            return R.error().message("手机号格式错误");
        }
        // 用户信息
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData addr = user_addrService.findById(pd);
        if (!addr.getString("USER_ID").equals(memUser.getACCOUNT_ID())) {
            return R.error().message("只能修改自己地址");
        }
        // 检查是否默认地址
        int isDefault = "1".equals(pd.getString("isDefault")) ? 1 : 0;
        pd.remove("isDefault");
        pd.put("COMMON_ADDR", isDefault);
        pd.put("USER_ID", memUser.getACCOUNT_ID());
        // 修改地址
        user_addrService.edit(pd);
        return R.ok().message("修改成功");
    }

    /**
     * 功能描述：获取商品列表 物理分页
     *
     * @author Ajie
     * @date 2020年5月28日12:10:16
     */
    @RequestMapping(value = "release/getProdList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("获取商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keywords", value = "关键字"),
            @ApiImplicitParam(name = "currentPage", value = "页码", example = "1", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "showCount", value = "页长", example = "10", required = true, dataType = "Integer"),
    })
    public R getProdList(Page page) throws Exception {
        // 请求的参数
        PageData pd = this.getPageData();
        page.setPd(pd);
        // 查询记录
        List<PageData> listPage = prodService.frontDataListPage(page);
        // 得到总页数
        int totalPage = page.getTotalPage();
        pd.put("curPageData", listPage);
        pd.put("totalPage", totalPage);
        return R.ok().data(pd);
    }

    /**
     * 功能描述：从商品详情购买商品-下订单
     *
     * @author Ajie
     * @date 2020/5/29 0029
     */
    @RequestMapping(value = "front/placeAnOrder", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "从商品详情购买商品-下订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROD_ID", value = "商品ID", required = true),
            @ApiImplicitParam(name = "num", value = "购买数量", required = true, dataType = "Integer"),
    })
    @CacheLock(prefix = "J0518")
    public R placeAnOrder() throws Exception {
        // 请求的参数
        PageData pd = this.getPageData();
        // 根据ID查询商品信息
        PageData prod = prodService.findById(pd);
        // 购买的数量
        int num = Convert.toInt(pd.get("num"));
        // 商品库存、价格
        int stock = Convert.toInt(prod.get("TOTAL_STOCKS"));
        double price = Convert.toDouble(prod.get("PRICE"));
        // 验证库存
        if (num > stock) {
            return R.error().message("库存不足");
        }
        // 获取缓存中的用户信息
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("USER_ID", memUser.getACCOUNT_ID());
        // 根据用户ID 获取默认收货地址
        PageData commonAddr = user_addrService.findByUserIdAndCommon(pd);
        if (commonAddr == null) {
            return R.error().message("请先添加收货地址");
        }
        // 计算总金额
        double totalAmonut = NumberUtil.mul(num, price);
        // 使用雪花算法生成的订单号
        String orderNumber = snowflake.nextIdStr();
        pd.put("ORDER_NUMBER", orderNumber);
        pd.put("PRODUCT_TOTAL_AMOUNT", totalAmonut);
        pd.put("PROD_NAME", prod.get("PROD_NAME"));
        pd.put("PIC", prod.get("PIC"));
        pd.put("PRICE", prod.get("PRICE"));
        pd.put("PROD_COUNT", pd.get("num"));    //购物车产品个数
        // 加入购物车时间
        pd.put("BASKET_DATE", "");
        // 更新时间
        pd.put("GMT_MODIFIED", "");
        // 创建订单项
        order_itemService.save(pd);
        // 取刚刚新建的订单项ID
        String orderItemId = Convert.toStr(pd.get("ORDER_ITEM_ID"));
        return R.ok().data("orderItemId", orderItemId);
    }

    /**
     * 功能描述：获取订单项信息
     *
     * @author Ajie
     * @date 2020/5/30 0030
     */
    @RequestMapping(value = "front/getOrderItem", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取订单项信息")
    @ApiImplicitParam(name = "ORDER_ITEM_ID", value = "订单项ID", required = true, dataType = "Integer")
    public R getOrderItem() throws Exception {
        // 请求的参数
        PageData pd = this.getPageData();
        for (Object key : pd.keySet()) {
            if (Tools.isEmpty(pd.get(key).toString())) {
                return R.parError();
            }
        }
        pd = order_itemService.findById(pd);
        return R.ok().data("item", pd);
    }

    /**
     * 功能描述：获取订单SKU信息
     *
     * @author Ajie
     * @date 2020年6月2日15:24:35
     */
    @RequestMapping(value = "front/getOrderSku", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取订单SKU信息")
    @ApiImplicitParam(name = "BASKET_ID", value = "购物车ID列表", required = true, dataType = "Integer")
    public R getOrderSku() throws Exception {
        // 请求的参数
        PageData pd = this.getPageData();
        if (Tools.isEmpty(pd.get("BASKET_ID").toString())) {
            return R.parError();
        }
        // 根据购物车ID 连表查询购物车和商品表及SKU表
        List<PageData> cartList = basketService.listByCartIdList(pd);
        return R.ok().data("item", cartList);
    }

    /**
     * 功能描述：获取用户默认收货地址
     *
     * @author Ajie
     * @date 2020/5/30 0030
     */
    @RequestMapping(value = "front/getUserCommonAddress", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取用户默认收货地址")
    @ApiImplicitParam(name = "USER_ID", value = "用户ID", required = true)
    public R getUserCommonAddress() throws Exception {
        // 请求的参数
        PageData pd = this.getPageData();
        for (Object key : pd.keySet()) {
            if (Tools.isEmpty(pd.get(key).toString())) {
                return R.parError();
            }
        }
        pd = user_addrService.findByUserIdAndCommon(pd);
        return R.ok().data(pd);
    }

    /**
     * 功能描述：从商品详情购买商品-付款
     *
     * @author Ajie
     * @date 2020/5/29 0029
     */
    @RequestMapping(value = "front/shopBuyPay", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "从商品详情购买商品-付款")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ORDER_ITEM_ID", value = "订单项ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "payType", value = "支付方式 1:积分、0：钱包余额", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "USER_ADDR_ID", value = "用户地址ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "password", value = "交易密码", required = true),
    })
    @CacheLock(prefix = "J0518")
    public R shopBuyPay() throws Exception {
        // 请求的参数
        PageData pd = this.getPageData();
        // 根据订单项ID查询信息
        PageData orderItem = order_itemService.findById(pd);
        // 根据ID查询商品信息
        PageData prod = prodService.findById(orderItem);
        // 购买的数量
        int num = Convert.toInt(orderItem.get("PROD_COUNT"));
        // 商品库存
        int stock = Convert.toInt(prod.get("TOTAL_STOCKS"));
        // 验证库存
        if (num > stock) {
            return R.error().message("库存不足");
        }
        // 获取缓存中的用户信息
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        String password = DigestUtil.md5Hex(memUser.getPHONE() + pd.getString("password"));
        if (!memUser.getSECURITY_PASSWORD().equals(password)) {
            return R.error().message("交易密码错误");
        }
        pd.put("ACCOUNT_ID", memUser.getACCOUNT_ID());
        PageData user = accountService.findById(pd);
        // 根据地址ID 获取收货地址
        PageData userAddress = user_addrService.findById(pd);
        // 获取总金额
        double totalAmonut = Convert.toDouble(orderItem.get("PRODUCT_TOTAL_AMOUNT"));
        // 验证支付方式
        if ("1".equals(pd.getString("payType"))) {
            double shopIntegral = Convert.toDouble(user.get("SHOP_INTEGRAL"));
            if (totalAmonut > shopIntegral) {
                return R.error().message("商城积分不足");
            }
        } else {
            double money = Convert.toDouble(user.get("MONEY"));
            if (totalAmonut > money) {
                return R.error().message("钱包余额不足");
            }
        }
        // 创建订单、订单收货地址，减库存、减余额、创建钱包消费记录
        pd.putAll(userAddress);
        pd.putAll(orderItem);
        pd.putAll(prod);
        pd.putAll(user);
        orderService.saveGenerateOrder(pd);

        return R.ok();
    }

    /**
     * 功能描述：从购物车购买商品-付款
     *
     * @author Ajie
     * @date 2020年6月2日16:12:27
     */
    @RequestMapping(value = "front/cartBuyPay", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "从购物车购买商品-付款")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "BASKET_ID", value = "购物车ID列表格式：1,2,3", required = true),
            @ApiImplicitParam(name = "payType", value = "支付方式 1:积分、0：钱包余额", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "USER_ADDR_ID", value = "用户地址ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "password", value = "交易密码", required = true),
    })
    @CacheLock(prefix = "J0518")
    public R cartBuyPay() throws Exception {
        // 请求的参数
        PageData pd = this.getPageData();
        for (Object key : pd.keySet()) {
            if (Tools.isEmpty(pd.get(key).toString())) {
                return R.parError();
            }
        }
        // 根据购物车ID 连表查询购物车和商品表及SKU表
        List<PageData> cartList = basketService.listByCartIdList(pd);
        // 计算总金额
        double totalAmonut = 0;
        int totalNumber = 0;
        for (PageData prod : cartList) {
            // 购买的数量
            int num = Convert.toInt(prod.get("BASKET_COUNT"));
            totalNumber += num;
            // 商品库存
            int stock = Convert.toInt(prod.get("TOTAL_STOCKS"));
            // 验证库存
            if (num > stock) {
                return R.error().message(prod.get("PROD_NAME") + " 库存不足");
            }
            totalAmonut += NumberUtil.mul(prod.get("BASKET_COUNT").toString(), prod.get("PRICE").toString()).doubleValue();
        }
        // 获取缓存中的用户信息
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        String password = DigestUtil.md5Hex(memUser.getPHONE() + pd.getString("password"));
        if (!memUser.getSECURITY_PASSWORD().equals(password)) {
            return R.error().message("交易密码错误");
        }
        // 根据地址ID 获取收货地址
        PageData userAddress = user_addrService.findById(pd);
        pd.put("ACCOUNT_ID", memUser.getACCOUNT_ID());
        PageData user = accountService.findById(pd);
        // 验证支付方式
        if ("1".equals(pd.getString("payType"))) {
            double shopIntegral = Convert.toDouble(user.get("SHOP_INTEGRAL"));
            if (totalAmonut > shopIntegral) {
                return R.error().message("商城积分不足");
            }
        } else {
            double money = Convert.toDouble(user.get("MONEY"));
            if (totalAmonut > money) {
                return R.error().message("钱包余额不足");
            }
        }
        // 使用雪花算法生成的订单号
        String orderNumber = snowflake.nextIdStr();
        // 创建订单、订单收货地址，减库存、减余额、创建钱包消费记录
        pd.putAll(user);
        pd.putAll(userAddress);
        pd.put("ORDER_NUMBER", orderNumber);
        pd.put("PRODUCT_TOTAL_AMOUNT", totalAmonut);
        pd.put("PROD_COUNT", totalNumber);
        orderService.saveCartGenerateOrder(pd, cartList);
        return R.ok();
    }

    /**
     * 功能描述：获取用户订单列表-逻辑分页
     *
     * @author Ajie
     * @date 2020/6/1 0001
     */
    @RequestMapping(value = "front/getUserOrderList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取用户订单列表-逻辑分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "STATUS", value = "状态：null 查询所有，或者输入对应的状态查询", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currentPage", value = "页码", example = "1", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "showCount", value = "页长", example = "10", required = true, dataType = "Integer"),
    })
    public R getUserOrderList() throws Exception {
        PageData pd = this.getPageData();
        if ("全部".equals(pd.getString("STATUS"))) {
            pd.remove("STATUS");
        }
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("USER_ID", user.getACCOUNT_ID());
        List<PageData> orderList = orderService.listAllByUserId(pd);
        for (PageData map : orderList) {
            List<PageData> orderItemList = order_itemService.listAllByOrderNum(map);
            map.put("orderItem", orderItemList);
        }
        // 执行分页
        Pager<PageData> pager = new Pager<>();
        int currentPage = Convert.toInt(pd.get("currentPage"));
        int showCount = Convert.toInt(pd.get("showCount"));
        pager.setCurentPageIndex(currentPage);
        pager.setCountPerpage(showCount);
        // 传入大集合
        pager.setBigList(orderList);
        // 获取总页数
        int totalPage = pager.getPageCount();
        // 获取分页后的列表
        List<PageData> pageList = pager.getSmallList();
        pd.put("totalPage", totalPage);
        pd.put("curPageData", pageList);
        return R.ok().data(pd);
    }

    /**
     * 功能描述：根据订单ID获取订单详情
     *
     * @author Ajie
     * @date 2020/6/1 0001
     */
    @RequestMapping(value = "front/getOrderDetail", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "根据订单ID获取订单详情")
    @ApiImplicitParam(name = "ORDER_ID", value = "订单ID", required = true, dataType = "Integer")
    public R getOrderDetail() throws Exception {
        PageData pd = this.getPageData();
        // 根据订单ID连表查询
        List<PageData> orderList = orderService.listByIdLinkedTable(pd);
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        if (!orderList.get(0).getString("USER_ID").equals(user.getACCOUNT_ID())) {
            return R.error().message("只能获取自己的订单");
        }
        return R.ok().data("item", orderList);
    }

    /**
     * 功能描述：确认收货
     *
     * @author Ajie
     * @date 2020/6/1 0001
     */
    @RequestMapping(value = "front/confirmSign", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "确认收货")
    @ApiImplicitParam(name = "ORDER_ID", value = "订单ID", required = true, dataType = "Integer")
    public R confirmSign() throws Exception {
        PageData pd = this.getPageData();
        // 根据订单ID连表查询
        List<PageData> orderList = orderService.listByIdLinkedTable(pd);
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        if (!orderList.get(0).getString("USER_ID").equals(user.getACCOUNT_ID())) {
            return R.error().message("只能确认自己的订单");
        }
        // 更新订单状态为已完成
        pd.put("STATUS", "已完成");
        pd.put("FINALLY_TIME", DateUtil.now());
        pd.put("GMT_MODIFIED", DateUtil.now());
        orderService.edit(pd);
        for (PageData order : orderList) {
            // 增加该商品销售数量
            pd.put("tag", "+");
            pd.put("PROD_ID", order.get("PROD_ID"));
            pd.put("num", order.get("PROD_COUNT"));
            prodService.updataSoldNum(pd);
        }
        return R.ok().message("收货成功");
    }

    /**
     * 功能描述：加入商品到购物车
     *
     * @author Ajie
     * @date 2020/6/1 0001
     */
    @RequestMapping(value = "front/addToCart", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "加入商品到购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PROD_ID", value = "商品ID", required = true),
            @ApiImplicitParam(name = "num", value = "数量", required = true, dataType = "Integer"),
    })
    public R addToCart() throws Exception {
        PageData pd = this.getPageData();
        // 根据商品ID查询
        PageData prod = prodService.findById(pd);
        pd.putAll(prod);
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("USER_ID", user.getACCOUNT_ID());
        basketService.save(pd);
        return R.ok().message("添加到购物车成功");
    }

    /**
     * 功能描述：获取当前用户购物车商品数据列表
     *
     * @author Ajie
     * @date 2020/6/1 0001
     */
    @RequestMapping(value = "front/getCartDataList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取当前用户购物车商品数据列表-逻辑分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "页码", example = "1", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "showCount", value = "页长", example = "10", required = true, dataType = "Integer"),
    })
    public R getCartDataList() throws Exception {
        PageData pd = this.getPageData();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("USER_ID", user.getACCOUNT_ID());
        // 根据用户ID 连表查询购物车和SKU表及商品表
        List<PageData> cartList = basketService.listAllByUserId(pd);
        // 执行分页
        Pager<PageData> pager = new Pager<>();
        int currentPage = Convert.toInt(pd.get("currentPage"));
        int showCount = Convert.toInt(pd.get("showCount"));
        pager.setCurentPageIndex(currentPage);
        pager.setCountPerpage(showCount);
        // 传入大集合
        pager.setBigList(cartList);
        // 总页数
        int totalPage = pager.getPageCount();
        // 得到分页后的列表
        List<PageData> curPageData = pager.getSmallList();
        pd.put("totalPage", totalPage);
        pd.put("curPageData", curPageData);
        return R.ok().data(pd);
    }

    /**
     * 功能描述：更新购物车商品数量
     *
     * @author Ajie
     * @date 2020/6/1 0001
     */
    @RequestMapping(value = "front/updataCartProdAmount", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "更新购物车商品数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "BASKET_ID", value = "购物车ID", required = true),
            @ApiImplicitParam(name = "BASKET_COUNT", value = "数量", required = true, dataType = "Integer"),
    })
    public R updataCartProdAmount() throws Exception {
        // 请求的参数
        PageData pd = this.getPageData();
        for (Object key : pd.keySet()) {
            if (Tools.isEmpty(pd.get(key).toString())) {
                return R.parError();
            }
        }
        basketService.edit(pd);
        return R.ok();
    }

    /**
     * 功能描述：删除购物车商品
     *
     * @author Ajie
     * @date 2020/6/1 0001
     */
    @RequestMapping(value = "front/deleteCartProd", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "删除购物车商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "BASKET_ID", value = "购物车ID", required = true),
    })
    @CacheLock(prefix = "J0518")
    public R deleteCartProd() throws Exception {
        // 请求的参数
        PageData pd = this.getPageData();
        for (Object key : pd.keySet()) {
            if (Tools.isEmpty(pd.get(key).toString())) {
                return R.parError();
            }
        }
        basketService.delete(pd);
        return R.ok();
    }


    /**
     * 功能描述：从购物车购买商品-下订单
     *
     * @author Ajie
     * @date 2020/5/29 0029
     */
    @RequestMapping(value = "front/cartPlaceAnOrder", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "从购物车购买商品-下订单")
    @ApiImplicitParam(name = "BASKET_ID_LIST", value = "购物车ID以,号分割", required = true)
    @CacheLock(prefix = "J0518")
    public R cartPlaceAnOrder() throws Exception {
        // 请求的参数
        PageData pd = this.getPageData();
        // 获取缓存中的用户信息
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("USER_ID", memUser.getACCOUNT_ID());
        // 根据用户ID 获取默认收货地址
        PageData commonAddr = user_addrService.findByUserIdAndCommon(pd);
        if (commonAddr == null) {
            return R.error().message("请先添加收货地址");
        }
        // 去除购物车ID列表末尾的,号
        String cartIdList = pd.getString("BASKET_ID_LIST");
        cartIdList = cartIdList.substring(0, cartIdList.length() - 1);
        // 根据购物车ID 连表查询购物车和商品表及SKU表
        pd.put("BASKET_ID", cartIdList);
        List<PageData> cartList = basketService.listByCartIdList(pd);
        for (PageData prod : cartList) {
            // 购买的数量
            int num = Convert.toInt(prod.get("BASKET_COUNT"));
            // 商品库存
            int stock = Convert.toInt(prod.get("TOTAL_STOCKS"));
            // 验证库存
            if (num > stock) {
                return R.error().message(prod.get("PROD_NAME") + " 库存不足");
            }
        }
        return R.ok().data("cartIdList", cartIdList);
    }


}
