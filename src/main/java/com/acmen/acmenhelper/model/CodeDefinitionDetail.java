package com.acmen.acmenhelper.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import static com.acmen.acmenhelper.util.NameConvertUtil.packageConvertPath;

/**
 * @author gaowenfeng
 * @date 2018/5/16
 */
@Data
public class CodeDefinitionDetail{
    /**
     * 项目在硬盘上的基础路径
     */
    public static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * java文件路径
     */
    public static final String JAVA_PATH = "/src/main/java";

    /**
     * 资源文件路径
     */
    public static final String RESOURCES_PATH = "/src/main/resources";

    /**
     * 模板文件路径
     */
    public static final String TEMPLATE_FILE_PATH = RESOURCES_PATH+"/generator/template";


    private CodeDefinition codeDefinition;

    private String projectPath;

    private String basePackage;

    private String modulePackage;

    private String mapperPackage;

    private String servicePackage;

    private String serviceImplPackage;

    private String controllerPackage;

    private String mapperInterfaceReference;

    public CodeDefinitionDetail(CodeDefinition codeDefinition){
        this(codeDefinition,null);
    }


    public CodeDefinitionDetail(CodeDefinition codeDefinition,String projectPath){
        this.codeDefinition = codeDefinition;
        this.projectPath = projectPath;

        this.basePackage = this.codeDefinition.getGroupId()+"."+this.codeDefinition.getArtifactId();
        this.modulePackage = this.basePackage+".model";
        this.mapperPackage = this.basePackage+".dao";
        this.servicePackage = packageConvertPath(this.basePackage+".service");
        this.serviceImplPackage = packageConvertPath(this.servicePackage+".impl");
        this.controllerPackage = packageConvertPath(this.basePackage+".web");
        this.mapperInterfaceReference = this.basePackage+".core.Mapper";
    }


}
