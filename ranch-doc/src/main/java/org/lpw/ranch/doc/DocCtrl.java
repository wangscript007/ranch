package org.lpw.ranch.doc;

import org.lpw.ranch.audit.AuditCtrlSupport;
import org.lpw.ranch.audit.AuditService;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.template.Templates;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Controller(DocModel.NAME + ".ctrl")
@Execute(name = "/doc/", key = DocModel.NAME, code = "14")
public class DocCtrl extends AuditCtrlSupport {
    @Inject
    private Request request;
    @Inject
    private DocService docService;

    /**
     * 检索文档。
     *
     * @return {PageList[DocModel]}。
     */
    @Execute(name = "query")
    public Object query() {
        return docService.query();
    }

    /**
     * 获取指定ID的文档信息集。
     * ids ID集。
     *
     * @return {id:{DocModel}}。
     */
    @Execute(name = "get", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "ids", failureCode = 13)
    })
    public Object get() {
        return docService.get(request.getAsArray("ids"));
    }

    /**
     * 创建新回复。
     * key 服务key。
     * owner 所有者ID。
     * author 作者ID。
     * scoreMin 最小分数值。
     * scoreMax 最大分数值。
     * sort 顺序。
     * subject 标题。
     * image 主图URI地址。
     * thumbnail 缩略图URI地址。
     * summary 标签。
     * label 标签。
     * content 内容。
     *
     * @return ""。
     */
    @Execute(name = "create", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.ID, parameter = "owner", failureCode = 3),
            @Validate(validator = Validators.ID, parameter = "author", failureCode = 4),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "subject", failureCode = 5),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "subject", failureCode = 6),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "image", failureCode = 7),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "thumbnail", failureCode = 8),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "source", failureCode = 9),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "content", failureCode = 10)
    })
    public Object create() {
        return docService.create(request.setToModel(new DocModel()));
    }

    /**
     * 阅读文档。
     * id 文档ID。
     * html 是否需要包装为HTML文档，true为需要，其他不需要。
     * css 应用样式表名称集，多个间以逗号分割。仅html=true时有效。
     *
     * @return 文档内容。
     */
    @Execute(name = "read", type = Templates.FREEMARKER, template = "read", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 11),
            @Validate(validator = DocService.VALIDATOR_EXISTS, parameter = "id", failureCode = 12)
    })
    public Object read() {
        Map<String, Object> map = new HashMap<>();
        boolean html = "true".equals(request.get("html"));
        map.put("html", html);
        if (html) {
            map.put("model", docService.findById(request.get("id")));
            map.put("css", request.getAsArray("css"));
        }
        map.put("content", docService.read(request.get("id")));

        return map;
    }

    /**
     * 增减收藏数。
     * id ID值。
     * favorite 收藏数：正数表示增加，负数表示减少。
     *
     * @return ""。
     */
    @Execute(name = "favorite", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 11),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = DocService.VALIDATOR_EXISTS, parameter = "id", failureCode = 12)
    })
    public Object favorite() {
        docService.favorite(request.get("id"), request.getAsInt("favorite"));

        return "";
    }

    /**
     * 增减收藏数。
     * id ID值。
     * comment 评论数：正数表示增加，负数表示减少。
     *
     * @return ""。
     */
    @Execute(name = "comment", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 11),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = DocService.VALIDATOR_EXISTS, parameter = "id", failureCode = 12)
    })
    public Object comment() {
        docService.comment(request.get("id"), request.getAsInt("comment"));

        return "";
    }

    /**
     * 刷新缓存信息。
     *
     * @return ""。
     */
    @Execute(name = "refresh", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object refresh() {
        docService.refresh();

        return "";
    }

    @Override
    protected AuditService getAuditService() {
        return docService;
    }
}
