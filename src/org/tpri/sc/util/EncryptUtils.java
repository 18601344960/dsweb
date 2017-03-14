package org.tpri.sc.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>加解密工具类<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月15日
 */
public final class EncryptUtils {

    /** 加密算法 */
    private static final String ARITHMETIC = "AES";

    /** 密钥文本 */
    private static final String SK_KEY_TEXT = "TPRI-ZC-20131023";

    /** 加密器 */
    private static Cipher encipher;

    /** 解密器 */
    private static Cipher decipher;

    /** Base64编译器 */
    private static Base64 coder;

    /** 静态变量初始化 */
    static {
        try {
            SecretKey secretKey = new SecretKeySpec(SK_KEY_TEXT.getBytes(), ARITHMETIC);
            encipher = Cipher.getInstance(ARITHMETIC);
            encipher.init(Cipher.ENCRYPT_MODE, secretKey);
            decipher = Cipher.getInstance(ARITHMETIC);
            decipher.init(Cipher.DECRYPT_MODE, secretKey);
            coder = new Base64(0, null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <B>构造方法</B><BR>
     */
    private EncryptUtils() {
    }

    /**
     * <B>方法名称：</B>编码数据<BR>
     * <B>概要说明：</B><BR>
     * 
     * @param data 原始数据
     * @return String 编码文本
     */
    public static String enBase64(byte[] data) {
        if (data == null || data.length < 1) {
            return null;
        }
        return new String(coder.encode(data));
    }

    /**
     * <B>方法名称：</B>解码数据<BR>
     * <B>概要说明：</B><BR>
     * 
     * @param str 编码文本
     * @return byte[] 原始数据
     */
    public static byte[] deBase64(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return coder.decode(str.getBytes());
    }

    /**
     * <B>方法名称：</B>加密文本<BR>
     * <B>概要说明：</B>根据指定算法，加密文本内容。<BR>
     * 
     * @param text 原始文本
     * @return String 加密文本
     */
    public static String encrypt(String text) {
        byte[] t = text.getBytes();
        try {
            t = encipher.doFinal(t);
            int len = t.length;
            byte[] r = new byte[len];
            for (int i = 0; i < len; i++) {
                r[i] = t[len - i - 1];
            }
            t = coder.encode(r);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new String(t).trim();
    }

    /**
     * <B>方法名称：</B>解密文本<BR>
     * <B>概要说明：</B>根据指定算法，解密文本内容。<BR>
     * 
     * @param text 加密文本
     * @return String 原始文本
     */
    public static String decrypt(String text) {
        byte[] t = text.getBytes();
        try {
            t = coder.decode(t);
            int len = t.length;
            byte[] r = new byte[len];
            for (int i = 0; i < len; i++) {
                r[i] = t[len - i - 1];
            }
            t = decipher.doFinal(r);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new String(t);
    }

    /**
     * <B>方法名称：</B>加密文本一致判断<BR>
     * <B>概要说明：</B>根据指定算法，判断文本与加密文本内容是否一致。<BR>
     * 
     * @param text 原始文本
     * @param encrypt 加密文本
     * @return String 加密后文本
     */
    public static boolean isSame(String text, String encrypt) {
        boolean ret = false;
        if (text != null && encrypt != null) {
            try {
                ret = encrypt.equals(encrypt(text));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}
