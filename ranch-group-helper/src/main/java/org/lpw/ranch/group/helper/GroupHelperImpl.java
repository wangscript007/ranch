package org.lpw.ranch.group.helper;

import org.lpw.ranch.util.ServiceHelperSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author lpw
 */
@Service("ranch.group.helper")
public class GroupHelperImpl extends ServiceHelperSupport implements GroupHelper {
    @Value("${ranch.group.key:ranch.group}")
    private String key;

    @Override
    protected String getKey() {
        return key;
    }
}
