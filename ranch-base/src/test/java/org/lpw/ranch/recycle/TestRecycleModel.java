package org.lpw.ranch.recycle;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author lpw
 */
@Component(TestRecycleModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = TestRecycleModel.NAME)
@Table(name = "t_recycle")
public class TestRecycleModel extends RecycleModelSupport {
    static final String NAME = "ranch.recycle";
}
