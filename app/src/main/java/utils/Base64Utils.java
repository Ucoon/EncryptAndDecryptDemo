package utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Base64加解密
 * Created by ZongJie on 2017/8/23.
 */

public class Base64Utils {

    /**
     * Base64加密字符串
     * @param str 待加密字符
     * @return
     */
    public static String getEncoder(String str){
        String encoderStr = null;
        try {
            encoderStr = Base64.encodeToString(str.getBytes("utf-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encoderStr;
    }

    /**
     * Base64解密字符串
     * @param encode 待解密字符串
     * @return
     */
    public static String getDecoder(String encode){
        String decoderStr = null;
        byte[] asBytes = Base64.decode(encode, Base64.DEFAULT);
        try {
            decoderStr = new String(asBytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decoderStr;
    }
}
