package hscsm.core.itf.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import hscsm.core.fnd.mq.dto.HeadLineDto;
import hscsm.core.itf.dto.ItfArInterface;
import hscsm.core.itf.dto.QueryUtilDto;

import java.util.List;

public interface IItfArInterfaceService extends IBaseService<ItfArInterface>, ProxySelf<IItfArInterfaceService>{

    void addCalcArInterface(int pageSize,String scheme, String applyNum);

    void updateArInterface();

    List<ItfArInterface> queryNeedUpdateArInterface();

    void updateArInterfaceMonthEnd(String deadDate);

    void updateArActualIncom(String date);

    void updatePer(List<QueryUtilDto> queryUtilDtos);

}