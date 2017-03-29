package org.lpw.ranch.message.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author lpw
 */
@Service("ranch.message.helper")
public class MessageHelperImpl implements MessageHelper {
    @Value("${ranch.message.key:ranch.message}")
    private String key;
}
