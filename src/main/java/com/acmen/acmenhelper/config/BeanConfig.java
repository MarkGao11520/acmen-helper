package com.acmen.acmenhelper.config;

import com.acmen.acmenhelper.generate.AbstractCodeGenerator;
import com.acmen.acmenhelper.generate.IProjectGenerator;
import com.acmen.acmenhelper.service.ICodeGeneratorService;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ProjectConfig projectConfig;

    @Bean
    public freemarker.template.Configuration cfg() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(new File(PROJECT_PATH+TEMPLATE_FILE_PATH));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }

    @Bean
    public IProjectGenerator projectGenerator() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> clazz = Class.forName(projectConfig.getProjectGeneratorClass());
        return (IProjectGenerator) clazz.newInstance();
    }

    @Bean
    public AbstractCodeGenerator codeGenerator() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> clazz = Class.forName(projectConfig.getCodeGeneratorClass());
        return (AbstractCodeGenerator) clazz.newInstance();
    }

}
