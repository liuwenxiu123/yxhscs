package hscs.pub.mapper;

import hscs.pub.dto.PubSoaSegmentValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/3/21.
 */
public interface YxPubSoaSegmentValueMapper  {
    List<PubSoaSegmentValue> selectSoaIcValidValue(@Param("description") String description,
                                                   @Param("segmentValue") String segmentValue,
                                                   @Param("valuesetName") String valuesetName,
                                                   @Param("fatherValue") String fatherValue);
}
