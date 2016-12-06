package org.lpw.ranch.comment;

import net.sf.json.JSONObject;
import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.util.Pagination;
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
    @Autowired
    protected Validator validator;
    @Autowired
    protected DateTime dateTime;
    @Autowired
    protected Pagination pagination;
    @Autowired
    protected CommentDao commentDao;
    @Value("${" + CommentModel.NAME + ".audit.default:0}")
    protected int defaultAudit;

    @Override
    public JSONObject query(int audit) {
        return commentDao.query(Audit.values()[audit], pagination.getPageSize(), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject queryByOwner(String owner) {
        return commentDao.query(Audit.Passed, owner, pagination.getPageSize(), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject queryByAuthor(String author) {
        return commentDao.query(author, pagination.getPageSize(), pagination.getPageNum()).toJson();
    }

    @Override
    public CommentModel create(CommentModel comment) {
        comment.setId(null);
        comment.setTime(dateTime.now());
        comment.setAudit(defaultAudit);
        commentDao.save(comment);

        return comment;
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
        if (validator.isEmpty(ids))
            return;

        commentDao.audit(ids, audit);
    }
}
