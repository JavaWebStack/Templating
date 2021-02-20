package org.javawebstack.templating;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.abstractdata.AbstractObject;

import java.util.Map;

public interface TemplateEngine {

    String render(String name, Map<String, Object> data) throws TemplateException;

    default String render(String name, Object data) {
        return render(name, AbstractElement.fromAbstractObject(data).toTree());
    }

}