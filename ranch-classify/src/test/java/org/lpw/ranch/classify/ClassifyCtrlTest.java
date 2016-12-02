package org.lpw.ranch.classify;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.model.Recycle;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.test.mock.MockHelper;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class ClassifyCtrlTest extends TephraTestSupport {
    @Autowired
    private Message message;
    @Autowired
    private Generator generator;
    @Autowired
    protected Converter converter;
    @Autowired
    private LiteOrm liteOrm;
    @Autowired
    private Request request;
    @Autowired
    private MockHelper mockHelper;
    @Autowired
    private ClassifyService classifyService;

    @Test
    public void query() {
        liteOrm.delete(new LiteQuery(ClassifyModel.class), null);
        List<ClassifyModel> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ClassifyModel classify = new ClassifyModel();
            classify.setCode("code " + converter.toString(i, "00"));
            classify.setName("name " + converter.toString(i, "00"));
            classify.setRecycle((i % 2 == 0 ? Recycle.No : Recycle.Yes).getValue());
            liteOrm.save(classify);
            list.add(classify);
        }

        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "1");
        mockHelper.mock("/classify/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(10, data.getInt("count"));
        Assert.assertEquals(20, data.getInt("size"));
        Assert.assertEquals(1, data.getInt("number"));
        JSONArray array = data.getJSONArray("list");
        for (int i = 0, size = array.size(); i < size; i++)
            equalsCodeName(array.getJSONObject(i), "code " + converter.toString(2 * i, "00"), "name " + converter.toString(2 * i, "00"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "1");
        mockHelper.mock("/classify/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(5, data.getInt("count"));
        Assert.assertEquals(20, data.getInt("size"));
        Assert.assertEquals(1, data.getInt("number"));
        array = data.getJSONArray("list");
        for (int i = 0; i < 5; i++)
            equalsCodeName(array.getJSONObject(i), "code " + (10 + 2 * i), "name " + (10 + 2 * i));
    }

    @Test
    public void get() {
        liteOrm.delete(new LiteQuery(ClassifyModel.class), null);
        List<ClassifyModel> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ClassifyModel classify = new ClassifyModel();
            classify.setCode("code " + i);
            classify.setName("name " + i);
            liteOrm.save(classify);
            list.add(classify);
        }

        mockHelper.reset();
        mockHelper.mock("/classify/get");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertTrue(data.isEmpty());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id," + list.get(0).getId() + "," + list.get(0).getId());
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertTrue(data.getJSONObject("id").isEmpty());
        JSONObject classify = data.getJSONObject(list.get(0).getId());
        Assert.assertEquals(list.get(0).getId(), classify.getString("id"));
        equalsCodeName(classify, "code 0", "name 0");
    }

    @Test
    public void create() {
        liteOrm.delete(new LiteQuery(ClassifyModel.class), null);
        mockHelper.reset();
        mockHelper.mock("/classify/create");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1202, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(ClassifyModel.NAME + ".code")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", generator.random(101));
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1203, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".code"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1204, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(ClassifyModel.NAME + ".name")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("name", generator.random(101));
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1205, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".name"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("name", "name");
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1291, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.getRequest().addParameter("name", "name 1");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        equalsCodeName(data, "code 1", "name 1");
        Assert.assertFalse(data.has("label"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 2");
        mockHelper.getRequest().addParameter("name", "name 2");
        mockHelper.getRequest().addParameter("label", "label 2");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        equalsCodeName(data, "code 2", "name 2");
        Assert.assertEquals("label 2", data.getString("label"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 3");
        mockHelper.getRequest().addParameter("name", "name 3");
        mockHelper.getRequest().addParameter("label", "{\"id\":\"id\",\"name\":\"name\"}");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        equalsCodeName(data, "code 3", "name 3");
        JSONObject label = data.getJSONObject("label");
        Assert.assertEquals("id", label.getString("id"));
        Assert.assertEquals("name", label.getString("name"));
    }

    @Test
    public void modify() {
        liteOrm.delete(new LiteQuery(ClassifyModel.class), null);
        List<ClassifyModel> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ClassifyModel classify = new ClassifyModel();
            classify.setCode("code " + i);
            classify.setName("name " + i);
            classify.setLabel("label " + i);
            liteOrm.save(classify);
            list.add(classify);
        }

        mockHelper.reset();
        mockHelper.mock("/classify/modify");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1201, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(ClassifyModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1201, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(ClassifyModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("code", generator.random(101));
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1203, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".code"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("code", "new code");
        mockHelper.getRequest().addParameter("name", generator.random(101));
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1205, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".name"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("code", "new code");
        mockHelper.getRequest().addParameter("name", "new name");
        mockHelper.getRequest().addParameter("label", "new label");
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1291, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.getRequest().addParameter("code", "new code");
        mockHelper.getRequest().addParameter("name", "new name");
        mockHelper.getRequest().addParameter("label", "new label");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertTrue(data.isEmpty());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("code", "new code");
        mockHelper.getRequest().addParameter("name", "new name");
        mockHelper.getRequest().addParameter("label", "new label");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(list.get(0).getId(), data.getString("id"));
        equalsCodeName(data, "new code", "new name");
        Assert.assertEquals("new label", data.getString("label"));
        ClassifyModel classify = liteOrm.findById(ClassifyModel.class, list.get(0).getId());
        Assert.assertEquals("new code", classify.getCode());
        Assert.assertEquals("new name", classify.getName());
        Assert.assertEquals("new label", classify.getLabel());
        classify = liteOrm.findById(ClassifyModel.class, list.get(1).getId());
        Assert.assertEquals("code 1", classify.getCode());
        Assert.assertEquals("name 1", classify.getName());
        Assert.assertEquals("label 1", classify.getLabel());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(list.get(0).getId(), data.getString("id"));
        equalsCodeName(data, "new code", "new name");
        Assert.assertEquals("new label", data.getString("label"));
        classify = liteOrm.findById(ClassifyModel.class, list.get(0).getId());
        Assert.assertEquals("new code", classify.getCode());
        Assert.assertEquals("new name", classify.getName());
        Assert.assertEquals("new label", classify.getLabel());
        classify = liteOrm.findById(ClassifyModel.class, list.get(1).getId());
        Assert.assertEquals("code 1", classify.getCode());
        Assert.assertEquals("name 1", classify.getName());
        Assert.assertEquals("label 1", classify.getLabel());
    }

    protected void equalsCodeName(JSONObject object, String code, String name) {
        Assert.assertEquals(code, object.getString("code"));
        Assert.assertEquals(name, object.getString("name"));
    }

    @Test
    public void delete() {
        liteOrm.delete(new LiteQuery(ClassifyModel.class), null);
        List<ClassifyModel> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ClassifyModel classify = new ClassifyModel();
            classify.setCode("code " + i);
            classify.setName("name " + i);
            classify.setLabel("label " + i);
            liteOrm.save(classify);
            list.add(classify);
        }

        mockHelper.reset();
        mockHelper.mock("/classify/delete");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1201, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(ClassifyModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/classify/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1201, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(ClassifyModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.mock("/classify/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1291, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        list.forEach(classify -> Assert.assertEquals(Recycle.No.getValue(), liteOrm.findById(ClassifyModel.class, classify.getId()).getRecycle()));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertEquals(Recycle.Yes.getValue(), liteOrm.findById(ClassifyModel.class, list.get(0).getId()).getRecycle());
        Assert.assertEquals(Recycle.No.getValue(), liteOrm.findById(ClassifyModel.class, list.get(1).getId()).getRecycle());
        object = classifyService.getJsons(new String[]{list.get(0).getId(), list.get(1).getId()});
        Assert.assertTrue(object.getJSONObject(list.get(0).getId()).isEmpty());
        Assert.assertFalse(object.getJSONObject(list.get(1).getId()).isEmpty());
    }
}
