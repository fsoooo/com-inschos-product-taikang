package com.inschos.product.taikang.assist.kit;

import com.alibaba.druid.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class PaySignKit {

    /**
     * @param map 要参与签名的集合信息
     * @param key 签名的key
     * @return 签名
     * @Description: 签名算法
     */
    public static String getSign(Map<String, Object> map, String key) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();
            if (!StringUtils.isEmpty(k)) {
                list.add(k + "=" + v + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.append("key=" + key).toString();
        result = MD5Kit.MD5Digest(result).toUpperCase();
        return result;
    }
}
