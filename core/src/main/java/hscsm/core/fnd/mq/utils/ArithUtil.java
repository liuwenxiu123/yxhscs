package hscsm.core.fnd.mq.utils;

import java.math.BigDecimal;

/**
 * Created by wangxu on 2018/3/9.
 */
public class ArithUtil {
    /**
     * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入。
     */

    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    // 这个类不能实例化
    private ArithUtil() {}

    /**
     * 提供精确的加法运算。
     *
     * @param v1
     *            被加数
     * @param v2
     *            加数
     * @return 两个参数的和
     */

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
//        return b1.add(b2).doubleValue();

        return b1.add(b2).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1
     *            被减数
     * @param v2
     *            减数
     * @return 两个参数的差
     */

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1
     *            被乘数
     * @param v2
     *            乘数
     * @return 两个参数的积
     */

    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @return 两个参数的商
     */

    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @param scale
     *            表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v
     *            需要四舍五入的数字
     * @param scale
     *            小数点后保留几位
     * @return 四舍五入后的结果
     */

    public static double round(double v, int scale) {
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * <p>add</p>
     * @param a
     * @param b
     * @return
     */
    public static float add(float a, float b) {

        BigDecimal b1 = new BigDecimal(a + "");
        BigDecimal b2 = new BigDecimal(b + "");

        return b1.add(b2).floatValue();

    }

    /**
     * <p>subtract</p>
     * @param a
     * @param b
     * @return
     */
    public static float subtract(float a, float b) {

        BigDecimal b1 = new BigDecimal(a + "");
        BigDecimal b2 = new BigDecimal(b + "");

        return b1.subtract(b2).floatValue();

    }

    /**
     * <p>multiply</p>
     * @param a
     * @param b
     * @return
     */
    public static float multiply(float a, float b) {

        BigDecimal b1 = new BigDecimal(a + "");
        BigDecimal b2 = new BigDecimal(b + "");

        return b1.multiply(b2).floatValue();

    }

    /**
     * <p>divide</p>
     * <p>当不整除，出现无限循环小数时，向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向上舍入, 1.55保留一位小数结果为1.6</p>
     * @param a
     * @param b
     * @return
     */
    public static float divide(float a, float b, int scale) {

        return divide(a, b, scale, BigDecimal.ROUND_HALF_UP);

    }

    /**
     * <p>divide</p>
     * @param a
     * @param b
     * @param scale
     * @param roundingMode
     * @return
     */
    public static float divide(float a, float b, int scale, int roundingMode) {

        /*
         * 通过BigDecimal的divide方法进行除法时就会抛异常的，异常如下：
         * java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result. at java.math.BigDecimal.divide(Unknown Source)
         * 解决之道：就是给divide设置精确的小数点divide(xxxxx,2, BigDecimal.ROUND_HALF_EVEN)
         * BigDecimal.ROUND_HALF_UP : 向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向上舍入, 1.55保留一位小数结果为1.6
         */

        BigDecimal b1 = new BigDecimal(a + "");
        BigDecimal b2 = new BigDecimal(b + "");

        return b1.divide(b2, scale, roundingMode).floatValue();

    }


}
