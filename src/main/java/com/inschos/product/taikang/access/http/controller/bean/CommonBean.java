package com.inschos.product.taikang.access.http.controller.bean;

public class CommonBean {

    public static final int orgid = 5466;
    public static final String key = "bfafa2d09dba76a7be9464c8";
    public static final String plancode = "10100878";
    public static final int period = 7;
    public static final String premium = "20";
    public static final String version = "1.0";
    public static final String format = "JSON";

    //核保、承保接口地址
    public static final String checkInsureUrl = "http://119.253.81.69:8080/soa/api/v1.0/service/E001";//核保
    public static final String signInsureUrl = "http://119.253.81.69:8080/soa/api/v1.0/service/E002";//签单
    //支付相关接口地址
    public static final String payInsureUrlTest = "http://wxpt-t.taikang.com/tkmap/wechat/wxpay/order.do";//支付-测试
    public static final String payInsureUrlProduct = "http://wxpt.taikang.com/tkmap/wechat/wxpay/order.do";//支付-生产
    public static final String payQueryUrlTest = "http://wxpt-t.taikang.com/tkmap/wechat/wxpay/orderQuery.do?appId=appId&wspTradeNo=wspTradeNo";//支付查询-测试
    public static final String payQueryUrlProduct = "http://wxpt.taikang.com/tkmap/wechat/wxpay/orderQuery.do?appId=appId&wspTradeNo=wspTradeNo";//支付查询-生产
    //性别(sex)
    public final static String man = "0";
    public final static String woman = "1";
    //证件类型(idType)
    public final static String idCard = "01";
    public final static String corporateCode = "02";
    public final static String passport = "03";
    public final static String officer = "04";
    public final static String macauPass = "05";
    public final static String taiwanPass = "06";
    public final static String idTypeBirth = "07";
    public final static String otherIdType = "10";

    public static String getSex(String sexType) {
        String result = "";
        switch (sexType) {
            case "男":
                result = "0";
                break;
            case "女":
                result = "1";
                break;
        }
        return result;
    }

    public static String getIdType(String idType) {
        String result = "";
        switch (idType) {
            case "身份证":
                result = "01";
                break;
            case "法人代码":
                result = "02";
                break;
            case "护照":
                result = "03";
                break;
            case "军官证":
                result = "04";
                break;
            case "港澳通行证":
                result = "05";
                break;
            case "出生日期":
                result = "06";
                break;
            case "台湾通行证":
                result = "07";
                break;
            case "其他":
                result = "10";
                break;
        }
        return result;
    }

    //关系(relation)
    public static String getRelation(String relation) {
        String result = "";
        switch (relation) {
            case "本人":
                result = "1";
                break;
            case "丈夫":
                result = "2";
                break;
            case "妻子":
                result = "3";
                break;
            case "父亲":
                result = "4";
                break;
            case "母亲":
                result = "5";
                break;
            case "儿子":
                result = "6";
                break;
            case "女儿":
                result = "7";
                break;
            case "法定":
                result = "-1";
                break;
            case "其它亲属":
                result = "26";
                break;
            case "同事":
                result = "27";
                break;
            case "朋友":
                result = "28";
                break;
            case "雇主":
                result = "29";
                break;
            case "其他":
                result = "30";
                break;

        }
        return result;
    }

    //核保状态(insureStatusCode)
    public static String getInsureStatus(String insureStatusCode) {
        String result = "";
        switch (insureStatusCode) {
            case "0":
                result = "成功";
                break;
            case "1001":
                result = "入参为空";
                break;
            case "1002":
                result = "入参格式不正确";
                break;
            case "1003":
                result = "报文解密失败";
                break;
            case "1004":
                result = "核保失败";
                break;
            case "1005":
                result = "核保异常，请重新提交订单";
                break;
            case "1006":
                result = "保单存储失败，请重新提交";
                break;
            case "1007":
                result = "订单处理中";
                break;
            case "1008":
                result = "订单未核保或核保失败（收费校验）";
                break;
            case "1009":
                result = "订单投保成功，请不要重复交易";
                break;
            case "1010":
                result = "撤单失败";
                break;
            case "1011":
                result = "该保单已撤单";
                break;
            case "1012":
                result = "订单号Orderno/plancode/policyno三者不匹配";
                break;
            case "1013":
                result = "该订单未投保，请先进行投保交易";
                break;
            case "1014":
                result = "该保单已结算无法撤单";
                break;
            case "1015":
                result = "撤单交易失败，请重新提交";
                break;
            case "1016":
                result = "被保人累计保额超过额度";
                break;
            case "1017":
                result = "该订单已有投保记录";
                break;
            case "1018":
                result = "被保险人年龄不符合要求";
                break;
            case "1019":
                result = "投保份数已达上限";
                break;
            case "1020":
                result = "您激活的保额至少需大于10000";
                break;
            case "1021":
                result = "被保险人在黑名单内，无权投保";
                break;
            case "1022":
                result = "EBA保费测算结果与移动互联保费计算结果有出入，请核查 ";
                break;
        }
        return result;
    }

}
