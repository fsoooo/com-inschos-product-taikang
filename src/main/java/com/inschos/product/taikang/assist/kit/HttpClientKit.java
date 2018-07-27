package com.inschos.product.taikang.assist.kit;

import com.inschos.common.assist.kit.L;
import com.inschos.common.assist.kit.StringKit;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpClientKit {

    private static final Logger logger = Logger.getLogger(HttpClientKit.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client;

    /**
     * http请求公共函数(Post,超时时间30s)
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static String post(String url,String contentType, String json) throws IOException {
        MediaType mediaType ;
        if (StringKit.isEmpty(contentType)) {
            mediaType = MediaType.parse("text/html;charset=UTF-8");
        } else {

            mediaType = MediaType.parse(contentType);
        }
        RequestBody requestBody = RequestBody.create(mediaType, json);
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();
        }
        L.log.debug("==================================Request======================================");
        L.log.debug(json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client
                .newCall(request)
                .execute();
        String result = "";
        if (response.body() != null) {
            result = response.body().string();
        }
        L.log.debug("==================================Response======================================");
        L.log.debug(result);
        return result;
    }
}
