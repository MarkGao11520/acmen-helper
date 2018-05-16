package com.acmen.acmenhelper.service;

import com.acmen.acmenhelper.common.ServiceMultiResult;
import com.acmen.acmenhelper.common.ServiceResult;
import com.acmen.acmenhelper.generate.AbstractCodeGenerator;
import com.acmen.acmenhelper.generate.IProjectGenerator;
import com.acmen.acmenhelper.model.CodeDefinition;
import com.acmen.acmenhelper.model.CodeDefinitionDetail;
import com.acmen.acmenhelper.model.DBDefinition;
import com.acmen.acmenhelper.util.ApplicationContextHolder;
import com.acmen.acmenhelper.util.CompressUtil;
import com.acmen.acmenhelper.util.DBUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成器 实现类
 * @author gaowenfeng
 * @date 2018/5/16
 */
@Service
public class CodeGeneratorServiceImpl implements ICodeGeneratorService{
    @Resource(name = "defaultProjectGenerator")
    private IProjectGenerator defaultProjectGenerator;


    @Override
    public ServiceMultiResult<String> getTableList(DBDefinition dbDefinition) {
        List<String> tables = new ArrayList<>();

        //创建驱动器
        Connection conn = DBUtil.getConnection(dbDefinition);
        ResultSet rs = null;
        DatabaseMetaData dbmd = null;

        try{
            rs = dbmd.getTables(null, dbDefinition.getDbName(), "%", null);
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
        } catch (Exception e) {
            //TODO 改成自定义异常
            throw new RuntimeException("数据库连接信息错误,或数据库不存在数据表!");
        } finally {
            DBUtil.killConnection(rs,conn);
        }
        return new ServiceMultiResult<>(tables.size(),tables);
    }

    @Override
    public ServiceResult<String> genCode(CodeDefinition codeDefinition) {
        //1.生成项目骨架 两种方案：
        //		1）调用系统命令行使用maven命令生成
        //		2）使用java.io生成
        String projectPath = defaultProjectGenerator.generateProjectStructure(codeDefinition);
        //2.使用mybatis的自动生成工具生成dao,mapper,pojo
        //3.使用freemarker模板引擎生成service，controller层代码
        CodeDefinitionDetail codeDefinitionDetail = new CodeDefinitionDetail(codeDefinition,projectPath);
        AbstractCodeGenerator defaultCodeGenerator = ApplicationContextHolder.getBean("defaultCodeGenerator");
        defaultCodeGenerator.setCodeDefinitionDetail(codeDefinitionDetail);
        defaultCodeGenerator.genCode();

        //4.打包项目，使用response输出
        //TODO
        String dist = "xxx.zip";
        CompressUtil.doZipCompress(projectPath,dist);
        return ServiceResult.of(dist);
    }


}
