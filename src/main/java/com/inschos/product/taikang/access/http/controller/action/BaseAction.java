package com.inschos.product.taikang.access.http.controller.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inschos.product.taikang.assist.kit.StringKit;
import com.inschos.product.taikang.access.http.controller.bean.BaseResponseBean;
import com.inschos.product.taikang.annotation.CheckParamsKit;
import com.inschos.product.taikang.assist.kit.JsonKit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseAction {
    protected final static boolean isJSONValid(String jsonInString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public <T> T requst2Bean(String body, Class<T> clazz) {
        T bean = JsonKit.json2Bean(body, clazz);
        if (bean == null) {
            try {
                bean = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    public String json(int code, String message, BaseResponseBean response) {
        if (response == null) {
            response = new BaseResponseBean();
        }
        response.code = code;
        CheckParamsKit.Entry<String, String> defaultEntry = CheckParamsKit.getDefaultEntry();
        defaultEntry.details = message;
        List<CheckParamsKit.Entry<String, String>> list = new ArrayList<>();
        list.add(defaultEntry);
        response.message = list;
        return JsonKit.bean2Json(response);
    }

    public BaseResponseBean responseBean(int code, String message, BaseResponseBean response) {
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

    public String json(int code, List<CheckParamsKit.Entry<String, String>> message, BaseResponseBean response) {
        if (response == null) {
            response = new BaseResponseBean();
        }

        response.code = code;
        response.message = message;

        return JsonKit.bean2Json(response);
    }

    public String json(BaseResponseBean response) {
        return JsonKit.bean2Json(response);
    }

    public List<CheckParamsKit.Entry<String, String>> checkParams(Object obj) {
        List<CheckParamsKit.Entry<String, String>> list = new ArrayList<>();

        if (obj == null) {
            CheckParamsKit.Entry<String, String> anEntry = CheckParamsKit.getDefaultEntry();
            anEntry.details = "解析失败";
            list.add(anEntry);
            return list;
        }

        CheckParamsKit.checkToArray(obj, list);
        if (list.isEmpty()) {
            return null;
        } else {
            return list;
        }
    }

}
