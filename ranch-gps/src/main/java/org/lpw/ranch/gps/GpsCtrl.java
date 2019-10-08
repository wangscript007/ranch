package org.lpw.ranch.gps;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(GpsModel.NAME + ".ctrl")
@Execute(name = "/gps/", key = GpsModel.NAME, code = "111")
public class GpsCtrl {
    @Inject
    private Request request;
    @Inject
    private GpsService gpsService;

    /**
     * 获取GPS坐标对应的地址信息。
     * lat 纬度坐标值。
     * lng 经度坐标值。
     *
     * @return {address:"",component:{}}。
     */
    @Execute(name = "address", validates = {
            @Validate(validator = Validators.LATITUDE, parameter = "lat", failureCode = 1),
            @Validate(validator = Validators.LONGITUDE, parameter = "lng", failureCode = 2)
    })
    public Object address() {
        return gpsService.address(request.get("lat"), request.get("lng"));
    }
}
