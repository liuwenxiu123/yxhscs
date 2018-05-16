package hscsm.core.itf.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hscsm.core.itf.dto.ContractData;
import hscsm.core.itf.dto.ItfContractValidated;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/4/2.
 */
public interface ItfContractValidatedMapper extends Mapper<ItfContractValidated> {

    /**
     * 获取需要更新的数据
     * @return 需要更新的数据信息
     */
    List<ContractData> getContractDataByStatus();

    /**
     * 修改接口行表的数据状态，ATTRIBUTE2
     * @param status 修改的目标状态
     * @return 更新记录条数
     */
    int updateItfImpLineStatus(@Param("applyNum") String applyNum, @Param("status") String status);

    /**
     * 更新目标表中的合同数据
     * @param contractData 跟新的数据
     * @return 更细记录条数
     */
    int updateContractValidated(@Param("contractData") ContractData contractData);

}
