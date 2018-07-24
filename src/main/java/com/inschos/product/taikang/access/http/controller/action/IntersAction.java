package com.inschos.product.taikang.access.http.controller.action;

import com.inschos.product.taikang.access.http.controller.bean.BaseResponseBean;
import com.inschos.product.taikang.access.http.controller.bean.BuyInsureBean;
import com.inschos.product.taikang.access.http.controller.bean.PayInsureBean;
import com.inschos.product.taikang.assist.kit.HttpClientKit;
import com.inschos.product.taikang.assist.kit.HttpKit;
import com.inschos.product.taikang.assist.kit.JsonKit;
import com.inschos.product.taikang.assist.kit.encryptUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                return responseBean(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败", response);
            }
            if (!isJSONValid(result)) {
                return responseBean(BaseResponseBean.CODE_FAILURE, interName + "接口返回报文解析失败", response);
            }
            response = JsonKit.json2Bean(result, BaseResponseBean.class);
            if (response.code != 200 || response.code == 500) {
                return responseBean(BaseResponseBean.CODE_FAILURE, interName + "接口服务请求失败", response);
            }
            return responseBean(BaseResponseBean.CODE_SUCCESS, interName + "接口请求成功", response);
        } catch (IOException e) {
            return responseBean(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败", response);
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
        String interName = "核保接口";
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
        String data = encryptUtil.getEncryptStr(orgid+"", JsonKit.bean2Json(buyRequest));
        BaseResponseBean interResponse = httpRequest(insureRequestUrl, data, interName);
        BuyInsureBean.Response buyResponse = new BuyInsureBean.Response();
        return json(BaseResponseBean.CODE_FAILURE, "接口完善中。。。", response);
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
        String interName = "缴费接口";
        if (request == null) {
            return json(BaseResponseBean.CODE_FAILURE, interName + "参数解析失败", response);
        }
        PayInsureBean.Requset payRequest = new PayInsureBean.Requset();
        payRequest.orderno = request.orderno;
        payRequest.orgid = orgid;
        payRequest.policyno = request.policyno;
        String data = encryptUtil.getEncryptStr(orgid+"", JsonKit.bean2Json(payRequest));
        BaseResponseBean interResponse = httpRequest(insureRequestUrl, data, interName);
        PayInsureBean.Response buyResponse = new PayInsureBean.Response();
        return json(BaseResponseBean.CODE_FAILURE, "接口完善中。。。", response);
    }

    /**
     * 承保
     *
     * @param httpServletRequest
     * @return
     */
    public String buyInsure(HttpServletRequest httpServletRequest) {
        BuyInsureBean.Requset request = JsonKit.json2Bean(HttpKit.readRequestBody(httpServletRequest), BuyInsureBean.Requset.class);
        BaseResponseBean response = new BaseResponseBean();
        return json(BaseResponseBean.CODE_FAILURE, "接口完善中。。。", response);
    }


}
