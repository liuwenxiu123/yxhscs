package hscsm.core.api.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hscsm.core.api.dto.WsAnalysis;

/**
 * Created by xieshuai on 2018/2/28.
 */
public interface WsAnalysisMapper extends Mapper<WsAnalysis> {
	
	public void insertWsTable(WsAnalysis wsTableDemo);

}