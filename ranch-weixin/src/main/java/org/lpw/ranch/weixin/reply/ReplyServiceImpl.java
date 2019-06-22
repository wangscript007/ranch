package org.lpw.ranch.weixin.reply;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.popular.helper.PopularHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.ranch.weixin.WeixinModel;
import org.lpw.ranch.weixin.WeixinService;
import org.lpw.ranch.weixin.info.InfoService;
import org.lpw.tephra.util.Http;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.wormhole.WormholeHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

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
    private Logger logger;
    @Inject
    private WormholeHelper wormholeHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private PopularHelper popularHelper;
    @Inject
    private WeixinService weixinService;
    @Inject
    private InfoService infoService;
    @Inject
    private Optional<Set<ReplyAlter>> alters;
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
    public void send(WeixinModel weixin, String openId, String receiveType, String receiveMessage, String eventKey) {
        replyDao.query(weixin.getKey(), receiveType, receiveMessage, 1, 0, 0).getList().forEach(reply -> {
            alters.ifPresent(set -> set.forEach(alter -> alter.alter(weixin, openId, receiveType, receiveMessage, eventKey, reply)));
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
                    article.put("url", formatUrl(reply.getSendUrl(), openId, eventKey));
                    article.put("picurl", reply.getSendPicurl());
                    JSONArray articles = new JSONArray();
                    articles.add(article);
                    object.put("articles", articles);
                    break;
                case "link":
                    JSONObject link = new JSONObject();
                    link.put("title", reply.getSendTitle());
                    link.put("description", reply.getSendDescription());
                    link.put("url", formatUrl(reply.getSendUrl(), openId, eventKey));
                    link.put("thumb_url", reply.getSendPicurl());
                    object.put("link", link);
                    break;
                case "miniprogrampage":
                    JSONObject miniprogrampage = new JSONObject();
                    if (!validator.isEmpty(reply.getSendAppId()))
                        miniprogrampage.put("appid", reply.getSendAppId());
                    miniprogrampage.put("title", reply.getSendTitle());
                    miniprogrampage.put("pagepath", formatUrl(reply.getSendUrl(), openId, eventKey));
                    miniprogrampage.put("thumb_media_id", reply.getSendPicurl());
                    object.put("miniprogrampage", miniprogrampage);
                    break;
                default:
                    return;
            }
            weixinService.byAccessToken(weixin, accessToken -> {
                String string = http.post("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken,
                        null, object.toJSONString());
                if (logger.isInfoEnable())
                    logger.info("发送微信回复[{}:{}]。", object.toJSONString(), string);

                return string;
            });
        });

        if (receiveType.equals("text"))
            popularHelper.increase(ReplyModel.NAME + ".text", receiveMessage);
    }

    private String formatUrl(String url, String openId, String eventKey) {
        String string = url.replaceAll("OPEN_ID", openId);
        if (!validator.isEmpty(eventKey))
            string = string + (string.indexOf('?') == -1 ? "?" : "&") + eventKey;

        return string;
    }
}
