package hscsm.core.sum.account.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Description
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/3/13.
 */
public interface RefreshMappingRedisMapper{
    List<String> getInterfaceNames(@Param("interfaceName")String interfaceName);
}
