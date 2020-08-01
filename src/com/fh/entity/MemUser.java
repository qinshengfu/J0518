package com.fh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 功能描述：前台用户实体类, 实现序列化接口 为了Redis缓存
 * @author Ajie
 * @date 2020年3月27日10:24:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemUser implements Serializable {

    /** 创建时间*/
    private String GMT_CREATE;
    /** 更新时间*/
    private String GMT_MODIFIED;
    /** 手机号*/
    private String PHONE;
    /** 登录密码*/
    private String LOGIN_PASSWORD;
    /** 安全密码*/
    private String SECURITY_PASSWORD;
    /** USDT钱包*/
    private Double USDT_WALLET;
    /** 金额*/
    private Double MONEY;
    /** 商城积分*/
    private Double SHOP_INTEGRAL;
    /** 推荐人数*/
    private Integer RECOMMENDED_NUMBER;
    /** 团队人数*/
    private Integer TEAM_NUMBER;
    /** 推荐人ID*/
    private String RECOMMENDER_ID;
    /** 推荐人*/
    private String RECOMMENDER;
    /** 推荐路径*/
    private String RE_PATH;
    /** 代数*/
    private Integer ALGEBRA;
    /** 用户级别*/
    private String USER_RANK;
    /** 用户状态 1:正常、0：封号*/
    private Integer USER_STATE;
    /** 省ID*/
    private String PROVINCE_ID;
    /** 省*/
    private String PROVINCE;
    /** 城市ID*/
    private String CITY_ID;
    /** 城市*/
    private String CITY;
    /** 区ID*/
    private String AREA_ID;
    /** 区*/
    private String AREA;
    /** 我的地址*/
    private String MY_ADDRESS;
    /** 是否特殊账号 1:正常、0：特殊*/
    private String IS_SPECIAL;
    /** 是否实名认证 1:是、0：否*/
    private String IS_REAL;
    /** ID*/
    private String ACCOUNT_ID;

}
