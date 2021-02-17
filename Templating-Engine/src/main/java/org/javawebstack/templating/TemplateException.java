package org.javawebstack.templating;

public class TemplateException extends Exception {

    public TemplateException(String message) {
        super(message);
    }

    public TemplateException(Exception parent) {
        super(parent);
    }

}
