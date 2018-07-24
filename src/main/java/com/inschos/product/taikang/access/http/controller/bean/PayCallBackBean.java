package com.inschos.product.taikang.access.http.controller.bean;

public class PayCallBackBean {

    public static class Response {
        public BaseResponse baseResponse;//返回json子对象
        public String responseCode;//受理返回码:3处理中,4处理成功,5处理失败
        public String responseMessage;//返回描述信息,获取支付串成功失败的描述
        public String transTime;//交易返回时间,yyyy-MM-dd HH:mm:ss
        public String sapCompanyCode;//收款公司
        public String orderId;//商户订单号
        public String transactionId;//交易流水号
        public String payTransactionId;//支付平台交易流水号
        public String channelTransactionId;//渠道交易流水号
        public String mobileTransactionId;//微信支付宝交易流水号
        public String money;//交易金额,单位：分
        public String accountNo;//收付款账号,泰康银行卡号
        public String outBankCode;//集中码
        public String sapSubjectCode;//科目号
        public String openId;//	用户的访问ID,公众号支付时用的openid
    }

    public static class BaseResponse{
        public String responseCode;//受理返回码:3处理中,4处理成功,5处理失败
        public String responseMessage;//返回描述信息,获取支付串成功失败的描述
        public String requestDateTime;//请求时间,yyyy-mm-dd hh:mm:ss
    }
}
