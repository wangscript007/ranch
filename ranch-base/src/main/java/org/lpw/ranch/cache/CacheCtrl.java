package org.lpw.ranch.cache;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller("ranch.cache.ctrl")
@Execute(name = "/cache/")
public class CacheCtrl {
    @Inject
    private Request request;
    @Inject
    private CacheService cacheService;

    @Execute(name = "remove", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object remove() {
        return cacheService.remove(request.get("type"), request.get("key"));
    }
}
