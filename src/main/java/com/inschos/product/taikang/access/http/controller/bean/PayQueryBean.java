package com.inschos.product.taikang.access.http.controller.bean;

public class PayQueryBean {

    //    public static class Requset implements Serializable { }
    public static class Requset {
        public String appId;//公众账号ID
        public String wspTradeNo;//支付流水号
        public String sign;
    }

    public static class Response {
        public String rspCode;
        public String rspDesc;
        public String openId;//用户在商户appId下的唯一标识
        public String tradeState;//交易状态	成功：SUCCESS，失败：FAIL
        public String sign;
    }
}
