package com.sharedcase.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * ClassName: AESUtil
 * Package: com.sharedcase.util
 * Description:
 *      AES 加密解密工具类
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/23 14:44
 */
public class AESUtil {
    private static final String KEY = "0123456789abcdef"; // 16位密钥，生产建议从配置文件读取
    private static final String ALGORITHM = "AES";

    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY.getBytes(), ALGORITHM));
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY.getBytes(), ALGORITHM));
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        return new String(cipher.doFinal(decoded));
    }
}
