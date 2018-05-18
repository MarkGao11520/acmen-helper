package com.acmen.acmenhelper.generate;

import com.acmen.acmenhelper.config.DependenciesConfig;
import com.acmen.acmenhelper.model.CodeDefinition;
import com.acmen.acmenhelper.model.CodeDefinitionDetail;
import com.acmen.acmenhelper.util.NameConvertUtil;
import com.google.common.collect.Lists;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import static com.acmen.acmenhelper.model.CodeDefinitionDetail.*;

/**
 * 默认项目结构生成器,使用java.io生成
 * @author gaowenfeng
 * @date 2018/5/16
 */
@Service("defaultProjectGenerator")
@Slf4j
public class DefaultProjectGenerator implements IProjectGenerator {
    private final static String LOG_PRE = "生成项目基本骨架>";

    @Value("${acmen.project.generate_path}")
    private String generatePath;

    @Autowired
    private DependenciesConfig dependenciesConfig;


    private static final List<String> dependencies = Lists.newArrayList();

    static {
        dependencies.add("tk.mybatis,mapper,3.4.2");
    }

    @Override
    public String generateProjectStructure(CodeDefinitionDetail codeDefinitionDetail) {

        //1.生成springboot项目骨架
        CodeDefinition codeDefinition = codeDefinitionDetail.getCodeDefinition();
        String buildInProjectName = doGenerateProjectStructure(codeDefinition);

        //2.将核心文件放入项目骨架
        String sourcePath = null;
        String targetPath = null;
        try {
            sourcePath = PROJECT_PATH+RESOURCES_PATH+"/generator/coreCode";
            targetPath = generatePath+buildInProjectName+JAVA_PATH+NameConvertUtil.packageConvertPath(codeDefinitionDetail.getBasePackage());
            FileUtils.copyDirectory(new File(sourcePath),new File(targetPath));
        } catch (IOException e) {
            throw new RuntimeException(LOG_PRE+"拷贝文件异常,sourcePath=["+sourcePath+"],targetPath=["+targetPath+"]",e);
        }

        //3.修改核心文件的包名
        File configurerDir = new File(targetPath+"/configurer");
        for (File configurerFile : configurerDir.listFiles()) {
            modifyFileContent(configurerFile,"basePackage",codeDefinitionDetail.getBasePackage());
        }

        //4.修改pom文件追加依赖
        String pomPath = generatePath+buildInProjectName+"/pom.xml";
        doAppendPomDependencies(dependencies,pomPath);

        //5.修改application.properties

        return buildInProjectName;
    }


    /**
     * 执行具体的创建项目骨架的逻辑
     * @param codeDefinition
     */
    private String doGenerateProjectStructure(CodeDefinition codeDefinition){
        String buildInProjectName = codeDefinition.getProjectName()+"_"+System.currentTimeMillis();

        Process pro = null;
        try{
            //1.1构建shell命令
            String[] cmds = buildCmd(codeDefinition,buildInProjectName);

            //1.2执行命令并阻塞至执行完成
            pro = Runtime.getRuntime().exec(cmds);
            pro.waitFor();

            //1.3输出结果
            List<String> lines = IOUtils.readLines(pro.getInputStream());
            lines.forEach(line->log.info(LOG_PRE+"参数=【"+codeDefinition+"】,输出=【"+line+"】"));
        } catch (Exception e) {
            log.error(LOG_PRE+"错误:"+e.getMessage());
            //TODO 自定义异常
            throw new RuntimeException(LOG_PRE+"错误");
        }finally {
            pro.destroy();
        }
        return buildInProjectName;
    }

    /**
     * 追加POM元素的Dependencies
     * @param dependencies
     */
    public void doAppendPomDependencies(List<String> dependencies,String sourcePom){
        XMLWriter writer= null;
        try {
            //1.打开文件，并构造Element树
            File file = new File(sourcePom);
            Document doc = new SAXReader().read(file);
            Element dependenciesRoot = doc.getRootElement().element("dependencies");

            //2.添加新的节点
            for (String line : dependencies) {
                String[] data = StringUtils.split(line,",");
                Element dependency = dependenciesRoot.addElement("dependency");
                Element groupId = dependency.addElement("groupId");
                Element artifactId = dependency.addElement("artifactId");
                Element version = dependency.addElement("version");

                groupId.setText(data[0]);
                artifactId.setText(data[1]);
                version.setText(data[2]);
            }

            //3.写入新的文件
            OutputFormat opf=new OutputFormat("\t",true,"UTF-8");
            opf.setTrimText(true);
            writer=new XMLWriter(new FileOutputStream(sourcePom),opf);
            writer.write(doc);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 修改文件内容
     * @param file
     * @param oldstr
     * @param newStr
     */
    private static void modifyFileContent(File file, String oldstr, String newStr) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            String line = null;
            //记住上一次的偏移量
            long lastPoint = 0;
            while ((line = raf.readLine()) != null) {
                long ponit = raf.getFilePointer();
                if(line.contains(oldstr)){
                    String str=line.replace(oldstr, newStr);
                    System.out.println("cur_pint:"+raf.getFilePointer()+",lastpoint:"+lastPoint);
                    raf.seek(lastPoint);
                    System.out.println("cur_pint:"+raf.getFilePointer()+",lastpoint:"+lastPoint);
                    raf.writeBytes(str);
                    System.out.println("cur_pint:"+raf.getFilePointer()+",lastpoint:"+lastPoint);
                    raf.seek(lastPoint);
                    System.out.println("cur_pint:"+raf.getFilePointer()+",lastpoint:"+lastPoint);
                    raf.readLine();
                    System.out.println("cur_pint:"+raf.getFilePointer()+",lastpoint:"+lastPoint);
                    ponit = raf.getFilePointer();
                }
                lastPoint = ponit;
            }
        } catch (Exception e) {
            throw new RuntimeException(LOG_PRE+"修改核心文件内容异常");
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
            }
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
                generatePath+buildInProjectName,
                codeDefinition.getDescription(),
                codeDefinition.getVersion(),
                };
        return cmds;
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
