package hscsm.core.fnd.mq.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by wangxu on 2018/3/3.
 */
public class Base64Utils {
    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptBASE64(String key) {
        byte[] bt;
        try {
            bt = (new BASE64Decoder()).decodeBuffer(key);
            return new String(bt);//如果出现乱码可以改成： String(bt, "utf-8")或 gbk
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(String key) {
        byte[] bt = new byte[0];
        try {
            bt = key.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return (new BASE64Encoder()).encodeBuffer(bt);
    }

}
