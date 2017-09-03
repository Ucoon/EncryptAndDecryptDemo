package utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ZongJie on 2017/8/25.
 */

public class MD5Utils {

    /**
     * 计算字符串MD5值
     * @param string
     * @return
     */
    public static String md5(String string){
        if(TextUtils.isEmpty(string)){
            return "";
        }
        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            byte[] digest = md5Digest.digest(string.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest){
                int temp = b & 0xff; //加盐
                String hexString = Integer.toHexString(temp);
                if(hexString.length() == 1){
                    stringBuffer.append("0");
                }
                stringBuffer.append(hexString);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5加密：对字符串多次加密
     * @param string
     * @param times
     * @return
     */
    public static String md5Times(String string, int times){
        String md5 = md5(string);
        for (int i = 0; i < times - 1; i++){
            md5 = md5(md5);
        }
        return md5(md5);
    }

    /**
     * MD5加密：String + key(盐值key)再进行MD5加密
     * @param string
     * @param slat 手动加盐
     * @return
     */
    public static String md5(String string, String slat){
        if(TextUtils.isEmpty(string)){
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest((string + slat).getBytes());
            String res = "";
            for (byte b : bytes){
                String temp = Integer.toHexString( b & 0xff);
                if (temp.length() == 1){
                    temp = "0" + temp;
                }
                res += temp;
            }
            return res;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 计算文件的MD5值
     * @param file
     * @return
     */
    public static String md5(File file){
        if(file == null || !file.isFile() || !file.exists()){
            return "";
        }
        FileInputStream fileInputStream = null;
        String res = "";
        byte buffer[] = new byte[8192];
        int len;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            while ((len = fileInputStream.read()) != -1){
                md5.update(buffer, 0, len);
            }
            byte[] bytes = md5.digest();
            for (byte b: bytes){
                String temp = Integer.toHexString( b & 0xff);
                if (temp.length() == 1){
                    temp = "0" + temp;
                }
                res += temp;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fileInputStream){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    /**
     * 采用nio的方式计算文件的MD5值
     * @param file
     * @return
     */
    public static String md5Bynio(File file){
        String res = "";
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            byte[] bytes = md5.digest();
            for (byte b : bytes){
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1){
                    temp = "0" + temp;
                }
                res += temp;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }finally {
            if (null != in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }
}
