package com.acmen.acmenhelper.service;

import com.acmen.acmenhelper.common.ServiceMultiResult;
import com.acmen.acmenhelper.common.ServiceResult;
import com.acmen.acmenhelper.config.ProjectConfig;
import com.acmen.acmenhelper.exception.GlobalException;
import com.acmen.acmenhelper.generate.AbstractCodeGenerator;
import com.acmen.acmenhelper.generate.IProjectGenerator;
import com.acmen.acmenhelper.model.CodeDefinition;
import com.acmen.acmenhelper.model.CodeDefinitionDetail;
import com.acmen.acmenhelper.model.DBDefinition;
import com.acmen.acmenhelper.util.ApplicationContextHolder;
import com.acmen.acmenhelper.util.CompressUtil;
import com.acmen.acmenhelper.util.DBUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成器 实现类
 * @author gaowenfeng
 * @date 2018/5/16
 */
@Service
@Lazy
@Slf4j
public class CodeGeneratorServiceImpl implements ICodeGeneratorService{

    private final static String LOG_PRE = "生成项目基本骨架>";


    @Autowired
    private ProjectConfig projectConfig;

    @Autowired
    private HttpSession session;

    @Autowired
    private IProjectGenerator projectGenerator;

    @Autowired
    private AbstractCodeGenerator codeGenerator;



    @Override
    public ServiceMultiResult<String> getTableList(DBDefinition dbDefinition){
        List<String> tables = new ArrayList<>();

        //创建驱动器
        Connection conn = DBUtil.getConnection(dbDefinition);
        ResultSet rs = null;

        try{
            DatabaseMetaData dbmd = conn.getMetaData();
            rs = dbmd.getTables(null, dbDefinition.getDbName(), "%", null);
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
        } catch (Exception e) {
            throw new GlobalException(1 , "数据库连接信息错误,或数据库不存在数据表!" , e);
        } finally {
            DBUtil.killConnection(rs,conn);
        }
        session.setAttribute("dbDefinition",dbDefinition);
        return new ServiceMultiResult<>(tables.size(),tables);
    }

    @Override
    public ServiceResult<String> genCode(CodeDefinitionDetail codeDefinitionDetail){
        String dist;
        try {
            //1.生成项目骨架：
            projectGenerator.generateProjectStructure(codeDefinitionDetail);

            //2.生成项目基本代码
            codeGenerator.genCode(codeDefinitionDetail);

            //3.打包项目，使用response输出
            dist = projectConfig.getGeneratePath()+codeDefinitionDetail.getCodeDefinition().getProjectName()+".zip";
            CompressUtil.doZipCompress(codeDefinitionDetail.getProjectPath(),dist);
        } finally {
            //4.删除项目
            destoryProject(codeDefinitionDetail.getProjectPath());
        }
        return ServiceResult.of(dist);
    }

    private void destoryProject(String projectPath){
        try {
            FileUtils.deleteDirectory(new File(projectPath));
        } catch (IOException e) {
            log.error("删除下载文件失败",e);
        }
    }


}
