package org.lpw.ranch.meta;

import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

/**
 * @author lpw
 */
@Controller(MetaModel.NAME + ".ctrl2")
public class TestMetaCtrl2 {
    @Execute(name = "query2")
    public Object query() {
        return "";
    }

    @Execute(name = "not-exists2")
    public Object notExists() {
        return "";
    }

    @Execute(name = "not-json2")
    public Object notJson(){
        return "";
    }
}
