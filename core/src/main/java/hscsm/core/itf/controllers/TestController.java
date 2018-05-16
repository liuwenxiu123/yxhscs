package hscsm.core.itf.controllers;

import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hscsm.core.api.dto.WsResponse;
import hscsm.core.api.service.IItfImpDataService;
import hscsm.core.fnd.mq.service.ImportDataService;
import hscsm.core.fnd.mq.service.MqService;
import hscsm.core.itf.dto.ItfArInterface;
import hscsm.core.itf.dto.QueryUtilDto;
import hscsm.core.itf.mapper.QueryUtilMapper;
import hscsm.core.itf.service.IItfArInterfaceService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeoutException;

@Controller
@RequestMapping("/test")
public class TestController extends BaseController {

    private static int DEFAULT_SIZE=80000;

    @Autowired
    private IItfArInterfaceService iItfArInterfaceService;

    @Autowired
    ImportDataService importDataService;

    @Autowired
    QueryUtilMapper queryUtilMapper;
    @Autowired
    MqService mqService;

    @RequestMapping(value = "/test1")
    @ResponseBody
    public String test1(HttpServletRequest request) {
        System.out.println("====================================================================");
        iItfArInterfaceService.addCalcArInterface(DEFAULT_SIZE,"B", null); // B方案 后期数据定时导入
        System.out.println("====================================================================");
        return "hello world";
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public String update(HttpServletRequest request) {
        System.out.println("====================================================================");
        iItfArInterfaceService.updateArInterface();
        System.out.println("====================================================================");
        return "update!!!";
    }

    @RequestMapping(value = "/monthUpdate")
    @ResponseBody
    public String monthUpdate(HttpServletRequest request) {

        System.out.println("====================================================================");
//        String [] dateList = {"2017-06-30","2017-07-31", "2017-08-31", "2017-09-30", "2017-10-31", "2017-11-30", "2017-12-31","2018-01-31", "2018-02-28", "2018-03-31"};
        String[] dateList = {"2017-06-30"};
        for (int i = 0; i < dateList.length; i++) {
            iItfArInterfaceService.updateArInterfaceMonthEnd(dateList[i]);
        }
        System.out.println("====================================================================");
        return "monthUpdate!!!";
    }


    @RequestMapping(value = "/mq")
    @ResponseBody
    public String mq(HttpServletRequest request) throws IOException, TimeoutException {
        System.out.println("====================================================================");
        try {
            importDataService.sendAllFailImportDatas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "mq!!!";
    }

    @RequestMapping(value = "/activeIncome")
    @ResponseBody
    public String activeIncome(HttpServletRequest request) throws IOException, TimeoutException {
        System.out.println("====================================================================");
        String[] dateList = {"2017-06"};
//        String [] dateList = {"2017-07", "2017-08", "2017-09", "2017-10", "2017-11","2017-12","2018-01","2018-02","2018-03", };
        for (int i = 0; i < dateList.length; i++) {
            iItfArInterfaceService.updateArActualIncom(dateList[i]);
        }
        return "activeIncome!!!";
    }

    @RequestMapping(value = "/testVal")
    @ResponseBody
    public String testVal(HttpServletRequest request) throws IOException, TimeoutException {
        mqService.sendMsg("", "");
        return "activeIncome!!!";
    }


    @RequestMapping(value = "/disabledData")
    @ResponseBody
    public ResponseData disabledData(HttpServletRequest request) {
        queryUtilMapper.disabledData();
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setMessage("数据都已作废~");
        return responseData;
    }
    @RequestMapping(value = "/addToArInit")
    @ResponseBody
    public ResponseData taat1(HttpServletRequest request, int pageSize, int time) {
        for (int i =0; i <time ; i++) {
            iItfArInterfaceService.addCalcArInterface(pageSize,"A", null);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setMessage("插入成功已经到目标表中!!! 该按钮已经废弃~");
        return responseData;
    }
    @RequestMapping(value = "/addToAr")
    @ResponseBody
    public ResponseData tt1(HttpServletRequest request, int pageSize) {
        iItfArInterfaceService.addCalcArInterface(pageSize,"B", null);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setMessage("从行表中插入已经到目标表中!!!");
        return responseData;
    }


    @RequestMapping(value = "/updateEveryDay") // 每天更新的
    @ResponseBody
    public ResponseData tt2(HttpServletRequest request) throws IOException, TimeoutException {
        iItfArInterfaceService.updateArInterface(); // 每天的更新逻辑
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setMessage("每天的更新完成!!!!");
        return responseData;
    }

    @RequestMapping(value = "/monthAndActCalc") //
    @ResponseBody
    public ResponseData tt3(HttpServletRequest request, @RequestParam(value = "date1", required = true) final String date1) throws IOException, TimeoutException {
        iItfArInterfaceService.updateArInterfaceMonthEnd(date1); // 每月的
        iItfArInterfaceService.updateArActualIncom(date1.split("-")[0] + "-" + date1.split("-")[1]); // 计算每个月的收入计提
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setMessage("每月更新,收入计提计算完成!!!!");
        return responseData;
    }


    @RequestMapping(value = "/updateRangeMonth")
    @ResponseBody
    public ResponseData updateRangeMonth(HttpServletRequest request, @RequestParam(value = "dateFrom", required = true) final String dateFrom, @RequestParam(value = "dateTo", required = true) final String dateTo) throws IOException, TimeoutException {

        List<String> aa = null;
        try {
            aa = this.getMonthBetween(dateFrom, dateTo);
            for (int i = 0; i < aa.size(); i++) {
                String dateStr = aa.get(i);
                iItfArInterfaceService.updateArInterfaceMonthEnd(dateStr); // 每月的
                iItfArInterfaceService.updateArActualIncom(dateStr.split("-")[0] + "-" + dateStr.split("-")[1]); // 计算每个月的收入计提
                System.out.println(dateStr+ "数据已经更新计算........................");
            }
        } catch (ParseException e) {
            throw new RuntimeException("失败!" + e.getStackTrace());
        }

        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setMessage("从" + dateTo + "到" + dateFrom + "时间段的,收入计提计算完成!!!!");
        return responseData;
    }


    private List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化为年月
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        max.add(Calendar.MONTH,1);
        max.setTime(max.getTime());
        Calendar curr = min;
        while (curr.before(max)) {
            int lastDay = curr.getActualMaximum(Calendar.DAY_OF_MONTH);
            //设置日历中月份的最大天数
            curr.set(Calendar.DAY_OF_MONTH, lastDay);
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }
        return result;
    }


    @RequestMapping(value = "/getTotal")
    @ResponseBody
    public String getTotal(HttpServletRequest request) {
        return queryUtilMapper.getArToatl().getAttribute2();
    }




    @RequestMapping(value = "/importByApplyNum") //
    @ResponseBody
    public ResponseData importByApplyNum(HttpServletRequest request, @RequestParam(value = "applyNum", required = true) final String applyNum) throws IOException, TimeoutException {
        iItfArInterfaceService.addCalcArInterface(1,"A", applyNum);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setMessage("插入完成!!!!");
        return responseData;
    }



    @RequestMapping(value = "/importByTimeRange") //
    @ResponseBody
    public ResponseData importByTimeRange(HttpServletRequest request, @RequestParam(value = "timeRange", required = true) final String timeRange) throws IOException, TimeoutException {
        iItfArInterfaceService.addCalcArInterface(1,"A", timeRange);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setMessage("插入完成!!!!");
        return responseData;
    }


    @RequestMapping(value = "/updatePre") //
    @ResponseBody
    public ResponseData updatePre(HttpServletRequest request, @RequestParam(value = "dataStr", required = true) final String dataStr) throws IOException, TimeoutException {

        String [] tt = dataStr.split("@");
        List<QueryUtilDto> queryUtilDtos = new ArrayList<>();
        for (int i=0; i< tt.length ;i++) {
            QueryUtilDto queryUtilDto = new QueryUtilDto();
            queryUtilDto.setAttribute1(tt[i]);
            queryUtilDtos.add(queryUtilDto);
        }
        iItfArInterfaceService.updatePer(queryUtilDtos);
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setMessage("申请编号:"+dataStr+",更新完成!!!!");
        return responseData;
    }


}

