package com.acmen.acmenhelper.generate.spiltmodule;

import com.acmen.acmenhelper.config.ProjectConfig;
import com.acmen.acmenhelper.config.SpiltModuleConfig;
import com.acmen.acmenhelper.exception.GlobalException;
import com.acmen.acmenhelper.generate.IProjectGenerator;
import com.acmen.acmenhelper.model.CodeDefinition;
import com.acmen.acmenhelper.model.CodeDefinitionDetail;
import com.acmen.acmenhelper.util.NameConvertUtil;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import static com.acmen.acmenhelper.model.CodeDefinitionDetail.*;

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
        //一、生成项目骨架
        //1.生成一个springboot 父项目
        CodeDefinition codeDefinition = codeDefinitionDetail.getCodeDefinition();
        String buildInProjectName = codeDefinition.getProjectName()+"_"+System.currentTimeMillis();
        doGenerateProjectStructure(codeDefinition,buildCmd(codeDefinition,buildInProjectName));

        //2.修改父项目的pom文件
        modifyParentPom(codeDefinition, buildInProjectName);

        //3.生成core,service,dao,web子模块
        for(String moduleName:moduleList) {
            String[] mavenCmd = buildMavenCmd(codeDefinition, buildInProjectName, moduleName);
            doGenerateProjectStructure(codeDefinition, mavenCmd);
        }

        //3.修改所有项目的pom文件
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
            doAppendPomDependencies(list,pomPath);
        }

        //4.将父项目的src拷贝到web项目的src
        try {
            File sourceDir = new File(projectConfig.getGeneratePath()+buildInProjectName+"/src");
            File distDir = new File(projectConfig.getGeneratePath()+buildInProjectName+"/"+codeDefinition.getArtifactId()+"-web/src");
            FileUtils.copyDirectory(sourceDir,distDir);
            sourceDir.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //5.填充core文件的配置
        String targetPath = projectConfig.getGeneratePath()+buildInProjectName+"/"+codeDefinition.getArtifactId()+"-core/"+JAVA_PATH+NameConvertUtil.packageConvertPath(codeDefinitionDetail.getBasePackage());
        try {
            File sourceDir = new File(PROJECT_PATH+RESOURCES_PATH+"/generator/coreCode");
            File targetDir = new File(targetPath);
            targetDir.mkdirs();
            FileUtils.copyDirectory(sourceDir,targetDir);
        } catch (IOException e) {
            throw new GlobalException(1 , LOG_PRE+"拷贝文件异常",e);
        }

        //6.修改核心文件的包名
        File configurerDir = new File(targetPath+"/core");
        for (File configurerFile : configurerDir.listFiles()) {
            appendFileContent(configurerFile,"package "+codeDefinitionDetail.getBasePackage()+".core;\n");
        }
    }

    private void modifyParentPom(CodeDefinition codeDefinition, String buildInProjectName) {
        String pomPath = projectConfig.getGeneratePath()+buildInProjectName+"/pom.xml";
        File file = new File(pomPath);
        XMLWriter writer= null;

        try {
            Document doc = new SAXReader().read(file);
            Element root = doc.getRootElement();
            root.element("packaging").setText("pom");
            root.remove(root.element("dependencies"));

            Element properties = root.element("properties");
            properties.addElement("project.version").setText(codeDefinition.getVersion());

            //3.写入新的文件
            OutputFormat opf=new OutputFormat("\t",true,"UTF-8");
            opf.setTrimText(true);
            writer=new XMLWriter(new FileOutputStream(pomPath),opf);
            writer.write(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 追加POM元素的Dependencies
     * @param dependencies
     */
    private void doAppendPomDependencies(List<String> dependencies,String sourcePom){
        XMLWriter writer= null;
        try {
            //1.打开文件，并构造Element树
            File file = new File(sourcePom);
            Document doc = new SAXReader().read(file);
            Element dependenciesRoot = doc.getRootElement().element("dependencies");
            dependenciesRoot.remove(dependenciesRoot.element("dependency"));

            //2.添加新的节点
            for (String line : dependencies) {
                String[] data = StringUtils.split(line,",");
                Element dependency = dependenciesRoot.addElement("dependency");
                Element groupId = dependency.addElement("groupId");
                Element artifactId = dependency.addElement("artifactId");

                groupId.setText(data[0]);
                artifactId.setText(data[1]);
                if(data.length==3) {
                    Element version = dependency.addElement("version");
                    version.setText(data[2]);
                }
            }

            //3.写入新的文件
            OutputFormat opf=new OutputFormat("\t",true,"UTF-8");
            opf.setTrimText(true);
            writer=new XMLWriter(new FileOutputStream(sourcePom),opf);
            writer.write(doc);
        } catch (Exception e) {
            throw new GlobalException(1,LOG_PRE+"追加POM元素节点异常",e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                log.error(LOG_PRE+"追加POM元素关闭writer异常");
            }
        }
    }


    private void doGenerateProjectStructure(CodeDefinition codeDefinition,String[] cmds){

        Process pro = null;
        try{
            //1.2执行命令并阻塞至执行完成
            pro = Runtime.getRuntime().exec(cmds);
            pro.waitFor();

            //1.3输出结果
            List<String> lines = IOUtils.readLines(pro.getInputStream());
            lines.forEach(line->log.info(LOG_PRE+"参数=【"+codeDefinition+"】,输出=【"+line+"】"));
        } catch (Exception e) {
            log.error(LOG_PRE+"错误:"+e.getMessage());
            throw new GlobalException(1 , LOG_PRE + "错误" , e);
        }finally {
            pro.destroy();
        }
    }

    /**
     * 构建创建项目的命令
     * @param codeDefinition
     * @return
     */
    private String[] buildCmd(CodeDefinition codeDefinition,String buildInProjectName){
        String shellPath = PROJECT_PATH+RESOURCES_PATH+"/build_springboot.sh";

        String[] cmds = new String[]{shellPath,
                codeDefinition.getGroupId(),
                codeDefinition.getArtifactId(),
                projectConfig.getGeneratePath()+buildInProjectName,
                codeDefinition.getDescription(),
                codeDefinition.getVersion(),
        };
        return cmds;
    }

    private String[] buildMavenCmd(CodeDefinition codeDefinition,String buildInProjectName,String module){
        String shellPath = PROJECT_PATH+RESOURCES_PATH+"/build_maven.sh";

        String[] cmds = new String[]{shellPath,
                projectConfig.getGeneratePath()+"/"+buildInProjectName,
                codeDefinition.getGroupId(),
                codeDefinition.getArtifactId()+"-"+module,
                codeDefinition.getVersion(),
        };
        return cmds;
    }

    /**
     * 最近文件内容
     * @param file
     * @param headerStr
     */
    private static void appendFileContent(File file, String headerStr) {
        byte[] header = headerStr.getBytes();
        try(RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            int srcLength = (int)raf.length() ;
            byte[] buff = new byte[srcLength];
            raf.read(buff , 0, srcLength);
            raf.seek(0);
            raf.write(header);
            raf.seek(header.length);
            raf.write(buff);
        } catch (Exception e) {
            throw new GlobalException(1 , LOG_PRE+"追加核心文件内容异常" , e);
        }
    }

}



