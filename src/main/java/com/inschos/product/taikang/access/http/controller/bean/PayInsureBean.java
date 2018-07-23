package com.inschos.product.taikang.access.http.controller.bean;

public class PayInsureBean {

    public static class Requset {
        public String orderno;//订单号
        public String orgid;//系统编码，由ECSM系统提供可由该字段识别销售机构
        public String policyno;//保单号
    }

    public static class Response {
        public String status;//投保状态	0 —成功；1— 失败
        public String desc;//失败原因,若成功则该值为空
        public String key;//返回校验码,若接入方无要求，则为空。
        public String orderno;//订单号
        public String policyno;//保单号 若失败则该值为空
        public String type;//类型 1—投保；2—撤单；
    }
}
