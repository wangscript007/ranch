package org.lpw.ranch.doc;

import com.alibaba.fastjson.JSONObject;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.audit.AuditHelper;
import org.lpw.ranch.recycle.RecycleHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Carousel;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.scheduler.DateJob;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lpw
 */
@Service(DocModel.NAME + ".service")
public class DocServiceImpl implements DocService, MinuteJob, DateJob {
    private static final String CACHE_RANDOM = DocModel.NAME + ".service.random";
    private static final String CACHE_MODEL = DocModel.NAME + ".service.model:";
    private static final String CACHE_JSON = DocModel.NAME + ".service.json:";
    private static final String CACHE_CONTENT = DocModel.NAME + ".service.content:";

    @Inject
    private Cache cache;
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
        String key = getCacheKey(CACHE_MODEL, id);
        DocModel doc = cache.get(key);
        if (doc == null)
            cache.put(key, doc = docDao.findById(id), false);

        return doc;
    }

    @Override
    public JSONObject queryByKey(int audit, String key) {
        return docDao.queryByKey(Audit.values()[audit], key, pagination.getPageSize(), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject queryByOwner(int audit, String owner) {
        return docDao.queryByOwner(Audit.values()[audit], owner, pagination.getPageSize(), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject queryByAuthor(int audit, String author) {
        return docDao.queryByAuthor(Audit.values()[audit], author, pagination.getPageSize(), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject queryByAuthor() {
        return docDao.queryByAuthor(userHelper.id(), pagination.getPageSize(), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject get(String[] ids) {
        JSONObject object = new JSONObject();
        for (String id : ids) {
            JSONObject doc = getJson(id, null, false);
            if (!doc.isEmpty())
                object.put(id, doc);
        }

        return object;
    }

    @Override
    public JSONObject create(DocModel doc) {
        DocModel model = new DocModel();
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
        model.setContent(toHtml(doc.getSource()));
        model.setTime(dateTime.now());
        model.setAudit(defaultAudit);
        docDao.save(model);

        return getJson(model.getId(), model, true);
    }

    private String toHtml(String markdown) {
        return HtmlRenderer.builder().build().render(Parser.builder().build().parse(markdown));
    }

    private JSONObject getJson(String id, DocModel doc, boolean full) {
        String key = getCacheKey(CACHE_JSON, id + full);
        JSONObject object = cache.get(key);
        if (object == null) {
            if (doc == null)
                doc = findById(id);
            if (doc != null) {
                Set<String> ignores = new HashSet<>();
                ignores.add("owner");
                ignores.add("author");
                if (full)
                    object = toJson(doc, ignores, true);
                else if (doc.getAudit() == Audit.Passed.getValue()) {
                    ignores.add("key");
                    ignores.add("scoreMin");
                    ignores.add("scoreMax");
                    ignores.add("sort");
                    ignores.add("source");
                    ignores.add("content");
                    auditHelper.addProperty(ignores);
                    object = toJson(doc, ignores, false);
                }
            }
            if (object == null)
                object = new JSONObject();
            cache.put(key, object, false);
        }

        return object;
    }

    private JSONObject toJson(DocModel doc, Set<String> ignores, boolean owner) {
        JSONObject object = modelHelper.toJson(doc, ignores);
        if (owner)
            object.put("owner", carousel.get(doc.getKey() + ".get", doc.getOwner()));
        object.put("author", userHelper.get(doc.getAuthor()));

        return object;
    }

    @Override
    public String read(String id) {
        String key = getCacheKey(CACHE_CONTENT, id);
        String content = cache.get(key);
        if (content == null)
            cache.put(key, content = findById(id).getContent(), false);
        AtomicInteger integer = read.get(id);
        if (integer == null)
            integer = new AtomicInteger();
        integer.incrementAndGet();
        read.put(id, integer);

        return content;
    }

    @Override
    public void favorite(String id, int n) {
        if (n == 0)
            return;

        docDao.favorite(id, n);
    }

    @Override
    public void comment(String id, int n) {
        if (n == 0)
            return;

        docDao.comment(id, n);
    }

    @Override
    public void pass(String[] ids, String auditRemark) {
        auditHelper.pass(DocModel.class, ids, auditRemark);
    }

    @Override
    public void refuse(String[] ids, String auditRemark) {
        auditHelper.refuse(DocModel.class, ids, auditRemark);
    }

    @Override
    public void delete(String id) {
        recycleHelper.delete(DocModel.class, id);
    }

    @Override
    public JSONObject recycle() {
        return recycleHelper.recycle(DocModel.class);
    }

    @Override
    public void restore(String id) {
        recycleHelper.restore(DocModel.class, id);
    }

    @Override
    public void refresh() {
        resetRandom();
    }

    private String getCacheKey(String prefix, String suffix) {
        String random = cache.get(CACHE_RANDOM);
        if (random == null)
            random = resetRandom();

        return prefix + random + suffix;
    }

    private String resetRandom() {
        String random = generator.random(32);
        cache.put(CACHE_RANDOM, random, true);

        return random;
    }

    @Override
    public void executeMinuteJob() {
        if (read.isEmpty())
            return;

        Map<String, AtomicInteger> map = new HashMap<>(read);
        read.clear();
        map.forEach((id, n) -> docDao.read(id, n.get()));
    }

    @Override
    public void executeDateJob() {
        resetRandom();
    }
}
