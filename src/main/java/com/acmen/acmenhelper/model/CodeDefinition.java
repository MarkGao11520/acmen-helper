package com.acmen.acmenhelper.model;

import lombok.Data;

import java.util.List;

/**
 * 代码自动工具原信息
 * @author gaowenfeng
 * @date 2018/5/16
 */
@Data
public class CodeDefinition {

    private List<String> tableList;

    private String groupId;

    private String artifactId;

    private String version = "0.0.1-SNAPSHOT";

    private String description;

    private String projectName;

    private String author = "Acmen-helper";
}
