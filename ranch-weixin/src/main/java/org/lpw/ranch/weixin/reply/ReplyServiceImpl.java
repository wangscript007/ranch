package org.lpw.ranch.weixin.reply;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(ReplyModel.NAME + ".service")
public class ReplyServiceImpl implements ReplyService {
    @Inject
    private Validator validator;
    @Inject
    private Pagination pagination;
    @Inject
    private ReplyDao replyDao;

    @Override
    public JSONObject query(String key, String receiveType, String receiveMessage, int state) {
        return replyDao.query(key, receiveType, receiveMessage, state, pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public void save(ReplyModel reply) {
        ReplyModel model = validator.isEmpty(reply.getId()) ? null : replyDao.findById(reply.getId());
        if (model == null)
            reply.setId(null);
        replyDao.save(reply);
    }

    @Override
    public void delete(String id) {
        replyDao.delete(id);
    }
}
