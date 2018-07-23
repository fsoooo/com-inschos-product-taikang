package com.inschos.product.taikang.access.rpc.service;

import com.inschos.product.taikang.access.http.controller.bean.BaseResponseBean;
import com.inschos.product.taikang.access.rpc.bean.BuyInsureBean;
import com.inschos.product.taikang.access.rpc.bean.CancelInsureBean;
import com.inschos.product.taikang.access.rpc.bean.PayInsureBean;

public interface IntersService {

    BaseResponseBean cancelInsure(CancelInsureBean.Requset requset);

}
