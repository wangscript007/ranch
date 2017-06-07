package org.lpw.ranch.recycle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lpw
 */
@Component("ranch.recycle.tester")
public class RecycleTesterImpl implements RecycleTester {
    @Inject
    private Message message;
    @Inject
    private Generator generator;
    @Inject
    private Sign sign;
    @Inject
    private MockHelper mockHelper;

    @Override
    public <T extends RecycleModel> void all(RecycleTesterDao<T> testerDao, String name, String uriPrefix, int codePrefix) {
        delete(testerDao, name, uriPrefix, codePrefix);
        restore(testerDao, name, uriPrefix, codePrefix);
        recycle(testerDao, name, uriPrefix);
    }

    @Override
    public <T extends RecycleModel> void delete(RecycleTesterDao<T> testerDao, String name, String uriPrefix, int codePrefix) {
        testerDao.clear();
        mockHelper.reset();
        mockHelper.mock("/" + uriPrefix + "/delete");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(codePrefix * 100 + 86, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(name + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/" + uriPrefix + "/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(codePrefix * 100 + 86, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(name + ".id")), object.getString("message"));

        RecycleModel model = testerDao.create(0, Recycle.No);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", model.getId());
        mockHelper.mock("/" + uriPrefix + "/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", model.getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/" + uriPrefix + "/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        model = testerDao.findById(model.getId());
        Assert.assertEquals(Recycle.Yes.getValue(), model.getRecycle());
    }

    @Override
    public <T extends RecycleModel> void restore(RecycleTesterDao<T> testerDao, String name, String uriPrefix, int codePrefix) {
        testerDao.clear();
        mockHelper.reset();
        mockHelper.mock("/" + uriPrefix + "/restore");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(codePrefix * 100 + 86, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(name + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/" + uriPrefix + "/restore");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(codePrefix * 100 + 86, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(name + ".id")), object.getString("message"));

        RecycleModel model = testerDao.create(0, Recycle.Yes);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", model.getId());
        mockHelper.mock("/" + uriPrefix + "/restore");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", model.getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/" + uriPrefix + "/restore");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        model = testerDao.findById(model.getId());
        Assert.assertEquals(Recycle.No.getValue(), model.getRecycle());
    }

    @Override
    public <T extends RecycleModel> void recycle(RecycleTesterDao<T> testerDao, String name, String uriPrefix) {
        testerDao.clear();
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            RecycleModel model = testerDao.create(i, Recycle.values()[i % 2]);
            if (i % 2 == 1)
                ids.add(model.getId());
        }

        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "2");
        mockHelper.getRequest().addParameter("pageNum", "1");
        mockHelper.mock("/" + uriPrefix + "/recycle");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "2");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/" + uriPrefix + "/recycle");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(5, data.getIntValue("count"));
        Assert.assertEquals(2, data.getIntValue("size"));
        Assert.assertEquals(1, data.getIntValue("number"));
        JSONArray list = data.getJSONArray("list");
        Assert.assertEquals(2, list.size());
        for (int i = 0; i < 2; i++)
            Assert.assertTrue(ids.contains(list.getJSONObject(i).getString("id")));
    }
}
