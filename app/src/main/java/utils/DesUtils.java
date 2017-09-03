package utils;

import android.os.Build;
import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ZongJie on 2017/8/28.
 */

public class DesUtils {
    private final static String HEX = "0123456789ABCCDEF";
    //DES是加密方式， CBC是工作模式, PKCS5Padding是填充模式
    private final static String TRANSFORMATION = "DES/CBC/PKCS5Padding";
    //初始化向量参数，AES为16bytes，DES为8bytes
    private final static String IVPARAMETERSPEC = "01020304";
    private final static String ALGORITHM = "DES";
    //SHA1PRNG 强随机种子算法
    private final static String SHA1PRNG = "SHA1PRNG";


    /**
     * 生成随机数，可作动态的密钥，加密和解密的密钥必须一致，否则无法解密
     * @return
     */
    public static String generateKey(){
        try {
            SecureRandom secureRandom = SecureRandom.getInstance(SHA1PRNG);
            byte[] bytes_key = new byte[20];// 长度不能够小于8位字节 因为DES固定格式为64bits，即8bytes。
            secureRandom.nextBytes(bytes_key);
            String str_key = toHex(bytes_key);
            return str_key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    //二进制转字符
    public static String toHex(byte[] buf){
        if (buf == null){
            return "";
        }
        StringBuffer res = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++){
            appendHex(res, buf[i]);
        }
        return res.toString();
    }

    public static void appendHex(StringBuffer sb, byte b){
        sb.append(HEX.charAt(b >> 4) & 0x0f).append(HEX.charAt(b & 0x0f));
    }

    /**
     * 对密钥进行处理 转换密钥(同时适用于其他对称加密算法)
     * @param key
     * @return
     * @throws Exception
     */
    public static Key getRawKey(String key) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom secureRandom = null;
        //for android
        if (Build.VERSION.SDK_INT >= 17){
            secureRandom = SecureRandom.getInstance(SHA1PRNG, "Crypto");
        } else {
            secureRandom = SecureRandom.getInstance(SHA1PRNG);
        }
        //for java
//        SecureRandom secureRandom = SecureRandom.getInstance(SHA1PRNG);
        secureRandom.setSeed(key.getBytes());
        //DES固定格式为64bits, 即8bytes
        keyGenerator.init(64, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] raw = secretKey.getEncoded();
        return new SecretKeySpec(raw, ALGORITHM);
    }

    /**
     * 对密钥进行处理 转换密钥(针对DES)
     * @param key
     * @return
     * @throws Exception
     */
    public static Key getRawKeyAnother(String key) throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(desKeySpec);
    }

    public static String encode(String key, String data){
        return encode(key, data.getBytes());
    }

    /**
     * 加密
     * @param key 加密私钥 长度不能小于8位
     * @param data 待加密字符串
     * @return 加密后的字节数组，一般结合Base61编码使用
     */
    public static String encode(String key, byte[] data){
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(IVPARAMETERSPEC.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, getRawKey(key), iv);
            byte[] bytes = cipher.doFinal(data);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密
     * @param key
     * @param data
     * @return
     */
    public static String decode(String key, byte[] data){
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(IVPARAMETERSPEC.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, getRawKey(key), iv);
            byte[] original = cipher.doFinal(data);
            String originakStr = new String(original);
            return originakStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密
     * @param key
     * @param data
     * @return
     */
    public static String decode(String key, String data){
        return decode(key, Base64.decode(data, Base64.DEFAULT));
    }
}
