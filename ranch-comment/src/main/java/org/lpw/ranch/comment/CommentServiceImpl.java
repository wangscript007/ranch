package org.lpw.ranch.comment;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.util.Carousel;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author lpw
 */
@Service(CommentModel.NAME + ".service")
public class CommentServiceImpl implements CommentService {
    private static final String CACHE_JSON = CommentModel.NAME + ".service.json:";

    @Autowired
    protected Cache cache;
    @Autowired
    protected Validator validator;
    @Autowired
    protected Converter converter;
    @Autowired
    protected DateTime dateTime;
    @Autowired
    protected Carousel carousel;
    @Autowired
    protected Pagination pagination;
    @Autowired
    protected CommentDao commentDao;
    @Value("${" + CommentModel.NAME + ".audit.default:0}")
    protected int defaultAudit;

    @Override
    public JSONObject query(int audit) {
        return toJson(commentDao.query(Audit.values()[audit], pagination.getPageSize(), pagination.getPageNum()), true, true, true, false);
    }

    @Override
    public JSONObject queryByOwner(String owner) {
        return toJson(commentDao.query(Audit.Passed, owner, pagination.getPageSize(), pagination.getPageNum()), false, false, true, true);
    }

    @Override
    public JSONObject queryByAuthor(String author) {
        return toJson(commentDao.query(author, pagination.getPageSize(), pagination.getPageNum()), false, true, false, false);
    }

    protected JSONObject toJson(PageList<CommentModel> pl, boolean key, boolean owner, boolean author, boolean child) {
        JSONObject object = pl.toJson(false);
        JSONArray array = new JSONArray();
        pl.getList().forEach(comment -> array.add(getJson(comment, key, owner, author, child)));
        object.put("list", array);

        return object;
    }

    @Override
    public JSONObject create(CommentModel comment) {
        comment.setId(null);
        comment.setTime(dateTime.now());
        comment.setAudit(defaultAudit);
        commentDao.save(comment);

        return getJson(comment, false, true, false, false);
    }

    protected JSONObject getJson(CommentModel comment, boolean key, boolean owner, boolean author, boolean child) {
        String cacheKey = CACHE_JSON + comment.getId() + key + owner + author + child;
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            object = new JSONObject();
            object.put("id", comment.getId());
            if (key)
                object.put("key", comment.getKey());
            if (owner)
                object.put("owner", carousel.get(comment.getKey() + ".get", comment.getOwner()));
            if (author)
                object.put("author", carousel.get("ranch.user.get", comment.getAuthor()));
            if (!validator.isEmpty(comment.getSubject()))
                object.put("subject", comment.getSubject());
            if (!validator.isEmpty(comment.getLabel()))
                object.put("label", comment.getLabel());
            object.put("content", comment.getContent());
            object.put("score", comment.getScore());
            object.put("time", converter.toString(comment.getTime()));
            if (child)
                getChildren(object, comment);
            cache.put(cacheKey, object, false);
        }

        return object;
    }

    protected void getChildren(JSONObject object, CommentModel comment) {
        PageList<CommentModel> pl = commentDao.query(Audit.Passed, comment.getId(), 0, 0);
        if (pl.getList().isEmpty())
            return;

        JSONArray children = new JSONArray();
        pl.getList().forEach(child -> children.add(getJson(child, false, false, true, true)));
        object.put("children", children);
    }

    @Override
    public void pass(String[] ids) {
        audit(ids, Audit.Passed);
    }

    @Override
    public void refuse(String[] ids) {
        audit(ids, Audit.Refused);
    }

    protected void audit(String[] ids, Audit audit) {
        if (!validator.isEmpty(ids))
            commentDao.audit(ids, audit);
    }
}
