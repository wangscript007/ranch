package org.lpw.ranch.gps;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author lpw
 */
@Component(GpsModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GpsModel {
    static final String NAME = "ranch.gps";
}
