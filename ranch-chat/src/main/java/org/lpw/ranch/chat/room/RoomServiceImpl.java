package org.lpw.ranch.chat.room;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(RoomModel.NAME + ".service")
public class RoomServiceImpl implements RoomService {
    @Inject
    private RoomDao roomDao;
}
