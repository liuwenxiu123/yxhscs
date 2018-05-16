package hscsm.core.itf.controllers;

import com.hand.hap.system.dto.ResponseData;
import hscsm.core.itf.service.IItfContractValidatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 合同取消红冲凭证生成job接口
 * TODO  测试用接口 项目上线删除
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/4/2.
 */
@Controller
@RequestMapping("/api/public")
public class ContractController {

    @Autowired
    private IItfContractValidatedService itfContractValidatedService;

    @RequestMapping(value = "/testContractJob",method = RequestMethod.GET)
    @ResponseBody
    public ResponseData testContractJob() {
        itfContractValidatedService.updateByStatus();
        ResponseData responseData = new ResponseData(true);
        responseData.setMessage("数据更新完毕");
        return responseData;
    }

}
