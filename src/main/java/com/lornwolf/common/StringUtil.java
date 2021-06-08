package com.lornwolf.common;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

    /**
     * item_name --> itemName
     * 
     * @param fieldName
     * @return
     */
    public static String item_name2itemName(String fieldName) {
        String temp = "";
        String[] ss = fieldName.trim().split("_| ");
        
        for (int i = 0; i < ss.length; i++) {

            if (StringUtils.isEmpty(ss[i])) {
                continue;
            }

            // ループ一回目の場合
            if (temp.equals("")) {
                temp = ss[i];
                continue;
            }

            temp = temp + ss[i].substring(0, 1).toUpperCase() + ss[i].substring(1);
        }
        return BigSmallString.toSmallAscii(temp);
    }

    /**
     * item_name --> ItemName
     * 
     * @param fieldName
     * @return
     */
    public static String item_name2ItemName(String fieldName) {
        String temp = "";
        String[] ss = fieldName.trim().split("_| ");
        
        for (int i = 0; i < ss.length; i++) {

            if (StringUtils.isEmpty(ss[i])) {
                continue;
            }

            temp = temp + ss[i].substring(0, 1).toUpperCase() + ss[i].substring(1).toLowerCase();
        }
        return BigSmallString.toSmallAscii(temp);
    }

    public static String item_name2Item_Name(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    
    /**
     * 空文字をNULLに変換する。
     * 
     * @param bean Beanオブジェクト
     * @return 変換後オブジェクト
     */
    public static Object blank2Null(Object bean) {
        Class classObj = bean.getClass();
        Field[] fields = classObj.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType().getSimpleName().equals("String")) {
                    String value = (String) field.get(bean);
                    if (StringUtils.isEmpty(value)) {
                        field.set(bean, null);
                    }
                }
            }
        } catch(Exception e) {
            // 何もしない
        }
        return bean;
    }

    public static boolean isDorl(String s) {
        boolean isDorL = true;
        int length = s.length();    
        for (int i = 0 ; i < length ; i++) {
            if(!((s.charAt(i) >= '0' && s.charAt(i) <= '9')
                || (s.charAt(i) >= 'a' && s.charAt(i) <= 'z')
                || (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z'))) {
                isDorL = false;
                break;
            }
        }
        return isDorL;
    }
}
