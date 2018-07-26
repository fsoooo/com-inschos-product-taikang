package com.inschos.product.taikang.access.http.controller.bean;

import java.io.Serializable;

public class PayBillBean {

    public static class Requset implements Serializable {
        public String businessType;//业务系列	个险，团险，银保
        public String cmsSystemSource;//来源系统
        public String billDate;//对账单日期,yyyy-MM-dd
    }

    public static class Response {
        public String responseCode;//受理返回码:S成功F失败
        public String responseMessage;//描述信息
        public String billUrl;//文件URL地址
    }
}
