package org.lpw.ranch.weixin;

import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lpw
 */
public class AutoTest extends TestSupport {
    @Inject
    private WeixinService weixinService;

    @Test
    public void auto() throws Exception {
        schedulerAspect.pause();

        Field field = WeixinServiceImpl.class.getDeclaredField("auto");
        field.setAccessible(true);
        Object object = field.get(weixinService);
        field.set(weixinService, true);

        httpAspect.reset();
        for (int i = 0; i < 6; i++)
            create(i);
        List<String> urls = new ArrayList<>();
        List<Map<String, String>> headers = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();
        List<String> contents = new ArrayList<>();
        contents.add("");
        contents.add("{}");
        contents.add("{\"access_token\":\"access token 11\"}");
        contents.add("");
        contents.add("{\"access_token\":\"access token 12\"}");
        contents.add("{}");
        contents.add("{\"access_token\":\"access token 13\"}");
        contents.add("{\"ticket\":\"jsapi ticket 13\"}");
        contents.add("{\"access_token\":\"access token 14\"}");
        contents.add("{\"ticket\":\"jsapi ticket 14\"}");
        httpAspect.get(urls, headers, parameters, contents);

        ((WeixinServiceImpl) weixinService).executeHourJob();
        Assert.assertEquals(10, urls.size());
        for (int i = 0; i < 2; i++) {
            String url = urls.get(i);
            Assert.assertTrue(url.contains("appid"));
            Assert.assertTrue(url.contains("secret"));
            String appId = url.substring(url.indexOf("appid") + 6, url.lastIndexOf('&'));
            String index = appId.substring(appId.lastIndexOf(' '));
            WeixinModel weixin = findByAppId(appId);
            Assert.assertEquals("access token" + index, weixin.getAccessToken());
            Assert.assertEquals("jsapi ticket" + index, weixin.getJsapiTicket());
            Assert.assertTrue(Math.abs(System.currentTimeMillis() - weixin.getTime().getTime()
                    - numeric.toInt(index.trim()) * TimeUnit.Hour.getTime()) < 2000L);
        }

        String url = urls.get(2);
        Assert.assertTrue(url.contains("appid"));
        Assert.assertTrue(url.contains("secret"));
        String appId = url.substring(url.indexOf("appid") + 6, url.lastIndexOf('&'));
        String index = appId.substring(appId.lastIndexOf(' '));
        WeixinModel weixin = findByAppId(appId);
        Assert.assertEquals("access token 11", weixin.getAccessToken());
        Assert.assertEquals("jsapi ticket" + index, weixin.getJsapiTicket());
        Assert.assertTrue(Math.abs(System.currentTimeMillis() - weixin.getTime().getTime()) < 2000L);
        Assert.assertTrue(urls.get(3).contains("access token 11"));

        url = urls.get(4);
        Assert.assertTrue(url.contains("appid"));
        Assert.assertTrue(url.contains("secret"));
        appId = url.substring(url.indexOf("appid") + 6, url.lastIndexOf('&'));
        index = appId.substring(appId.lastIndexOf(' '));
        weixin = findByAppId(appId);
        Assert.assertEquals("access token 12", weixin.getAccessToken());
        Assert.assertEquals("jsapi ticket" + index, weixin.getJsapiTicket());
        Assert.assertTrue(Math.abs(System.currentTimeMillis() - weixin.getTime().getTime()) < 2000L);
        Assert.assertTrue(urls.get(5).contains("access token 12"));

        url = urls.get(6);
        Assert.assertTrue(url.contains("appid"));
        Assert.assertTrue(url.contains("secret"));
        appId = url.substring(url.indexOf("appid") + 6, url.lastIndexOf('&'));
        weixin = findByAppId(appId);
        Assert.assertEquals("access token 13", weixin.getAccessToken());
        Assert.assertEquals("jsapi ticket 13", weixin.getJsapiTicket());
        Assert.assertTrue(Math.abs(System.currentTimeMillis() - weixin.getTime().getTime()) < 2000L);
        Assert.assertTrue(urls.get(7).contains("access token 13"));

        url = urls.get(8);
        Assert.assertTrue(url.contains("appid"));
        Assert.assertTrue(url.contains("secret"));
        appId = url.substring(url.indexOf("appid") + 6, url.lastIndexOf('&'));
        weixin = findByAppId(appId);
        Assert.assertEquals("access token 14", weixin.getAccessToken());
        Assert.assertEquals("jsapi ticket 14", weixin.getJsapiTicket());
        Assert.assertTrue(Math.abs(System.currentTimeMillis() - weixin.getTime().getTime()) < 2000L);
        Assert.assertTrue(urls.get(9).contains("access token 14"));

        field.set(weixinService, object);
        schedulerAspect.press();
    }
}
