package org.lpw.ranch.comment;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.audit.AuditHelper;
import org.lpw.ranch.user.User;
import org.lpw.ranch.util.Carousel;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.util.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lpw
 */
@Service(CommentModel.NAME + ".service")
public class CommentServiceImpl implements CommentService {
    private static final String CACHE_JSON = CommentModel.NAME + ".service.json:";

    @Inject
    private Cache cache;
    @Inject
    private DateTime dateTime;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Carousel carousel;
    @Inject
    private User user;
    @Inject
    private Pagination pagination;
    @Inject
    private AuditHelper auditHelper;
    @Inject
    private CommentDao commentDao;
    @Value("${" + CommentModel.NAME + ".audit.default:0}")
    private int defaultAudit;

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

    private JSONObject toJson(PageList<CommentModel> pl, boolean key, boolean owner, boolean author, boolean child) {
        JSONObject object = pl.toJson(false);
        JSONArray array = new JSONArray();
        pl.getList().forEach(comment -> array.add(getJson(comment, key, owner, author, child)));
        object.put("list", array);

        return object;
    }

    @Override
    public CommentModel findById(String id) {
        return commentDao.findById(id);
    }

    @Override
    public JSONObject create(CommentModel comment) {
        CommentModel model = new CommentModel();
        model.setKey(comment.getKey());
        model.setOwner(comment.getOwner());
        model.setAuthor(comment.getAuthor());
        model.setSubject(comment.getSubject());
        model.setLabel(comment.getLabel());
        model.setContent(comment.getContent());
        model.setScore(comment.getScore());
        model.setTime(dateTime.now());
        model.setAudit(defaultAudit);
        commentDao.save(model);

        return getJson(model, false, true, false, false);
    }

    private JSONObject getJson(CommentModel comment, boolean key, boolean owner, boolean author, boolean child) {
        String cacheKey = CACHE_JSON + comment.getId() + key + owner + author + child;
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            Set<String> ignores = new HashSet<>();
            if (!key)
                ignores.add("key");
            ignores.add("owner");
            ignores.add("author");
            auditHelper.addProperty(ignores);
            object = modelHelper.toJson(comment, ignores);
            if (owner)
                object.put("owner", carousel.get(comment.getKey() + ".get", comment.getOwner()));
            if (author)
                object.put("author", user.get(comment.getAuthor()));
            if (child)
                getChildren(object, comment);
            cache.put(cacheKey, object, false);
        }

        return object;
    }

    private void getChildren(JSONObject object, CommentModel comment) {
        PageList<CommentModel> pl = commentDao.query(Audit.Passed, comment.getId(), 0, 0);
        if (pl.getList().isEmpty())
            return;

        JSONArray children = new JSONArray();
        pl.getList().forEach(child -> children.add(getJson(child, false, false, true, true)));
        object.put("children", children);
    }

    @Override
    public void pass(String[] ids, String auditRemark) {
        commentDao.audit(ids, Audit.Passed, auditRemark);
    }

    @Override
    public void refuse(String[] ids, String auditRemark) {
        commentDao.audit(ids, Audit.Refused, auditRemark);
    }

    @Override
    public void delete(String id) {
        commentDao.delete(id);
    }
}
