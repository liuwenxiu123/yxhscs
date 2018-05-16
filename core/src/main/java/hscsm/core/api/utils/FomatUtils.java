package hscsm.core.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by shuai.xie on 18/2/28.
 */
public class FomatUtils {

    /**
     * 判断是否为日期
     * @param dateStr : 日期字符串
     * @return 日期
     */
    public Date getDate(String dateStr) {
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date result = null;

        try {
            result = datetimeFormat.parse(dateStr);
        } catch (ParseException e) {
            try {
                result = dateFormat.parse(dateStr);
            } catch (ParseException e1) {
                result = null;
            }
        }

        return result;
    }

    /**
	 * 判断日期是否为 yyyy-MM-dd 或者 yyyy-MM-dd HH:mm:ss 格式的日期
	 * @param dateStr :日期字符串
	 * @return 返回校验结果 null:传入null "true":校验格式正确 "false":校验格式错误
	 * */
    public String getDateRexp(String dateStr){
		Pattern patDateTime = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}");
		Pattern patDate = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}");
		String result = "";
		if(dateStr!=null){
			if (patDateTime.matcher(dateStr).matches()) {
				result = "true";
			} else {
				if (patDate.matcher(dateStr).matches()) {
					result = "true";
				}else {
					result = "false";
				}
			}
		}
		return result;
	}

    /**
     * 判断是否为数字
     * @param numberStr
     * @return boolean
     * @description 判断是否为数字
     */
    public boolean isNum(String numberStr) {
        return numberStr.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

	/**
	* 判断是否为整数
	* @param str 传入的字符串
	* @return 是整数返回true,否则返回false
	*/
	public boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
     * 判断字段是否为空.
     * @param str 字段值
     * @return { 空: true, 非空: false}
     */
    public boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }
		public static void main(String[] args) {

			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				System.out.println(new Date());
			} catch (Exception e) {
				e.printStackTrace();
			}
			FomatUtils fomatUtils=new FomatUtils();
			String  i="3.23";
			String i2="wqdqw";
			String i3="423423.233d";
			String i4="2342134234";
			System.out.println("日期1"+fomatUtils.getDate("2017-3-29"));
			System.out.println("日期2"+fomatUtils.getDate("2017/3/29"));
			if(fomatUtils.isNum(i)){
				System.out.println(i+"是数字");
			}else{
				System.out.println(i+"不是数字");
			}
			if(fomatUtils.isNum(i2)){
				System.out.println(i2+"是数字");
			}else{
				System.out.println(i2+"不是数字");
			}
			if(fomatUtils.isNum(i3)){
				System.out.println(i3+"是数字");
			}else{
				System.out.println(i3+"不是数字");
			}
			if(fomatUtils.isNum(i4)){
				System.out.println(i4+"是数字");
			}else{
				System.out.println(i4+"不是数字");
			}
			
			
		}
}
