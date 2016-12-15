package org.lpw.ranch.doc;

import net.sf.json.JSONObject;
import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.audit.AuditHelper;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.scheduler.DateJob;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Autowired
    protected Cache cache;
    @Autowired
    protected Generator generator;
    @Autowired
    protected DateTime dateTime;
    @Autowired
    protected ModelHelper modelHelper;
    @Autowired
    protected Carousel carousel;
    @Autowired
    protected AuditHelper auditHelper;
    @Autowired
    protected DocDao docDao;
    @Value("${" + DocModel.NAME + ".audit.default:0}")
    protected int defaultAudit;
    protected Map<String, AtomicInteger> read = new ConcurrentHashMap<>();

    @Override
    public DocModel findById(String id) {
        String key = getCacheKey(CACHE_MODEL, id);
        DocModel doc = cache.get(key);
        if (doc == null)
            cache.put(key, doc = docDao.findById(id), false);

        return doc;
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
        model.setAuthor(doc.getAuthor());
        model.setScoreMin(doc.getScoreMin());
        model.setScoreMax(doc.getScoreMax());
        model.setSort(doc.getSort());
        model.setSubject(doc.getSubject());
        model.setImage(doc.getImage());
        model.setThumbnail(doc.getThumbnail());
        model.setSummary(doc.getSummary());
        model.setLabel(doc.getLabel());
        model.setContent(doc.getContent());
        model.setTime(dateTime.now());
        model.setAudit(defaultAudit);
        docDao.save(model);

        return getJson(model.getId(), model, true);
    }

    protected JSONObject getJson(String id, DocModel doc, boolean full) {
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

    protected JSONObject toJson(DocModel doc, Set<String> ignores, boolean owner) {
        JSONObject object = modelHelper.toJson(doc, ignores);
        if (owner)
            object.put("owner", carousel.get(doc.getKey() + ".get", doc.getOwner()));
        object.put("author", carousel.getUser(doc.getAuthor()));

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
        docDao.audit(ids, Audit.Passed, auditRemark);
    }

    @Override
    public void refuse(String[] ids, String auditRemark) {
        docDao.audit(ids, Audit.Refused, auditRemark);
    }

    /**
     * 刷新缓存。
     */
    public void refresh() {
        resetRandom();
    }

    protected String getCacheKey(String prefix, String suffix) {
        String random = cache.get(CACHE_RANDOM);
        if (random == null)
            random = resetRandom();

        return prefix + random + suffix;
    }

    protected String resetRandom() {
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
