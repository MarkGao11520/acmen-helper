package com.acmen.acmenhelper.generate.spiltmodule;

import com.acmen.acmenhelper.common.ApiResponse;
import com.acmen.acmenhelper.config.ProjectConfig;
import com.acmen.acmenhelper.config.SpiltModuleConfig;
import com.acmen.acmenhelper.exception.GlobalException;
import com.acmen.acmenhelper.generate.IProjectGenerator;
import com.acmen.acmenhelper.model.CodeDefinition;
import com.acmen.acmenhelper.model.CodeDefinitionDetail;
import com.acmen.acmenhelper.util.FileUtil;
import com.acmen.acmenhelper.util.NameConvertUtil;
import com.acmen.acmenhelper.util.PomUtil;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.acmen.acmenhelper.model.CodeDefinitionDetail.*;
import static com.acmen.acmenhelper.util.ExecCommandUtil.*;

/**
 * 生成分模块的项目骨架
 * @author gaowenfeng
 * @date 2018/5/19
 */
@Slf4j
@Data
@Service
public class SpiltModuleProjectGenerator implements IProjectGenerator {
    private final static String LOG_PRE = "分模块生成项目基本骨架>";

    private final static String[] moduleList = new String[]{"core","web","service","dao"};

    @Autowired
    private SpiltModuleConfig spiltModuleConfig;

    @Autowired
    private ProjectConfig projectConfig;

    @Override
    public void generateProjectStructure(CodeDefinitionDetail codeDefinitionDetail) {
        CodeDefinition codeDefinition = codeDefinitionDetail.getCodeDefinition();
        String buildInProjectName = codeDefinition.getProjectName()+"_"+System.currentTimeMillis();

        //一、生成项目骨架
        //1.生成一个springboot 父项目
        doGenerateProjectStructure(codeDefinition,buildBootCmd(codeDefinition,projectConfig.getGeneratePath(),buildInProjectName));

        //2.修改父项目的pom文件
        modifyParentPomFile(codeDefinition, buildInProjectName);

        //3.生成core,service,dao,web子模块
        for(String moduleName:moduleList) {
            String[] mavenCmd = buildMavenCmd(codeDefinition,projectConfig.getGeneratePath(), buildInProjectName, moduleName);
            doGenerateProjectStructure(codeDefinition, mavenCmd);
        }

        //4.修改所有项目的pom文件
        modifyChildPomFile(codeDefinition, buildInProjectName);

        //5.将父项目的src拷贝到web项目的src
        FileUtil.moveDir(projectConfig.getGeneratePath()+buildInProjectName+"/src",
                projectConfig.getGeneratePath()+buildInProjectName+"/"+codeDefinition.getArtifactId()+"-web/src");

        //6.填充core文件的配置
        String targetPath = projectConfig.getGeneratePath()+buildInProjectName+"/"+codeDefinition.getArtifactId()+"-core/"+JAVA_PATH+NameConvertUtil.packageConvertPath(codeDefinitionDetail.getBasePackage());
        FileUtil.copyDir(PROJECT_PATH+RESOURCES_PATH+"/generator/coreCode",targetPath);

        //7.修改核心文件的包名
        FileUtil.modifyCorePackage(targetPath,codeDefinitionDetail.getBasePackage());

        String projectPath = projectConfig.getGeneratePath()+buildInProjectName;
        codeDefinitionDetail.setProjectPath(projectPath);
    }

    /**
     * 修改父模块Pom文件
     * @param codeDefinition
     * @param buildInProjectName
     */
    private void modifyParentPomFile(CodeDefinition codeDefinition, String buildInProjectName) {
        String pomPath = projectConfig.getGeneratePath()+buildInProjectName+"/pom.xml";
        try {
            PomUtil.handlePom(root -> {
                root.element("packaging").setText("pom");
                root.remove(root.element("dependencies"));

                Element properties = root.element("properties");
                properties.addElement("project.version").setText(codeDefinition.getVersion());
            },pomPath);
        } catch (Exception e) {
            throw new GlobalException(ApiResponse.Status.INTERNAL_SERVER_ERROR.getCode(),"修改POM文件异常",e);
        }
    }

    /**
     * 修改子模块Pom文件
     * @param codeDefinition
     * @param buildInProjectName
     */
    private void modifyChildPomFile(CodeDefinition codeDefinition, String buildInProjectName) {
        try {
            for(String moduleName:moduleList){
                List<String> list = Lists.newArrayList();
                if(moduleName.equals("core")) {
                    list = spiltModuleConfig.getDependencies();
                }else{
                    list.add(codeDefinition.getGroupId()+","+codeDefinition.getArtifactId()+"-core"+",${project.version}");
                    if(moduleName.equals("web")){
                        list.add(codeDefinition.getGroupId()+","+codeDefinition.getArtifactId()+"-service"+",${project.version}");
                    }else if(moduleName.equals("service")){
                        list.add(codeDefinition.getGroupId()+","+codeDefinition.getArtifactId()+"-dao"+",${project.version}");
                    }
                }

                String pomPath = projectConfig.getGeneratePath()+buildInProjectName+"/"+codeDefinition.getArtifactId()+"-"+moduleName+"/pom.xml";

                PomUtil.removeElement(pomPath,"dependencies","dependency");
                PomUtil.removeElement(pomPath,null,"groupId");
                PomUtil.removeElement(pomPath,null,"version");
                PomUtil.appendPomDependencies(pomPath,list);
            }
        } catch (Exception e) {
            throw new GlobalException(ApiResponse.Status.INTERNAL_SERVER_ERROR.getCode(),"修改POM文件异常",e);
        }
    }




}



