package org.lpw.ranch.doc;

import com.alibaba.fastjson.JSONObject;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.audit.AuditHelper;
import org.lpw.ranch.doc.relation.RelationService;
import org.lpw.ranch.doc.topic.TopicModel;
import org.lpw.ranch.doc.topic.TopicService;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.ranch.recycle.RecycleHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Carousel;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.bean.BeanFactory;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.lucene.LuceneHelper;
import org.lpw.tephra.scheduler.DateJob;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lpw
 */
@Service(DocModel.NAME + ".service")
public class DocServiceImpl implements DocService, MinuteJob, DateJob {
    private static final String CACHE_MODEL = DocModel.NAME + ".service.model:";
    private static final String CACHE_JSON = DocModel.NAME + ".service.json:";
    private static final String CACHE_READ = DocModel.NAME + ".service.read:";

    @Inject
    private Cache cache;
    @Inject
    private Validator validator;
    @Inject
    private Generator generator;
    @Inject
    private DateTime dateTime;
    @Inject
    private Context context;
    @Inject
    private Io io;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private LuceneHelper luceneHelper;
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
    private TopicService topicService;
    @Inject
    private RelationService relationService;
    @Inject
    private DocDao docDao;
    @Value("${" + DocModel.NAME + ".content.folder:}")
    private String folder;
    @Value("${" + DocModel.NAME + ".audit.default:0}")
    private int defaultAudit;
    private Map<String, AtomicInteger> read = new ConcurrentHashMap<>();
    private Map<String, AtomicInteger> praise = new ConcurrentHashMap<>();

    @Override
    public JSONObject query(String classify, String author, String category, String subject, String label, String type, Audit audit) {
        if (!validator.isEmpty(author))
            author = userHelper.findIdByUid(author, author);
        if (validator.isEmpty(classify))
            return docDao.query(author, category, subject, label, type, audit, Recycle.No,
                    pagination.getPageSize(20), pagination.getPageNum()).toJson(this::toJson);

        PageList<TopicModel> pl = topicService.query(classify, author, subject, label, type, audit);
        if (pl.getList().isEmpty())
            return pl.toJson();

        Set<String> ids = new HashSet<>();
        pl.getList().forEach(topic -> ids.add(topic.getDoc()));

        return docDao.query(ids, 0, 0).toJson(this::toJson);
    }

    @Override
    public JSONObject queryByAuthor() {
        return docDao.query(userHelper.id(), null, null, null, null, null, Recycle.No,
                pagination.getPageSize(), pagination.getPageNum()).toJson(this::toJson);
    }

    @Override
    public DocModel findById(String id) {
        String cacheKey = CACHE_MODEL + id;
        DocModel doc = cache.get(cacheKey);
        if (doc == null)
            cache.put(cacheKey, doc = docDao.findById(id), false);

        return doc;
    }

    @Override
    public JSONObject find(String id) {
        return toJsonFull(findById(id), true);
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

            object.put(id, toJsonFull(doc, true));
        }

        return object;
    }

    @Override
    public JSONObject search(String category, String[] words) {
        List<String> ids = luceneHelper.query(getLuceneKey(category), words, true, 1024);
        if (ids == null)
            return query(null, null, null, null, null, null, Audit.Pass);

        if (ids.isEmpty())
            return BeanFactory.getBean(PageList.class).setPage(0, 0, 0).toJson();

        return docDao.query(new HashSet<>(ids), pagination.getPageSize(20), pagination.getPageNum())
                .toJson(doc -> toJsonFull(doc, false));
    }

    @Override
    public JSONObject save(DocModel doc, String[] classifies, boolean markdown) {
        DocModel model = validator.isEmpty(doc.getId()) ? null : findById(doc.getId());
        if (model == null) {
            model = new DocModel();
            model.setCreate(dateTime.now());
        }
        model.setAuthor(userHelper.id());
        model.setCategory(doc.getCategory());
        model.setSort(doc.getSort());
        model.setSubject(doc.getSubject());
        model.setImage(doc.getImage());
        model.setThumbnail(doc.getThumbnail());
        model.setSummary(doc.getSummary());
        model.setLabel(doc.getLabel());
        model.setType(doc.getType());
        model.setJson(doc.getJson());
        model.setModify(dateTime.now());
        model.setAudit(defaultAudit);
        docDao.save(model);
        saveSourceContent(model, doc.getSource(), markdown);
        docDao.save(model);
        topicService.save(model, new HashSet<>(Arrays.asList(classifies)));
        clearCache(model.getId());

        return toJsonFull(model, true);
    }

    private void saveSourceContent(DocModel doc, String source, boolean markdown) {
        doc.setSource(source);
        if (markdown)
            doc.setContent(HtmlRenderer.builder().build().render(Parser.builder().build().parse(source)).trim());
        if (validator.isEmpty(folder))
            return;

        String path = context.getAbsolutePath(folder) + "/";
        io.mkdirs(path);
        io.write(path + doc.getId() + ".source", source.getBytes());
        if (markdown)
            io.write(path + doc.getId() + ".content", doc.getContent().getBytes());
        doc.setSource(null);
        doc.setContent(null);
    }

    private JSONObject toJson(DocModel doc) {
        return toJsonFull(doc, false);
    }

    private JSONObject toJsonFull(DocModel doc, boolean full) {
        String key = CACHE_JSON + doc.getId() + ":" + full;
        JSONObject object = cache.get(key);
        if (object == null) {
            object = modelHelper.toJson(doc);
            object.put("classifies", topicService.classifies(doc.getId()));
            object.put("author", userHelper.get(doc.getAuthor()));
            if (full) {
                object.put("source", getSource(doc));
                String content = getContent(doc);
                if (content != null)
                    object.put("content", content);
            }
            cache.put(key, object, false);
        }

        return object;
    }

    @Override
    public String source(String id) {
        return getSource(findById(id));
    }

    @Override
    public String read(String id) {
        DocModel doc = putRead(id);
        String content = getContent(doc);

        return content == null ? getSource(doc) : content;
    }

    @Override
    public JSONObject readJson(String id) {
        DocModel doc = putRead(id);
        String cacheKey = CACHE_READ + id;
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            object = toJsonFull(doc, true);
            object.put("relation", relationService.find(id));
            cache.put(cacheKey, object, false);
        }

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
        for (String id : ids)
            topicService.audit(id, Audit.Pass);
        clearCache(ids);
    }

    @Override
    public void reject(String[] ids, String auditRemark) {
        auditHelper.reject(DocModel.class, ids, auditRemark);
        for (String id : ids)
            topicService.audit(id, Audit.Reject);
        clearCache(ids);
    }

    @Override
    public void delete(String id) {
        recycleHelper.delete(DocModel.class, id);
        topicService.recycle(id, Recycle.Yes);
        clearCache(id);
    }

    @Override
    public JSONObject recycle() {
        return recycleHelper.recycle(DocModel.class);
    }

    @Override
    public void restore(String id) {
        recycleHelper.restore(DocModel.class, id);
        topicService.recycle(id, Recycle.No);
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

    @Override
    public void refresh() {
        relationService.clear();
        List<DocModel> list = docDao.query(null, null, null, null, null, Audit.Pass, Recycle.No,
                0, 0).getList();
        if (list.isEmpty())
            return;

        Map<String, List<DocModel>> map = new HashMap<>();
        list.forEach(doc -> map.computeIfAbsent(doc.getCategory(), key -> new ArrayList<>()).add(doc));
        map.forEach((category, docs) -> {
            String luceneKey = getLuceneKey(category);
            luceneHelper.clear(luceneKey);

            Map<String, String> sources = new HashMap<>();
            DocModel previous = null;
            for (DocModel doc : docs) {
                if (previous != null) {
                    relationService.save(doc.getId(), previous.getId(), "previous", 0);
                    relationService.save(previous.getId(), doc.getId(), "next", 0);
                }
                previous = doc;
                sources.put(doc.getId(), doc.getSubject() + "," + doc.getSummary() + "," + doc.getLabel() + ","
                        + getSource(doc).replaceAll("<[^>]*>", " ").replace('"', ' ')
                        .replace('“', ' ').replace('”', ' ').replace('\'', ' ')
                        .replaceAll("&nbsp;", " ").replaceAll("\\s+", " "));
                luceneHelper.source(luceneKey, doc.getId(), sources.get(doc.getId()));
            }

            luceneHelper.index(luceneKey);
            docs.forEach(doc -> {
                List<String> ids = luceneHelper.query(luceneKey, sources.get(doc.getId()), false, 11);
                for (int i = 1, size = ids.size(); i < size; i++)
                    relationService.save(doc.getId(), ids.get(i), "alike", i - 1);
                clearCache(doc.getId());
            });
        });
    }

    private String getLuceneKey(String category) {
        return DocModel.NAME + "." + category;
    }

    private String getSource(DocModel doc) {
        return doc.getSource() == null ? readFromFile(doc.getId(), ".source") : doc.getSource();
    }

    private String getContent(DocModel doc) {
        return doc.getContent() == null ? readFromFile(doc.getId(), ".content") : doc.getContent();
    }

    private String readFromFile(String id, String suffix) {
        File file = new File(context.getAbsolutePath(folder) + "/" + id + suffix);

        return file.exists() ? io.readAsString(file.getAbsolutePath()) : null;
    }

    @Override
    public void executeDateJob() {
        refresh();
    }

    private void clearCache(String[] ids) {
        for (String id : ids)
            clearCache(id);
    }

    private void clearCache(String id) {
        cache.remove(CACHE_MODEL + id);
        cache.remove(CACHE_JSON + id + ":true");
        cache.remove(CACHE_JSON + id + ":false");
        cache.remove(CACHE_READ + id);
    }
}
