package utils;

/**
 * Created by ZongJie on 2017/9/3.
 */

public class XORUtils {

    /**
     * 固定key的方式：这种方式加密解密算法一样
     * 若是中文字符串 转为bytes方式，则解密后会成乱码，
     * 所以对于中文字符串，应转化为char数组
     * @param bytes
     * @return
     */
    public static byte[] encrypt(byte[] bytes){
        if(bytes == null){
            return null;
        }
        int len = bytes.length;
        int key = 0x12;
        for (int i = 0; i < len; i++){
            bytes[i] ^= key;
        }
        return bytes;
    }

    /**
     * 固定key的方式：这种方式加密解密算法一样(适用于中文字符加解密)
     * @param str
     * @return
     */
    public static String encryptString(String str){
        int key = 0x12;
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++){
            charArray[i] = (char) (charArray[i] ^ key);
        }
        return new String(charArray);
    }

    /**
     * 不固定key的加密方式
     * @param bytes
     * @return
     */
    public static byte[] encryptForKey(byte[] bytes){
        if(bytes == null){
            return null;
        }
        int len = bytes.length;
        int key = 0x12;
        for (int i = 0; i < len; i++){
            bytes[i] = (byte) (bytes[i] ^ key);
            key = bytes[i];
        }
        return bytes;
    }

    public static byte[] decrypt(byte[] bytes){
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        int key = 0x12;
        for (int i = len - 1; i > 0; i--){
            bytes[i] = (byte) (bytes[i] ^ bytes[i-1]);
        }
        bytes[0] = (byte) (bytes[0] ^ key);
        return bytes;
    }
}
