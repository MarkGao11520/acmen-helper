package com.acmen.acmenhelper.generate;

import com.acmen.acmenhelper.exception.GlobalException;
import com.acmen.acmenhelper.model.CodeDefinitionDetail;

/**
 * 代码生成器抽象类
 * @author gaowenfeng
 * @date 2018/5/16
 */
public abstract class AbstractCodeGenerator {

    /**
     * 通过数据表名称生成代码，Model 名称通过解析数据表名称获得，下划线转大驼峰的形式。
     * 如输入表名称 "t_user_detail" 将生成 TUserDetail、TUserDetailMapper、TUserDetailService ...
     * @param codeDefinitionDetail
     */
    public void genCode(CodeDefinitionDetail codeDefinitionDetail) throws GlobalException {
        genConfigCode(codeDefinitionDetail);
        genBaseCode(codeDefinitionDetail);
    }

    /**
     * 生成配置文件文件
     * @param codeDefinitionDetail
     */
    protected abstract void genConfigCode(CodeDefinitionDetail codeDefinitionDetail);

    /**
     * 生成项目基本代码
     * @param codeDefinitionDetail
     */
    protected abstract void genBaseCode(CodeDefinitionDetail codeDefinitionDetail);
}
