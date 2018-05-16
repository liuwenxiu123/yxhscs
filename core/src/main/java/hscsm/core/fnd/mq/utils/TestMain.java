package hscsm.core.fnd.mq.utils;

import hscsm.core.fnd.mq.mapper.ItfImplLineMapper;

import java.util.*;

/**
 * Created by wangxu on 2018/3/9.
 */
public class TestMain {


    public static void main(String[] argv)  throws Exception {
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE, -1);    //得到前一天
//        calendar.add(Calendar.MONTH, -3);    //得到前一个月
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH)+1;
//        System.out.println(year+ "-" + month + "" + calendar.getTime());
//        String a = "2018-01-01";

//        System.out.println(a.substring(0,a.lastIndexOf("-")));

//        Map map = new HashMap();
//        List list = new ArrayList();
//        list.add(11);
//        list.add(222);
//        map.put("aa", list);
//
//        List list1 = (List) map.get("aa");
//        list1.add(333);
//
//        List list2 = (List)map.get("aa");
//        Iterator it = map.entrySet().iterator();
//
//        while (it.hasNext()) {
//            Map.Entry entry = (Map.Entry) it.next();
//            Object key = entry.getKey();
//            Object value = entry.getValue();
//            System.out.println("key=" + key + " value=" + value);
//        }

//    Date date = new Date("2018-01-01");
//    Date date1 = new Date("2018-1-1");
//    System.out.println(" date2: " +date1);

        List <String> list = new ArrayList<>();
        list.add("1");
        list.add("11");
        list.add("12");
        list.add(0,"33");
        list.add(0,"1133");
        System.out.println(list.get(0));

    }
}
