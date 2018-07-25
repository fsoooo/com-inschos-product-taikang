package com.inschos.product.taikang.access.http.controller.bean;


import com.inschos.common.assist.kit.JsonKit;
import com.inschos.common.assist.kit.L;
import com.inschos.common.assist.kit.StringKit;
import com.inschos.product.taikang.assist.kit.*;
import com.inschos.product.taikang.assist.kit.*;

public class ActionBean {

    private final static String SALT_VALUE_TEST = "InschosTest";
    private final static String SALT_VALUE_ONLINE = "InschosOnLine";
    public String salt;
    public String url;
    public String body;
    public int buildCode;
    public String platform;
    public int apiCode = 1;
    public String userId;
    public int userType;
    public String managerUuid;
    public String accountUuid;
    public String username;
    public String phone;
    public String email;
    public long sysId;
    public int type;
    public long tokenTime;
    public String referer;
}
