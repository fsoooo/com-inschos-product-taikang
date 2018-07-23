package com.inschos.product.taikang.access.rpc.service;


import com.inschos.product.taikang.access.rpc.bean.AccountBean;

/**
 * Created by IceAnt on 2018/4/16.
 */
public interface AccountService {

    AccountBean getAccount(String token);

    AccountBean findByUuid(String uuid);

    AccountBean findByUser(long sysId, int userType, String userId);
}
