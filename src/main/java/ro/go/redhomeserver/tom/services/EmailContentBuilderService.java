package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ro.go.redhomeserver.tom.dtos.EmailData;

@Service
public class EmailContentBuilderService {

    private final TemplateEngine templateEngine;

    @Autowired
    public EmailContentBuilderService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(EmailData data) {
        Context context = new Context();
        data.setContext(context);
        return templateEngine.process((String) context.getVariable("templateName"), context);
    }
}
