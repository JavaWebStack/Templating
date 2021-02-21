package org.javawebstack.templating.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.javawebstack.templating.TemplateEngine;
import org.javawebstack.templating.TemplateException;
import org.javawebstack.templating.TemplateProvider;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class MustacheEngine implements TemplateEngine {

    private final TemplateProvider provider;
    private final MustacheFactory factory;
    private final Map<String, Mustache> templates = new HashMap<>();

    public MustacheEngine(TemplateProvider provider) {
        this.provider = provider;
        this.factory = new DefaultMustacheFactory();
    }

    public String render(String name, Map<String, Object> data) throws TemplateException {
        if(!name.endsWith(".mustache"))
            name += ".mustache";
        if(!templates.containsKey(name)) {
            try {
                String source = provider.getTemplate(name);
                if (source == null)
                    throw new TemplateException("Template '" + name + "' not found");
                Mustache template = factory.compile(source);
                templates.put(name, template);
            } catch (IOException exception) {
                throw new TemplateException(exception);
            }
        }
        Mustache template = templates.get(name);
        StringWriter writer = new StringWriter();
        try {
            template.execute(writer, data);
        } catch (Exception exception) {
            throw new TemplateException(exception);
        }
        return writer.toString();
    }
}
