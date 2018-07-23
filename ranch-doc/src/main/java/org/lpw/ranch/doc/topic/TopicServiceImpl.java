package org.lpw.ranch.doc.topic;

import com.alibaba.fastjson.JSONArray;
import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.doc.DocModel;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.dao.orm.PageList;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Set;

/**
 * @author lpw
 */
@Service(TopicModel.NAME + ".service")
public class TopicServiceImpl implements TopicService {
    @Inject
    private Pagination pagination;
    @Inject
    private TopicDao topicDao;

    @Override
    public PageList<TopicModel> query(String classify, String subject, String label, Audit audit) {
        return topicDao.query(classify, subject, label, audit, Recycle.No, pagination.getPageSize(20), pagination.getPageNum());
    }

    @Override
    public JSONArray classifies(String doc) {
        JSONArray array = new JSONArray();
        topicDao.query(doc).getList().forEach(topic -> array.add(topic.getClassify()));

        return array;
    }

    @Override
    public void save(DocModel doc, Set<String> classifies) {
        if (doc == null)
            return;

        topicDao.delete(doc.getId());
        classifies.forEach(classify -> {
            TopicModel topic = new TopicModel();
            topic.setDoc(doc.getId());
            topic.setClassify(classify);
            topic.setSort(doc.getSort());
            topic.setSubject(doc.getSubject());
            topic.setLabel(doc.getLabel());
            topic.setTime(doc.getTime());
            topic.setAudit(doc.getAudit());
            topic.setRecycle(doc.getRecycle());
            topicDao.save(topic);
        });
    }

    @Override
    public void audit(String doc, Audit audit) {
        topicDao.audit(doc, audit);
    }

    @Override
    public void recycle(String doc, Recycle recycle) {
        topicDao.recycle(doc, recycle);
    }
}
