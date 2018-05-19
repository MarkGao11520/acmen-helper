package com.acmen.acmenhelper.generate.defaults;

import com.acmen.acmenhelper.common.ApiResponse;
import com.acmen.acmenhelper.config.ProjectConfig;
import com.acmen.acmenhelper.exception.GlobalException;
import com.acmen.acmenhelper.generate.IProjectGenerator;
import com.acmen.acmenhelper.model.CodeDefinition;
import com.acmen.acmenhelper.model.CodeDefinitionDetail;
import com.acmen.acmenhelper.util.FileUtil;
import com.acmen.acmenhelper.util.NameConvertUtil;
import com.acmen.acmenhelper.util.PomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static com.acmen.acmenhelper.model.CodeDefinitionDetail.*;
import static com.acmen.acmenhelper.util.ExecCommandUtil.buildBootCmd;
import static com.acmen.acmenhelper.util.ExecCommandUtil.doGenerateProjectStructure;

/**
 * 默认项目结构生成器,使用java.io生成
 * @author gaowenfeng
 * @date 2018/5/16
 */
@Slf4j
public class DefaultProjectGenerator implements IProjectGenerator {
    private final static String LOG_PRE = "生成项目基本骨架>";

    @Autowired
    private ProjectConfig projectConfig;



    @Override
    public void generateProjectStructure(CodeDefinitionDetail codeDefinitionDetail) {
        CodeDefinition codeDefinition = codeDefinitionDetail.getCodeDefinition();
        String buildInProjectName = codeDefinition.getProjectName()+"_"+System.currentTimeMillis();

        //1.生成springboot项目骨架
        String[] cmds = buildBootCmd(codeDefinition,projectConfig.getGeneratePath(),buildInProjectName);
        doGenerateProjectStructure(codeDefinition,cmds);

        //2.将核心文件放入项目骨架
        String targetPath = projectConfig.getGeneratePath()+buildInProjectName+JAVA_PATH+NameConvertUtil.packageConvertPath(codeDefinitionDetail.getBasePackage());
        FileUtil.copyDir(PROJECT_PATH+RESOURCES_PATH+"/generator/coreCode",
                targetPath);

        //3.修改核心文件的包名
        FileUtil.modifyCorePackage(targetPath,codeDefinitionDetail.getBasePackage());

        //4.修改pom文件追加依赖
        try {
            String pomPath = projectConfig.getGeneratePath()+buildInProjectName+"/pom.xml";
            PomUtil.appendPomDependencies(pomPath,projectConfig.getDependencies());
        } catch (Exception e) {
            throw new GlobalException(ApiResponse.Status.INTERNAL_SERVER_ERROR.getCode(),"修改POM文件异常",e);
        }

        String projectPath = projectConfig.getGeneratePath()+buildInProjectName;
        codeDefinitionDetail.setProjectPath(projectPath);
    }





    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        CodeDefinition codeDefinition = new CodeDefinition();
        codeDefinition.setArtifactId("demo");
        codeDefinition.setGroupId("com.example");

        codeDefinition.setProjectName("test-demo");
        codeDefinition.setDescription("测试");
        codeDefinition.setVersion("vt2.1");

        CodeDefinitionDetail codeDefinitionDetail = new CodeDefinitionDetail(codeDefinition);

        new DefaultProjectGenerator().generateProjectStructure(codeDefinitionDetail);
    }





}
