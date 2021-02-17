package org.javawebstack.templating;

import java.util.Map;

public interface TemplateEngine {

    String render(String name, Map<String, Object> data) throws TemplateException;

}