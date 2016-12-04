package org.lpw.ranch.model;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author lpw
 */
@Component(TestModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = TestModel.NAME)
@Table(name = "t_model")
public class TestModel extends ModelSupport {
    static final String NAME = "ranch.model";
}
