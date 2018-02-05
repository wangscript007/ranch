package org.lpw.ranch.comment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.audit.AuditHelper;
import org.lpw.ranch.recycle.RecycleHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Carousel;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Timestamp;
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
    private Validator validator;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Carousel carousel;
    @Inject
    private UserHelper userHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private AuditHelper auditHelper;
    @Inject
    private RecycleHelper recycleHelper;
    @Inject
    private CommentDao commentDao;
    @Value("${" + CommentModel.NAME + ".audit.default:0}")
    private int defaultAudit;

    @Override
    public JSONObject query(int audit, String owner, String author, Timestamp start, Timestamp end) {
        return toJson(commentDao.query(Audit.values()[audit], owner, author, start, end, pagination.getPageSize(), pagination.getPageNum()), true, true, true, false);
    }

    @Override
    public JSONObject queryByOwner(String owner) {
        return toJson(commentDao.query(Audit.Pass, owner, pagination.getPageSize(), pagination.getPageNum()), false, false, true, true);
    }

    @Override
    public JSONObject queryByAuthor(String author) {
        return toJson(commentDao.query(author, pagination.getPageSize(), pagination.getPageNum()), false, true, false, false);
    }

    private JSONObject toJson(PageList<CommentModel> pl, boolean key, boolean owner, boolean author, boolean child) {
        JSONObject object = pl.toJson(false);
        JSONArray array = new JSONArray();
        pl.getList().forEach(comment -> array.add(getJson(comment.getId(), comment, key, owner, author, child)));
        object.put("list", array);

        return object;
    }

    @Override
    public JSONObject get(String[] ids) {
        JSONObject json = new JSONObject();
        for (String id : ids) {
            JSONObject object = getJson(id, null, true, true, true, false);
            if (!object.isEmpty())
                json.put(id, object);
        }

        return json;
    }

    @Override
    public JSONObject create(CommentModel comment) {
        CommentModel model = new CommentModel();
        model.setKey(comment.getKey());
        model.setOwner(comment.getOwner());
        model.setAuthor(validator.isEmpty(comment.getAuthor()) ? userHelper.id() : comment.getAuthor());
        model.setSubject(comment.getSubject());
        model.setLabel(comment.getLabel());
        model.setContent(comment.getContent());
        model.setScore(comment.getScore());
        model.setTime(dateTime.now());
        model.setAudit(defaultAudit);
        commentDao.save(model);

        return getJson(model.getId(), model, false, true, false, false);
    }

    private JSONObject getJson(String id, CommentModel comment, boolean key, boolean owner, boolean author, boolean child) {
        String cacheKey = CACHE_JSON + id + key + owner + author + child;
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            if (comment == null)
                comment = commentDao.findById(id);
            if (comment == null)
                object = new JSONObject();
            else {
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
                    object.put("author", userHelper.get(comment.getAuthor()));
                if (child)
                    getChildren(object, comment);
            }
            cache.put(cacheKey, object, false);
        }

        return object;
    }

    private void getChildren(JSONObject object, CommentModel comment) {
        PageList<CommentModel> pl = commentDao.query(Audit.Pass, comment.getId(), 0, 0);
        if (pl.getList().isEmpty())
            return;

        JSONArray children = new JSONArray();
        pl.getList().forEach(child -> children.add(getJson(child.getId(), child, false, false, true, true)));
        object.put("children", children);
    }

    @Override
    public void pass(String[] ids, String auditRemark) {
        auditHelper.pass(CommentModel.class, ids, auditRemark);
    }

    @Override
    public void reject(String[] ids, String auditRemark) {
        auditHelper.reject(CommentModel.class, ids, auditRemark);
    }

    @Override
    public void delete(String id) {
        recycleHelper.delete(CommentModel.class, id);
    }

    @Override
    public JSONObject recycle() {
        return recycleHelper.recycle(CommentModel.class);
    }

    @Override
    public void restore(String id) {
        recycleHelper.restore(CommentModel.class, id);
    }
}
