package org.lpw.ranch.audit;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author lpw
 */
@Component(TestAuditModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = TestAuditModel.NAME)
@Table(name = "t_audit")
public class TestAuditModel extends AuditModelSupport {
    static final String NAME = "ranch.audit";
}
