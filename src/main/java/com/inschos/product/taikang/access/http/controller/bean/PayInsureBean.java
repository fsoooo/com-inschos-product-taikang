package com.inschos.product.taikang.access.http.controller.bean;


import com.alibaba.druid.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class PayInsureBean {

    public static class Requset {
        public String appId;//微信分配的公众账号ID
        public String openId;//用户在商户appId下的唯一标识	若tradeType=JSAPI，此参数必传。
        public String tradeNo;//商户订单号	32位长度，7位(系统来源标识,包括渠道号+5位产品码)+yyyymmddhhmmssSSS+8位随机数字。
        public String tradeType;//交易类型	取值如下：JSAPI，NATIVE，APP，MWEB(H5支付类型),详细说明见参数规定。
        public String goodsBody;//商品或支付单简要描述	示例：Ipad mini 16G 白色
        public String goodsDetail;//商品名称明细列表	示例：Ipad mini 16G 白色
        public String totalFee;//订单总金额	只能为整数，示例：888。
        public String clientIp;//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
        public String timeStart;//交易起始时间	格式：yyyyMMddHHmmss
        public String timeExpire;//交易结束时间	格式：yyyyMMddHHmmss。timeExpire-timeStart必须大于5分钟。
        public String goodsTag;//商品标记	代金券或立减优惠功能的参数，说明详见代金券或立减优惠
        public String productId;//商品ID	tradeType=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
        public String successRedirectUrl;//支付结果成功的页面跳转地址
        public String failRedirectUrl;//支付结果失败的页面跳转地址
        public String cancelRedirectUrl;//支付取消的页面跳转地址
        public String notifyUrl;//接收微信支付异步通知回调地址
        public String limitPay;//默认为支持信用卡支付，若需要禁止使用信用卡支付，limitPay=‘Y’
        public String userCardId;//支付人真实身份证件号	实名认证需要
        public String userName;//支付人真实姓名	实名认证需要
        public String sign;//数字签名

    }

    public static class RequsetHeaders{
        public String appId;//第三方公众号唯一标识
        public String payMode;//支付模式	微信支付：wx,现金红包：wr,资金平台：fp,企业付款：we
        public String channel;//商户渠道（此为新增属性）	示例：泰康人寿:rs；泰康在线：zx
        public String sign;//数据签名	将appId、payMode及key拼接成如示例所示的串：appid=XXXX&channel=XXXX&paymode=XXXX&key=XXXX,然后经过MD5签名后所得的值
    }

    public static class Response {
        public String rspCode;
        public String rspDesc;
        public String wspTradeNo;//支付流水号，WSP生成,81位字符串
        public String sign;
    }
}
