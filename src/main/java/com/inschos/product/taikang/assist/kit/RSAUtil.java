package com.inschos.product.taikang.assist.kit;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RSAUtil {

    private static BouncyCastleProvider castleProvider = null;

    private static BouncyCastleProvider getCastleProvider() {
        if (null == castleProvider) {
            castleProvider = new BouncyCastleProvider();
        }
        return castleProvider;
    }

    public static void createFile(String fileName, byte[] raw) throws IOException {
        File file = new File(fileName);
        OutputStream out = new FileOutputStream(file);
        out.write(raw);
        out.close();
    }

    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", getCastleProvider());
            final int KEY_SIZE = 1024;
            keyPairGen.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGen.genKeyPair();
            return keyPair;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA", getCastleProvider());
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
        try {
            if (keyFac != null) {
                return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
            }
        } catch (InvalidKeySpecException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) {

        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA", getCastleProvider());
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
        try {
            if (keyFac != null) {
                return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
            }
        } catch (InvalidKeySpecException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] encrypt(Key key, byte[] data) {

        try {
            Cipher cipher = Cipher.getInstance("RSA", getCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            int blockSize = cipher.getBlockSize();
            int outputSize = cipher.getOutputSize(data.length);
            int leavedSize = data.length % blockSize;
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (data.length - i * blockSize > 0) {
                if (data.length - i * blockSize > blockSize)
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
                else
                    cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
                i++;
            }
            return raw;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(Key key, byte[] raw) {
        try {
            Cipher cipher = Cipher.getInstance("RSA", getCastleProvider());
            cipher.init(cipher.DECRYPT_MODE, key);
            int blockSize = cipher.getBlockSize();
            ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
            int j = 0;
            while (raw.length - j * blockSize > 0) {
                bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
                j++;
            }
            return bout.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
