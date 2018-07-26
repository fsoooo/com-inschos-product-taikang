package com.inschos.product.taikang.access.http.controller.bean;

import java.io.Serializable;

public class PayRefundBean {

    public static class Requset implements Serializable {
        public String businessType;//业务系列	个险，团险，银保
        public String cmsSystemSource;//来源系统
        public String cmsPayTag;//支付方式
        public String cmsPayChannel;//支付渠道101：微信支付 102：支付宝支付
        public String requestDateTime;//		提交时间		否	19	YYYY-MM-DD HH:MM:SS
        public String cmsVersion;//调用接口版本	1.0
        public String cmsFormat;//传输参数格式	JSON
        public String cmsBusinessType;//业务类型
        public String orderId;//商户订单号
        public String money;//提交金额,本次退款金额,单位：分
        public String sapCompanyCode;//收款公司
        public String transactionId;//交易流水号
        public String payTransactionId;//支付平台交易流水号
        public String channelTransactionId;//渠道交易流水号
        public String mobileTransactionId;//微信支付宝交易流水号
        public String currencyCode;//币种,CNY
        public String totalManey;//交易总金额,收款时对应的订单金额，单位：分
        public String summary;//备注,商品交易信息描述

    }

    public static class Response {
        public BaseResponse baseResponse;//返回json子对象
        public String responseCode;//交易状态
        public String responseMessage;//交易描述
        public String requestDateTime;//请求时间,yyyy-mm-dd hh:mm:ss
        public String sapCompanyCode;//收款机构,SAP公司码
        public String money;//交易金额,单位：分
        public String transactionId;//业务交易流水号
        public String payTransactionId;//支付平台交易流水号
        public String channelTransactionId;//第三方渠道交易流水号
        public String refundNo;//第三方渠道退款交易流水号
        public String mobileTransactionId;//微信支付宝交易流水号
        public String accountNo;//付款银行账号
        public String outBankCode;//集中码
        public String sapSubjectCode;//科目号
    }

    public static class BaseResponse {
        public String responseCode;//受理返回码:3处理中,4处理成功,5处理失败
        public String responseMessage;//返回描述信息,获取支付串成功失败的描述
    }


}
