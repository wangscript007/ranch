package org.lpw.ranch.doc.topic;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(TopicModel.NAME + ".ctrl")
@Execute(name = "/doc/topic/", key = TopicModel.NAME, code = "0")
public class TopicCtrl {
    @Inject
    private Request request;
    @Inject
    private TopicService topicService;
}
