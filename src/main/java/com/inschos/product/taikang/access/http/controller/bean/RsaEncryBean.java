package com.inschos.product.taikang.access.http.controller.bean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RsaEncryBean {
    public static final byte[] pubModBytes = getBytesFromFile(new File("pubModBytes.dat"));
    public static final byte[] pubPubExpBytes = getBytesFromFile(new File("pubPubExpBytes.dat"));
    public static final byte[] priModBytes = getBytesFromFile(new File("priModBytes.dat"));
    public static final byte[] priPriExpBytes = getBytesFromFile(new File("priPriExpBytes.dat"));
    private static byte[] getBytesFromFile(File file) {
        byte[] ret = null;
        try {
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
            ret = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
