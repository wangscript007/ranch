package org.lpw.ranch.doc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.audit.AuditHelper;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.ranch.recycle.RecycleHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Carousel;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lpw
 */
@Service(DocModel.NAME + ".service")
public class DocServiceImpl implements DocService, MinuteJob {
    private static final String CACHE_JSON = DocModel.NAME + ".service.json:";
    private static final String CACHE_CONTENT = DocModel.NAME + ".service.content:";

    @Inject
    private Cache cache;
    @Inject
    private Validator validator;
    @Inject
    private Generator generator;
    @Inject
    private DateTime dateTime;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Carousel carousel;
    @Inject
    private UserHelper userHelper;
    @Inject
    private AuditHelper auditHelper;
    @Inject
    private RecycleHelper recycleHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private DocDao docDao;
    @Value("${" + DocModel.NAME + ".audit.default:0}")
    private int defaultAudit;
    private Map<String, AtomicInteger> read = new ConcurrentHashMap<>();

    @Override
    public DocModel findById(String id) {
        return docDao.findById(id);
    }

    @Override
    public JSONArray query(String key, String owner, String author, String subject, Audit audit) {
        JSONArray array = new JSONArray();
        docDao.query(key, owner, author, subject, audit, Recycle.No, pagination.getPageSize(), pagination.getPageNum())
                .getList().forEach(doc -> array.add(getJson(doc.getId(), doc, false)));

        return array;
    }

    @Override
    public JSONObject queryByAuthor() {
        return docDao.queryByAuthor(userHelper.id(), pagination.getPageSize(), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject get(String[] ids) {
        JSONObject object = new JSONObject();
        for (String id : ids) {
            JSONObject doc = getJson(id, null, true);
            if (!doc.isEmpty())
                object.put(id, doc);
        }

        return object;
    }

    @Override
    public JSONObject save(DocModel doc) {
        DocModel model = validator.isEmpty(doc.getId()) ? new DocModel() : findById(doc.getId());
        if (model == null)
            model = new DocModel();
        model.setKey(doc.getKey());
        model.setOwner(doc.getOwner());
        model.setAuthor(userHelper.id());
        model.setScoreMin(doc.getScoreMin());
        model.setScoreMax(doc.getScoreMax());
        model.setSort(doc.getSort());
        model.setSubject(doc.getSubject());
        model.setImage(doc.getImage());
        model.setThumbnail(doc.getThumbnail());
        model.setSummary(doc.getSummary());
        model.setLabel(doc.getLabel());
        model.setSource(doc.getSource());
        model.setContent(HtmlRenderer.builder().build().render(Parser.builder().build().parse(doc.getSource())));
        model.setTime(dateTime.now());
        model.setAudit(defaultAudit);
        docDao.save(model);
        clearCache(model.getId());

        return getJson(model.getId(), model, false);
    }

    private JSONObject getJson(String id, DocModel doc, boolean passable) {
        String key = CACHE_JSON + id + passable;
        JSONObject object = cache.get(key);
        if (object == null) {
            if (doc == null)
                doc = findById(id);
            if (doc != null && (!passable || doc.getAudit() == Audit.Passed.getValue())) {
                object = modelHelper.toJson(doc);
                object.put("owner", carousel.get(doc.getKey() + ".get", doc.getOwner()));
                object.put("author", userHelper.get(doc.getAuthor()));
            } else
                object = new JSONObject();
            cache.put(key, object, false);
        }

        return object;
    }

    @Override
    public String source(String id) {
        return findById(id).getSource();
    }

    @Override
    public String read(String id) {
        String key = CACHE_CONTENT + id;
        String content = cache.get(key);
        if (content == null)
            cache.put(key, content = findById(id).getContent(), false);
        read.computeIfAbsent(id, i -> new AtomicInteger()).incrementAndGet();

        return content;
    }

    @Override
    public void favorite(String id, int n) {
        if (n == 0)
            return;

        docDao.favorite(id, n);
        clearCache(id);
    }

    @Override
    public void comment(String id, int n) {
        if (n == 0)
            return;

        docDao.comment(id, n);
        clearCache(id);
    }

    @Override
    public void pass(String[] ids, String auditRemark) {
        auditHelper.pass(DocModel.class, ids, auditRemark);
        clearCache(ids);
    }

    @Override
    public void refuse(String[] ids, String auditRemark) {
        auditHelper.refuse(DocModel.class, ids, auditRemark);
        clearCache(ids);
    }

    @Override
    public void delete(String id) {
        recycleHelper.delete(DocModel.class, id);
        clearCache(id);
    }

    @Override
    public JSONObject recycle() {
        return recycleHelper.recycle(DocModel.class);
    }

    @Override
    public void restore(String id) {
        recycleHelper.restore(DocModel.class, id);
        clearCache(id);
    }

    @Override
    public void executeMinuteJob() {
        if (read.isEmpty())
            return;

        Map<String, AtomicInteger> map = new HashMap<>(read);
        read.clear();
        map.forEach((id, n) -> {
            docDao.read(id, n.get());
            clearCache(id);
        });
    }

    private void clearCache(String[] ids) {
        for (String id : ids)
            clearCache(id);
    }

    private void clearCache(String id) {
        cache.remove(CACHE_JSON + id + true);
        cache.remove(CACHE_JSON + id + false);
        cache.remove(CACHE_CONTENT + id);
    }
}
