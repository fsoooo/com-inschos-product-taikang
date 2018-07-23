package com.inschos.product.taikang.access.http.controller.bean;

/**
 * 接口地址公共信息
 */
public class CommonBean {


    //测试接口：
    //接口名称	地址
    //tkinsure	http://10.137.123.61:8080/ecsm-service/tkinsure?wsdl
    //tkcancel	http://10.137.123.61:8080/ecsm-service/tkcancel?wsdl
    //tkinsuresign	http://10.137.123.61:8080/ecsm-service/tkinsuresign?wsdl
    //测试环境系统编码orgid：5466
    //测试环境Key：bfafa2d09dba76a7be9464c8
    //测试产品编码：10100878
    //测试保险期间：17
    //测试保费：20
    //说明：
    //	tkinsure包含uwService、insureService，该二个接口配套使用。uwService为投保接口主要进行投核保校验，当投保接口返回成功，则可以调用insureService进行交费签单。
    //	Tkcancel包含cancelService，该接口为撤单交易接口
    //	tkinsuresign包含insureSignService接口，该接口为一步交易接口，主要用于投核保均由出单系统控制，当出单后，用于把保单同步至ECSM系统。
    private static final String SERVER_HOST_ACCOUNT = "http://122.14.202.146:9200/account";

    private static final String SERVER_HOST_INSURE = "http://122.14.202.146:9200/trading";

    private static final String SERVER_HOST_YUNDA = "http://yunda.com/test";

    private static final String SERVER_HOST_TEST = "https://api-yunda.inschos.com/webapi";

}
