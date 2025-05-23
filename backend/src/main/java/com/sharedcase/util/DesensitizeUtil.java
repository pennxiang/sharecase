package com.sharedcase.util;


/**
 * ClassName: DesensitizeUtil
 * Package: com.sharedcase.util
 * Description:
 *      敏感信息脱敏展示
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 16:05
 */
public class DesensitizeUtil {

    public static String maskIdCard(String IdCard) {
        if (IdCard.length() == 18) {
            return IdCard.substring(0, 2) + "*********" + IdCard.substring(16);
        }
        return null;
    }

    public static String maskPhone(String phone) {
        if (phone.length() == 11) {
            return phone.substring(0, 3) + "****" + phone.substring(7) ;
        }
        return null;
    }
}
