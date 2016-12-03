package org.lpw.ranch.classify;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.model.Recycle;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.test.MockScheduler;
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
    private Converter converter;
    @Autowired
    private Cache cache;
    @Autowired
    private LiteOrm liteOrm;
    @Autowired
    private Request request;
    @Autowired
    private MockHelper mockHelper;
    @Autowired
    private MockScheduler mockScheduler;
    @Autowired
    private ClassifyService classifyService;

    @Test
    public void query() {
        liteOrm.delete(new LiteQuery(ClassifyModel.class), null);
        for (int i = 0; i < 20; i++) {
            ClassifyModel classify = new ClassifyModel();
            classify.setCode("code " + converter.toString(i, "00"));
            classify.setName("name " + converter.toString(i, "00"));
            classify.setRecycle((i % 2 == 0 ? Recycle.No : Recycle.Yes).getValue());
            liteOrm.save(classify);
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
    public void tree() {
        mockScheduler.pause();
        cache.remove(ClassifyModel.NAME + ".service.tree:");
        liteOrm.delete(new LiteQuery(ClassifyModel.class), null);
        ClassifyModel classify1a = create(1, false);
        ClassifyModel classify1b = create(1, false);
        create(1, true);
        ClassifyModel classify11 = create(11, false);
        ClassifyModel classify12 = create(12, false);
        ClassifyModel classify111 = create(111, false);
        ClassifyModel classify121 = create(121, false);
        ClassifyModel classify2 = create(2345, false);

        mockHelper.reset();
        mockHelper.mock("/classify/tree");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1202, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(ClassifyModel.NAME + ".code")), object.getString("message"));

        for (int i = 0; i < 5; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("code", "code");
            mockHelper.mock("/classify/tree");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getInt("code"));
            JSONArray data = object.getJSONArray("data");
            Assert.assertEquals(3, data.size());
            if (classify1a.getId().compareTo(classify1b.getId()) < 0) {
                Assert.assertEquals(classify1a.getId(), data.getJSONObject(0).getString("id"));
                Assert.assertEquals(classify1b.getId(), data.getJSONObject(1).getString("id"));
            } else {
                Assert.assertEquals(classify1b.getId(), data.getJSONObject(0).getString("id"));
                Assert.assertEquals(classify1a.getId(), data.getJSONObject(1).getString("id"));
            }
            Assert.assertEquals(classify2.getId(), data.getJSONObject(2).getString("id"));
            JSONArray array = data.getJSONObject(0).getJSONArray("children");
            Assert.assertEquals(2, array.size());
            Assert.assertEquals(classify11.getId(), array.getJSONObject(0).getString("id"));
            Assert.assertEquals(classify12.getId(), array.getJSONObject(1).getString("id"));
            Assert.assertFalse(data.getJSONObject(1).has("children"));
            JSONArray children = array.getJSONObject(0).getJSONArray("children");
            Assert.assertEquals(1, children.size());
            Assert.assertEquals(classify111.getId(), children.getJSONObject(0).getString("id"));
            children = array.getJSONObject(1).getJSONArray("children");
            Assert.assertEquals(1, children.size());
            Assert.assertEquals(classify121.getId(), children.getJSONObject(0).getString("id"));

            classify1a.setRecycle(Recycle.Yes.getValue());
            liteOrm.save(classify1a);
            classify121.setRecycle(Recycle.Yes.getValue());
            liteOrm.save(classify121);
        }

        classifyService.refresh();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.mock("/classify/tree");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONArray data = object.getJSONArray("data");
        Assert.assertEquals(2, data.size());
        Assert.assertEquals(classify1b.getId(), data.getJSONObject(0).getString("id"));
        Assert.assertEquals(classify2.getId(), data.getJSONObject(1).getString("id"));
        JSONArray array = data.getJSONObject(0).getJSONArray("children");
        Assert.assertEquals(2, array.size());
        Assert.assertEquals(classify11.getId(), array.getJSONObject(0).getString("id"));
        Assert.assertEquals(classify12.getId(), array.getJSONObject(1).getString("id"));
        JSONArray children = array.getJSONObject(0).getJSONArray("children");
        Assert.assertEquals(1, children.size());
        Assert.assertEquals(classify111.getId(), children.getJSONObject(0).getString("id"));
        Assert.assertFalse(array.getJSONObject(1).has("children"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.mock("/classify/tree");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(1, data.size());
        Assert.assertEquals(classify1b.getId(), data.getJSONObject(0).getString("id"));
        array = data.getJSONObject(0).getJSONArray("children");
        Assert.assertEquals(2, array.size());
        Assert.assertEquals(classify11.getId(), array.getJSONObject(0).getString("id"));
        Assert.assertEquals(classify12.getId(), array.getJSONObject(1).getString("id"));
        children = array.getJSONObject(0).getJSONArray("children");
        Assert.assertEquals(1, children.size());
        Assert.assertEquals(classify111.getId(), children.getJSONObject(0).getString("id"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 3");
        mockHelper.mock("/classify/tree");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONArray("data");
        Assert.assertTrue(data.isEmpty());
        mockScheduler.press();
    }

    @Test
    public void get() {
        liteOrm.delete(new LiteQuery(ClassifyModel.class), null);
        List<ClassifyModel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            list.add(create(i, null, i % 2 == 0));
        ClassifyModel classify1 = create(11, false);
        JSONObject label = new JSONObject();
        for (int i = 0; i < 2; i++)
            label.put(list.get(i).getId(), list.get(i).getName());
        ClassifyModel classify2 = create(22, label.toString(), false);
        label.put("links", false);
        ClassifyModel classify3 = create(33, label.toString(), false);
        label.put("links", true);
        ClassifyModel classify4 = create(44, label.toString(), false);

        mockHelper.reset();
        mockHelper.mock("/classify/get");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertTrue(data.isEmpty());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id," + list.get(0).getId() + "," + list.get(1).getId() + "," + list.get(1).getId());
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertFalse(data.has("id"));
        Assert.assertFalse(data.has(list.get(0).getId()));
        JSONObject classify = data.getJSONObject(list.get(1).getId());
        Assert.assertEquals(list.get(1).getId(), classify.getString("id"));
        equalsCodeName(classify, "code 1", "name 1");

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id," + list.get(0).getId() + "," + list.get(1).getId() + "," + list.get(1).getId());
        mockHelper.getRequest().addParameter("links", "true");
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertFalse(data.has("id"));
        Assert.assertFalse(data.has(list.get(0).getId()));
        classify = data.getJSONObject(list.get(1).getId());
        Assert.assertEquals(list.get(1).getId(), classify.getString("id"));
        equalsCodeName(classify, "code 1", "name 1");

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", classify1.getId());
        mockHelper.getRequest().addParameter("links", "true");
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(1, data.size());
        classify = data.getJSONObject(classify1.getId());
        Assert.assertEquals(classify1.getId(), classify.getString("id"));
        equalsCodeName(classify, "code 11", "name 11");

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", classify2.getId());
        mockHelper.getRequest().addParameter("links", "true");
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(1, data.size());
        classify = data.getJSONObject(classify2.getId());
        Assert.assertEquals(classify2.getId(), classify.getString("id"));
        equalsCodeName(classify, "code 22", "name 22");

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", classify3.getId());
        mockHelper.getRequest().addParameter("links", "true");
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(1, data.size());
        classify = data.getJSONObject(classify3.getId());
        Assert.assertEquals(classify3.getId(), classify.getString("id"));
        equalsCodeName(classify, "code 33", "name 33");

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", classify4.getId());
        mockHelper.getRequest().addParameter("links", "true");
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(2, data.size());
        classify = data.getJSONObject(classify4.getId());
        Assert.assertEquals(classify4.getId(), classify.getString("id"));
        equalsCodeName(classify, "code 44", "name 44");
        classify = data.getJSONObject(list.get(1).getId());
        Assert.assertEquals(list.get(1).getId(), classify.getString("id"));
        equalsCodeName(classify, "code 1", "name 1");
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
        for (int i = 0; i < 2; i++)
            list.add(create(i, false));

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

    private void equalsCodeName(JSONObject object, String code, String name) {
        Assert.assertEquals(code, object.getString("code"));
        Assert.assertEquals(name, object.getString("name"));
    }

    @Test
    public void delete() {
        liteOrm.delete(new LiteQuery(ClassifyModel.class), null);
        List<ClassifyModel> list = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            list.add(create(i, false));

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

        String cacheKey = ClassifyModel.NAME + ".service.random";
        cache.remove(cacheKey);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(1).getId());
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/delete");
        String random = cache.get(cacheKey);
        Assert.assertEquals(32, random.length());
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        for (int i = 0, size = list.size(); i < size; i++) {
            ClassifyModel classify = liteOrm.findById(ClassifyModel.class, list.get(i).getId());
            if (i == 1 || i > 9) {
                Assert.assertEquals(Recycle.Yes.getValue(), classify.getRecycle());
                Assert.assertFalse(classifyService.getJsons(new String[]{classify.getId()}, false).has(classify.getId()));
            } else {
                Assert.assertEquals(Recycle.No.getValue(), classify.getRecycle());
                Assert.assertTrue(classifyService.getJsons(new String[]{classify.getId()}, false).has(classify.getId()));
            }
        }
    }

    @Test
    public void recycle() {
        liteOrm.delete(new LiteQuery(ClassifyModel.class), null);
        for (int i = 0; i < 20; i++)
            create(10 + i, i % 2 == 0);

        mockHelper.reset();
        mockHelper.mock("/classify/recycle");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1291, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "1");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/recycle");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(10, data.getInt("count"));
        Assert.assertEquals(20, data.getInt("size"));
        Assert.assertEquals(1, data.getInt("number"));
        JSONArray array = data.getJSONArray("list");
        for (int i = 0, size = array.size(); i < size; i++)
            equalsCodeName(array.getJSONObject(i), "code " + (10 + 2 * i), "name " + (10 + 2 * i));
    }

    @Test
    public void restore() {
        liteOrm.delete(new LiteQuery(ClassifyModel.class), null);
        List<ClassifyModel> list = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            list.add(create(i, true));

        mockHelper.reset();
        mockHelper.mock("/classify/restore");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1201, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(ClassifyModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/classify/restore");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1201, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(ClassifyModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.mock("/classify/restore");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1291, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/restore");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        list.forEach(classify -> Assert.assertEquals(Recycle.Yes.getValue(), liteOrm.findById(ClassifyModel.class, classify.getId()).getRecycle()));

        String cacheKey = ClassifyModel.NAME + ".service.random";
        cache.remove(cacheKey);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(10).getId());
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/restore");
        String random = cache.get(cacheKey);
        Assert.assertEquals(32, random.length());
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        for (int i = 0, size = list.size(); i < size; i++) {
            ClassifyModel classify = liteOrm.findById(ClassifyModel.class, list.get(i).getId());
            if (i == 1 || i == 10) {
                Assert.assertEquals(Recycle.No.getValue(), classify.getRecycle());
                Assert.assertTrue(classifyService.getJsons(new String[]{classify.getId()}, false).has(classify.getId()));
            } else {
                Assert.assertEquals(Recycle.Yes.getValue(), classify.getRecycle());
                Assert.assertFalse(classifyService.getJsons(new String[]{classify.getId()}, false).has(classify.getId()));
            }
        }
    }

    private ClassifyModel create(int code, boolean recycle) {
        return create(code, "label " + code, recycle);
    }

    private ClassifyModel create(int code, String label, boolean recycle) {
        ClassifyModel classify = new ClassifyModel();
        classify.setCode("code " + code);
        classify.setName("name " + code);
        classify.setLabel(label);
        classify.setRecycle((recycle ? Recycle.Yes : Recycle.No).getValue());
        liteOrm.save(classify);

        return classify;
    }

    @Test
    public void refresh() {
        String cacheKey = ClassifyModel.NAME + ".service.random";
        cache.remove(cacheKey);
        mockHelper.reset();
        mockHelper.mock("/classify/refresh");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1291, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));
        Assert.assertNull(cache.get(cacheKey));

        mockHelper.reset();
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/refresh");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        String random = cache.get(cacheKey);
        Assert.assertEquals(32, random.length());

        ((ClassifyServiceImpl) classifyService).executeDateJob();
        String random2 = cache.get(cacheKey);
        Assert.assertEquals(32, random2.length());
        Assert.assertNotEquals(random, random2);
    }
}
