package org.lpw.ranch.util;

import org.springframework.stereotype.Service;

/**
 * @author lpw
 */
@Service("ranch.util.service-helper")
public class ServiceHelperImpl extends ServiceHelperSupport {
    @Override
    protected String getKey() {
        return "key";
    }
}
