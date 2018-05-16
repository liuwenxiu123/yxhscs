package hscsm.core.sum.account.service;

import hscs.ae.dto.AeFeedbackMessage;
import hscsm.core.sum.account.dto.EbsReturnData;
import java.util.List;

/**
 * Description
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/3/7.
 */
public interface ISumAccountService {
    void sendSumAccountToEBS(String company, String eventBatchId, String accountingDate, String accountName);

    EbsReturnData insertByAsynResult(List<AeFeedbackMessage> feedbackMessages) throws Exception;
}
