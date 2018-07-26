package com.inschos.product.taikang.access.http.controller.action;

import com.inschos.common.assist.kit.CharsetConvertKit;
import com.inschos.common.assist.kit.HttpClientKit;
import com.inschos.common.assist.kit.HttpKit;
import com.inschos.common.assist.kit.JsonKit;
import com.inschos.product.taikang.access.http.controller.bean.*;
import com.inschos.product.taikang.assist.kit.ByteKit;
import com.inschos.product.taikang.assist.kit.RSAUtil;
import com.inschos.product.taikang.assist.kit.encryptUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

import static com.inschos.product.taikang.access.http.controller.bean.CommonBean.*;
import static com.inschos.product.taikang.access.http.controller.bean.RsaEncryBean.*;

@Component
public class IntersAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(IntersAction.class);

    /**
     * http请求公共函数
     *
     * @param url       请求地址
     * @param json      请求报文,格式是json
     * @param interName 接口名称
     * @return String
     */
    private BaseResponseBean httpRequest(String url, String json, String interName) {
        BaseResponseBean response = new BaseResponseBean();
        if (interName == null) {
            interName = "";
        }
        try {
            logger.info(interName + "接口请求地址：" + url);
            logger.info(interName + "接口请求参数：" + json);
            String result = HttpClientKit.post(url, JsonKit.bean2Json(json));
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
        String data = encryptUtil.getEncryptStr(key, requestData);
        BaseResponseBean interResponse = httpRequest(checkInsureUrl, orgid + "|" + data, interName);
        if (interResponse.code != 200) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败", response);
        }
        String result = interResponse.data.toString();
        result = encryptUtil.getDecryptStr(key, result);
        if (!isJSONValid(result)) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
        }
        BuyInsureBean.Response buyResponse = JsonKit.json2Bean(result, BuyInsureBean.Response.class);
        if (buyResponse != null) {
            response.data = buyResponse;
            if (buyResponse.status == "0") {
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
        payRequest.businessType = request.businessType;
        payRequest.cmsSystemSource = request.cmsSystemSource;
        payRequest.cmsBusinessType = request.cmsBusinessType;
        payRequest.cmsVersion = request.cmsVersion;
        payRequest.cmsFormat = request.cmsFormat;
        payRequest.cmsPayChannel = request.cmsPayChannel;
        payRequest.cmsPayTag = request.cmsPayTag;
        payRequest.orderId = request.orderId;
        payRequest.transactionId = request.transactionId;
        payRequest.requestDateTime = request.requestDateTime;
        payRequest.currencyCode = request.currencyCode;
        payRequest.money = request.money;
        payRequest.summary = request.summary;
        payRequest.isCheck = request.isCheck;
        payRequest.accountName = request.accountName;
        payRequest.certificateType = request.certificateType;
        payRequest.certificateNo = request.certificateNo;
        payRequest.mobile = request.mobile;
        payRequest.isLimitCreditPay = "0";
        payRequest.openId = "";
        payRequest.deviceInfo = request.deviceInfo;//应用类型
        payRequest.mchAppId = request.mchAppId;//应用标识(H5支付必填)
        payRequest.mchAppName = request.mchAppName;//应用名(H5支付必填)
        payRequest.isRaw = "";
        payRequest.successURL = request.successURL;
        payRequest.failURL = "";
        //编码转换,加解密处理,请求接口
        RSAUtil rsaUtil = new RSAUtil();
        ByteKit byteKit = new ByteKit();
        RSAPublicKey recoveryPubKey = rsaUtil.generateRSAPublicKey(pubModBytes, pubPubExpBytes);
        RSAPrivateKey recoveryPriKey = rsaUtil.generateRSAPrivateKey(priModBytes, priPriExpBytes);
        String json = JsonKit.bean2Json(payRequest);
        String gbk = CharsetConvertKit.utf82gbk(json);
        byte[] encryptData = rsaUtil.encrypt(recoveryPriKey, gbk.getBytes());
        String data = null;
        try {
            data = new String(encryptData, "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
        }
        BaseResponseBean interResponse = httpRequest(payInsureUrl, data, interName);
        if (interResponse.code != 200) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败", response);
        }
        Object result = interResponse.data;
        byte[] returnResponse = byteKit.toByteArray(result);
        byte[] decryptData = rsaUtil.decrypt(recoveryPubKey, returnResponse);
        Object responseData = byteKit.toObject(decryptData);
        if (responseData == null) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
        }
        PayInsureBean.Response payResponse = JsonKit.json2Bean(JsonKit.bean2Json(responseData), PayInsureBean.Response.class);
        if (payResponse != null) {
            response.data = payResponse;
            if (payResponse.responseCode == "3") {
                return json(BaseResponseBean.CODE_SUCCESS, interName + "处理中", response);
            } else if (payResponse.responseCode == "4") {
                return json(BaseResponseBean.CODE_SUCCESS, interName + "处理成功", response);
            } else if (payResponse.responseCode == "5") {
                return json(BaseResponseBean.CODE_FAILURE, interName + "处理失败", response);
            }
        }
        return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
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
        //TODO======================================支付查询信息=============================================
        PayQueryBean.Requset payQueryRequest = new PayQueryBean.Requset();
        payQueryRequest.businessType = request.businessType;//业务系列	个险，团险，银保
        payQueryRequest.cmsSystemSource = request.cmsSystemSource;//来源系统
        payQueryRequest.cmsPayTag = request.cmsPayTag;//支付方式
        payQueryRequest.cmsPayChannel = request.cmsPayChannel;//支付渠道,101：微信支付 102：支付宝支付
        payQueryRequest.requestDateTime = request.requestDateTime;//提交时间	,YYYY-MM-DD HH:MM:SS
        payQueryRequest.cmsVersion = version;//调用接口版本	1.0
        payQueryRequest.cmsFormat = format;//传输参数格式	JSON
        PayQueryBean.QueryList queryList = new PayQueryBean.QueryList();
        queryList.sapCompanyCode = "";
        queryList.transactionId = "";
        List<PayQueryBean.QueryList> queryLists = new ArrayList<>();
        queryLists.add(queryList);
        payQueryRequest.queryList = queryLists;//查询集合
        RSAUtil rsaUtil = new RSAUtil();
        ByteKit byteKit = new ByteKit();
        RSAPublicKey recoveryPubKey = rsaUtil.generateRSAPublicKey(pubModBytes, pubPubExpBytes);
        RSAPrivateKey recoveryPriKey = rsaUtil.generateRSAPrivateKey(priModBytes, priPriExpBytes);
        String json = JsonKit.bean2Json(payQueryRequest);
        String gbk = CharsetConvertKit.utf82gbk(json);
        byte[] encryptData = rsaUtil.encrypt(recoveryPriKey, gbk.getBytes());
        String payQueryData = null;
        try {
            payQueryData = new String(encryptData, "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return json(BaseResponseBean.CODE_FAILURE, interName + "参数处理失败", response);
        }
        //TODO======================================订单信息=============================================
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
        String orderData = encryptUtil.getEncryptStr(key, requestData);
        SignInsureBean.UnionRequest unionRequest = new SignInsureBean.UnionRequest();
        unionRequest.order = orderData;
        unionRequest.paymentQuery = payQueryData;
        requestData = JsonKit.bean2Json(unionRequest);
        BaseResponseBean interResponse = httpRequest(signInsureUrl, orgid + "|" + requestData, interName);
        if (interResponse.code != 200) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败", response);
        }
        String result = interResponse.data.toString();
        result = encryptUtil.getDecryptStr(key, result);
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
     * 支付查询
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
        payQueryRequest.businessType = request.businessType;//业务系列	个险，团险，银保
        payQueryRequest.cmsSystemSource = request.cmsSystemSource;//来源系统
        payQueryRequest.cmsPayTag = request.cmsPayTag;//支付方式
        payQueryRequest.cmsPayChannel = request.cmsPayChannel;//支付渠道,101：微信支付 102：支付宝支付
        payQueryRequest.requestDateTime = request.requestDateTime;//提交时间	,YYYY-MM-DD HH:MM:SS
        payQueryRequest.cmsVersion = version;//调用接口版本	1.0
        payQueryRequest.cmsFormat = format;//传输参数格式	JSON
        PayQueryBean.QueryList queryList = new PayQueryBean.QueryList();
        queryList.sapCompanyCode = "";
        queryList.transactionId = "";
        List<PayQueryBean.QueryList> queryLists = new ArrayList<>();
        queryLists.add(queryList);
        payQueryRequest.queryList = queryLists;//查询集合
        //编码转换,加解密处理,请求接口
        RSAUtil rsaUtil = new RSAUtil();
        ByteKit byteKit = new ByteKit();
        RSAPublicKey recoveryPubKey = rsaUtil.generateRSAPublicKey(pubModBytes, pubPubExpBytes);
        RSAPrivateKey recoveryPriKey = rsaUtil.generateRSAPrivateKey(priModBytes, priPriExpBytes);
        String json = JsonKit.bean2Json(payQueryRequest);
        String gbk = CharsetConvertKit.utf82gbk(json);
        byte[] encryptData = rsaUtil.encrypt(recoveryPriKey, gbk.getBytes());
        String data = null;
        try {
            data = new String(encryptData, "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logger.info("请求数据" + data);
        BaseResponseBean interResponse = httpRequest(payQueryUrl, data, interName);
        logger.info("返回数据" + JsonKit.bean2Json(interResponse));
        if (interResponse.code != 200) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败", response);
        }
        Object result = interResponse.data;
        byte[] returnResponse = byteKit.toByteArray(result);
        byte[] decryptData = rsaUtil.decrypt(recoveryPubKey, returnResponse);
        Object responseData = byteKit.toObject(decryptData);
        if (responseData == null) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
        }
        PayQueryBean.Response payQueryResponse = JsonKit.json2Bean(JsonKit.bean2Json(responseData), PayQueryBean.Response.class);
        if (payQueryResponse != null) {
            response.data = payQueryResponse;
            if (payQueryResponse.responseCode == "3") {
                return json(BaseResponseBean.CODE_SUCCESS, interName + "处理中", response);
            } else if (payQueryResponse.responseCode == "4") {
                return json(BaseResponseBean.CODE_SUCCESS, interName + "处理成功", response);
            } else if (payQueryResponse.responseCode == "5") {
                return json(BaseResponseBean.CODE_FAILURE, interName + "处理失败", response);
            }
        }
        return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
    }

    /**
     * 支付通知(回调)
     *
     * @param httpServletRequest
     * @return
     */
    public String payCallBack(HttpServletRequest httpServletRequest) {
        PayCallBackBean.Response request = JsonKit.json2Bean(HttpKit.readRequestBody(httpServletRequest), PayCallBackBean.Response.class);
        BaseResponseBean response = new BaseResponseBean();
        String interName = "支付通知";
        if (request == null) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "参数解析失败", response);
        }
        PayCallBackBean.Response payCallBackResponse = new PayCallBackBean.Response();
        payCallBackResponse.responseCode = request.responseCode;//受理返回码:3处理中,4处理成功,5处理失败
        payCallBackResponse.responseMessage = request.responseMessage;//返回描述信息,获取支付串成功失败的描述
        payCallBackResponse.transTime = request.transTime;//交易返回时间,yyyy-MM-dd HH:mm:ss
        payCallBackResponse.sapCompanyCode = request.sapCompanyCode;//收款公司
        payCallBackResponse.orderId = request.orderId;//商户订单号
        payCallBackResponse.transactionId = request.transactionId;//交易流水号
        payCallBackResponse.payTransactionId = request.payTransactionId;//支付平台交易流水号
        payCallBackResponse.channelTransactionId = request.channelTransactionId;//渠道交易流水号
        payCallBackResponse.mobileTransactionId = request.mobileTransactionId;//微信支付宝交易流水号
        payCallBackResponse.money = request.money;//交易金额,单位：分
        payCallBackResponse.accountNo = request.accountNo;//收付款账号,泰康银行卡号
        payCallBackResponse.outBankCode = request.outBankCode;//集中码
        payCallBackResponse.sapSubjectCode = request.sapSubjectCode;//科目号
        payCallBackResponse.openId = request.openId;//用户的访问ID,公众号支付时用的openid
        PayCallBackBean.BaseResponse baseResponse = new PayCallBackBean.BaseResponse();
        baseResponse.requestDateTime = "";
        baseResponse.responseCode = "";
        baseResponse.responseMessage = "";
        payCallBackResponse.baseResponse = baseResponse;//返回json子对象
        return json(BaseResponseBean.CODE_FAILURE, interName + "接口完善中。。。", response);
    }

    /**
     * 交易退款
     *
     * @param httpServletRequest
     * @return
     */
    public String payRefund(HttpServletRequest httpServletRequest) {
        PayRefundBean.Requset request = JsonKit.json2Bean(HttpKit.readRequestBody(httpServletRequest), PayRefundBean.Requset.class);
        BaseResponseBean response = new BaseResponseBean();
        String interName = "交易退款";
        if (request == null) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "参数解析失败", response);
        }
        PayRefundBean.Requset payRefundRequest = new PayRefundBean.Requset();
        payRefundRequest.businessType = request.businessType;//业务系列	个险，团险，银保
        payRefundRequest.cmsSystemSource = request.cmsSystemSource;//来源系统
        payRefundRequest.cmsPayTag = request.cmsPayTag;//支付方式
        payRefundRequest.cmsPayChannel = request.cmsPayChannel;//支付渠道,101：微信支付 102：支付宝支付
        payRefundRequest.requestDateTime = request.requestDateTime;//提交时间	,YYYY-MM-DD HH:MM:SS
        payRefundRequest.cmsVersion = version;//调用接口版本	1.0
        payRefundRequest.cmsFormat = format;//传输参数格式	JSON
        payRefundRequest.cmsBusinessType = request.cmsBusinessType;//业务类型
        payRefundRequest.orderId = request.orderId;//商户订单号
        payRefundRequest.money = request.money;//提交金额,本次退款金额,单位：分
        payRefundRequest.sapCompanyCode = request.sapCompanyCode;//收款公司
        payRefundRequest.transactionId = request.transactionId;//交易流水号
        payRefundRequest.payTransactionId = request.payTransactionId;//支付平台交易流水号
        payRefundRequest.channelTransactionId = request.channelTransactionId;//渠道交易流水号
        payRefundRequest.mobileTransactionId = request.mobileTransactionId;//微信支付宝交易流水号
        payRefundRequest.currencyCode = request.currencyCode;//币种,CNY
        payRefundRequest.totalManey = request.totalManey;//交易总金额,收款时对应的订单金额，单位：分
        payRefundRequest.summary = request.summary;//备注,商品交易信息描述
        //编码转换,加解密处理,请求接口
        RSAUtil rsaUtil = new RSAUtil();
        ByteKit byteKit = new ByteKit();
        RSAPublicKey recoveryPubKey = rsaUtil.generateRSAPublicKey(pubModBytes, pubPubExpBytes);
        RSAPrivateKey recoveryPriKey = rsaUtil.generateRSAPrivateKey(priModBytes, priPriExpBytes);
        String json = JsonKit.bean2Json(payRefundRequest);
        String gbk = CharsetConvertKit.utf82gbk(json);
        byte[] encryptData = rsaUtil.encrypt(recoveryPriKey, gbk.getBytes());
        String data = null;
        try {
            data = new String(encryptData, "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logger.info("请求数据" + data);
        BaseResponseBean interResponse = httpRequest(payQueryUrl, data, interName);
        logger.info("返回数据" + JsonKit.bean2Json(interResponse));
        if (interResponse.code != 200) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败", response);
        }
        Object result = interResponse.data;
        byte[] returnResponse = byteKit.toByteArray(result);
        byte[] decryptData = rsaUtil.decrypt(recoveryPubKey, returnResponse);
        Object responseData = byteKit.toObject(decryptData);
        if (responseData == null) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
        }
        PayRefundBean.Response payRefundResponse = JsonKit.json2Bean(JsonKit.bean2Json(responseData), PayRefundBean.Response.class);
        if (payRefundResponse != null) {
            response.data = payRefundResponse;
            if (payRefundResponse.responseCode == "3") {
                return json(BaseResponseBean.CODE_SUCCESS, interName + "处理中", response);
            } else if (payRefundResponse.responseCode == "4") {
                return json(BaseResponseBean.CODE_SUCCESS, interName + "处理成功", response);
            } else if (payRefundResponse.responseCode == "5") {
                return json(BaseResponseBean.CODE_FAILURE, interName + "处理失败", response);
            }
        }
        return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
    }


    /**
     * 交易对账
     *
     * @param httpServletRequest
     * @return
     */
    public String payBill(HttpServletRequest httpServletRequest) {
        PayBillBean.Requset request = JsonKit.json2Bean(HttpKit.readRequestBody(httpServletRequest), PayBillBean.Requset.class);
        BaseResponseBean response = new BaseResponseBean();
        String interName = "交易对账";
        if (request == null) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "参数解析失败", response);
        }
        PayBillBean.Requset payQueryRequest = new PayBillBean.Requset();
        payQueryRequest.businessType = request.businessType;//业务系列	个险，团险，银保
        payQueryRequest.cmsSystemSource = request.cmsSystemSource;//来源系统
        payQueryRequest.billDate = request.billDate;//对账日期
        //编码转换,加解密处理,请求接口
        RSAUtil rsaUtil = new RSAUtil();
        ByteKit byteKit = new ByteKit();
        RSAPublicKey recoveryPubKey = rsaUtil.generateRSAPublicKey(pubModBytes, pubPubExpBytes);
        RSAPrivateKey recoveryPriKey = rsaUtil.generateRSAPrivateKey(priModBytes, priPriExpBytes);
        String json = JsonKit.bean2Json(payQueryRequest);
        String gbk = CharsetConvertKit.utf82gbk(json);
        byte[] encryptData = rsaUtil.encrypt(recoveryPriKey, gbk.getBytes());
        String data = null;
        try {
            data = new String(encryptData, "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logger.info("请求数据" + data);
        BaseResponseBean interResponse = httpRequest(payQueryUrl, data, interName);
        logger.info("返回数据" + JsonKit.bean2Json(interResponse));
        if (interResponse.code != 200) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败", response);
        }
        Object result = interResponse.data;
        byte[] returnResponse = byteKit.toByteArray(result);
        byte[] decryptData = rsaUtil.decrypt(recoveryPubKey, returnResponse);
        Object responseData = byteKit.toObject(decryptData);
        if (responseData == null) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
        }
        PayBillBean.Response paybillResponse = JsonKit.json2Bean(JsonKit.bean2Json(responseData), PayBillBean.Response.class);
        if (paybillResponse != null) {
            response.data = paybillResponse;
            if (paybillResponse.responseCode == "3") {
                return json(BaseResponseBean.CODE_SUCCESS, interName + "处理中", response);
            } else if (paybillResponse.responseCode == "4") {
                return json(BaseResponseBean.CODE_SUCCESS, interName + "处理成功", response);
            } else if (paybillResponse.responseCode == "5") {
                return json(BaseResponseBean.CODE_FAILURE, interName + "处理失败", response);
            }
        }
        return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
    }


}
