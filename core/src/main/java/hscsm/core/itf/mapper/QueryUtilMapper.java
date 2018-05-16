package hscsm.core.itf.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hscsm.core.fnd.mq.dto.HeadLineDto;
import hscsm.core.itf.dto.ItfArInterface;
import hscsm.core.itf.dto.QueryUtilDto;
import org.opensaml.xml.signature.Q;

import java.util.List;
import java.util.Map;

public interface QueryUtilMapper extends Mapper<QueryUtilDto>{

    public QueryUtilDto queryAttrData(QueryUtilDto queryUtilDto);

    public QueryUtilDto queryCollectDate(QueryUtilDto queryUtilDto);

    public QueryUtilDto getAttrByTwoInterface(String applyNum);

    public List getAttrByTwoInterfaceByList(List<QueryUtilDto> queryUtilDtos);

    public QueryUtilDto getContractStatus(String applyNum);

    public QueryUtilDto getApplicateStatus(String applyNum);

    public QueryUtilDto getSeriveFunction(String applyNum);

    public List getSeriveFunctionList(List<QueryUtilDto> queryUtilDtos);

    public void updateContractStatus(List<QueryUtilDto> queryUtilDtos);

    public void updateApplicationStatus(List<QueryUtilDto> queryUtilDtos);

    public void updateReapplyStatus(List<QueryUtilDto> queryUtilDtos);

    public void updateCollectDate(List<QueryUtilDto> queryUtilDtos);

    public QueryUtilDto getArToatl();

    public void disabledData();


}