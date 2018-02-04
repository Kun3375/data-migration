package com.kun.migration.util;

import com.kun.migration.configuration.properties.DataProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CaoZiye
 * @version 1.0 2017/11/13 12:02
 */
@Component
public class AesUtil implements InitializingBean{
    
    private static final Logger log = LoggerFactory.getLogger(AesUtil.class);
    @Autowired
    private DataProperties dataProperties;
    
    private static String aesKey = "87dxfbkq2p-0";
    
    public static String getAesKey() {
        return aesKey;
    }
    
    /*
     * AES 加密 ********************************************************************
     */
    
    public static Map<String, String> encrypt(String key, Collection<String> texts) {
        Map<String, String> results = new HashMap<>();
        texts.forEach((text) ->
            results.put(text, encrypt(key, text))
        );
        return results;
    }
    
    public static String[] encrypt(String key, String... texts) {
        String[] results = new String[texts.length];
        for (int i = 0; i < texts.length; i++) {
            results[i] = encrypt(key, texts[i]);
        }
        return results;
    }
    
    public static String encrypt (String key, String text) {
        String result = null;
        try {
            byte[] bytes = key.getBytes("UTF-8");
            SecretKeySpec aes = new SecretKeySpec(bytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, aes);
        
            byte[] encodedBytes = cipher.doFinal(text.getBytes("UTF-8"));
            result = Base64.getEncoder().encodeToString(encodedBytes);
        } catch (Exception e) {
            log.info("加密失败", e);
            e.printStackTrace();
        }
        return result;
    }
    
    /*
    * AES 解密 ********************************************************************
    */
    
    public static Map<String, String> decrypt(String key, Collection<String> texts) {
        Map<String, String> results = new HashMap<>();
        texts.forEach((text) ->
                results.put(text, decrypt(key, text))
        );
        return results;
    }
    
    public static String[] decrypt(String key, String... texts) {
        String[] results = new String[texts.length];
        for (int i = 0; i < texts.length; i++) {
            results[i] = decrypt(key, texts[i]);
        }
        return results;
    }
    
    public static String decrypt (String key, String text) {
        String result = null;
        try {
            byte[] bytes = key.getBytes("UTF-8");
            SecretKeySpec aes = new SecretKeySpec(bytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(2, aes);
            
            byte[] decodedBytes = Base64.getDecoder().decode(text);
            result = new String(cipher.doFinal(decodedBytes),"UTF-8");
        } catch (Exception e) {
            log.info("解密失败", e);
            e.printStackTrace();
        }
        return result;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        AesUtil.aesKey = dataProperties.getAesKey();
    }
}
