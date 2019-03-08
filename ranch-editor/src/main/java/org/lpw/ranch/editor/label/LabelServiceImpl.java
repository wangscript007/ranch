package org.lpw.ranch.editor.label;

import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lpw
 */
@Service(LabelModel.NAME + ".service")
public class LabelServiceImpl implements LabelService {
    @Inject
    private Converter converter;
    @Inject
    private Validator validator;
    @Inject
    private LabelDao labelDao;

    @Override
    public Set<String> query(List<String> names) {
        Set<String> set = null;
        for (int i = 0, size = names.size(); i < size; i++) {
            Set<String> editors = editors(names.get(i));
            if (i == 0) {
                set = editors;

                continue;
            }

            set.retainAll(editors);
            if (set.isEmpty())
                return null;
        }

        return set;
    }

    private Set<String> editors(String name) {
        Set<String> set = new HashSet<>();
        labelDao.query(name).getList().forEach(label -> set.add(label.getEditor()));

        return set;
    }

    @Override
    public List<LabelModel> query(String name) {
        return labelDao.query(name).getList();
    }

    @Override
    public void save(String editor, String names, boolean autoClose) {
        labelDao.delete(editor);
        for (String name : converter.toArray(names, ",")) {
            if (validator.isEmpty(name))
                continue;

            LabelModel label = new LabelModel();
            label.setEditor(editor);
            label.setName(name);
            labelDao.save(label);
        }
        if (autoClose)
            labelDao.close();
    }
}
