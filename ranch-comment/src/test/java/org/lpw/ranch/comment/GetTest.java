package org.lpw.ranch.comment;

import org.junit.Test;
import org.lpw.ranch.audit.Audit;

/**
 * @author lpw
 */
public class GetTest extends TestSupport {
    @Test public void get(){
        CommentModel comment1=create(1, Audit.Passed);
        CommentModel comment2=create(2, Audit.Passed);
    }
}
