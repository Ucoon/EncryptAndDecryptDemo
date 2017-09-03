package utils;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ZongJie on 2017/8/31.
 */

public class AESUtils {
    private final static String HEX = "0123456789ABCCDEF";
    //AES是加密方式， CBC是工作模式, PKCS5Padding是填充模式
    private final static String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private final static String ALGORITHM = "AES";
    //SHA1PRNG 强随机种子算法
    private final static String SHA1PRNG = "SHA1PRNG";


    /**
     * AES加密
     * @param key 密钥
     * @param data 待加密字符
     * @return
     */
    public static String encrypt(String key, String data){
        if (TextUtils.isEmpty(data)){
            return data;
        }
        byte[] res = encrypt(key, data.getBytes());
        //return new String(res); //直接将加密后的字节数组利用String的API转成字符只能得到乱码
        //return ByteToHex(res); //采用的byte[] 到 String转换的方法都是将byte[] 二进制利用16进制的char[]来表示
        //采用Android中Base64将AES加密后的数据转成字符串同时实现再次加密转化为暗文
        return Base64.encodeToString(res, Base64.DEFAULT);
    }

    /**
     * AES加密
     * @param key 密钥
     * @param bytes 待加密字节
     * @return
     */
    public static byte[] encrypt(String key, byte[] bytes){
        try {
            byte[] raw = getRawKey(key);
            SecretKeySpec secretKeySpec = new SecretKeySpec(raw, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] encrypted = cipher.doFinal(bytes);
            return encrypted;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES解密
     * @param key 密钥
     * @param data 待解密字符串
     * @return
     */
    public static String decrypt(String key, String data){
        if (TextUtils.isEmpty(data)){
            return data;
        }
//        byte[] bytes = HexToByte(data);
//        byte[] bytes = Base64Utils.getDecoder(data);
        byte[] bytes = Base64.decode(data, Base64.DEFAULT);
        byte[] res = decrypt(key, bytes);
        return new String(res);
    }

    /**
     * AES解密
     * @param key 密钥
     * @param data 待解密字节组
     * @return
     */
    public static byte[] decrypt(String key, byte[] data){
        try {
            byte[] raw = getRawKey(key);
            SecretKeySpec spec = new SecretKeySpec(raw, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, spec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] decrypted = cipher.doFinal(data);
            return decrypted;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 对密钥进行处理 转换密钥(同时适用于其他对称加密算法)
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] getRawKey(String key) throws Exception {
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
        //AES格式为128bits 或 256bits 或 192bites
        keyGenerator.init(128, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] raw = secretKey.getEncoded();
        return raw;
    }

    //二进制转字符
    public static String BytetoHex(byte[] buf){
        if (buf == null){
            return "";
        }
        StringBuffer res = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++){
            appendHex(res, buf[i]);
        }
        return res.toString();
    }

    /**
     * (16进制)字符转二进制
     * @param hexString
     * @return
     */
    public static byte[] HexToByte(String hexString){
        int len = hexString.length()/2;
        byte[] res = new byte[len];
        for (int i = 0; i < len; i++){
            res[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i +2), 16).byteValue();
        }
        return res;
    }

    public static void appendHex(StringBuffer sb, byte b){
        sb.append(HEX.charAt(b >> 4) & 0x0f).append(HEX.charAt(b & 0x0f));
    }
}
