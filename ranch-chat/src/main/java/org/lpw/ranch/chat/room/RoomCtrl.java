package org.lpw.ranch.chat.room;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(RoomModel.NAME + ".ctrl")
@Execute(name = "/chat/room/", key = RoomModel.NAME, code = "0")
public class RoomCtrl {
    @Inject
    private Request request;
    @Inject
    private RoomService roomService;
}
