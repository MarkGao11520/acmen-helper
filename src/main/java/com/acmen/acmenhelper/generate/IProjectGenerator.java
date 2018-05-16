package com.acmen.acmenhelper.generate;

import com.acmen.acmenhelper.model.CodeDefinition;

/**
 * 项目结构生成器接口
 * @author gaowenfeng
 * @date 2018/5/16
 */
public interface IProjectGenerator {
    /**
     * 生成项目结构
     * @param codeDefinition
     * @return
     */
    String generateProjectStructure(CodeDefinition codeDefinition);
}
