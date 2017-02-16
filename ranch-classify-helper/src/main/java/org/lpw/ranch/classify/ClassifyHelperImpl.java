package org.lpw.ranch.classify;

import org.lpw.ranch.util.ServiceHelperSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author lpw
 */
@Service("ranch.classify.helper")
public class ClassifyHelperImpl extends ServiceHelperSupport implements ClassifyHelper {
    @Value("${ranch.classify.key:ranch.classify}")
    private String key;

    @Override
    protected String getKey() {
        return key;
    }
}
