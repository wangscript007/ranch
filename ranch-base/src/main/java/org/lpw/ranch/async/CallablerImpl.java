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
    private Notifier notifier;

    @Override
    public Callabler set(Callable<String> callable, Notifier notifier) {
        this.callable = callable;
        this.notifier = notifier;

        return this;
    }

    @Override
    public String call() throws Exception {
        try {
            String result = callable.call();
            if (notifier != null)
                notifier.success(result);
            closables.close();

            return result;
        } catch (Exception e) {
            if (notifier != null)
                notifier.failure();
            failables.forEach(failable -> failable.fail(e));

            throw e;
        }
    }
}
