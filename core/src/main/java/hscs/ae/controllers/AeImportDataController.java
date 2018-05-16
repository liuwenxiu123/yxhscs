package hscs.ae.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import hscs.ae.cache.AeEventBatchesCache;
import hscs.ae.dto.*;
import hscs.ae.exception.AeNoDadaException;
import hscs.ae.mapper.AeEventBatchesMapper;
import hscs.ae.mapper.AeEventLinesMapper;
import hscs.ae.service.*;
import hscs.ae.util.RandomNumUtils;
import hscs.utils.controllers.ExcelUtil;
import hscs.utils.controllers.HscsUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static hscs.utils.controllers.HscsConstants.Arith.TEN;
import static hscs.utils.controllers.HscsConstants.Public.*;

/**
 * @author yihao.xing@hand-china.com
 * @version 1.0
 * @name AeImportDataController
 * @description AE_上传数据
 */
@Controller
public class AeImportDataController extends BaseController {
    private static final String CSV ="csv" ;
    private static final String EXCEL ="excel" ;
    private static final String ERROR_TYPE ="errorType" ;
    private static final String MYSQL ="mysql" ;
    private static Logger logger = LoggerFactory.getLogger(AeImportDataController.class);

    @Autowired
    private AeEventLinesMapper eventLinesMapper;

    @Autowired
    private IAeEventHeadersService aeEventHeadersService;

    @Autowired
    private AeEventBatchesMapper aeEventBatchesMapper;

    @Autowired
    private IAeTfrInterfaceService aeTfrInterfaceService;

    @Autowired
    private IAeTfrLineInterfaceService aeTfrLineInterfaceService;

    @Autowired
    private IAeEventProcessService iAeEventProcessService;


    @Autowired
    private ICodeService codeService;

    @Autowired
    private AeEventBatchesCache aeEventBatchesCache;


    @Autowired
    private IAeEventBatchesService aebService;
    /*使用注解获取框架线程池*/
    @Autowired
    @Qualifier("taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Value("${db.type}")
    private String dbtype;//获取当前数据库类型


    private Integer startRow = 1;

    /**
     * 模板下载
     *
     * @param request
     * @return Map resultMap
     * @throws Exception
     * @author yihao.xing@hand-china.com
     * @returnType Map
     */
    @RequestMapping(value = "/ae/import/downLoadExcelTemplate")
    public Map<String, String> downloadExcelTemplate(HttpServletRequest request, HttpServletResponse response) {
        //返回信息
        Map<String, String> resultMap = new HashMap<String, String>();
        //事件接口ID
        String headerId = request.getParameter("headerId");
        //来源系统代码
        String systemCode = request.getParameter("systemCode");
        //导出类型
        String testType = request.getParameter("testType");

        try {
            if (StringUtils.isNotEmpty(headerId)
                    && StringUtils.isNotEmpty(systemCode) && StringUtils.isNotEmpty(testType)) {
                IRequest iRequest = createRequestContext(request);
                List<AeTfrLineInterface> templateDataList = new ArrayList<AeTfrLineInterface>();
                List<AeEventLines> templateList = new ArrayList<AeEventLines>();
                //快码取值对应的DTO——CodeValue
                List<CodeValue> codeValueList = new ArrayList<CodeValue>();
                boolean isSystemCode = false;

                Date nowDate = new Date();
                SimpleDateFormat matter = new SimpleDateFormat("yyyyMMddHHmmss");
                String fileName = matter.format(nowDate).toString();

                HSSFWorkbook workbook = null;
                Sheet sheet = null;
                HSSFCellStyle style = null;
                Row row = null;

                //excel
                ByteArrayOutputStream baos = null;
                InputStream is = null;
                ServletOutputStream out = null;
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;

                //csv
                //CsvWriter writer = null;
                FileOutputStream fos = null;
                OutputStreamWriter osw = null;
                BufferedWriter bw = null;


                //获取事件接口定义信息
                templateList = eventLinesMapper.getTemplateField(Long.valueOf(headerId));
                //通过快码名称，获取对应快码所有值集
                codeValueList = codeService.selectCodeValuesByCodeName(iRequest, "HSCS.GB.SOURCE_SYS");

                //接口定义头表
                /*AeEventHeaders aeEventHeaders = new AeEventHeaders();
                aeEventHeaders.setEventBatchId(Long.valueOf(headerId));*/
                AeEventBatches aeEventBatches = new AeEventBatches();
                aeEventBatches.setEventBatchId(Long.valueOf(headerId));
                //根据接口头表ID查询对应的接口头信息
                //List<AeEventHeaders> eventHeadersList = aeEventHeadersService.select(iRequest, aeEventHeaders, 1, 99999999);
                List<AeEventBatches> eventHeadersList = aeEventBatchesMapper.select(aeEventBatches);

                fileName = systemCode + "_" + eventHeadersList.get(0).getEventBatchName() + "_" + fileName;
                /**
                 * 效验快码中是否存在有效的系统代码
                 */
                if (CollectionUtils.isNotEmpty(codeValueList)) {
                    for (CodeValue codeValue : codeValueList) {
                        if (StringUtils.isNotBlank(codeValue.getValue())
                                && codeValue.getValue().equals(systemCode)) {
                            isSystemCode = true;
                        }
                    }
                }

                    /*用于分别下载Excel、CSV文件*/
                if (CollectionUtils.isNotEmpty(templateList)) {

                    try {
                        if (EXCEL.equals(testType)) {//Excel文件
                            workbook = new HSSFWorkbook();
                            sheet = workbook.createSheet((eventHeadersList.get(0) != null && StringUtils.isNotEmpty(eventHeadersList.get(0).getEventBatchName()) ? eventHeadersList.get(0).getEventBatchName() : ""));
                            sheet.setDefaultColumnWidth((short) 15);

                            style = workbook.createCellStyle();
                            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
                            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                            //对前三列进行列名设置
                            row = sheet.createRow((short) 0);

                            if (isSystemCode) {
                                Cell cell2 = row.createCell(0);
                                cell2.setCellStyle(style);
                                cell2.setCellValue("*来源系统代码");
                            }

                            Cell cell3 = row.createCell(1);
                            cell3.setCellStyle(style);
                            cell3.setCellValue("*事件名称");

                            Cell cell4 = row.createCell(2);
                            cell4.setCellStyle(style);
                            cell4.setCellValue("*单据编码");

                            Cell cell5 = row.createCell(3);
                            cell5.setCellStyle(style);
                            cell5.setCellValue("*入账日期");

                            Cell cell0 = row.createCell(4);
                            cell0.setCellStyle(style);
                            cell0.setCellValue("*核算主体");

                            Cell cell1 = row.createCell(5);
                            cell1.setCellStyle(style);
                            cell1.setCellValue("*是否冲销");


                            /**
                             * 动态设置列名
                             */
                            int i = 5;
                            for (AeEventLines str : templateList) {
                                i++;
                                Cell cell = row.createCell(i);

                                if ("Y".equals(str.getRequiredFlag())) {
                                    //当前字段必输时，带*并且背景颜色变化
                                    HSSFCellStyle style1 = workbook.createCellStyle();
                                    style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                                    style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                                    style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
                                    style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
                                    style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                                    style1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                                    style1.setFillPattern(CellStyle.SOLID_FOREGROUND);
                                    cell.setCellStyle(style1);
                                    cell.setCellValue("*" + str.getTitleText());
                                } else {
                                    HSSFCellStyle style1 = workbook.createCellStyle();
                                    style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                                    style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                                    style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
                                    style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
                                    style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                                    cell.setCellStyle(style1);
                                    cell.setCellValue(str.getTitleText());
                                }

                            }
                            /**
                             * 将Excel中某些列的10行数据进行赋值
                             */
                            for (int j = 1; j <= TEN; j++) {
                                Row tempRow = sheet.createRow((short) j);
                                    /*tempRow.createCell(0).setCellStyle(style);
                                    tempRow.createCell(0).setCellValue("");*/

                                tempRow.createCell(0).setCellStyle(style);
                                tempRow.createCell(0).setCellValue(systemCode);

                                tempRow.createCell(1).setCellStyle(style);
                                tempRow.createCell(1).setCellValue((eventHeadersList.get(0) != null && StringUtils.isNotEmpty(eventHeadersList.get(0).getEventBatchName()) ? eventHeadersList.get(0).getEventBatchName() : ""));

                            }

                            baos = new ByteArrayOutputStream();
                            workbook.write(baos);
                            /**
                             * 在这里需要设置编码格式，否则会出现乱码的情况
                             */
                            byte[] content = baos.toByteArray();
                            is = new ByteArrayInputStream(content);
                            response.reset();
                            bis = new BufferedInputStream(is);//将数据写入io流

                        } else if (CSV.equals(testType)) {//CSV
                        //else if ("csv".equals(testType)) {//CSV文件
                                /*CSV*/
                            File csvFile = null;
                            csvFile = new File(fileName + "."+testType);
                            fos = new FileOutputStream(csvFile);
                            osw = new OutputStreamWriter(fos, "GBK");
                            bw = new BufferedWriter(osw);

                            for (int i = 0; i <= TEN; i++) {
                                StringBuilder sb = new StringBuilder();
                                if (i == 0) {
                                    bw.append("*来源系统代码").append(",").append("*事件名称").append(",").append("*单据编码").append(",").append("*入账日期").append(",").append("*核算主体").append(",").append("*是否冲销");
                                    /*动态设置列名*/
                                    for (AeEventLines str : templateList) {
                                        if ("Y".equals(str.getRequiredFlag())) {
                                            bw.append(",").append("*" + str.getTitleText());
                                        } else {
                                            bw.append(",").append(str.getTitleText());
                                        }
                                    }
                                } else {
                                    bw.append(systemCode).append(",").append((eventHeadersList.get(0) != null && StringUtils.isNotEmpty(eventHeadersList.get(0).getEventBatchName()) ? eventHeadersList.get(0).getEventBatchName() : ""));

                                }
                                bw.newLine();//重新一行编辑，类似于换行（BufferedWriter独有）
                            }
                            bw.flush();

                            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(csvFile.getName(), "UTF-8"));

                            response.setHeader("Content-Length", String.valueOf(csvFile.length()));

                            bis = new BufferedInputStream(new FileInputStream(csvFile));
                        }

                        //防止在不同浏览器出现乱码情况
                        ExcelUtil.compatibleFileName(request, response, fileName, testType);
                        out = response.getOutputStream();
                        bos = new BufferedOutputStream(out);//将数据通过浏览器输出
                        byte[] buff = new byte[2048];

                        int bytesRead;
                        // Simple read/write loop.
                        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                            bos.write(buff, 0, bytesRead);
                        }
                        resultMap.put("status", "success");
                    } catch (final IOException e) {
                        throw e;
                    } finally {

                        if (bis != null) {
                            bis.close();
                        }
                        if (bos != null) {
                            bos.close();
                        }
                        if (baos != null) {
                            baos.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                        if (bw != null) {
                            bw.close();
                        }
                        if (osw != null) {
                            osw.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("status", "error");
        }
        return resultMap;
    }

    /**
     * 导入数据
     * 优化方案：将所有的效验逻辑放在同一个循环遍历(遍历Excel所有有效行数据)，各个校验方法只需执行当前行列的单元格操作即可
     *
     * @param request
     * @return Map resultMap
     * @throws Exception
     * @author yihao.xing@hand-china.com
     * @returnType Map
     */
    @RequestMapping(value = "/ae/import/saveAndCheckExcelData", method = {RequestMethod.POST})
    public ResponseData saveAndCheckExcelData(MultipartFile chooseFile, HttpServletRequest request) throws IOException, InvalidFormatException {
        ResponseData rs = new ResponseData();
        IRequest iRequest = createRequestContext(request);
        Long userId = iRequest.getUserId();//获取当前用户ID
        String userName = iRequest.getUserName();//获取当前用户名
        InputStream is = null;
        String fileParentName = chooseFile.getOriginalFilename();//获取文件名称+格式
        is = chooseFile.getInputStream();
        String suffix = chooseFile.getOriginalFilename().substring(
                chooseFile.getOriginalFilename().lastIndexOf("."));//获取文件的后缀（用于判断文件类型）
        //获取批次号
        Date nowDate = new Date();
        SimpleDateFormat matter = new SimpleDateFormat("yyyyMMddHHmmss");
        String batchNum = matter.format(nowDate).toString();
        batchNum = userName + RandomNumUtils.randomChars(6) + batchNum;

        /*ItfImpInterfaces impInterfaces = new ItfImpInterfaces();
        impInterfaces.setBatchNum(batchNum);
        List<ItfImpInterfaces> batchList = interfacesService.selectAllNumber(impInterfaces);*/
        AeTfrInterface aeTfrInterface = new AeTfrInterface();
        aeTfrInterface.setBatchNo(batchNum);
        List<AeTfrInterface> batchList = aeTfrInterfaceService.select(iRequest, aeTfrInterface, 1, 99999);

        String errorFlag = null;//存放错误信息
        //效验当前批次号是否存在重复
        if (batchList.isEmpty()) {
            if (XLSX.equals(suffix) || XLS.equals(suffix)){
                rs = importExcel(request, is, batchNum, fileParentName);
            }else {
                errorFlag = "errorType";
            }

        } else {
            if (ERROR_TYPE.equals(errorFlag)) {
                rs.setMessage("当前文件格式有误，请稍候上传数据！");
            }else {
                rs.setMessage("当前此批次号已被占用锁定，请稍候上传数据！");
            }
            rs.setSuccess(false);
            return rs;
        }

        return rs;
    }

    /**
     * 上传Excel相关逻辑
     *
     * @param request        HttpServletRequest请求
     * @param is             InputStream输入流
     * @param batchNum       批次号
     * @param fileParentName 文件名称+格式
     * @return
     * @throws IOException
     */
    public ResponseData importExcel(HttpServletRequest request, InputStream is, String batchNum, String fileParentName) throws IOException {
        ResponseData rs = new ResponseData();
        IRequest iRequest = createRequestContext(request);
        List headIdList = new ArrayList();
        StringBuffer result = new StringBuffer();
        Workbook wb = null;//根据版本选择创建Workbook的方式
        try {
            wb = ExcelUtil.getExcelWorkbook(fileParentName,is);//根据不同版本进行解析
            //获取第一个sheet页的数据
            Sheet sheet = wb.getSheetAt(0);
            //判断Excel中是否存在多个sheet页
            if (wb.getNumberOfSheets() == 1) {
                if (sheet != null) {
                    int colNum = sheet.getRow(0).getPhysicalNumberOfCells();//获取列数
                    int rows = sheet.getLastRowNum();//除去标题行，获取行数据
                    if (rows != 0) {
                        //检查是否存在空行
                        rows = ExcelUtil.getInviliadRowsItf(sheet, colNum, rows);
                        logger.info("****当前有效数据为****" + rows);

                        // TODO: 2017/11/10  逻辑效验一：效验前两列是否存在null值以及数据是否全部相同,以及前6列是否存在null值(前6列)
                        Map<Boolean, String> flag1 = ExcelUtil.getCellNumbersEqual(sheet, 0, rows);
                        Map<Boolean, String> flag2 = ExcelUtil.getCellNumbersEqual(sheet, 1, rows);
                        Map<Boolean, String> flag3 = ExcelUtil.getCellNotNull(sheet, 2, rows);
                        Map<Boolean, String> flag4 = ExcelUtil.getCellNotNull(sheet, 3, rows);
                        Map<Boolean, String> flag5 = ExcelUtil.getCellNotNull(sheet, 4, rows);
                        Map<Boolean, String> flag6 = ExcelUtil.getCellNotNull(sheet, 5, rows);

                        if (!flag1.containsKey(false) && !flag2.containsKey(false) && !flag3.containsKey(false)
                                && !flag4.containsKey(false) && !flag5.containsKey(false) && !flag6.containsKey(false)) {
                            String eventName = "";
                            String sourceSystemCode = "";
                            Row tempCells = sheet.getRow(1);
                            Cell cell2 = tempCells.getCell(1);
                            Cell cell1 = tempCells.getCell(0);
                            eventName = ExcelUtil.formatCellValueToStringUtil(cell2).trim();
                            sourceSystemCode = ExcelUtil.formatCellValueToStringUtil(cell1).trim();

                            //判断日期
                            Cell cell3=tempCells.getCell(3);
                             String accountDate= ExcelUtil.formatCellValueToStringUtil(cell3).trim();
//                             String documentNo = ExcelUtil.formatCellValueToStringUtil(cell2).trim();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                format.setLenient(false);
                                format.parse(accountDate);
                            }catch (ParseException e){
                                throw new AeNoDadaException("入账日期不为日期格式");

                            }



                            //通过快码名称，获取对应快码所有值集
                            List<CodeValue> codeValueList = codeService.selectCodeValuesByCodeName(iRequest, "HSCS.GB.SOURCE_SYS");
                            //定义一个存放快码的list，遍历获取到的快码集合，去除对应快码的值
                            List<String> codeList = new ArrayList<String>();
                            codeValueList.forEach(codeValue -> {
                                codeList.add(codeValue.getValue());
                            });

                            //通过事件名称，获取是否存在对应的判断事件批次中
                            AeEventBatches aeEventBatches = new AeEventBatches();
                            aeEventBatches.setEventBatchName(eventName);
                            List<AeEventBatches> batchesList = aeEventBatchesMapper.selectEffectiveEvent(aeEventBatches);
                            if(batchesList.size()==0){ // 根据配置没有找到事件批次，给出相应报错 add by wwl 11.28
                                throw new AeNoDadaException("当前excel数据找不到对应的事件批次信息");
                            }
                            //通过事件名称获取对应的事件批次信息（包括冻结标示）
                            AeEventBatches eventBatches = batchesList.get(0);

                            //获取当期批次下的符合条件的定义的动态列字段
                            List<AeEventLines> excelDataList = new ArrayList<AeEventLines>();
                            //excelDataList = eventLinesMapper.getTemplateField(eventBatches.getEventBatchId());

                            // 校验文件模板与系统中定义的接口的一致性
                            Map<Boolean, String> nameRequired = getCellNameRequired(eventBatches.getEventBatchId(), iRequest, sheet.getRow(0), colNum);

                            Long eventBatchId = eventBatches.getEventBatchId();//获取事件批次ID

                            //根据批次ID获取对应子事件头数据
                            AeEventHeaders aeEventHeaders = new AeEventHeaders();
                            aeEventHeaders.setEventBatchId(eventBatchId);
                            List<AeEventHeaders> aeEventHeadersList = aeEventHeadersService.select(iRequest, aeEventHeaders, 1, 99999);

//                            AeEventBatches eventBt = aeEventBatchesMapper.getAebById(eventBatchId);//查询出需要的数据，供调用程序使用
                            AeEventBatches eventBatche = aeEventBatchesCache.getValue(eventBatchId.toString());
                            if(aeEventBatches==null){
                                AeEventBatches aeEventBatches1=new AeEventBatches();
                                aeEventBatches1.setEventBatchId(eventBatchId);
                                List<AeEventBatches> ae=aebService.select(iRequest,aeEventBatches1,1,1);
                                eventBatche= ae.get(0);
                            }
                            AeEventBatches eventBt = eventBatche;

                            // TODO: 2017/11/27  逻辑效验：来源系统、事件名称是否存在；接口是否冻结；校验文件模板与系统中定义的接口的一致性
                            if (codeList.contains(sourceSystemCode) && Y.equals(eventBatches.getFrozenFlag()) && !nameRequired.containsKey(false)) {
                                if(batchesList != null && !batchesList.isEmpty()){
                                    Map<String,Boolean> fieldNoNull = new HashMap<>();//获取需要设置值的字段

                                    //插入数据
                                    String doucmentNos = saveAllNumbers(sheet, rows, colNum, iRequest, batchNum, "FILE_IMPORT",fieldNoNull,aeEventHeadersList);
                                    // TODO: 2017/11/28 将Excel数据插入interface表后，返回对应的信息（成功/失败）
                                    if (FALSE.equals(doucmentNos)) {
                                        rs.setSuccess(false);
                                        rs.setMessage("当前文件数据插入失败！");
                                        return rs;
                                    } else {
                                        rs.setMessage("当前文件数据插入成功，批次号为：" + batchNum + "，请到AE接口展示界面查看结果");
                                    }

                                    //异步执行后续的程序包 by:yihao.xing
                                    this.taskExecutor.execute(
                                            () -> {
                                                try {
                                                     iAeEventProcessService.entryToTransfer(iRequest,eventBt,null,doucmentNos,null);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                    );
                                }
                            } else {
                                if (!codeList.contains(sourceSystemCode)) {
                                    rs.setMessage("结算系统中快码“来源系统”不存在值 【"+sourceSystemCode + "】！");
                                }else if (batchesList == null) {
                                    rs.setMessage("外围接口事件分类下的有效事件名称中不存在值【"+eventName + "】！");
                                }else if (!Y.equals(eventBatches.getFrozenFlag())) {
                                    rs.setMessage("该事件名称未冻结！");
                                }else if (nameRequired.containsKey(false)) {
                                    rs.setMessage(nameRequired.get(false));
                                }

                                rs.setSuccess(false);
                                rs.setRows(headIdList);
                                return rs;
                            }

                        } else {//效验前两列是否存在null值以及数据是否全部相同
                            rs.setSuccess(false);
                            String message = "";
                            /*判断报错信息*/
                            if (flag1.containsKey(false)) {
                                 message = flag1.get(false);
                            } else if (flag2.containsKey(false))  {
                                message = flag2.get(false);
                            } else if (flag3.containsKey(false))  {
                                message = flag3.get(false);
                            } else if (flag4.containsKey(false))  {
                                message = flag4.get(false);
                            } else if (flag5.containsKey(false))  {
                                message = flag5.get(false);
                            } else if (flag6.containsKey(false))  {
                                message = flag6.get(false);
                            }
                            rs.setMessage(message);
                            return rs;
                        }

                    } else {
                        rs.setSuccess(false);
                        rs.setMessage("当前Excel文件不存在有效数据，请检验后重新上传！");
                        return rs;
                    }
                }
            } else {
                result.append("Excel中有多个sheet页\n");
                rs.setSuccess(false);
                rs.setMessage("Excel中有多个sheet页\n");
                return rs;
            }

        }catch (AeNoDadaException e){
            rs.setMessage(HscsUtil.getRootCause(e).getMessage());
            return rs;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            result.append("[Excel]数据效验逻辑存在空值，请确认数据无误\n");
            rs.setSuccess(false);
            rs.setMessage(result.toString());
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
          //  result.append("[Excel]系统错误,请联系管理员.\n");
             result.append(HscsUtil.getRootCause(e).getMessage()); // 还是抛出具体错误。 wwl 11.30
            rs.setSuccess(false);
            rs.setMessage(result.toString());
            return rs;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return rs;

    }


    /**
     * 校验文件模板与系统中定义的接口的一致性
     */
    public Map<Boolean,String> getCellNameRequired(Long eventBatchId, IRequest iRequest, Row tempCells, int colNum) {
        Map<Boolean,String> map = new HashMap<Boolean,String>();
        Boolean flag = true;
        Long headerId;
        if (eventBatchId == null) {
            map.put(false,"根据当前接口名称，无法获取到对应的接口ID！");
            return map;
        }

        //根据事件批次ID来获取所有符合条件的动态列
        List<AeEventLines> excelDataList = new ArrayList<AeEventLines>();
        excelDataList = eventLinesMapper.getTemplateField(eventBatchId);
        int col = 6;//动态列开始索引
        //比较是否存在多列数据
        if (colNum != (excelDataList.size() + col)) {
            flag = false;
            map.put(false,"当前Excel上传文件与系统事件定义的通用字段数量不一致！");
        } else {
            for (AeEventLines data : excelDataList) {
                Cell cell = tempCells.getCell(col);
                String name = ExcelUtil.formatCellValueToStringUtil(tempCells.getCell(col));
                //进行比较时，防止数据库的字符串前后存在空格，所以在进行字符串比较时，使用trim将其去除
                String text = "Y".equals(data.getRequiredFlag()) ? "*" + data.getTitleText().trim() : data.getTitleText().trim();
                if (name.equals(text)) {
                    ++col;
                    continue;
                } else {
                    flag = false;
                    map.put(false,"当前Excel模板与系统事件定义的通用字段名称/顺序不一致！错误列为第【"+(col+1)+"】列");
                    break;
                }
            }
        }
        return map;
    }

    /**
     * 按照顺序，将Excel的数据插入Interface表中
     */
    private String saveAllNumbers(Sheet sheet, int rows, int colNum, IRequest iRequest, String batchNum, String moudleCode, Map<String,Boolean> fieldNoNull, List<AeEventHeaders> aeEventHeadersList) {
        //Map<String, AeTfrInterface> map = new HashMap<String, AeTfrInterface>();//以分组条件为key的形式，存放头数据
        //List<AeTfrLineInterface> lineInterfacLlist = new ArrayList<AeTfrLineInterface>();//存放行数据list
        //Map<String, List<AeTfrLineInterface>> lineMap = new HashMap<String, List<AeTfrLineInterface>>();//以分组条件为key的形式，存放相同的行数据

        List<AeImportData> aeImportDataList = new ArrayList<AeImportData>();//存放封装的接口数据list（excel每一行数据）
        Map<String, List<AeImportData>> importDataMap = new HashMap<String, List<AeImportData>>();//将符合分组条件为key的相同数据，分组存放在map中

        Map<String, List<AeTfrInterface>> headerDataMap = new HashMap<String, List<AeTfrInterface>>();
        Map<String, List<AeTfrLineInterface>> lineDataMap = new HashMap<String, List<AeTfrLineInterface>>();

        List<AeTfrInterface> list = new ArrayList<AeTfrInterface>();//存放头数据list
        List<List<AeImportData>> allList = new ArrayList<>(); //将分批的数据封装到此list中

        StringBuilder documenNos = new StringBuilder();
        try {
            //通过子事件头ID对应Excel动态的字段
            List<AeEventLines> aeEventLinesList = eventLinesMapper.getTemplateField(aeEventHeadersList.get(0).getEventBatchId());
            int count = 0;//统计导入的行数据

            for (int i = startRow; i <= rows; i++) {
                String key = null;
                Row cells = sheet.getRow(i);
                String tempRowData = null;

            /*AeImportData aeImportData = new AeImportData();
            aeImportData.setBatchNo(batchNum);//批次号
            aeImportData.setAccountingStatus("NEW");//入账状态
            aeImportData.setImportMethod("INTERFACE_IMPORT");//导入方式
            aeImportData.setDisableFlag("N");//失效状态
            aeImportData.setLineCount(null);//行数
            aeImportData.setSourceSystem(ExcelUtil.formatCellValueToStringUtil(cells.getCell(0)));//来源系统
            aeImportData.setEventBatchName(ExcelUtil.formatCellValueToStringUtil(cells.getCell(1)));//事件名称
            aeImportData.setDocumentNo(ExcelUtil.formatCellValueToStringUtil(cells.getCell(2)));//单据号
            aeImportData.setAccountingDate( HscsUtil.str2Date( ExcelUtil.formatCellValueToStringUtil(cells.getCell(3)),null) );//入账日期
            aeImportData.setAccountingCompany(ExcelUtil.formatCellValueToStringUtil(cells.getCell(4)));//核算主体
            aeImportData.setReversalFlag(ExcelUtil.formatCellValueToStringUtil(cells.getCell(5)));//是否冲销

            key = aeImportData.getDocumentNo() + aeImportData.getAccountingDate() + aeImportData.getAccountingCompany() + aeImportData.getReversalFlag();*/

                AeTfrInterface aeTfrInterface = new AeTfrInterface();
                aeTfrInterface.setBatchNo(batchNum);//批次号
                aeTfrInterface.setAccountingStatus("NEW");//入账状态
                aeTfrInterface.setImportMethod("FILE_IMPORT");//导入方式
                aeTfrInterface.setDisableFlag("N");//失效状态
                aeTfrInterface.setLineCount(null);//行数
                aeTfrInterface.setSourceSystem(ExcelUtil.formatCellValueToStringUtil(cells.getCell(0)));//来源系统
                aeTfrInterface.setEventBatchName(ExcelUtil.formatCellValueToStringUtil(cells.getCell(1)));//事件名称
                aeTfrInterface.setDocumentNo(ExcelUtil.formatCellValueToStringUtil(cells.getCell(2)));//单据号
                aeTfrInterface.setAccountingDate( HscsUtil.str2Date( ExcelUtil.formatCellValueToStringUtil(cells.getCell(3)),null) );//入账日期
                aeTfrInterface.setAccountingCompany(ExcelUtil.formatCellValueToStringUtil(cells.getCell(4)));//核算主体
                aeTfrInterface.setReversalFlag(ExcelUtil.formatCellValueToStringUtil(cells.getCell(5)));//是否冲销

                key = aeTfrInterface.getDocumentNo() + aeTfrInterface.getAccountingDate() + aeTfrInterface.getAccountingCompany() + aeTfrInterface.getReversalFlag();

                documenNos.append(",").append(aeTfrInterface.getDocumentNo());

                int j = 6;//列数的索引
                boolean isFirst=true;//非空字段的判断，默认true，不为空
                AeTfrLineInterface aeTfrLineInterface = new AeTfrLineInterface();
                while (j < colNum) {

                    Cell cell = cells.getCell(j);
                    tempRowData = cell != null ? ExcelUtil.formatCellValueToStringUtil(cell) : "";

                    int nums = j - 5;//固定列后的动态列的位数
                    String columnName = HscsUtil.columnToCamel(aeEventLinesList.get(nums - 1).getFieldName());

                    fieldNoNull.put(columnName,true);
                    /*if(isFirst){
                        fieldNoNull.put("value"+nums,true);
                    }*/
                    Class<AeTfrLineInterface> aeTfrLineInterfaceClass = AeTfrLineInterface.class;
                    //获取所有方法名称
                    Method[] methods = aeTfrLineInterfaceClass.getDeclaredMethods();
                    //获取所有字段名称
                    Field[] fields = aeTfrLineInterfaceClass.getDeclaredFields();
                    //根据动态字段，拼接所需要的字段名称
                    for (Field field : fields) {
                        String fieldName = field.getName();//字段名称
                        if (columnName.equals(field.getName()) ) {
                            field.setAccessible(true);
                            try {
                                field.set(aeTfrLineInterface, (tempRowData != null && !"".equals(tempRowData) ) ? tempRowData : null);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    ++j;
                }
                aeTfrLineInterface.setEventName(aeEventHeadersList.get(0).getEventName());//当前行子事件名称

                // TODO: 2017/11/26  以key来进行判断，若map中存在，则直接将当前行数据存放到value中的list中，若不存在，则在map指那个添加一条新数据
            /*List<AeImportData> dataList = new ArrayList<AeImportData>();//存放Excel每行数据
            if (!importDataMap.containsKey(key)) {//当map不包含key
                dataList.add(aeImportData);//将当前行数据存放到list中
                importDataMap.put(key,dataList);//在map中新增一条数据
            } else {//若已存在对应key，则在对应的value中新增当前行数据
                importDataMap.get(key).add(aeImportData);
            }*/

                // TODO: 2017/11/27  将当前Excel数据进行分组处理，将其放在map中
                List<AeTfrInterface> headerList = new ArrayList<AeTfrInterface>();//存放Excel每行数据
                if (!headerDataMap.containsKey(key)) {//当map不包含key
                    headerList.add(aeTfrInterface);//将当前行数据存放到list中
                    headerDataMap.put(key,headerList);//在map中新增一条数据
                } else {//若已存在对应key，则在对应的value中新增当前行数据
                    headerDataMap.get(key).add(aeTfrInterface);
                }

                // TODO: 2017/11/27  将当前Excel数据进行分批处理，存到封装的map中
                List<AeTfrLineInterface> lineList = new ArrayList<AeTfrLineInterface>();//存放Excel每行数据
                if (!lineDataMap.containsKey(key)) {//当map不包含key
                    lineList.add(aeTfrLineInterface);//将当前行数据存放到list中
                    lineDataMap.put(key,lineList);//在map中新增一条数据
                } else {//若已存在对应key，则在对应的value中新增当前行数据
                    lineDataMap.get(key).add(aeTfrLineInterface);
                }

                isFirst=false;


            }

            // TODO: 2017/11/27  将分组后的头行map进行遍历，然后进行插入
            //将分组好的数据进行插入
            if (!headerDataMap.isEmpty()) {
                headerDataMap.forEach((headerKey,list1) -> {//遍历头数据
                    list1.get(0).setLineCount(Long.valueOf(list1.size()));//设置每一批头数据的行数
                    AeTfrInterface aeTfrInterface = aeTfrInterfaceService.insertSelective(iRequest, list1.get(0));

                    lineDataMap.forEach((lineKey,list2) -> {//遍历行数据
                        if (headerKey.equals(lineKey)) {//判断是否属于同一批数据
                            //aeTfrLineInterfaceService.insertAll(list2, aeTfrInterface.getTfrInterfaceId(), fieldNoNull);
                            if (MYSQL.equals(dbtype)) {
                                aeTfrLineInterfaceService.insertAll(list2, aeTfrInterface.getTfrInterfaceId(), fieldNoNull);
                            } else {
                                list2.get(0).setTfrInterfaceId(aeTfrInterface.getTfrInterfaceId());
                                aeTfrLineInterfaceService.insertAllOracle(list2, aeTfrInterface.getTfrInterfaceId(), fieldNoNull);
                            }
                        }
                    });

                });

            }

        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }



        logger.debug("*********导入Excel头数据为 {} ：" + headerDataMap.size() + "*****行数据为 {} ："+ lineDataMap.size());
        if(documenNos !=null && documenNos.toString().length()>1){
            return documenNos.toString().substring(1);
        }else{
            return "false";
        }
    }



}