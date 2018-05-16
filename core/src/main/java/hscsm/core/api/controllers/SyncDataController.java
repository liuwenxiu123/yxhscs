package hscsm.core.api.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import hscsm.core.api.dto.WsResponse;
import hscsm.core.api.service.IItfImpDataService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xieshuai on 2018/2/26.
 */

@Controller
@RequestMapping("/api/public")
public class SyncDataController extends BaseController implements BeanFactoryAware {

    @Autowired
    private IItfImpDataService itfImpDataService;
    private BeanFactory beanFactory;

    @PostMapping({"/impdata"})
    public List<WsResponse> impdata(@RequestBody List<Map<String,Object>> impInterfaces, HttpServletRequest request) {
        List<WsResponse> list=new ArrayList<>();
        try{
          list=itfImpDataService.dealImpInterfaceMap(impInterfaces);
      }catch (Exception e){
          //报文解析错误
          WsResponse wsResponse=new WsResponse();
          wsResponse.setSourceSystem("");
          wsResponse.setInterfaceName("");
          wsResponse.setBatchNum("");
          wsResponse.setSyncStatus("E");
          wsResponse.setErrorCode("E10001");
          wsResponse.setErrorMessage("报文解析异常!");
          wsResponse.setUniqueCode(null);
          list.add(wsResponse);
      }
        return list;
    }


   @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}

