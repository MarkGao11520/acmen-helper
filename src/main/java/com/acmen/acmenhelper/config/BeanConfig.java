package com.acmen.acmenhelper.config;

import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

import static com.acmen.acmenhelper.model.CodeDefinitionDetail.PROJECT_PATH;
import static com.acmen.acmenhelper.model.CodeDefinitionDetail.TEMPLATE_FILE_PATH;

/**
 * @author gaowenfeng
 * @date 2018/5/17
 */
@Configuration
public class BeanConfig {

    @Bean
    public freemarker.template.Configuration cfg() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(new File(PROJECT_PATH+TEMPLATE_FILE_PATH));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }
}
