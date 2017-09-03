package com.my.encryptanddecryptdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.AESUtils;
import utils.Base64Utils;
import utils.DesUtils;
import utils.MD5Utils;
import utils.XORUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String data = "这是一个测试编码和加解密的字符串数据";


    @Bind(R.id.btn_base64_str)
    Button mBtnBase64Str;
    @Bind(R.id.btn_md5_encrypt)
    Button mBtnMd5Encrypt;
    @Bind(R.id.btn_des_encrypt)
    Button mBtnDesEncrypt;
    @Bind(R.id.btn_aes_encrypt)
    Button mBtnAesEncrypt;
    @Bind(R.id.btn_xor_encrypt)
    Button mBtnXorEncrypt;
    @Bind(R.id.et_input)
    EditText mEmtInput;
    @Bind(R.id.tv_content)
    TextView mTvContent;

    private String encodeBase64Str;
    private String encryptDES;
    private String encryptAES;
    private String encryptXor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_base64_str, R.id.btn_md5_encrypt, R.id.btn_des_encrypt, R.id.btn_aes_encrypt, R.id.btn_xor_encrypt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_base64_str:
                base64Str();
                break;
            case R.id.btn_md5_encrypt:
                md5Encrypt();
                break;
            case R.id.btn_des_encrypt:
                desEncrypt();
                break;
            case R.id.btn_aes_encrypt:
                aesEncrypt();
                break;
            case R.id.btn_xor_encrypt:
                xorEncrypt();
                break;
        }
    }

    private boolean isBase64Encode = true;

    private void base64Str(){
        mTvContent.setText("");
        if(isBase64Encode){
            encodeBase64Str = Base64Utils.getEncoder(data);
            Log.d(TAG, "初始化数据明文: " + data);
            Log.d(TAG, "base64编码后暗文: " + encodeBase64Str);
            mTvContent.setText("base64编码后暗文: " + encodeBase64Str);
        }else{
            Log.d(TAG, "base64编码后暗文: " + Base64Utils.getDecoder(encodeBase64Str));
            mTvContent.setText("base64编码后暗文: " + Base64Utils.getDecoder(encodeBase64Str));
        }
        isBase64Encode = !isBase64Encode;
    }

    /**
     * MD5加密 (MD5加密不可逆)
     */
    private void md5Encrypt() {
        mTvContent.setText("");
        String md5EncryptStr = MD5Utils.md5(data);
        Log.d("TAG:" + TAG, "----MD5加前原文: " + data);
        Log.d("TAG:" + TAG, "----MD5加密后: " + md5EncryptStr);
        mTvContent.setText("MD5加密后: " + md5EncryptStr);

    }

    private boolean isDesEncrypt = true;

    private void desEncrypt() {

        mTvContent.setText("");
        if (isDesEncrypt) {
            encryptDES = DesUtils.encode("deskey", data);
            Log.d("TAG:" + TAG, "----DES加密后: " + encryptDES);
            mTvContent.setText("DES加密后: " + encryptDES);
        } else {
            String decodeDES = DesUtils.decode("deskey", encryptDES);
            Log.d("TAG:" + TAG, "----DES解密后: " + decodeDES);
            mTvContent.setText("DES解密后: " + decodeDES);
        }

        isDesEncrypt = !isDesEncrypt;
    }

    private boolean isAesEncrypt = true;

    /**
     * AES 加解密
     */
    private void aesEncrypt() {
        mTvContent.setText("");
        if (isAesEncrypt) {
            encryptAES = AESUtils.encrypt("aeskey", data);
            Log.d("TAG:" + TAG, "----AES加密后: " + encryptAES);
            mTvContent.setText("AES加密后: " + encryptAES);
        } else {
            String decryptAES = AESUtils.decrypt("aeskey", encryptAES);
            Log.d("TAG:" + TAG, "----AES解密后: " + decryptAES);
            mTvContent.setText("AES解密后: " + decryptAES);
        }

        isAesEncrypt = !isAesEncrypt;

    }

    private boolean isXorEncrypt = true;
    /**
     * 异或加密算法
     */
    private void xorEncrypt() {
        if (isXorEncrypt) {
            encryptXor = XORUtils.encryptString(data);
            Log.d("TAG:"+TAG,"----XOR异或加密: "+ Base64.encodeToString(encryptXor.getBytes(),Base64.DEFAULT));
            mTvContent.setText("XOR异或加密: "+Base64.encodeToString(encryptXor.getBytes(),Base64.DEFAULT));
        }else {
            String decryptXor = XORUtils.encryptString(encryptXor);
            Log.d("TAG:" + TAG, "----XOR异或解密: " + decryptXor);
            mTvContent.setText("XOR异或解密: " + decryptXor);
        }
        isXorEncrypt = !isXorEncrypt;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
