package org.lpw.ranch.account.log;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.account.AccountModel;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.TimeUnit;

/**
 * @author lpw
 */
public class RejectTest extends TestSupport {
    @Test
    public void reject() {
        AccountModel account1 = new AccountModel();
        account1.setUser("user 1");
        account1.setOwner("owner 1");
        account1.setPending(10);
        liteOrm.save(account1);
        LogModel log1 = new LogModel();
        log1.setUser("user 1");
        log1.setAccount(account1.getId());
        log1.setType("deposit");
        log1.setAmount(1);
        liteOrm.save(log1);
        LogModel log2 = new LogModel();
        log2.setUser("user 1");
        log2.setAccount(account1.getId());
        log2.setType("deposit");
        log2.setAmount(2);
        liteOrm.save(log2);

        mockHelper.reset();
        mockHelper.mock("/account/log/reject");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2221, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(LogModel.NAME + ".ids")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id0," + log1.getId());
        mockHelper.mock("/account/log/reject");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id0," + log1.getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/account/log/reject");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals(1, object.getJSONArray("data").size());
        Assert.assertEquals(log1.getId(), object.getJSONArray("data").getString(0));
        AccountModel account11 = liteOrm.findById(AccountModel.class, account1.getId());
        Assert.assertEquals(0, account11.getBalance());
        Assert.assertEquals(0, account11.getDeposit());
        Assert.assertEquals(9, account11.getPending());
        LogModel log11 = liteOrm.findById(LogModel.class, log1.getId());
        Assert.assertEquals(0, log11.getBalance());
        Assert.assertEquals(2, log11.getState());
        Assert.assertTrue(System.currentTimeMillis() - log11.getEnd().getTime() < 2000L);
        Assert.assertEquals(1, log11.getIndex());
        LogModel log22 = liteOrm.findById(LogModel.class, log2.getId());
        Assert.assertEquals(0, log22.getBalance());
        Assert.assertEquals(0, log22.getState());
        Assert.assertNull(log22.getEnd());
        Assert.assertEquals(2, log22.getIndex());

        thread.sleep(3, TimeUnit.Second);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id0," + log1.getId() + "," + log2.getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/account/log/reject");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals(1, object.getJSONArray("data").size());
        Assert.assertEquals(log2.getId(), object.getJSONArray("data").getString(0));
        AccountModel account111 = liteOrm.findById(AccountModel.class, account1.getId());
        Assert.assertEquals(0, account111.getBalance());
        Assert.assertEquals(0, account111.getDeposit());
        Assert.assertEquals(7, account111.getPending());
        LogModel log111 = liteOrm.findById(LogModel.class, log1.getId());
        Assert.assertEquals(0, log111.getBalance());
        Assert.assertEquals(2, log111.getState());
        Assert.assertTrue(System.currentTimeMillis() - log111.getEnd().getTime() > 2000L);
        Assert.assertEquals(1, log111.getIndex());
        LogModel log222 = liteOrm.findById(LogModel.class, log2.getId());
        Assert.assertEquals(0, log222.getBalance());
        Assert.assertEquals(2, log222.getState());
        Assert.assertTrue(System.currentTimeMillis() - log222.getEnd().getTime() < 2000L);
        Assert.assertEquals(2, log222.getIndex());
    }
}
