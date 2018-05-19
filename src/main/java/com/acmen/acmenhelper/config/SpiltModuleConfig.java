package com.acmen.acmenhelper.config;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gaowenfeng
 * @date 2018/5/19
 */
@Component
@ConfigurationProperties(prefix = "acmen.spiltmodule")
@Data
public class SpiltModuleConfig {
    private List<String> dependencies = Lists.newArrayList();

}
