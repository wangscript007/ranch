package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.ctrl.upload.UploadListener;

import javax.annotation.Resource;
import javax.inject.Inject;

/**
 * @author lpw
 */
public class PortraitUploadListenerTest extends TestSupport {
    @Inject
    private Session session;
    @Resource(name = UserModel.NAME + ".upload-listener.portrait")
    private UploadListener uploadListener;

    @Test
    public void getKey() {
        Assert.assertEquals(UserModel.NAME + ".portrait", uploadListener.getKey());
    }

    @Test
    public void isUploadEnable() {
        mockHelper.reset();
        mockHelper.mock("/user/modify");
        UserModel user = create(0);
        online(user);

        session.set(UserModel.NAME + ".service.session", user);
        Assert.assertFalse(uploadListener.isUploadEnable(null, null, null));
        Assert.assertFalse(uploadListener.isUploadEnable(null, "application/json", null));
        Assert.assertFalse(uploadListener.isUploadEnable(null, "image/png", null));
        Assert.assertFalse(uploadListener.isUploadEnable(null, "/image/png", "a.png"));
        Assert.assertFalse(uploadListener.isUploadEnable(null, "image/png", "a.jpg"));
        Assert.assertFalse(uploadListener.isUploadEnable(null, "image/gif", "a.jpg"));

        session.remove(UserModel.NAME + ".service.session");
        Assert.assertFalse(uploadListener.isUploadEnable(null, "image/png", "a.png"));
        Assert.assertFalse(uploadListener.isUploadEnable(null, "image/gif", "a.gif"));
        Assert.assertFalse(uploadListener.isUploadEnable(null, "image/jpeg", "a.jpg"));
        Assert.assertFalse(uploadListener.isUploadEnable(null, "image/jpeg", "a.jpeg"));

        session.set(UserModel.NAME + ".service.session", user);
        Assert.assertTrue(uploadListener.isUploadEnable(null, "image/png", "a.png"));
        Assert.assertTrue(uploadListener.isUploadEnable(null, "image/gif", "a.gif"));
        Assert.assertTrue(uploadListener.isUploadEnable(null, "image/jpeg", "a.jpg"));
        Assert.assertTrue(uploadListener.isUploadEnable(null, "image/jpeg", "a.jpeg"));
    }

    @Test
    public void upload() {
        mockHelper.reset();
        mockHelper.mock("/user/modify");
        UserModel user1 = create(0);
        session.set(UserModel.NAME + ".service.session", user1);
        JSONObject object = new JSONObject();
        object.put("path", "uri");
        uploadListener.complete(object);
        UserModel user2 = session.get(UserModel.NAME + ".service.session");
        Assert.assertEquals(user1.getId(), user2.getId());
        Assert.assertEquals("uri", user2.getPortrait());
    }
}
