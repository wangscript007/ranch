package org.lpw.ranch.weixin.reply;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.lpw.ranch.weixin.WeixinModel;
import org.lpw.ranch.weixin.WeixinService;
import org.lpw.tephra.util.Http;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.wormhole.WormholeHelper;
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
    private WormholeHelper wormholeHelper;
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
            object.put("msgtype", reply.getSendType());
            switch (reply.getSendType()) {
                case "text":
                    JSONObject text = new JSONObject();
                    text.put("content", reply.getSendMessage());
                    object.put("text", text);
                    break;
                case "image":
                case "mpnews":
                    JSONObject media = new JSONObject();
                    media.put("media_id", reply.getSendMessage());
                    object.put(reply.getSendType(), media);
                    break;
                case "news":
                    JSONObject article = new JSONObject();
                    article.put("title", reply.getSendTitle());
                    article.put("description", reply.getSendDescription());
                    article.put("url", reply.getSendUrl());
                    article.put("picurl", reply.getSendPicurl());
                    JSONArray articles = new JSONArray();
                    articles.add(article);
                    object.put("articles", articles);
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
