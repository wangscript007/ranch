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
import org.lpw.tephra.dao.orm.PageList;
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
    private static final String CACHE_MODEL = DocModel.NAME + ".service.model:";
    private static final String CACHE_JSON = DocModel.NAME + ".service.json:";

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
    private Map<String, AtomicInteger> praise = new ConcurrentHashMap<>();

    @Override
    public DocModel findById(String id) {
        String cacheKey = CACHE_MODEL + id;
        DocModel doc = cache.get(cacheKey);
        if (doc == null)
            cache.put(cacheKey, doc = docDao.findById(id), false);

        return doc;
    }

    @Override
    public JSONObject query(String key, String author, String subject, String label, Audit audit) {
        return query(docDao.query(key, userHelper.findIdByUid(author, author), subject, label, audit, Recycle.No,
                pagination.getPageSize(20), pagination.getPageNum()));
    }

    @Override
    public JSONObject queryByAuthor() {
        return query(docDao.query(null, userHelper.id(), null, null, null, Recycle.No,
                pagination.getPageSize(), pagination.getPageNum()));
    }

    @Override
    public JSONObject queryByKey(String key) {
        return query(docDao.query(key, null, null, null, Audit.Pass, Recycle.No,
                pagination.getPageSize(), pagination.getPageNum()));
    }

    private JSONObject query(PageList<DocModel> pl) {
        JSONObject object = pl.toJson(false);
        JSONArray list = new JSONArray();
        pl.getList().forEach(doc -> list.add(toJson(doc)));
        object.put("list", list);

        return object;
    }

    @Override
    public JSONObject get(String[] ids) {
        JSONObject object = new JSONObject();
        for (String id : ids) {
            if (object.containsKey(id))
                continue;

            DocModel doc = findById(id);
            if (doc == null || doc.getAudit() != Audit.Pass.getValue() || doc.getRecycle() != Recycle.No.getValue())
                continue;

            object.put(id, toJson(doc));
        }

        return object;
    }

    @Override
    public JSONObject save(DocModel doc, boolean markdown) {
        DocModel model = validator.isEmpty(doc.getId()) ? new DocModel() : findById(doc.getId());
        model.setKey(doc.getKey());
        model.setAuthor(userHelper.id());
        model.setSort(doc.getSort());
        model.setSubject(doc.getSubject());
        model.setImage(doc.getImage());
        model.setThumbnail(doc.getThumbnail());
        model.setSummary(doc.getSummary());
        model.setLabel(doc.getLabel());
        model.setSource(doc.getSource());
        model.setContent(markdown ? HtmlRenderer.builder().build().render(Parser.builder().build().parse(doc.getSource())).trim()
                : doc.getSource());
        model.setJson(doc.getJson());
        model.setTime(dateTime.now());
        model.setAudit(defaultAudit);
        docDao.save(model);
        clearCache(model.getId());

        return toJson(model);
    }

    private JSONObject toJson(DocModel doc) {
        String key = CACHE_JSON + doc.getId();
        JSONObject object = cache.get(key);
        if (object == null) {
            object = modelHelper.toJson(doc);
            object.put("author", userHelper.get(doc.getAuthor()));
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
        return putRead(id).getContent();
    }

    @Override
    public JSONObject readJson(String id) {
        DocModel doc = putRead(id);
        JSONObject object = toJson(doc);
        object.put("content", doc.getContent());

        return object;
    }

    private DocModel putRead(String id) {
        read.computeIfAbsent(id, i -> new AtomicInteger()).incrementAndGet();

        return findById(id);
    }

    @Override
    public void favorite(String id, int n) {
        if (n == 0)
            return;

        DocModel doc = findById(id);
        doc.setFavorite(Math.max(0, doc.getFavorite() + n));
        docDao.save(doc);
        clearCache(id);
    }

    @Override
    public void comment(String id, int n) {
        if (n == 0)
            return;

        DocModel doc = findById(id);
        doc.setComment(Math.max(0, doc.getComment() + n));
        docDao.save(doc);
        clearCache(id);
    }

    @Override
    public void praise(String id) {
        praise.computeIfAbsent(id, i -> new AtomicInteger()).incrementAndGet();
    }

    @Override
    public void pass(String[] ids, String auditRemark) {
        auditHelper.pass(DocModel.class, ids, auditRemark);
        clearCache(ids);
    }

    @Override
    public void reject(String[] ids, String auditRemark) {
        auditHelper.reject(DocModel.class, ids, auditRemark);
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
        read();
        praise();
    }

    private void read() {
        if (read.isEmpty())
            return;

        Map<String, AtomicInteger> map = new HashMap<>(read);
        read.clear();
        map.forEach((id, n) -> {
            DocModel doc = findById(id);
            doc.setRead(doc.getRead() + n.get());
            docDao.save(doc);
            clearCache(id);
        });
    }

    private void praise() {
        if (praise.isEmpty())
            return;

        Map<String, AtomicInteger> map = new HashMap<>(praise);
        praise.clear();
        map.forEach((id, n) -> {
            DocModel doc = findById(id);
            doc.setPraise(doc.getPraise() + n.get());
            docDao.save(doc);
            clearCache(id);
        });
    }

    private void clearCache(String[] ids) {
        for (String id : ids)
            clearCache(id);
    }

    private void clearCache(String id) {
        cache.remove(CACHE_MODEL + id);
        cache.remove(CACHE_JSON + id);
    }
}
