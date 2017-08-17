package org.lpw.ranch.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.account.log.LogModel;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class QueryTest extends TestSupport {
    @Inject
    private AccountService accountService;

    @Test
    public void query() throws Exception {
        mockUser();
        setClassify(0, 0);
        mockHelper.reset();
        mockHelper.mock("/account/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2205, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.not-exists-and-not-sign-in", message.get(AccountModel.NAME + ".user")), object.getString("message"));

        mockUser();
        setClassify(0, 0);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", generator.random(37));
        mockHelper.mock("/account/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2205, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.not-exists-and-not-sign-in", message.get(AccountModel.NAME + ".user")), object.getString("message"));

        mockUser();
        setClassify(0, 0);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user 1");
        mockHelper.mock("/account/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONArray data = object.getJSONArray("data");
        Assert.assertEquals(1, data.size());
        JSONObject obj = data.getJSONObject(0);
        Assert.assertEquals(13, obj.size());
        JSONObject user = obj.getJSONObject("user");
        Assert.assertEquals("user 1", user.getString("id"));
        Assert.assertEquals("name user 1", user.getString("name"));
        Assert.assertEquals("", obj.getString("owner"));
        for (String property : new String[]{"type", "balance", "deposit", "withdraw", "reward", "profit", "consume", "remitIn", "remitOut", "pending"})
            Assert.assertEquals(0, obj.getIntValue(property));
        AccountModel account = liteOrm.findById(AccountModel.class, obj.getString("id"));
        Assert.assertEquals("user 1", account.getUser());
        Assert.assertEquals("", account.getOwner());
        Assert.assertEquals(0, account.getType());
        Assert.assertEquals(0, account.getBalance());
        Assert.assertEquals(0, account.getDeposit());
        Assert.assertEquals(0, account.getWithdraw());
        Assert.assertEquals(0, account.getReward());
        Assert.assertEquals(0, account.getProfit());
        Assert.assertEquals(0, account.getConsume());
        Assert.assertEquals(0, account.getRemitIn());
        Assert.assertEquals(0, account.getRemitOut());
        Assert.assertEquals(0, account.getPending());
        Assert.assertEquals(digest.md5(AccountModel.NAME + ".service.checksum&user 1&&0&0&0&0&0&0&0&0&0&0"), account.getChecksum());
        Assert.assertEquals(0, liteOrm.count(new LiteQuery(LogModel.class), null));

        mockUser();
        setClassify(100, 0);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user 2");
        mockHelper.getRequest().addParameter("owner", "owner 2");
        mockHelper.mock("/account/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(1, data.size());
        obj = data.getJSONObject(0);
        Assert.assertEquals(13, obj.size());
        user = obj.getJSONObject("user");
        Assert.assertEquals("user 2", user.getString("id"));
        Assert.assertEquals("name user 2", user.getString("name"));
        Assert.assertEquals("owner 2", obj.getString("owner"));
        for (String property : new String[]{"type", "deposit", "withdraw", "profit", "consume", "remitIn", "remitOut", "pending"})
            Assert.assertEquals(0, obj.getIntValue(property));
        for (String property : new String[]{"balance", "reward"})
            Assert.assertEquals(100, obj.getIntValue(property));
        account = liteOrm.findById(AccountModel.class, obj.getString("id"));
        Assert.assertEquals("user 2", account.getUser());
        Assert.assertEquals("owner 2", account.getOwner());
        Assert.assertEquals(0, account.getType());
        Assert.assertEquals(100, account.getBalance());
        Assert.assertEquals(0, account.getDeposit());
        Assert.assertEquals(0, account.getWithdraw());
        Assert.assertEquals(100, account.getReward());
        Assert.assertEquals(0, account.getProfit());
        Assert.assertEquals(0, account.getConsume());
        Assert.assertEquals(0, account.getRemitIn());
        Assert.assertEquals(0, account.getRemitOut());
        Assert.assertEquals(0, account.getPending());
        Assert.assertEquals(digest.md5(AccountModel.NAME + ".service.checksum&user 2&owner 2&0&100&0&0&100&0&0&0&0&0"), account.getChecksum());
        PageList<LogModel> pl = liteOrm.query(new LiteQuery(LogModel.class), null);
        Assert.assertEquals(1, pl.getList().size());
        LogModel log = pl.getList().get(0);
        Assert.assertEquals("user 2", log.getUser());
        Assert.assertEquals(account.getId(), log.getAccount());
        Assert.assertEquals("owner 2", log.getOwner());
        Assert.assertEquals("reward", log.getType());
        Assert.assertEquals("sign-up", log.getChannel());
        Assert.assertEquals(100, log.getAmount());
        Assert.assertEquals(100, log.getBalance());
        Assert.assertEquals(1, log.getState());
        Assert.assertTrue(System.currentTimeMillis() - log.getStart().getTime() < 2000L);
        Assert.assertTrue(System.currentTimeMillis() - log.getEnd().getTime() < 2000L);
        Assert.assertNull(log.getJson());

        mockUser();
        setClassify(100, 1);
        mockCarousel.register("ranch.user.sign", "{\"code\":0,data:{\"id\":\"sign in id\"}}");
        for (int i = 0; i < 2; i++) {
            mockHelper.reset();
            mockHelper.mock("/account/query");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            data = object.getJSONArray("data");
            Assert.assertEquals(2, data.size());
            obj = data.getJSONObject(0);
            Assert.assertEquals(13, obj.size());
            user = obj.getJSONObject("user");
            Assert.assertEquals("sign in id", user.getString("id"));
            Assert.assertEquals("name sign in id", user.getString("name"));
            Assert.assertEquals("", obj.getString("owner"));
            for (String property : new String[]{"type", "balance", "deposit", "withdraw", "reward", "profit", "consume", "remitIn", "remitOut", "pending"})
                Assert.assertEquals(0, obj.getIntValue(property));
            account = liteOrm.findById(AccountModel.class, obj.getString("id"));
            Assert.assertEquals("sign in id", account.getUser());
            Assert.assertEquals("", account.getOwner());
            Assert.assertEquals(0, account.getType());
            Assert.assertEquals(0, account.getBalance());
            Assert.assertEquals(0, account.getDeposit());
            Assert.assertEquals(0, account.getWithdraw());
            Assert.assertEquals(0, account.getReward());
            Assert.assertEquals(0, account.getProfit());
            Assert.assertEquals(0, account.getConsume());
            Assert.assertEquals(0, account.getRemitIn());
            Assert.assertEquals(0, account.getRemitOut());
            Assert.assertEquals(0, account.getPending());
            Assert.assertEquals(digest.md5(AccountModel.NAME + ".service.checksum&sign in id&&0&0&0&0&0&0&0&0&0&0"), account.getChecksum());

            obj = data.getJSONObject(1);
            Assert.assertEquals(13, obj.size());
            user = obj.getJSONObject("user");
            Assert.assertEquals("sign in id", user.getString("id"));
            Assert.assertEquals("name sign in id", user.getString("name"));
            Assert.assertEquals("", obj.getString("owner"));
            Assert.assertEquals(1, obj.getIntValue("type"));
            for (String property : new String[]{"deposit", "withdraw", "profit", "consume", "remitIn", "remitOut", "pending"})
                Assert.assertEquals(0, obj.getIntValue(property));
            for (String property : new String[]{"balance", "reward"})
                Assert.assertEquals(100, obj.getIntValue(property));
            account = liteOrm.findById(AccountModel.class, obj.getString("id"));
            Assert.assertEquals("sign in id", account.getUser());
            Assert.assertEquals("", account.getOwner());
            Assert.assertEquals(1, account.getType());
            Assert.assertEquals(100, account.getBalance());
            Assert.assertEquals(0, account.getDeposit());
            Assert.assertEquals(0, account.getWithdraw());
            Assert.assertEquals(100, account.getReward());
            Assert.assertEquals(0, account.getProfit());
            Assert.assertEquals(0, account.getConsume());
            Assert.assertEquals(0, account.getRemitIn());
            Assert.assertEquals(0, account.getRemitOut());
            Assert.assertEquals(0, account.getPending());
            Assert.assertEquals(digest.md5(AccountModel.NAME + ".service.checksum&sign in id&&1&100&0&0&100&0&0&0&0&0"), account.getChecksum());


            pl = liteOrm.query(new LiteQuery(LogModel.class).order("c_start desc"), null);
            Assert.assertEquals(2, pl.getList().size());
            log = pl.getList().get(0);
            Assert.assertEquals("sign in id", log.getUser());
            Assert.assertEquals(account.getId(), log.getAccount());
            Assert.assertEquals("reward", log.getType());
            Assert.assertEquals("sign-up", log.getChannel());
            Assert.assertEquals(100, log.getAmount());
            Assert.assertEquals(100, log.getBalance());
            Assert.assertEquals(1, log.getState());
            if (i == 0) {
                Assert.assertTrue(System.currentTimeMillis() - log.getStart().getTime() < 2000);
                Assert.assertTrue(System.currentTimeMillis() - log.getEnd().getTime() < 2000);
            } else {
                Assert.assertTrue(System.currentTimeMillis() - log.getStart().getTime() > 2000);
                Assert.assertTrue(System.currentTimeMillis() - log.getEnd().getTime() > 2000);
            }
            thread.sleep(3, TimeUnit.Second);
        }
    }

    private void setClassify(int balance, int type) {
        mockCarousel.register("ranch.classify.find", (key, header, parameter, cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject data = new JSONObject();
            data.put("value", parameter.get("key").equals("sign-up.type") ? type : balance);
            json.put("data", data);

            return json.toJSONString();
        });
    }
}
