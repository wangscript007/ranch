package org.lpw.ranch.cache;

import org.lpw.tephra.cache.Cache;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service("ranch.cache.service")
public class CacheServiceImpl implements CacheService {
    @Inject
    private Cache cache;

    @Override
    public boolean remove(String type, String key) {
        return cache.remove(type, key) != null;
    }
}
