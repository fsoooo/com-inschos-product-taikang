package com.inschos.product.taikang.access.rpc.impl;

import com.inschos.common.assist.kit.JsonKit;
import com.inschos.dock.api.InsureService;
import com.inschos.dock.bean.*;
import com.inschos.product.taikang.access.http.controller.bean.BaseResponseBean;
import com.inschos.product.taikang.access.rpc.bean.BuyInsureBean;
import com.inschos.product.taikang.access.rpc.bean.PayInsureBean;
import com.inschos.product.taikang.access.rpc.service.IntersService;
import com.inschos.product.taikang.annotation.CheckParamsKit;
import com.inschos.product.taikang.assist.kit.encryptUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.inschos.product.taikang.access.rpc.bean.CommonBean.insureUrl;
import static com.inschos.product.taikang.access.rpc.bean.CommonBean.testOrgid;

@Service
public class IntersImpl implements IntersService, InsureService {

    private static final Logger logger = Logger.getLogger(IntersImpl.class);

    private BaseResponseBean json(int code, String message, BaseResponseBean response) {
        if (response == null) {
            response = new BaseResponseBean();
        }
        response.code = code;
        CheckParamsKit.Entry<String, String> defaultEntry = CheckParamsKit.getDefaultEntry();
        defaultEntry.details = message;
        List<CheckParamsKit.Entry<String, String>> list = new ArrayList<>();
        list.add(defaultEntry);
        response.message = list;
        return response;
    }

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
        return json(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败", response);
    }

    /**
     * 预投保
     *
     * @param request
     * @return
     */
    @Override
    public RpcResponse<RspPreInsureBean> preInsure(InsureBean request) {
        return null;
    }

    /**
     * 核保
     *
     * @param request
     * @return
     */
    @Override
    public RpcResponse<RspInsureBean> insure(InsureBean request) {
        RpcResponse response = new RpcResponse();
        String interName = "核保接口";
        if (request == null) {
            response.code = 500;
            response.message = interName + "参数解析失败";
            return response;
        }
        BuyInsureBean.Requset buyRequest = new BuyInsureBean.Requset();
        buyRequest.orderno = request.warrantyUuid;//订单信息,订单号小于等于20位
        buyRequest.orderfrom = "";//订单来源
        buyRequest.Plancode = request.productCode;//产品编码
        buyRequest.orgid = testOrgid;//系统编码
        buyRequest.startdate = request.startTime;//生效时间
        buyRequest.applydate = request.tradeTime + "";//投保时间
        buyRequest.period = request.insurePeriod + "";//保险期间
        buyRequest.premium = request.premium;//保费
        buyRequest.amnt = request.amount;//保额
        //投保人信息
        buyRequest.name = request.policyholder.name;
        buyRequest.cidtype = request.policyholder.cardType + "";
        buyRequest.cid = request.policyholder.cardCode;
        buyRequest.sex = request.policyholder.sex + "";//代码表见附录。
        buyRequest.birth = request.policyholder.birthday;//格式：yyyy-mm-dd
        buyRequest.mobile = request.policyholder.phone;
        buyRequest.email = request.policyholder.email;
        //被保人信息
        PersonBean personBean = new PersonBean();
        for (PersonBean recognizee : request.recognizees) {
            personBean.relation = recognizee.relation;//被保人与投保人关系 码表
            personBean.name = recognizee.name;
            personBean.cardType = recognizee.cardType;//被保人证件类型
            personBean.cardCode = recognizee.cardCode;
            personBean.sex = recognizee.sex;
            personBean.birthday = recognizee.birthday;
        }
        buyRequest.insurerelation = personBean.relation + "";//被保人与投保人关系 码表
        buyRequest.insurename = personBean.name;
        buyRequest.insurecidtype = personBean.cardType + "";//被保人证件类型
        buyRequest.insurecid = personBean.cardCode;
        buyRequest.insuresex = personBean.sex + "";
        buyRequest.insurebirth = personBean.birthday;
        //受益人信息
        buyRequest.bnfrelation = request.beneficiary.relation + "";
        buyRequest.bnfname = request.beneficiary.name;
        buyRequest.bnfcidtype = request.beneficiary.cardType + "";
        buyRequest.bnfcid = request.beneficiary.cardCode;
        buyRequest.bnfsex = request.beneficiary.sex + "";
        buyRequest.bnfbirth = request.beneficiary.birthday;
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
        String data = encryptUtil.getEncryptStr(testOrgid, JsonKit.bean2Json(buyRequest));
        BaseResponseBean interResponse = httpRequest(insureUrl, data, interName);
        BuyInsureBean.Response buyResponse = new BuyInsureBean.Response();
        return response;
    }

    /**
     * 缴费
     *
     * @param request
     * @return
     */
    @Override
    public RpcResponse<RspPayBean> pay(PayBean request) {
        RpcResponse response = new RpcResponse();
        String interName = "缴费接口";
        if (request == null) {
            response.code = 500;
            response.message = interName + "参数解析失败";
            return response;
        }
        PayInsureBean.Requset payRequest = new PayInsureBean.Requset();
        payRequest.orderno = request.aplNo;
        payRequest.orgid = testOrgid;
        payRequest.policyno = request.aplNo;
        String data = encryptUtil.getEncryptStr(testOrgid, JsonKit.bean2Json(payRequest));
        BaseResponseBean interResponse = httpRequest(insureUrl, data, interName);
        PayInsureBean.Response buyResponse = new PayInsureBean.Response();
        return response;
    }

    /**
     * 承保(请求参数同核保)
     *
     * @param request
     * @return
     */
    @Override
    public RpcResponse<RspIssueBean> issue(IssueBean request) {
        return null;
    }

    /**
     * 承保
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponseBean signInsure(InsureBean request) {
        BaseResponseBean response = new BaseResponseBean();
        String interName = "承保接口";
        if (request == null) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "参数解析失败", response);
        }
        BuyInsureBean.Requset buyRequest = new BuyInsureBean.Requset();
        buyRequest.orderno = request.warrantyUuid;//订单信息,订单号小于等于20位
        buyRequest.orderfrom = "";//订单来源
        buyRequest.Plancode = request.productCode;//产品编码
        buyRequest.orgid = testOrgid;//系统编码
        buyRequest.startdate = request.startTime;//生效时间
        buyRequest.applydate = request.tradeTime + "";//投保时间
        buyRequest.period = request.insurePeriod + "";//保险期间
        buyRequest.premium = request.premium;//保费
        buyRequest.amnt = request.amount;//保额
        //投保人信息
        buyRequest.name = request.policyholder.name;
        buyRequest.cidtype = request.policyholder.cardType + "";
        buyRequest.cid = request.policyholder.cardCode;
        buyRequest.sex = request.policyholder.sex + "";//代码表见附录。
        buyRequest.birth = request.policyholder.birthday;//格式：yyyy-mm-dd
        buyRequest.mobile = request.policyholder.phone;
        buyRequest.email = request.policyholder.email;
        //被保人信息
        PersonBean personBean = new PersonBean();
        for (PersonBean recognizee : request.recognizees) {
            personBean.relation = recognizee.relation;//被保人与投保人关系 码表
            personBean.name = recognizee.name;
            personBean.cardType = recognizee.cardType;//被保人证件类型
            personBean.cardCode = recognizee.cardCode;
            personBean.sex = recognizee.sex;
            personBean.birthday = recognizee.birthday;
        }
        buyRequest.insurerelation = personBean.relation + "";//被保人与投保人关系 码表
        buyRequest.insurename = personBean.name;
        buyRequest.insurecidtype = personBean.cardType + "";//被保人证件类型
        buyRequest.insurecid = personBean.cardCode;
        buyRequest.insuresex = personBean.sex + "";
        buyRequest.insurebirth = personBean.birthday;
        //受益人信息
        buyRequest.bnfrelation = request.beneficiary.relation + "";
        buyRequest.bnfname = request.beneficiary.name;
        buyRequest.bnfcidtype = request.beneficiary.cardType + "";
        buyRequest.bnfcid = request.beneficiary.cardCode;
        buyRequest.bnfsex = request.beneficiary.sex + "";
        buyRequest.bnfbirth = request.beneficiary.birthday;
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
        String data = encryptUtil.getEncryptStr(testOrgid, JsonKit.bean2Json(buyRequest));
        BaseResponseBean interResponse = httpRequest(insureUrl, data, interName);
        BuyInsureBean.Response buyResponse = new BuyInsureBean.Response();
        return response;
    }

}
