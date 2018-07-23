package com.inschos.product.taikang.access.http.controller.action;

import com.inschos.product.taikang.access.http.controller.bean.ActionBean;
import com.inschos.product.taikang.access.http.controller.bean.BaseResponseBean;
import com.inschos.product.taikang.assist.kit.HttpClientKit;
import com.inschos.product.taikang.assist.kit.JsonKit;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
    private String httpRequest(String url, String json, String interName) {
        BaseResponseBean response = new BaseResponseBean();
        if (interName == null) {
            interName = "";
        }
        try {
            String result = HttpClientKit.post(url, JsonKit.bean2Json(json));
            if (result == null) {
                return json(BaseResponseBean.CODE_FAILURE, interName + "接口请求失败", response);
            }
            if (!isJSONValid(result)) {
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
     * 请求接口公共参数
     * @return
     */
    private String requestComment(){
        return "请求接口公共参数";
    }

    /**
     * 核保
     * @param actionBean
     * @return
     */
    public String checkInsure(ActionBean actionBean) {
        BaseResponseBean response = new BaseResponseBean();
        return json(BaseResponseBean.CODE_FAILURE, "接口完善中。。。", response);
    }

    /**
     * 缴费
     * @param actionBean
     * @return
     */
    public String payInsure(ActionBean actionBean) {
        BaseResponseBean response = new BaseResponseBean();
        return json(BaseResponseBean.CODE_FAILURE, "接口完善中。。。", response);
    }

    /**
     * 撤保
     * @param actionBean
     * @return
     */
    public String cancelInsure(ActionBean actionBean) {
        BaseResponseBean response = new BaseResponseBean();
        return json(BaseResponseBean.CODE_FAILURE, "接口完善中。。。", response);
    }

    /**
     * 承保
     * @param actionBean
     * @return
     */
    public String buyInsure(ActionBean actionBean) {
        BaseResponseBean response = new BaseResponseBean();
        return json(BaseResponseBean.CODE_FAILURE, "接口完善中。。。", response);
    }


}
