package com.acmen.acmenhelper.utIl;

import com.acmen.acmenhelper.AcmenHelperApplicationTests;
import com.acmen.acmenhelper.config.ProjectConfig;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author gaowenfeng
 * @date 2018/5/17
 */
public class DomTest1 extends AcmenHelperApplicationTests {

    @Autowired
    private ProjectConfig projectConfig;

    @Test
    public void test(){
        appendPomDependencies(projectConfig.getDependencies());
    }

    public void appendPomDependencies(List<String> dependencies){
        XMLWriter writer= null;
        try {
            //1.打开文件，并构造Element树
            File file = new File("pom.xml");
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
            writer=new XMLWriter(new FileOutputStream("out.xml"),opf);
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
}
