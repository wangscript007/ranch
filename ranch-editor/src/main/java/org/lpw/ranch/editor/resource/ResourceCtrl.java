package org.lpw.ranch.editor.resource;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ResourceModel.NAME + ".ctrl")
@Execute(name = "/editor/resource/", key = ResourceModel.NAME, code = "0")
public class ResourceCtrl {
    @Inject
    private Request request;
    @Inject
    private ResourceService resourceService;
}
