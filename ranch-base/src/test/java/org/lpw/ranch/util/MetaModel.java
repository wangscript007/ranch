package org.lpw.ranch.util;

import org.lpw.tephra.dao.model.ModelSupport;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Table;

/**
 * @author lpw
 */
@Component(MetaModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Table(name = "t_util_meta")
public class MetaModel extends ModelSupport {
    static final String NAME = "ranch.util.meta";
}
