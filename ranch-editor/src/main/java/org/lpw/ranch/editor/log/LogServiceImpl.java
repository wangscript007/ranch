package org.lpw.ranch.editor.log;

import org.lpw.ranch.editor.element.ElementModel;
import org.lpw.tephra.util.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(LogModel.NAME + ".service")
public class LogServiceImpl implements LogService {
    @Inject
    private DateTime dateTime;
    @Inject
    private LogDao logDao;

    @Override
    public void save(ElementModel element, Operation operation) {
        LogModel log = new LogModel();
        log.setEditor(element.getEditor());
        log.setParent(element.getParent());
        log.setElement(element.getId());
        log.setSort(element.getSort());
        log.setType(element.getType());
        log.setX(element.getX());
        log.setY(element.getY());
        log.setWidth(element.getWidth());
        log.setHeight(element.getHeight());
        log.setJson(element.getJson());
        log.setCreate(element.getCreate());
        log.setModify(element.getModify());
        log.setOperation(operation.ordinal());
        log.setTime(dateTime.now());
    }
}
