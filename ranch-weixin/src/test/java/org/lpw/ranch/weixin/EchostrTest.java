package org.lpw.ranch.weixin;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author lpw
 */
public class EchostrTest extends TestSupport {
    @Test
    public void echostr() {
        schedulerAspect.pause();

        create(0, "wx-app-id-0");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("signature", "signature string");
        mockHelper.getRequest().addParameter("timestamp", "timestamp string");
        mockHelper.getRequest().addParameter("nonce", "nonce string");
        mockHelper.getRequest().addParameter("echostr", "echo string");
        mockHelper.mock("/weixin/wx-app-id");
        Assert.assertEquals("failure", mockHelper.getResponse().asString());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("signature", "signature string");
        mockHelper.getRequest().addParameter("timestamp", "timestamp string");
        mockHelper.getRequest().addParameter("nonce", "nonce string");
        mockHelper.getRequest().addParameter("echostr", "echo string");
        mockHelper.mock("/weixin/wx-app-id-0");
        Assert.assertEquals("failure", mockHelper.getResponse().asString());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("signature", digest.sha1("nonce stringtoken 0timestamp string"));
        mockHelper.getRequest().addParameter("timestamp", "timestamp string");
        mockHelper.getRequest().addParameter("nonce", "nonce string");
        mockHelper.getRequest().addParameter("echostr", "echo string");
        mockHelper.mock("/weixin/wx-app-id-0");
        Assert.assertEquals("failure", mockHelper.getResponse().asString());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("signature", digest.sha1("nonce stringtimestamp stringtoken 0"));
        mockHelper.getRequest().addParameter("timestamp", "timestamp string");
        mockHelper.getRequest().addParameter("nonce", "nonce string");
        mockHelper.getRequest().addParameter("echostr", "echo string");
        mockHelper.mock("/weixin/wx-app-id-0");
        Assert.assertEquals("echo string", mockHelper.getResponse().asString());

        schedulerAspect.press();
    }
}
