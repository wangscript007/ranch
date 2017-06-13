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
import java.lang.reflect.Field;

/**
 * @author lpw
 */
public class QueryTest extends TestSupport {
    @Inject
    private AccountService accountService;

    @Test
    public void query() throws Exception {
        Field field = AccountServiceImpl.class.getDeclaredField("balance");
        field.setAccessible(true);
        Object balance = field.get(accountService);
        field.set(accountService, 0);

        mockUser();
        mockHelper.reset();
        mockHelper.mock("/account/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2204, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.empty-and-not-sign-in", message.get(AccountModel.NAME + ".user")), object.getString("message"));

        mockUser();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", generator.random(37));
        mockHelper.mock("/account/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2204, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.empty-and-not-sign-in", message.get(AccountModel.NAME + ".user")), object.getString("message"));

        mockUser();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user 1");
        mockHelper.mock("/account/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONArray data = object.getJSONArray("data");
        Assert.assertEquals(1, data.size());
        JSONObject obj = data.getJSONObject(0);
        Assert.assertEquals(11, obj.size());
        JSONObject user = obj.getJSONObject("user");
        Assert.assertEquals("user 1", user.getString("id"));
        Assert.assertEquals("name user 1", user.getString("name"));
        Assert.assertEquals("", obj.getString("owner"));
        for (String property : new String[]{"type", "balance", "deposit", "withdraw", "reward", "profit", "consume", "pending"})
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
        Assert.assertEquals(0, account.getPending());
        Assert.assertEquals(digest.md5(AccountModel.NAME + ".service.checksum&user 1&&0&0&0&0&0&0&0&0"), account.getChecksum());
        Assert.assertEquals(0, liteOrm.count(new LiteQuery(LogModel.class), null));

        mockUser();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user 2");
        mockHelper.getRequest().addParameter("owner", "owner 2");
        mockHelper.mock("/account/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(1, data.size());
        obj = data.getJSONObject(0);
        Assert.assertEquals(11, obj.size());
        user = obj.getJSONObject("user");
        Assert.assertEquals("user 2", user.getString("id"));
        Assert.assertEquals("name user 2", user.getString("name"));
        Assert.assertEquals("owner 2", obj.getString("owner"));
        for (String property : new String[]{"type", "balance", "deposit", "withdraw", "reward", "profit", "consume", "pending"})
            Assert.assertEquals(0, obj.getIntValue(property));
        account = liteOrm.findById(AccountModel.class, obj.getString("id"));
        Assert.assertEquals("user 2", account.getUser());
        Assert.assertEquals("owner 2", account.getOwner());
        Assert.assertEquals(0, account.getType());
        Assert.assertEquals(0, account.getBalance());
        Assert.assertEquals(0, account.getDeposit());
        Assert.assertEquals(0, account.getWithdraw());
        Assert.assertEquals(0, account.getReward());
        Assert.assertEquals(0, account.getProfit());
        Assert.assertEquals(0, account.getConsume());
        Assert.assertEquals(0, account.getPending());
        Assert.assertEquals(digest.md5(AccountModel.NAME + ".service.checksum&user 2&owner 2&0&0&0&0&0&0&0&0"), account.getChecksum());
        Assert.assertEquals(0, liteOrm.count(new LiteQuery(LogModel.class), null));

        field.set(accountService, 100);
        mockUser();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,data:{\"id\":\"sign in id\"}}");
        for (int i = 0; i < 2; i++) {
            mockHelper.reset();
            mockHelper.mock("/account/query");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            data = object.getJSONArray("data");
            Assert.assertEquals(1, data.size());
            obj = data.getJSONObject(0);
            Assert.assertEquals(11, obj.size());
            user = obj.getJSONObject("user");
            Assert.assertEquals("sign in id", user.getString("id"));
            Assert.assertEquals("name sign in id", user.getString("name"));
            Assert.assertEquals("", obj.getString("owner"));
            for (String property : new String[]{"type", "withdraw", "reward", "profit", "consume", "pending"})
                Assert.assertEquals(0, obj.getIntValue(property));
            for (String property : new String[]{"balance", "deposit"})
                Assert.assertEquals(100, obj.getIntValue(property));
            account = liteOrm.findById(AccountModel.class, obj.getString("id"));
            Assert.assertEquals("sign in id", account.getUser());
            Assert.assertEquals("", account.getOwner());
            Assert.assertEquals(0, account.getType());
            Assert.assertEquals(100, account.getBalance());
            Assert.assertEquals(100, account.getDeposit());
            Assert.assertEquals(0, account.getWithdraw());
            Assert.assertEquals(0, account.getReward());
            Assert.assertEquals(0, account.getProfit());
            Assert.assertEquals(0, account.getConsume());
            Assert.assertEquals(0, account.getPending());
            Assert.assertEquals(digest.md5(AccountModel.NAME + ".service.checksum&sign in id&&0&100&100&0&0&0&0&0"), account.getChecksum());
            PageList<LogModel> pl = liteOrm.query(new LiteQuery(LogModel.class), null);
            Assert.assertEquals(1, pl.getList().size());
            LogModel log = pl.getList().get(0);
            Assert.assertEquals("sign in id", log.getUser());
            Assert.assertEquals(account.getId(), log.getAccount());
            Assert.assertEquals("deposit", log.getType());
            Assert.assertEquals(100, log.getAmount());
            Assert.assertEquals(100, log.getBalance());
            Assert.assertEquals(3, log.getState());
            if (i == 0) {
                Assert.assertTrue(System.currentTimeMillis() - log.getStart().getTime() < 2000);
                Assert.assertTrue(System.currentTimeMillis() - log.getEnd().getTime() < 2000);
            } else {
                Assert.assertTrue(System.currentTimeMillis() - log.getStart().getTime() > 2000);
                Assert.assertTrue(System.currentTimeMillis() - log.getEnd().getTime() > 2000);
            }
            thread.sleep(3, TimeUnit.Second);
        }

        field.set(accountService, balance);
    }
}
