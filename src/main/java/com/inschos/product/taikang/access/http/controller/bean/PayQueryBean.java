package com.inschos.product.taikang.access.http.controller.bean;

import java.io.Serializable;
import java.util.List;

public class PayQueryBean {

    public static class Requset implements Serializable {
        public String businessType;//业务系列	个险，团险，银保
        public String cmsSystemSource;//来源系统
        public String cmsPayTag;//支付方式
        public String cmsPayChannel;//支付渠道,101：微信支付 102：支付宝支付
        public String requestDateTime;//提交时间	,YYYY-MM-DD HH:MM:SS
        public String cmsVersion;//调用接口版本	1.0
        public String cmsFormat;//传输参数格式	JSON
        public List<QueryList> queryList;//查询集合
    }

    public static class QueryList {
        public String sapCompanyCode;//SAP公司码
        public String transactionId;//交易流水号
    }

    public static class Response {
        public String responseCode;//受理返回码
        public String responseMessage;//描述信息
        public String requestDateTime;//请求时间,yyyy-mm-dd hh:mm:ss
        public List<ResultList> resultList;//json中的集合对象
    }

    public static class ResultList {
        public BaseResponse baseResponse;//返回json子对象
        public String orderId;//商户订单号
        public String transactionId;//交易流水号
        public String payTransactionId;//支付平台交易流水号
        public String channelTransactionId;//渠道交易流水号
        public String mobileTransactionId;//微信支付宝交易流水号
        public String money;//交易金额,单位：分
        public String transactionSuccessDateTime;//交易成功时间
        public String transTime;//交易返回时间,yyyy-MM-dd HH:mm:ss
        public String accountNo;//收付款账号,泰康银行卡号
        public String outBankCode;//集中码
        public String sapSubjectCode;//科目号
        public String openId;//	用户的访问ID,公众号支付时用的openid
    }

    public static class BaseResponse {
        public String responseCode;//受理返回码:3处理中,4处理成功,5处理失败
        public String responseMessage;//返回描述信息,获取支付串成功失败的描述
    }
}
