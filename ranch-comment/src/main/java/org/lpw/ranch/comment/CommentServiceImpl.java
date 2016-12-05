package org.lpw.ranch.comment;

import org.lpw.tephra.util.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author lpw
 */
@Service(CommentModel.NAME + ".service")
public class CommentServiceImpl implements CommentService {
    @Autowired
    protected DateTime dateTime;
    @Autowired
    protected CommentDao commentDao;
    @Value("${" + CommentModel.NAME + ".audit.default:0}")
    protected int defaultAudit;

    @Override
    public CommentModel create(CommentModel comment) {
        comment.setId(null);
        comment.setTime(dateTime.now());
        comment.setAudit(defaultAudit);
        commentDao.save(comment);

        return comment;
    }
}
