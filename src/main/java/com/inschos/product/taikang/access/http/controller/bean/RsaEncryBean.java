package com.inschos.product.taikang.access.http.controller.bean;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RsaEncryBean {
    public static final byte[] pubModBytes = getBytesFromFile("/encrypt/pubBPMMod.dat");
    public static final byte[] pubPubExpBytes = getBytesFromFile("/encrypt/pubBPMExp.dat");
    public static final byte[] priModBytes = getBytesFromFile("/encrypt/priBPMMod.dat");
    public static final byte[] priPriExpBytes = getBytesFromFile("/encrypt/priBPMExp.dat");
    private static byte[] getBytesFromFile(String path) {
        byte[] ret = null;
        try {
            InputStream in = RsaEncryBean.class.getResourceAsStream(path);
            if(in!=null){

                ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
                byte[] b = new byte[1024];
                int n;
                while ((n = in.read(b)) != -1) {
                    out.write(b, 0, n);
                }
                in.close();
                out.close();
                ret = out.toByteArray();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
