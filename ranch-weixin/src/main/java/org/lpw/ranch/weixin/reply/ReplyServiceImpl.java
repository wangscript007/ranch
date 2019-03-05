package org.lpw.ranch.weixin.reply;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.lpw.ranch.weixin.WeixinModel;
import org.lpw.ranch.weixin.WeixinService;
import org.lpw.tephra.util.Http;
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
    private Http http;
    @Inject
    private Pagination pagination;
    @Inject
    private WeixinService weixinService;
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

    @Override
    public void send(WeixinModel weixin, String openId, String receiveType, String receiveMessage) {
        replyDao.query(weixin.getKey(), receiveType, receiveMessage, 1, 0, 0).getList().forEach(reply -> {
            JSONObject object = new JSONObject();
            object.put("touser", openId);
            switch (reply.getSendType()) {
                case "text":
                    object.put("msgtype", "text");
                    JSONObject text = new JSONObject();
                    text.put("content", reply.getSendMessage());
                    object.put("text", text);
                    break;
                case "image":
                case "mpnews":
                    object.put("msgtype", reply.getSendType());
                    JSONObject media = new JSONObject();
                    media.put("media_id", reply.getSendMessage());
                    object.put(reply.getSendType(), media);
                    break;
                default:
                    return;
            }
            weixinService.byAccessToken(weixin, accessToken ->
                    http.post("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken,
                            null, object.toJSONString()));
        });
    }
}
