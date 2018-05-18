package com.acmen.acmenhelper.config;

import com.google.common.collect.Lists;
import lombok.Data;
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
public class DependenciesConfig {
    private List<String> dependencies = Lists.newArrayList();
}
