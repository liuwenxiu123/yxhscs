package hscs.ae.util;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author chao.cao@hand-china.com
 * @version 1.0
 * @name RandomNumUtils
 * @description:chao.随机生成字符串
 * @date 2017年12月28日
 */
public class RandomNumUtils {
    private static final ArrayList<Character> randomArray = new ArrayList<>();
    private static final Random RANDOM=new Random();
    /**
     * 数字长度
     */
    private static final int NUMBER_SIZE=10;
    /**
     * 字母长度
     */
    private static final int CHAR_SIZE=26;
    private static final char ONE = '0';
    private static final char NINE = '9';
    private static final char A = 'A';
    private static final char Z = 'Z';
    static {
        //添加数字到randomArray
        for(char i=ONE;i<=NINE;i++){
            randomArray.add(i);
        }
        //添加字母到randomArray
        for(char i=A;i<=Z;i++){
            randomArray.add(i);
        }
    }
    /**
     * 生成一个size位的随机字符串，只包含字母
     * @param size
     * @return
     */
    public static String randomChars(int size){
        String result="";
        for(int i=0;i<size;i++){
            int randomNum=RANDOM.nextInt(CHAR_SIZE);
            result+=randomArray.get(NUMBER_SIZE+randomNum);
        }
        return result;
    }
    /**
     * 生成一个size位的随机字符串，只包含数字
     * @param size
     * @return
     */
    public static String randomNumbers(int size){
        String result="";
        for(int i=0;i<size;i++){
            int randomNum=RANDOM.nextInt(NUMBER_SIZE);
            result+=randomArray.get(randomNum);
        }
        return result;
    }
    /**
     * 生成一个size位的随机字符串，包含字母和数字
     * @param size
     * @return
     */
    public static String randomString(int size){
        String result="";
        for(int i=0;i<size;i++){
            int randomNum=RANDOM.nextInt(randomArray.size());
            result+=randomArray.get(randomNum);
        }
        return result;
    }

}
