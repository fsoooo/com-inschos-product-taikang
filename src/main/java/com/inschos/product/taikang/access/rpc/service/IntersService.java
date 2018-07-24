package com.inschos.product.taikang.access.rpc.service;

import com.inschos.dock.bean.InsureBean;
import com.inschos.product.taikang.access.http.controller.bean.BaseResponseBean;

public interface IntersService {

    BaseResponseBean signInsure(InsureBean requset);

}
