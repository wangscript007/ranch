package org.lpw.ranch.async;

import org.lpw.tephra.atomic.Closables;
import org.lpw.tephra.atomic.Failable;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author lpw
 */
@Service(AsyncModel.NAME + ".callabler")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CallablerImpl implements Callabler {
    @Inject
    private Closables closables;
    @Inject
    private Set<Failable> failables;
    private Callable<String> callable;

    @Override
    public Callabler set(Callable<String> callable) {
        this.callable = callable;

        return this;
    }

    @Override
    public String call() throws Exception {
        try {
            String result = callable.call();
            closables.close();

            return result;
        } catch (Exception e) {
            failables.forEach(failable -> failable.fail(e));

            throw e;
        }
    }
}
