package com.acmen.acmenhelper.generate;

import com.acmen.acmenhelper.model.CodeDefinitionDetail;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 默认代码生成器，SOA单体架构
 * @author gaowenfeng
 * @date 2018/5/16
 */
@Service("defaultCodeGenerator")
@Scope("prototype")
public class DefaultCodeGenerator extends AbstractCodeGenerator {


    @Override
    protected void genController(String tableName, String modelName) {

    }

    @Override
    protected void genService(String tableName, String modelName) {

    }

    @Override
    protected void genModelAndMapper(String tableName, String modelName) {

    }
}
