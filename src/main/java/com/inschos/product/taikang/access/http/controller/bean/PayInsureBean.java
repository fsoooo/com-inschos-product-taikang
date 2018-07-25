package com.inschos.product.taikang.access.http.controller.bean;

import java.io.Serializable;

public class PayInsureBean {

    public static class Requset implements Serializable {
        public String businessType;//业务系列	个险，团险，银保
        public String cmsSystemSource;//来源系统
        public String cmsBusinessType;//业务类型	,细化业务类型如：首期，续期
        public String sapCompanyCode;//SAP公司码
        public String cmsVersion;//调用接口版本,1.0
        public String cmsFormat;//传输参数格式,JSON
        public String cmsPayChannel;//支付渠道,101：微信支付 102：支付宝支付 (访问查询接口时不作为查询依据)详见5.2
        public String cmsPayTag;// 支付方式
        public String orderId;//订单号
        public String transactionId;//交易流水号,	2位来源系统码+年+月+日+时+分+秒+流水号(来源系统唯一)
        public String requestDateTime;//请求时间,YYYY-MM-DD HH:MM:SS
        public String currencyCode;//币种,CNY,只支持人民币
        public String money;//交易金额,单位：分
        public String summary;//商品描述
        public String isCheck;//是否校验客户信息,0否1是(为空表示不校验，H5支付不校验客户信息)
        public String accountName;//客户户名
        public String certificateType;//证件类型	,只支持身份证,默认传0
        public String certificateNo;//证件号
        public String mobile;//手机号码,是/否,校验客户信息是填写
        public String isLimitCreditPay;//是否禁用信用卡0否1是（为空表示不禁用）
        public String openId;//用户id,	H5支付不填
        public String deviceInfo;//应用类型
        public String mchAppId;//应用标识(H5支付必填)
        public String mchAppName;//应用名(H5支付必填)
        public String isRaw;//原生支付.H5不填
        public String successURL;//支付成功返回地址
        public String failURL;//支付失败返回地址(H5不填)
    }

    public static class Response {
        public BaseResponse baseResponse;//返回json子对象
        public String responseCode;//交易状态
        public String responseMessage;//状态描述,返回码非"S"时有值
        public String requestDateTime;//请求时间,yyyy-mm-dd hh:mm:ss
        public String sapCompanyCode;//收款公司码
        public String orderId;//订单号,业务系统订单号
        public String transactionId;//交易流水号,业务系统交易流水号
        public String payTransactionId;//支付平台交易流水号
        public String channelTransactionId;//渠道交易流水号
        public String payCharacters;//支付串
    }

    public static class BaseResponse{
        public String responseCode;//受理返回码:3处理中,4处理成功,5处理失败
        public String responseMessage;//返回描述信息,获取支付串成功失败的描述
    }
}
