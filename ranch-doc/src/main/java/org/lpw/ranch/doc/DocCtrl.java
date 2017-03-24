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

    @Execute(name = "query-by-key", validates = {
            @Validate(validator = Validators.BETWEEN, number = {0, 2}, parameter = "audit", failureCode = 13),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.SIGN)
    })
    public Object queryByKey() {
        return docService.queryByKey(request.getAsInt("audit"), request.get("key"));
    }

    @Execute(name = "query-by-owner", validates = {
            @Validate(validator = Validators.BETWEEN, number = {0, 2}, parameter = "audit", failureCode = 13),
            @Validate(validator = Validators.ID, parameter = "owner", failureCode = 3),
            @Validate(validator = Validators.SIGN)
    })
    public Object queryByOwner() {
        return docService.queryByOwner(request.getAsInt("audit"), request.get("owner"));
    }

    @Execute(name = "query-by-author", validates = {
            @Validate(validator = Validators.BETWEEN, number = {0, 2}, parameter = "audit", failureCode = 13),
            @Validate(validator = Validators.ID, parameter = "author", failureCode = 4),
            @Validate(validator = Validators.SIGN)
    })
    public Object queryByAuthor() {
        return docService.queryByAuthor(request.getAsInt("audit"), request.get("author"));
    }

    @Execute(name = "query-by-user", validates = {
            @Validate(validator = Validators.BETWEEN, number = {0, 2}, parameter = "audit", failureCode = 13),
            @Validate(validator = Validators.ID, parameter = "author", failureCode = 4),
            @Validate(validator = Validators.SIGN)
    })
    public Object queryByUser() {
        return docService.queryByAuthor();
    }

    @Execute(name = "get", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "ids", failureCode = 13)
    })
    public Object get() {
        return docService.get(request.getAsArray("ids"));
    }

    @Execute(name = "create", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.ID, parameter = "owner", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "subject", failureCode = 5),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "subject", failureCode = 6),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "image", failureCode = 7),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "thumbnail", failureCode = 8),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "source", failureCode = 9),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "content", failureCode = 10),
            @Validate(validator = Validators.SIGN)
    })
    public Object create() {
        return docService.create(request.setToModel(new DocModel()));
    }

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

    @Execute(name = "favorite", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 11),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = DocService.VALIDATOR_EXISTS, parameter = "id", failureCode = 12)
    })
    public Object favorite() {
        docService.favorite(request.get("id"), request.getAsInt("favorite"));

        return "";
    }

    @Execute(name = "comment", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 11),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = DocService.VALIDATOR_EXISTS, parameter = "id", failureCode = 12)
    })
    public Object comment() {
        docService.comment(request.get("id"), request.getAsInt("comment"));

        return "";
    }

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
