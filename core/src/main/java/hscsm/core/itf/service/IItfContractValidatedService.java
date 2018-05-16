package hscsm.core.itf.service;

import com.hand.hap.system.service.IBaseService;
import hscsm.core.itf.dto.ItfContractValidated;

/**
 * Description
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/4/2.
 */
public interface IItfContractValidatedService extends IBaseService<ItfContractValidated> {

    void updateByStatus();

}
