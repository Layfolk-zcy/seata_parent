package com.starter.druid.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * @author 后端技术框架
 * @version 2.0.0
 * @title: 数据库连接池Druid实现模块
 * @projectName staging-framework-starters
 * @description: TODO 数据源加解密类
 * @date 2022/6/26 下午13:14
 */
public class AesCbcUtil {
    public static final Logger log = LoggerFactory.getLogger(AesCbcUtil.class);

    static{
        try{
            Security.addProvider(new BouncyCastleProvider());
        }catch(Exception e){
            log.error("数据源加解密出现异常",e);
        }
    }

    //解密密钥(自行随机生成)
    public static final String SECRETWORD = "yjvn849lhmy75fv5";//密钥key
    public static final String IV  = "jsd85lkh9ufnv05u";//向量iv

    //加密
    public static String Encrypt(String content) throws Exception{
        try{
            byte[] raw = SECRETWORD.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding","BC");
            IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
            byte[] encrypted = cipher.doFinal(content.getBytes());
            return new BASE64Encoder().encode(encrypted);
        }catch (Exception e){
            log.error("数据源加密出现异常",e);
        }
        return "";
    }

    //解密
    public static String Decrypt(String content) throws Exception {
        try {
            byte[] raw = SECRETWORD.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding","BC");
            IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(content);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                log.error("数据源字符串转换出现异常",e);
            }
        } catch (Exception ex) {
            log.error("数据源解密出现异常",ex);
        }
        return "";
    }
}
