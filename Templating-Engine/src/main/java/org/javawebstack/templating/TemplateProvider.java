package org.javawebstack.templating;

import java.io.IOException;

public interface TemplateProvider {

    String getTemplate(String name) throws IOException;

}
