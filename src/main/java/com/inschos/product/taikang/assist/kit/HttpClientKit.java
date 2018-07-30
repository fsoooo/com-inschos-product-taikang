package com.inschos.product.taikang.assist.kit;

import com.inschos.common.assist.kit.L;
import com.inschos.common.assist.kit.StringKit;
import com.inschos.product.taikang.access.http.controller.bean.HeaderBean;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpClientKit {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient client = new OkHttpClient();

    private static OkHttpClient fileClient = new OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build();

    public static String post(String url, String body) throws IOException {
        return post(url, body, client);
    }

    public static String get(String url) throws IOException {
        return get(url,client);
    }

    public static String post(String url,  String body, List<HeaderBean.Header> headers) throws IOException {
        MediaType mediaType;
        mediaType = MediaType.parse("text/html;charset=UTF-8");
        Map<String, String> headersData = new HashMap<String, String>();//添加你的请求头参数
        //TODO 判空后遍历
        if (headers != null && headers.size() != 0) {
            for (HeaderBean.Header header : headers) {
                headersData.put(header.headerKey, header.headerValue);
            }
        }
        Headers addHeaders = SetHeaders(headersData);
        RequestBody requestBody = RequestBody.create(mediaType, body);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .headers(addHeaders)
                .build();
        Response response = client.newCall(request).execute();
        String result = "";
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            result = responseBody.string();
        }
        return result;
    }

    public static String post(String url, String contentType, String body) throws IOException {
        MediaType mediaType;
        if (StringKit.isEmpty(contentType)) {
            mediaType = MediaType.parse("text/html;charset=UTF-8");
        } else {

            mediaType = MediaType.parse(contentType);
        }
        RequestBody requestBody = RequestBody.create(mediaType, body);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String result = "";
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            result = responseBody.string();
        }
        return result;
    }

    public static String uploadFile(String url, String json) throws IOException {
        return post(url, json, fileClient);
    }

    private static String post(String url, String json, OkHttpClient client) throws IOException {

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String result = "";
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            result = responseBody.string();
        }
        return result;
    }

    private static String get(String url, OkHttpClient client) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String result = "";
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            result = responseBody.string();
        }
        return result;
    }

    public static InputStream downloadFile(String url) {

        InputStream is = null;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = fileClient.newCall(request).execute();

            ResponseBody body = response.body();
            if (body != null) {
                is = body.byteStream();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            is = null;
        }

        return is;
    }

    private static Headers SetHeaders(Map<String, String> headersParams) {
        Headers headers = null;
        okhttp3.Headers.Builder headersbuilder = new okhttp3.Headers.Builder();

        if (headersParams != null) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersbuilder.add(key, headersParams.get(key));
                L.log.debug("get http", "get_headers===" + key + "====" + headersParams.get(key));
            }
        }
        headers = headersbuilder.build();

        return headers;
    }

}
