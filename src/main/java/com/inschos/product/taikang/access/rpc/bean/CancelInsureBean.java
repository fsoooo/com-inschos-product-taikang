package com.inschos.product.taikang.access.rpc.bean;

public class CancelInsureBean {

    public static class Requset {
        public String orderno;//订单号
        public String orderfrom;//订单来源
        public String plancode;//产品编码
        public String orgid;//系统编码
        public String policyno;//保单号
        public String insurename;//被保人姓名
        public String insurecid;//被保人证件号码
    }

    public static class Response {
        public String status;//投保状态	0 —成功；1— 失败
        public String desc;//失败原因,若成功则该值为空
        public String key;//返回校验码,若接入方无要求，则为空。
        public String orderno;//订单号
        public String policyno;//保单号 若失败则该值为空
        public String type;//类型 1—投保；2—撤单；3—保全
    }
}
