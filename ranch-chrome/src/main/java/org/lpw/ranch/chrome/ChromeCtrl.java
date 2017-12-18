package org.lpw.ranch.chrome;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.context.Response;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.template.Templates;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.Coder;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ChromeModel.NAME + ".ctrl")
@Execute(name = "/chrome/", key = ChromeModel.NAME, code = "29")
public class ChromeCtrl {
    @Inject
    private Validator validator;
    @Inject
    private Context context;
    @Inject
    private Coder coder;
    @Inject
    private Generator generator;
    @Inject
    private Message message;
    @Inject
    private Request request;
    @Inject
    private Templates templates;
    @Inject
    private Response response;
    @Inject
    private ChromeService chromeService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return chromeService.query(request.get("key"), request.get("name"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 3),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "pages", failureCode = 4),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "filename", failureCode = 5),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        return chromeService.save(request.setToModel(new ChromeModel()));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        chromeService.delete(request.get("id"));

        return "";
    }

    @Execute(name = "pdf", type = Templates.STREAM, validates = {
            @Validate(validator = ChromeService.VALIDATOR_MEMORY, failureCode = 98),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "url", failureCode = 6),
            @Validate(validator = ChromeService.VALIDATOR_KEY_EXISTS, parameter = "key", failureCode = 7)
    })
    public Object pdf() {
        byte[] pdf = chromeService.pdf(request.get("key"), request.get("url"), request.getAsInt("width"),
                request.getAsInt("height"), request.get("pages"), request.getAsInt("wait"));
        if (pdf == null)
            return templates.get().failure(2911, message.get(ChromeModel.NAME + ".pdf.null"), null, null);

        String filename = request.get("filename");
        if (validator.isEmpty(filename))
            filename = chromeService.findByKey(request.get("key")).getFilename();
        if (validator.isEmpty(filename))
            filename = generator.random(32);
        response.setHeader("Content-Disposition", "attachment; filename*=" + context.getCharset(null)
                + "''" + coder.encodeUrl(filename, null) + ".pdf");
        response.setContentType("application/pdf");

        return pdf;
    }

    @Execute(name = "png", type = Templates.STREAM, validates = {
            @Validate(validator = ChromeService.VALIDATOR_MEMORY, failureCode = 98),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "url", failureCode = 6),
            @Validate(validator = ChromeService.VALIDATOR_KEY_EXISTS, parameter = "key", failureCode = 7)
    })
    public Object png() {
        byte[] png = chromeService.png(request.get("key"), request.get("url"), request.getAsInt("x"), request.getAsInt("y"),
                request.getAsInt("width"), request.getAsInt("height"), request.getAsInt("wait"));
        if (png == null)
            return templates.get().failure(2912, message.get(ChromeModel.NAME + ".png.null"), null, null);

        response.setContentType("image/png");

        return png;
    }

    @Execute(name = "jpg", type = Templates.STREAM, validates = {
            @Validate(validator = ChromeService.VALIDATOR_MEMORY, failureCode = 98),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "url", failureCode = 6),
            @Validate(validator = ChromeService.VALIDATOR_KEY_EXISTS, parameter = "key", failureCode = 7)
    })
    public Object jpg() {
        byte[] jpg = chromeService.jpg(request.get("key"), request.get("url"), request.getAsInt("x"), request.getAsInt("y"),
                request.getAsInt("width"), request.getAsInt("height"), request.getAsInt("wait"));
        if (jpg == null)
            return templates.get().failure(2913, message.get(ChromeModel.NAME + ".jpg.null"), null, null);

        response.setContentType("image/jpeg");

        return jpg;
    }
}
