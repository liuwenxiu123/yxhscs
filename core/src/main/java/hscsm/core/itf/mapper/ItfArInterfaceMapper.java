package hscsm.core.itf.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hscsm.core.fnd.mq.dto.HeadLineDto;
import hscsm.core.itf.dto.BatchDto;
import hscsm.core.itf.dto.ItfArInterface;
import hscsm.core.itf.dto.QueryUtilDto;

import java.util.List;

public interface ItfArInterfaceMapper extends Mapper<ItfArInterface>{

    public void batchInsertArInterface(List<ItfArInterface> itfArInterfaces);

    public List<ItfArInterface> queryNeedUpdateArInterface ();

    public List<ItfArInterface> querUpdateActualIncome (List<QueryUtilDto> queryUtilDtos);

    public void updateArInterfaceData (List<ItfArInterface> ItfArInterface);

    public List<ItfArInterface> getItfArByIds(List<QueryUtilDto> QueryUtilDtos);

    public void updateArPaymentStatus (List<QueryUtilDto> queryUtilDtos);

    public List<ItfArInterface> queryInShallPeriod(String date);

    public List<ItfArInterface> queryActualIncome(String date);

    public List<ItfArInterface> queryMonthRaw(String dataStr);

    public void updateMonthFlag(List<ItfArInterface> itfArInterList);

    public void updatePreValue(List<ItfArInterface> itfArInterList);

}