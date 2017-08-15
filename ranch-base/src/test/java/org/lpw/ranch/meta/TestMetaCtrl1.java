package org.lpw.ranch.meta;

import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

/**
 * @author lpw
 */
@Controller(MetaModel.NAME + ".ctrl1")
@Execute(name = "/meta/", key = MetaModel.NAME)
public class TestMetaCtrl1 {
    @Execute(name = "query")
    public Object query() {
        return "";
    }

    @Execute(name = "col")
    public Object col() {
        return "";
    }

    @Execute(name = "not-exists")
    public Object notExists() {
        return "";
    }

    @Execute(name = "not-json")
    public Object notJson() {
        return "";
    }
}
