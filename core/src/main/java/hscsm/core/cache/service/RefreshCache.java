package hscsm.core.cache.service;

import com.hand.hap.cache.impl.HashStringRedisCache;
import com.hand.hap.cache.impl.HashStringRedisCacheGroup;
import com.hand.hap.core.components.ApplicationContextHelper;
import hscs.ae.service.cache.AePeriodCache;
import hscs.ae.service.cache.BaseAeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * TODO HAP缓存刷新 项目上线删除
 */
@Service
public class RefreshCache extends BaseAeCache {
    @Autowired
    AePeriodCache apc;

    public void doBatchDel() {
        RefreshCache rkc = ApplicationContextHelper.getApplicationContext().getBean("refreshCache",RefreshCache.class);
        rkc.batchDel();
    }

    private void batchDel(){
        String PRE_STR = "hap";
        Set<String> set = rt.keys(PRE_STR +"*");
        set.forEach(keyStr->{
            System.out.println(keyStr);
            rt.delete(keyStr);
        });
        // 重新加载
        Map<String, HashStringRedisCache> cacheBeans = ApplicationContextHelper.getApplicationContext().getBeansOfType(HashStringRedisCache.class);
        if (cacheBeans != null) {
            cacheBeans.forEach((k, v) -> v.init());
        }
        Map<String, HashStringRedisCacheGroup> cacheBeans2 = ApplicationContextHelper.getApplicationContext().getBeansOfType(HashStringRedisCacheGroup.class);
        if (cacheBeans2 != null) {
            cacheBeans2.forEach((k, v) -> v.init());
        }
        // 期间刷新
        apc.freshAll();
    }

    @Override
    public void whenStart() {

    }
}
