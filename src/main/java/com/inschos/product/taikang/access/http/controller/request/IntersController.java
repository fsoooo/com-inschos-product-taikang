package com.inschos.product.taikang.access.http.controller.request;

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
     *
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/checkInsure/**")
    @ResponseBody
    public String checkInsure(HttpServletRequest httpServletRequest) {
        return intersAction.checkInsure(httpServletRequest);
    }

    /**
     * 缴费接口
     *
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/payInsure/**")
    @ResponseBody
    public String payInsure(HttpServletRequest httpServletRequest) {
        return intersAction.payInsure(httpServletRequest);
    }

    /**
     * 承保接口
     *
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/buyInsure/**")
    @ResponseBody
    public String buyInsure(HttpServletRequest httpServletRequest) {
        return intersAction.buyInsure(httpServletRequest);
    }
}
