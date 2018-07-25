package com.inschos.product.taikang.access.http.controller.bean;

import java.util.List;

public class BuyInsureBean {

    public static class Requset {
        //订单信息
        public String orderno;//随机生成,订单号小于等于20位
        public String orderfrom;//订单来源
        public String Plancode;//产品编码
        public int orgid;//系统编码
        public String startdate;//生效时间(date 格式：yyyy-mm-dd hh:mm:ss)
        public String applydate;//投保时间
        public int period;//保险期间
        public String premium;//保费(NUMBER(11,2))
        public String amnt;//保额(NUMBER(11,2))
        //投保人信息
        public String name;
        public String cidtype;
        public String cid;
        public String sex;//代码表见附录。
        public String birth;//date 格式：yyyy-mm-dd
        public String mobile;
        public String email;
        //被保人信息
        public String insurerelation;//被保人与投保人关系 码表
        public String insurename;
        public String insurecidtype;//被保人证件类型
        public String insurecid;
        public String insuresex;
        public String insurebirth;//date 格式：yyyy-mm-dd
        //受益人信息
        public String bnfrelation;
        public String bnfname;
        public String bnfcidtype;
        public String bnfcid;
        public String bnfsex;
        public String bnfbirth;//date 格式：yyyy-mm-dd
        //其他信息
        public String agentno;//业务员编码
        public String agenttype;//业务员渠道
        public String agentcomcode;//业务员所属分公司
        public List<PolicySpldatas> policyspldatas;//特殊项字段
    }

    public static class PolicySpldatas{
        public String psit_code;//约定项编码
        public String psit_desc;//约定项描述
        public String psdt_value;//约定项值
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
