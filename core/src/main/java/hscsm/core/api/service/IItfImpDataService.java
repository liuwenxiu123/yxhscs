package hscsm.core.api.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import hscsm.core.api.dto.WsAnalysis;
import hscsm.core.api.dto.WsResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by xieshuai on 2018/2/28.
 */

public interface IItfImpDataService extends IBaseService<WsAnalysis>, ProxySelf<IItfImpDataService> {
    /**
     *做数据转换
     */
    public List<WsResponse> dealImpInterfaceMap(List<Map<String,Object>> impItfMapList) throws Exception;
}
