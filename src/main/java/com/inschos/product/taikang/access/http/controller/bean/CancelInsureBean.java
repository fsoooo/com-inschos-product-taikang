package com.inschos.product.taikang.access.http.controller.bean;

public class CancelInsureBean {

    public static class Requset {
        public String channel_code;
        public String insured_name;
        public String insured_code;
        public String insured_phone;
        public String insured_email;
        public String insured_province;
        public String insured_city;
        public String insured_county;
        public String insured_address;
        public String bank_name;
        public String bank_code;
        public String bank_phone;
        public String bank_address;
        public String channel_order_code;
    }

    public static class Response extends BaseResponseBean {
        public ResponseData data;
    }

    public static class ResponseData {

    }
}
