package com.acmen.acmenhelper.generate;

import com.acmen.acmenhelper.exception.GlobalException;
import com.acmen.acmenhelper.model.CodeDefinitionDetail;

/**
 * 代码生成器抽象类
 * @author gaowenfeng
 * @date 2018/5/16
 */
public abstract class AbstractCodeGenerator {

    protected CodeDefinitionDetail codeDefinitionDetail;

    public void setCodeDefinitionDetail(CodeDefinitionDetail codeDefinitionDetail) {
        this.codeDefinitionDetail = codeDefinitionDetail;
    }

    /**
     * 通过数据表名称生成代码，Model 名称通过解析数据表名称获得，下划线转大驼峰的形式。
     * 如输入表名称 "t_user_detail" 将生成 TUserDetail、TUserDetailMapper、TUserDetailService ...
     */
    public void genCode() throws GlobalException {
        for (String tableName : this.codeDefinitionDetail.getCodeDefinition().getTableList()) {
            genCodeByCustomModelName(tableName, null);
        }
    }

    /**
     * 通过数据表名称，和自定义的 Model 名称生成代码
     * 如输入表名称 "t_user_detail" 和自定义的 Model 名称 "User" 将生成 User、UserMapper、UserService ...
     * @param tableName 数据表名称
     * @param modelName 自定义的 Model 名称
     */
    public void genCodeByCustomModelName(String tableName, String modelName) throws GlobalException {
        genModelAndMapper(tableName, modelName);
        genFtlCode(tableName, modelName);
    }



    /**
     * 根据模板引擎生成代码
     * @param tableName
     * @param modelName
     */
    protected abstract void genFtlCode(String tableName, String modelName) throws GlobalException;

    /**
     * 生成model层代码
     * @param tableName
     * @param modelName
     */
    protected abstract void genModelAndMapper(String tableName, String modelName) throws GlobalException;
}
