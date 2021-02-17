package org.javawebstack.templating.freemarker;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.javawebstack.templating.TemplateEngine;
import org.javawebstack.templating.TemplateException;
import org.javawebstack.templating.TemplateProvider;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerEngine implements TemplateEngine {

    private final Configuration configuration;
    private final TemplateProvider provider;
    private final Map<String, Template> templates = new HashMap<>();

    public FreemarkerEngine(TemplateProvider provider) {
        this.provider = provider;
        configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setTemplateLoader(new ProviderTemplateLoader());
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
    }

    public String render(String name, Map<String, Object> data) throws TemplateException {
        if(!name.endsWith(".ftl") && !name.endsWith(".ftlx") && !name.endsWith(".ftlh"))
            name += ".ftl";
        if(!templates.containsKey(name)) {
            try {
                Template template = configuration.getTemplate(name);
                templates.put(name, template);
            } catch (IOException exception) {
                throw new TemplateException(exception);
            }
        }
        Template template = templates.get(name);
        StringWriter writer = new StringWriter();
        try {
            template.process(data, writer);
        } catch (freemarker.template.TemplateException | IOException e) {
            throw new TemplateException(e);
        }
        return writer.toString();
    }

    private class ProviderTemplateLoader implements TemplateLoader {
        public Object findTemplateSource(String s) throws IOException {
            try {
                return provider.getTemplate(s);
            } catch (IOException ignored) {
                return null;
            }
        }
        public long getLastModified(Object o) {
            return 0;
        }
        public Reader getReader(Object o, String s) throws IOException {
            return new StringReader((String) o);
        }
        public void closeTemplateSource(Object o) throws IOException {

        }
    }

}
