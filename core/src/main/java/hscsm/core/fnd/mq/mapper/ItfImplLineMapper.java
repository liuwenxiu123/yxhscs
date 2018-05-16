package hscsm.core.fnd.mq.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hscsm.core.fnd.mq.dto.HeadLineDto;
import hscsm.core.fnd.mq.dto.ItfImpLines;
import hscsm.core.itf.dto.BatchDto;
import hscsm.core.itf.dto.ItfArInterface;
import hscsm.core.itf.dto.QueryUtilDto;

import java.util.List;
import java.util.Map;

/**
 * Created by xieshuai on 2018/2/28.
 */
public interface ItfImplLineMapper extends Mapper<ItfImpLines> {
	
	public List<ItfImpLines> getFailLineData();

	public List<ItfImpLines> getSuccessHeadersData();

	public void batchUpdateFailDataFlags(List<HeadLineDto> headLineDtos);

	public void updateSuccessHeaderFlags(List<HeadLineDto> headLineDtos);

	public List<ItfImpLines> queryCalcedItfs(int pageSize);

	public void batchUpdateLineFlag(List<BatchDto> batchDtos);

	public List<ItfImpLines> getLineByAttr(QueryUtilDto queryUtilDto);

	public void updateTimeFlag(List<HeadLineDto> headLineDtos);

	public List<ItfImpLines> getAdd_MAL();

	public List<ItfImpLines> getAdd_MALMonth();

	public List<ItfImpLines> queryCalcedItfsByApplyNum(String applyNum);

	public List<ItfImpLines> queryCalcedItfsByTimeRange(QueryUtilDto queryUtilDto);


	public List<ItfImpLines> queryUpdatePer(List<QueryUtilDto> queryUtilDtos);

}