package com.inschos.product.taikang.access.rpc.action;

import com.inschos.dock.api.InsureService;
import com.inschos.dock.bean.*;
import com.inschos.product.taikang.access.http.controller.bean.BaseResponseBean;
import com.inschos.product.taikang.access.rpc.bean.BuyInsureBean;
import com.inschos.product.taikang.access.rpc.bean.CancelInsureBean;
import com.inschos.product.taikang.access.rpc.bean.PayInsureBean;
import com.inschos.product.taikang.access.rpc.service.IntersService;
import com.inschos.product.taikang.annotation.CheckParamsKit;
import com.inschos.product.taikang.assist.kit.HttpClientKit;
import com.inschos.product.taikang.assist.kit.JsonKit;
import com.inschos.product.taikang.assist.kit.encryptUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.inschos.product.taikang.access.rpc.bean.CommonBean.*;

@Service
public class IntersAction implements IntersService,InsureService {

    private static final Logger logger = Logger.getLogger(IntersAction.class);

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
        if (interName == null) {
            interName = "";
        }
        try {
            String result = HttpClientKit.post(url, JsonKit.bean2Json(json));
            if (result == null) {
                return json(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败", response);
            }
            if (!JsonKit.isJSONValid(result)) {
                return json(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
            }
            response = JsonKit.json2Bean(result, BaseResponseBean.class);
            if (response.code != 200 || response.code == 500) {
                return json(BaseResponseBean.CODE_FAILURE, interName + "接口服务请求失败", response);
            }
            return json(BaseResponseBean.CODE_SUCCESS, interName + "接口请求成功", response);
        } catch (IOException e) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败", response);
        }
    }

    /**
     * 预投保
     *
     * @param requset
     * @return
     */
    @Override
    public RpcResponse<RspPreInsureBean> preInsure(InsureBean requset) {
        return null;
    }

    /**
     * 核保
     *
     * @param requset
     * @return
     */
    @Override
    public  RpcResponse<RspInsureBean> insure(InsureBean requset) {
        RpcResponse response = new RpcResponse();
        String interName = "核保接口";
        if(requset==null){
            response.code = 500;
            response.message = interName + "参数解析失败";
            return response;
        }
        BuyInsureBean.Requset buyRequest = new BuyInsureBean.Requset();
        buyRequest.orderno = requset.warrantyUuid;//订单信息,订单号小于等于20位
        buyRequest.orderfrom = "";//订单来源
        buyRequest.Plancode = requset.productCode;//产品编码
        buyRequest.orgid = testOrgid;//系统编码
        buyRequest.startdate = requset.startTime;//生效时间
        buyRequest.applydate = requset.tradeTime+"";//投保时间
        buyRequest.period = requset.insurePeriod+"";//保险期间
        buyRequest.premium = requset.premium;//保费
        buyRequest.amnt = requset.amount;//保额
        //投保人信息
        buyRequest.name = requset.policyholder.name;
        buyRequest.cidtype = requset.policyholder.cardType+"";
        buyRequest.cid = requset.policyholder.cardCode;
        buyRequest.sex = requset.policyholder.sex+"";//代码表见附录。
        buyRequest.birth = requset.policyholder.birthday;//格式：yyyy-mm-dd
        buyRequest.mobile = requset.policyholder.phone;
        buyRequest.email = requset.policyholder.email;
        //被保人信息
        PersonBean personBean = new PersonBean();
        for (PersonBean recognizee : requset.recognizees) {
            personBean.relation = recognizee.relation;//被保人与投保人关系 码表
            personBean.name = recognizee.name;
            personBean.cardType = recognizee.cardType;//被保人证件类型
            personBean.cardCode = recognizee.cardCode;
            personBean.sex = recognizee.sex;
            personBean.birthday = recognizee.birthday;
        }
        buyRequest.insurerelation = personBean.relation+"";//被保人与投保人关系 码表
        buyRequest.insurename = personBean.name;
        buyRequest.insurecidtype = personBean.cardType+"";//被保人证件类型
        buyRequest.insurecid = personBean.cardCode;
        buyRequest.insuresex = personBean.sex+"";
        buyRequest.insurebirth = personBean.birthday;
        //受益人信息
        buyRequest.bnfrelation = requset.beneficiary.relation+"";
        buyRequest.bnfname = requset.beneficiary.name;
        buyRequest.bnfcidtype = requset.beneficiary.cardType+"";
        buyRequest.bnfcid = requset.beneficiary.cardCode;
        buyRequest.bnfsex = requset.beneficiary.sex+"";
        buyRequest.bnfbirth = requset.beneficiary.birthday;
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
        String data = encryptUtil.getEncryptStr(testOrgid,JsonKit.bean2Json(buyRequest));
        BaseResponseBean interResponse =  httpRequest(insureUrl, data, interName);
        BuyInsureBean.Response buyResponse = new BuyInsureBean.Response();
        return response;
    }

    /**
     * 缴费
     *
     * @param requset
     * @return
     */
    @Override
    public RpcResponse<RspPayBean> pay(PayBean requset) {
        return null;
    }

    /**
     * 承保
     *
     * @param requset
     * @return
     */
    @Override
    public RpcResponse<RspIssueBean> issue(IssueBean requset) {
        return null;
    }

    /**
     * 撤保
     *
     * @param requset
     * @return
     */
    @Override
    public BaseResponseBean cancelInsure(CancelInsureBean.Requset requset) {
        return null;
    }

}
