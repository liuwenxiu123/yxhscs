package hscsm.core.ae.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Description
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/4/19.
 */
public interface YxAeEventProcessMapper {
    List<String> getReferenceList(@Param("batch") int batch,
                                  @Param("batchMod") int batchMod,
                                  @Param("accountingDateTo") String accountingDateTo,
                                  @Param("basicTable") String basicTable,
                                  @Param("accStatusField") String accStatusField,
                                  @Param("accDateField") String accDateField,
                                  @Param("referenceField") String referenceField,
                                  @Param("primaryKeyField") String primaryKeyField);

    String getBasicTableByCategory(@Param("eventBatchName") String eventBatchName);
}
