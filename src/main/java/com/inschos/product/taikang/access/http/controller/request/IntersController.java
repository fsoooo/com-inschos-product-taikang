package com.inschos.product.taikang.access.http.controller.request;

import com.inschos.product.taikang.access.http.controller.bean.ActionBean;
import com.inschos.product.taikang.access.http.controller.action.IntersAction;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * User: wangsl
 * Date: 2018/07/23
 * Time: 14:12
 * 泰康产品对接：核保，缴费，撤保，承保
 */
@Controller
@RequestMapping("/ins_api")
public class IntersController {

    private static final Logger logger = Logger.getLogger(IntersController.class);
    @Autowired
    private IntersAction intersAction;

    /**
     * 核保接口
     * @param actionBean
     * @return
     */
    @RequestMapping("/checkInsure/**")
    @ResponseBody
    public String checkInsure(ActionBean actionBean) {
        return intersAction.checkInsure(actionBean);
    }

    /**
     * 缴费接口
     * @param actionBean
     * @return
     */
    @RequestMapping("/payInsure/**")
    @ResponseBody
    public String payInsure(ActionBean actionBean) {
        return intersAction.payInsure(actionBean);
    }

    /**
     * 撤保接口
     * @param actionBean
     * @return
     */
    @RequestMapping("/cancelInsure/**")
    @ResponseBody
    public String cancelInsure(ActionBean actionBean) {
        return intersAction.cancelInsure(actionBean);
    }

    /**
     * 承保接口
     * @param actionBean
     * @return
     */
    @RequestMapping("/buyInsure/**")
    @ResponseBody
    public String buyInsure(ActionBean actionBean) {
        return intersAction.buyInsure(actionBean);
    }
}
