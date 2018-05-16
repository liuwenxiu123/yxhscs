package hscsm.core.sum.account.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hscs.ae.dto.AeFeedbackMessage;
import hscsm.core.sum.account.dto.BatchParam;
import hscsm.core.sum.account.dto.ResultMessage;
import hscsm.core.sum.account.dto.SumAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 总账mapper
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/3/7.
 */
public interface SumAccountMapper extends Mapper<SumAccount> {

    List<SumAccount> getSumAccountsByStatus(@Param("jeBatchName") String jeBatchName,
                                            @Param("company") String company,
                                            @Param("eventBatchId") String eventBatchId,
                                            @Param("accountingDate")String accountingDate,
                                            @Param("batchName") String batchName);

    int updateByResult(@Param("resultMessages") List<ResultMessage> resultMessages,
                       @Param("status") String status,
                       @Param("groupId") Long id);

    /**
     * 总账发布接口调用，异步结果插入，目标表:hscs_ae_feedback_message
     * @param result 对账结果异步回调信息
     */
    int insertByAsynResult(@Param("result")AeFeedbackMessage result);

    List<BatchParam> selectBatchParams(@Param("company") String company,
                                       @Param("eventBatchId") String eventBatchId,
                                       @Param("accountingDate")String accountingDate,
                                       @Param("batchName") String batchName);

}
