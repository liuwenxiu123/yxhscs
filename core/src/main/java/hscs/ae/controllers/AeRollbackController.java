package hscs.ae.controllers;

import com.hand.hap.code.rule.service.ISysCodeRuleProcessService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hscs.ae.dto.AeTfrBatchEvents;
import hscs.ae.service.impl.AeRollBackOnEventApply;
import hscs.ae.service.impl.rollback.ErrorRollBackFast;
import hscs.utils.controllers.HscsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* @author wwl
* @version 1.0
* @name AeTestController
* @description
* @date 2018/1/22
*/
@Controller
public class AeRollbackController extends BaseController {
    @Autowired
    private ErrorRollBackFast errorRollbackFast;
    @Autowired
    private ISysCodeRuleProcessService haha;

    @Autowired
    private AeRollBackOnEventApply arboeApply;

    @RequestMapping(value = "/hscs/ae/tfr/events/prevalidate")
    @ResponseBody
    public ResponseData prevalidate(HttpServletRequest request, @RequestBody List<AeTfrBatchEvents> atbeList) throws Exception {
        ResponseData rd = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        try {
            arboeApply.preValidate(atbeList,true);
        } catch (Exception e) {
            e.printStackTrace();
            rd.setSuccess(false);
            rd.setMessage(e.getMessage());
            return rd;
        }
        rd.setMessage("成功");
        return rd;
    }
    @RequestMapping(value = "/hscs/ae/tfr/events/runRollback")
    @ResponseBody
    public ResponseData runRollback(HttpServletRequest request, @RequestBody List<AeTfrBatchEvents> atbeList) throws Exception {
        ResponseData rd = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        try {
            arboeApply.reRunRollBack(requestCtx,atbeList);
        } catch (Exception e) {
            e.printStackTrace();
            rd.setSuccess(false);
            rd.setMessage(e.getMessage());
            return rd;
        }
        rd.setMessage("成功");
        return rd;
    }

    @RequestMapping(value = "/hscs/ae/tfr/events/te")
    @ResponseBody
    public ResponseData te(HttpServletRequest request,Long eventBatchId,String accountingDateFrom,String accountingDateTo,String references,String phase) throws Exception {
        ResponseData rd = new ResponseData();
        Map a= new HashMap();
        a.put("var","aaa");
        String str = haha.getRuleCode("asd",a) ;
        rd.setMessage(str);
        return rd;
    }
    // ReverseVerifyArSetOrders
    @RequestMapping(value = "/hscs/ae/tfr/events/errRoll")
    @ResponseBody
    public ResponseData errRoll(HttpServletRequest request,Long eventBatchId,String accountingDateFrom,String accountingDateTo,String references,String phase) throws Exception {
        ResponseData rd = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        final String nullStr="null";
        Date dateFrom=null;
        Date dateTo=null;
        if(!nullStr.equals(accountingDateFrom)){
            dateFrom=sdf.parse(accountingDateFrom);
        }
        if(!nullStr.equals(accountingDateTo)){
            dateTo=sdf.parse(accountingDateTo);
        }
//        Long eventBatchId = 117L;
        int a = 0;
        try {
            a = errorRollbackFast.entry(requestCtx,eventBatchId,dateFrom,dateTo,references,phase);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = HscsUtil.getRootCause(e).getMessage();
            rd.setMessage(Optional.ofNullable(errorMessage).orElse("发生错误"));
            return rd;
        }
        if(a==0){
            rd.setMessage("根据条件未找到回滚数据");
        }else{
            rd.setMessage("成功回滚"+a+"条");
        }
        return rd;
    }
}
