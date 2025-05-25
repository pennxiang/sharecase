package com.sharedcase.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * ClassName: HashUtil
 * Package: com.sharedcase.util
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/25 13:05
 */
public class HashUtil {
    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // 手动转换为十六进制字符串
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA256 计算失败", e);
        }
    }
}
