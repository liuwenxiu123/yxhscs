package hscsm.core.sum.account.service.impl;

import hitf.itf.cache.DefinationLovValueCache;
import hitf.itf.cache.MappingDataValueCache;
import hitf.itf.dto.ItfDefinationHeaders;
import hitf.itf.mapper.ItfDefinationHeadersMapper;
import hscsm.core.sum.account.mapper.RefreshMappingRedisMapper;
import hscsm.core.sum.account.service.IRefreshMappingRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * Description
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/3/13.
 */
@Service
public class RefreshMappingRedisServiceImpl implements IRefreshMappingRedisService {

    @Autowired
    private ItfDefinationHeadersMapper itfDefinationHeadersMapper;

    @Autowired
    private MappingDataValueCache mappingDataValueCache;

    @Autowired
    private DefinationLovValueCache definationLovValueCache;

    @Autowired
    private RefreshMappingRedisMapper refreshMappingRedisMapper;

    private Logger logger = LoggerFactory.getLogger(hscsm.core.sum.account.service.impl.RefreshMappingRedisServiceImpl.class);

    @Override
    public void refreshRedis(String name) {
        List<String> interfaceNames = refreshMappingRedisMapper.getInterfaceNames(name);
        this.logger.info("名字带有[" + name + "]的接口查询,总计[" + interfaceNames.size() +"]个接口缓存，现在开始执行!");
        for(String interfaceName : interfaceNames){
            ItfDefinationHeaders definationHeaders = new ItfDefinationHeaders();
            definationHeaders.setInterfaceName(interfaceName);
            List<ItfDefinationHeaders> definationHeadersList = this.itfDefinationHeadersMapper.select(definationHeaders);
            if (definationHeadersList != null) {
                Iterator var5 = definationHeadersList.iterator();

                while(var5.hasNext()) {
                    ItfDefinationHeaders headers = (ItfDefinationHeaders)var5.next();
                    this.mappingDataValueCache.updatedefinationMapDataValue(headers.getHeaderId());
                    this.definationLovValueCache.updateDefinationLovValue(headers);
                }
            }
            this.logger.info("接口[" + interfaceName + "]缓存执行完毕");
        }

    }
}
