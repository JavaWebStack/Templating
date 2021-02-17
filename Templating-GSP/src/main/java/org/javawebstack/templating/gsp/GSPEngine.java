package org.javawebstack.templating.gsp;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import org.javawebstack.templating.TemplateException;
import org.javawebstack.templating.TemplateProvider;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class GSPEngine implements org.javawebstack.templating.TemplateEngine {

    private final TemplateProvider provider;
    private final SimpleTemplateEngine engine;
    private final Map<String, Template> templates = new HashMap<>();

    public GSPEngine(TemplateProvider provider) {
        this.provider = provider;
        this.engine = new SimpleTemplateEngine();
    }

    public String render(String name, Map<String, Object> data) throws TemplateException {
        if(!name.endsWith(".gsp"))
            name += ".gsp";
        if(!templates.containsKey(name)) {
            try {
                String source = provider.getTemplate(name);
                if(source == null)
                    throw new TemplateException("Template '" + name + "' not found");
                Template template = engine.createTemplate(source);
                templates.put(name, template);
            } catch (IOException | ClassNotFoundException exception) {
                throw new TemplateException(exception);
            }
        }
        Template template = templates.get(name);
        StringWriter writer = new StringWriter();
        try {
            template.make(data).writeTo(writer);
        } catch (Exception exception) {
            throw new TemplateException(exception);
        }
        return writer.toString();
    }

}
