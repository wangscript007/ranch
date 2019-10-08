package org.lpw.ranch.doc;

import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.audit.AuditCtrlSupport;
import org.lpw.ranch.audit.AuditHelper;
import org.lpw.ranch.audit.AuditService;
import org.lpw.ranch.user.helper.UserHelper;
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
@Execute(name = "/doc/", key = DocModel.NAME, code = "114")
public class DocCtrl extends AuditCtrlSupport {
    @Inject
    private Request request;
    @Inject
    private AuditHelper auditHelper;
    @Inject
    private DocService docService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return docService.query(request.get("classify"), request.get("author"), request.get("category"), request.get("subject"),
                request.get("label"), request.get("type"), auditHelper.get(request.getAsInt("audit", -1)));
    }

    @Execute(name = "query-by-author", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object queryByAuthor() {
        return docService.queryByAuthor();
    }

    @Execute(name = "find", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = DocService.VALIDATOR_EXISTS, parameter = "id", failureCode = 2)
    })
    public Object find() {
        return docService.find(request.get("id"), true);
    }

    @Execute(name = "get", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "ids", failureCode = 3)
    })
    public Object get() {
        return docService.get(request.getAsArray("ids"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "classifies", failureCode = 4),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "category", failureCode = 11),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "subject", failureCode = 5),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "subject", failureCode = 6),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "image", failureCode = 7),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "thumbnail", failureCode = 8),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 9),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "source", failureCode = 10),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = DocService.VALIDATOR_EXISTS, emptyable = true, parameter = "id", failureCode = 2)
    })
    public Object save() {
        return docService.save(request.setToModel(DocModel.class), request.getAsArray("classifies"),
                request.getAsBoolean("markdown"));
    }

    @Execute(name = "source", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = DocService.VALIDATOR_EXISTS, parameter = "id", failureCode = 2)
    })
    public Object source() {
        return docService.source(request.get("id"));
    }

    @Execute(name = "index")
    public Object index() {
        return docService.query(request.get("classify"), null, request.get("category"), null, null, null, Audit.Pass);
    }

    @Execute(name = "search")
    public Object search() {
        return docService.search(request.get("category"), request.getAsArray("words"));
    }

    @Execute(name = "read", type = Templates.FREEMARKER, template = "read", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = DocService.VALIDATOR_EXISTS, parameter = "id", failureCode = 2)
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

    @Execute(name = "read-json", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = DocService.VALIDATOR_EXISTS, parameter = "id", failureCode = 2)
    })
    public Object readJson() {
        return docService.readJson(request.get("id"));
    }

    @Execute(name = "favorite", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = DocService.VALIDATOR_EXISTS, parameter = "id", failureCode = 2)
    })
    public Object favorite() {
        docService.favorite(request.get("id"), request.getAsInt("favorite"));

        return "";
    }

    @Execute(name = "comment", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = DocService.VALIDATOR_EXISTS, parameter = "id", failureCode = 2)
    })
    public Object comment() {
        docService.comment(request.get("id"), request.getAsInt("comment"));

        return "";
    }

    @Execute(name = "praise", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = DocService.VALIDATOR_EXISTS, parameter = "id", failureCode = 2)
    })
    public Object praise() {
        docService.praise(request.get("id"));

        return "";
    }

    @Execute(name = "refresh", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object refresh() {
        return docService.refresh();
    }

    @Override
    protected AuditService getAuditService() {
        return docService;
    }
}
