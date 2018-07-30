package com.inschos.product.taikang.access.http.controller.bean;

public class PayCallBackBean {

    public static class Request {
        public String tradeNo;//订单号
        public String wspTradeNo;//支付流水号,81位字符串
        public String openId;//用户在商户appId下的唯一标识
        public String tradeState;//交易结果,SUCCESS表示成功,否则返回其他值。
        public String transactionId;//微信支付订单号,当支付状态为“SUCCESS”时，返回支付订单号
        public String payTime;//成功支付时间	当支付状态为“SUCCESS”时，返回支付时间
        public String sign;//签名串,sign值规则见统一下单接口

    }

    public static class Response {
        public String rspCode;//受理返回码:2000处理成功,100处理失败
        public String rspDesc;
    }
}
