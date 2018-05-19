package com.acmen.acmenhelper.config;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 要添加的Dependencies信息
 * @author gaowenfeng
 * @date 2018/5/17
 */
@Component
@ConfigurationProperties(prefix = "acmen")
@Data
public class ProjectConfig {

    private String generatePath;

    private String codeGeneratorClass;

    private String projectGeneratorClass;

    private String isSpiltModule;

    private List<String> dependencies = Lists.newArrayList();
}
