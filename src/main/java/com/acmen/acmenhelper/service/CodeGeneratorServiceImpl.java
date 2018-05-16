package com.acmen.acmenhelper.service;

import com.acmen.acmenhelper.common.ServiceMultiResult;
import com.acmen.acmenhelper.common.ServiceResult;
import com.acmen.acmenhelper.model.CodeDefinition;
import com.acmen.acmenhelper.model.DBDefinition;
import org.springframework.stereotype.Service;

/**
 * 代码生成器 实现类
 * @author gaowenfeng
 * @date 2018/5/16
 */
@Service
public class CodeGeneratorServiceImpl implements ICodeGeneratorService{
    @Override
    public ServiceMultiResult<String> getTableList(DBDefinition dbDefinition) {
        return null;
    }

    @Override
    public ServiceResult<String> genCode(CodeDefinition codeDefinition) {
        //1.生成项目骨架 两种方案：
        //		1）调用系统命令行使用maven命令生成
        //		2）使用java.io生成
        //
        //2.使用mybatis的自动生成工具生成dao,mapper,pojo
        //
        //3.使用freemarker模板引擎生成service，controller层代码
        //
        //4.打包项目，使用response输出
        return null;
    }
}
