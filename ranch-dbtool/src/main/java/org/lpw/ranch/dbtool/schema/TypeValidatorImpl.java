package org.lpw.ranch.dbtool.schema;

import org.lpw.tephra.bean.BeanFactory;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.lpw.tephra.dao.dialect.Dialect;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lpw
 */
@Controller(SchemaService.VALIDATOR_TYPE)
public class TypeValidatorImpl extends ValidatorSupport implements ContextRefreshedListener {
    private Set<String> types;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return types.contains(parameter);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return SchemaModel.NAME + ".illegal-type";
    }

    @Override
    public int getContextRefreshedSort() {
        return 23;
    }

    @Override
    public void onContextRefreshed() {
        types = new HashSet<>();
        BeanFactory.getBeans(Dialect.class).forEach(dialect -> types.add(dialect.getName()));
        types.add("classify");
    }
}
