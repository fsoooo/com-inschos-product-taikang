package com.inschos.product.taikang.access.http.controller.action;

import com.fasterxml.jackson.core.type.TypeReference;
import com.inschos.common.assist.kit.HttpKit;
import com.inschos.common.assist.kit.JsonKit;
import com.inschos.common.assist.kit.ListKit;
import com.inschos.product.taikang.access.http.controller.bean.*;
import com.inschos.product.taikang.assist.kit.EncryptUtil;
import com.inschos.product.taikang.assist.kit.HttpClientKit;
import com.inschos.product.taikang.assist.kit.MD5Kit;
import com.inschos.product.taikang.assist.kit.PaySignKit;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static com.inschos.product.taikang.access.http.controller.bean.CommonBean.*;

@Component
public class IntersAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(IntersAction.class);

    /**
     * http请求公共函数
     *
     * @param url       请求地址
     * @param json      请求报文,格式是json
     * @param interName 接口名称
     * @param headers   headers
     * @param type      请求类型:POST/GET
     * @return String
     */
    private BaseResponseBean httpRequest(String url, String json, String interName, String headers, String type) {
        BaseResponseBean response = new BaseResponseBean();
        if (interName == null) {
            interName = "";
        }
        try {
            logger.info(interName + "接口请求地址：" + url);
            logger.info(interName + "接口请求参数：" + json);
            String result = "";
            if (headers == null) {
                result = HttpClientKit.post(url, json);
            } else {
                List<HeaderBean.Header> headerList = JsonKit.json2Bean(headers, new TypeReference<List<HeaderBean.Header>>() {
                });
                if (headerList != null && headerList.size() != 0) {
                    result = HttpClientKit.post(url, json, headerList);
                }
            }
            logger.info(interName + "接口返回数据：" + result);
            if (result == null) {
                return responseBean(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败,返回null", response);
            }
            response.data = result;
            return responseBean(BaseResponseBean.CODE_SUCCESS, interName + "接口请求成功", response);
        } catch (IOException e) {
            e.printStackTrace();
            return responseBean(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败,请求出错", response);
        }
    }

    /**
     * 核保
     *
     * @param httpServletRequest
     * @return
     */
    public String checkInsure(HttpServletRequest httpServletRequest) {
        BuyInsureBean.Requset request = JsonKit.json2Bean(HttpKit.readRequestBody(httpServletRequest), BuyInsureBean.Requset.class);
        BaseResponseBean response = new BaseResponseBean();
        String interName = "核保";
        if (request == null) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "参数解析失败", response);
        }
        BuyInsureBean.Requset buyRequest = new BuyInsureBean.Requset();
        buyRequest.orderno = request.orderno;//订单信息,订单号小于等于20位
        buyRequest.orderfrom = "";//订单来源
        buyRequest.Plancode = plancode;//产品编码
        buyRequest.orgid = orgid;//系统编码
        buyRequest.startdate = request.startdate;//生效时间
        buyRequest.applydate = request.applydate;//投保时间
        buyRequest.period = period;//保险期间
        buyRequest.premium = premium;//保费
        buyRequest.amnt = request.amnt;//保额
        //投保人信息
        buyRequest.name = request.name;
        buyRequest.cidtype = request.cidtype;
        buyRequest.cid = request.cid;
        buyRequest.sex = request.sex;//代码表见附录。
        buyRequest.birth = request.birth;//格式：yyyy-mm-dd
        buyRequest.mobile = request.mobile;
        buyRequest.email = request.email;
        //被保人信息
        buyRequest.insurerelation = request.insurerelation;//被保人与投保人关系 码表
        buyRequest.insurename = request.insurename;
        buyRequest.insurecidtype = request.insurecidtype;//被保人证件类型
        buyRequest.insurecid = request.insurecid;
        buyRequest.insuresex = request.insuresex;
        buyRequest.insurebirth = request.insurebirth;
        //受益人信息
        buyRequest.bnfrelation = request.bnfrelation;
        buyRequest.bnfname = request.bnfname;
        buyRequest.bnfcidtype = request.bnfcidtype;
        buyRequest.bnfcid = request.bnfcid;
        buyRequest.bnfsex = request.bnfsex;
        buyRequest.bnfbirth = request.bnfbirth;
        //其他信息
        buyRequest.agentno = "52589745";//业务员编码
        buyRequest.agenttype = "AG";//业务员渠道
        buyRequest.agentcomcode = "1";//业务员所属分公司
        BuyInsureBean.PolicySpldatas policySpldata = new BuyInsureBean.PolicySpldatas();
        policySpldata.psdt_value = "";
        policySpldata.psit_code = "";
        policySpldata.psit_desc = "";
        List<BuyInsureBean.PolicySpldatas> policySpldatas = new ArrayList<>();
        policySpldatas.add(policySpldata);
        buyRequest.policyspldatas = policySpldatas;
        //加解密处理,请求接口
        String requestData = JsonKit.bean2Json(buyRequest);
        logger.info(interName + "接口请求json" + requestData);
        String data = EncryptUtil.getEncryptStr(key, requestData);
        List<HeaderBean.Header> headerList = new ArrayList<>();
        BaseResponseBean interResponse = httpRequest(checkInsureUrl, orgid + "|" + data, interName, JsonKit.bean2Json(headerList),"post");
        if (interResponse.code != 200) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败", response);
        }
        String result = interResponse.data.toString();
        result = EncryptUtil.getDecryptStr(key, result);
        if (!isJSONValid(result)) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
        }
        BuyInsureBean.Response buyResponse = JsonKit.json2Bean(result, BuyInsureBean.Response.class);
        if (buyResponse != null) {
            response.data = buyResponse;
            logger.info("核保状态:" + buyResponse.status);
            if (buyResponse.status == 0) {
                return json(BaseResponseBean.CODE_SUCCESS, interName + "成功", response);
            } else {
                return json(BaseResponseBean.CODE_FAILURE, interName + "失败", response);
            }
        } else {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
        }
    }

    /**
     * 承保(参数分两部分,支付查询信息和订单信息)
     *
     * @param httpServletRequest
     * @return
     */
    public String buyInsure(HttpServletRequest httpServletRequest) {
        SignInsureBean.Requset request = JsonKit.json2Bean(HttpKit.readRequestBody(httpServletRequest), SignInsureBean.Requset.class);
        BaseResponseBean response = new BaseResponseBean();
        String interName = "承保";
        if (request == null) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "参数解析失败", response);
        }
        SignInsureBean.Requset signRequest = new SignInsureBean.Requset();
        //业务参数
        signRequest.businessType = request.businessType;//业务系列,详见支付接口5.6
        signRequest.cmsSystemSource = request.cmsSystemSource;//来源系统	,详见支付接口5.7
        signRequest.cmsPayTag = request.cmsPayTag;//支付方式,详见支付接口5.3
        signRequest.cmsPayChannel = request.cmsPayChannel;//支付渠道,101：微信支付 102：支付宝支付
        signRequest.requestDateTime = request.requestDateTime;//提交时间	,YYYY-MM-DD HH:MM:SS
        signRequest.cmsVersion = version;//调用接口版本,固定传1.0
        signRequest.cmsFormat = format;//传输参数格式,固定传JSON
        SignInsureBean.queryInfo queryInfo = new SignInsureBean.queryInfo();
        queryInfo.sapCompanyCode = "";
        queryInfo.transactionId = "";
        List<SignInsureBean.queryInfo> queryInfos = new ArrayList<>();
        queryInfos.add(queryInfo);
        signRequest.queryList = queryInfos;//查询集合
        //保单信息
        signRequest.orderno = request.orderno;//订单信息,订单号小于等于20位
        signRequest.orderfrom = "";//订单来源
        signRequest.Plancode = plancode;//产品编码
        signRequest.orgid = orgid;//系统编码
        signRequest.startdate = request.startdate;//生效时间
        signRequest.applydate = request.applydate;//投保时间
        signRequest.period = period;//保险期间
        signRequest.premium = premium;//保费
        signRequest.amnt = request.amnt;//保额
        //投保人信息
        signRequest.name = request.name;
        signRequest.cidtype = request.cidtype;
        signRequest.cid = request.cid;
        signRequest.sex = request.sex;//代码表见附录。
        signRequest.birth = request.birth;//格式：yyyy-mm-dd
        signRequest.mobile = request.mobile;
        signRequest.email = request.email;
        //被保人信息
        signRequest.insurerelation = request.insurerelation;//被保人与投保人关系 码表
        signRequest.insurename = request.insurename;
        signRequest.insurecidtype = request.insurecidtype;//被保人证件类型
        signRequest.insurecid = request.insurecid;
        signRequest.insuresex = request.insuresex;
        signRequest.insurebirth = request.insurebirth;
        //受益人信息
        signRequest.bnfrelation = request.bnfrelation;
        signRequest.bnfname = request.bnfname;
        signRequest.bnfcidtype = request.bnfcidtype;
        signRequest.bnfcid = request.bnfcid;
        signRequest.bnfsex = request.bnfsex;
        signRequest.bnfbirth = request.bnfbirth;
        //其他信息
        signRequest.agentno = "52589745";//业务员编码
        signRequest.agenttype = "AG";//业务员渠道
        signRequest.agentcomcode = "1";//业务员所属分公司
        SignInsureBean.PolicySpldatas policySpldata = new SignInsureBean.PolicySpldatas();
        policySpldata.psdt_value = "";
        policySpldata.psit_code = "";
        policySpldata.psit_desc = "";
        List<SignInsureBean.PolicySpldatas> policySpldatas = new ArrayList<>();
        policySpldatas.add(policySpldata);
        signRequest.policyspldatas = policySpldatas;
        //加解密处理,请求接口
        String requestData = JsonKit.bean2Json(signRequest);
        String orderData = EncryptUtil.getEncryptStr(key, requestData);
        //TODO 核保参数未凑齐
        SignInsureBean.UnionRequest unionRequest = new SignInsureBean.UnionRequest();
        unionRequest.order = orderData;
        unionRequest.appId = "公众账号id";
        unionRequest.wspTradeNo = "支付流水号";
        requestData = JsonKit.bean2Json(unionRequest);
        List<HeaderBean.Header> headerList = new ArrayList<>();
        BaseResponseBean interResponse = httpRequest(signInsureUrl, orgid + "|" + requestData, interName, JsonKit.bean2Json(headerList),"post");
        if (interResponse.code != 200) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败", response);
        }
        String result = interResponse.data.toString();
        result = EncryptUtil.getDecryptStr(key, result);
        if (!isJSONValid(result)) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
        }
        SignInsureBean.Response signResponse = JsonKit.json2Bean(result, SignInsureBean.Response.class);
        if (signResponse != null) {
            response.data = signResponse;
            if (signResponse.status == "0") {
                return json(BaseResponseBean.CODE_SUCCESS, interName + "成功", response);
            } else {
                return json(BaseResponseBean.CODE_SUCCESS, interName + "失败", response);
            }
        } else {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
        }
    }


    /**
     * 缴费
     * HTTP/POST请求
     *
     * @param httpServletRequest
     * @return
     */
    public String payInsure(HttpServletRequest httpServletRequest) {
        PayInsureBean.Requset request = JsonKit.json2Bean(HttpKit.readRequestBody(httpServletRequest), PayInsureBean.Requset.class);
        BaseResponseBean response = new BaseResponseBean();
        String interName = "缴费";
        if (request == null) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "参数解析失败", response);
        }
        PayInsureBean.Requset payRequest = new PayInsureBean.Requset();
        payRequest.appId = request.appId;
        payRequest.openId = request.openId;//用户在商户appId下的唯一标识	若tradeType=JSAPI，此参数必传。
        payRequest.tradeNo = request.tradeNo;//商户订单号	32位长度，7位(系统来源标识,包括渠道号+5位产品码)+yyyymmddhhmmssSSS+8位随机数字。
        payRequest.tradeType = request.tradeType;//交易类型	取值如下：JSAPI，NATIVE，APP，MWEB(H5支付类型),详细说明见参数规定。
        payRequest.goodsBody = request.goodsBody;//商品或支付单简要描述	示例：Ipad mini 16G 白色
        payRequest.goodsDetail = request.goodsDetail;//商品名称明细列表	示例：Ipad mini 16G 白色
        payRequest.totalFee = request.totalFee;//订单总金额	只能为整数，示例：888。
        payRequest.clientIp = request.clientIp;//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
        payRequest.timeStart = request.timeStart;//交易起始时间	格式：yyyyMMddHHmmss
        payRequest.timeExpire = request.timeExpire;//交易结束时间	格式：yyyyMMddHHmmss。timeExpire-timeStart必须大于5分钟。
        payRequest.goodsTag = request.goodsTag;//商品标记	代金券或立减优惠功能的参数，说明详见代金券或立减优惠
        payRequest.productId = request.productId;//商品ID	tradeType=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
        payRequest.successRedirectUrl = request.successRedirectUrl;//支付结果成功的页面跳转地址
        payRequest.failRedirectUrl = request.failRedirectUrl;//支付结果失败的页面跳转地址
        payRequest.cancelRedirectUrl = request.cancelRedirectUrl;//支付取消的页面跳转地址
        payRequest.notifyUrl = request.notifyUrl;//接收微信支付异步通知回调地址
        payRequest.limitPay = request.limitPay;//默认为支持信用卡支付，若需要禁止使用信用卡支付，limitPay=‘Y’
        payRequest.userCardId = request.userCardId;//支付人真实身份证件号	实名认证需要
        payRequest.userName = request.userName;//支付人真实姓名	实名认证需要

        Field[] fields = payRequest.getClass().getFields();
        List<String> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();

        for (Field field : fields) {
            if(field.isAccessible()){
                String name = field.getName();
                list.add(name);
                try {
                    Object  value = field.get(payRequest);
                    map.put(name,value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        Collections.sort(list);

        StringBuilder builder = new StringBuilder();
        boolean isInit = true;
        for (String s : list) {
            if(isInit){
                isInit = false;
                builder.append("&");
            }
            builder.append(s).append("=").append(map.getOrDefault(s,""));
        }
        String string = builder.toString();

        //TODO 加签处理
        //TODO 1.将Sign之外所有值非空的字段，外加字段key，按照字段名（字母部分全部小写）的ASCII码值排序；
        //TODO 2.将上述字段按照排列的顺序拼接成串，格式为key1=value1&key2=value2…
        //TODO 3.MD5加密转换后的串；
        //TODO 4.加密的串中的字母部分全部转成大写格式，得到的结果即为sign。

        Map<String, Object> requsetMap = new HashMap<String, Object>();
        requsetMap.put();

        String requsetSign = PaySignKit.getSign(requsetMap,key);
        payRequest.sign = requsetSign;//数字签名
        //设置header
        PayInsureBean.RequsetHeaders payRequestHeaders = new PayInsureBean.RequsetHeaders();
        payRequestHeaders.appId = request.appId;
        payRequestHeaders.payMode = "wx";
        payRequestHeaders.channel = "zx";
        String headerSign = MD5Kit.MD5Digest("appid="+request.appId+"&channel=zx&paymode=wx&key="+key);
        payRequestHeaders.sign = headerSign;//将appId、payMode及key拼接成如示例所示的串：appid=XXXX&channel=XXXX&paymode=XXXX&key=XXXX,然后经过MD5签名后所得的值


        return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
    }

    /**
     * 支付查询
     * HTTP/GET请求
     *
     * @param httpServletRequest
     * @return
     */
    public String payQuery(HttpServletRequest httpServletRequest) {
        PayQueryBean.Requset request = JsonKit.json2Bean(HttpKit.readRequestBody(httpServletRequest), PayQueryBean.Requset.class);
        BaseResponseBean response = new BaseResponseBean();
        String interName = "支付查询";
        if (request == null) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "参数解析失败", response);
        }
        PayQueryBean.Requset payQueryRequest = new PayQueryBean.Requset();

        return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
    }

    /**
     * 支付通知(回调)
     *
     * @param httpServletRequest
     * @return
     */
    public String payCallBack(HttpServletRequest httpServletRequest) {
        PayCallBackBean.Request request = JsonKit.json2Bean(HttpKit.readRequestBody(httpServletRequest), PayCallBackBean.Request.class);
        BaseResponseBean response = new BaseResponseBean();
        String interName = "支付通知";
        if (request == null) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "参数解析失败", response);
        }
        PayCallBackBean.Request payCallBackRequest = new PayCallBackBean.Request();
        payCallBackRequest.tradeNo = request.tradeNo;//交易流水号
        payCallBackRequest.wspTradeNo = request.wspTradeNo;//支付流水号,81位字符串
        payCallBackRequest.openId = request.openId;//用户的访问ID,公众号支付时用的openid
        payCallBackRequest.tradeState = request.tradeState;//交易结果,SUCCESS表示成功,否则返回其他值。
        payCallBackRequest.transactionId = request.transactionId;//微信支付订单号,当支付状态为“SUCCESS”时，返回支付订单号
        payCallBackRequest.payTime = request.payTime;//成功支付时间	当支付状态为“SUCCESS”时，返回支付时间
        payCallBackRequest.sign = request.sign;//签名串,sign值规则见统一下单接口
        //TODO 处理通知逻辑
        PayCallBackBean.Response payCallBackResponse = new PayCallBackBean.Response();
        payCallBackResponse.rspCode = "100";
        payCallBackResponse.rspDesc = "处理失败";
        response.data = payCallBackResponse;
        return json(BaseResponseBean.CODE_FAILURE, interName + "接口完善中。。。", response);
    }

}
