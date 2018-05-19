package com.acmen.acmenhelper.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author gaowenfeng
 * @date 2018/5/19
 */
@Slf4j
public class PomUtil {


    private static final String LOG_PRE = "修改POM文件";


    /**
     * 删除元素节点
     * @param pomPath pom文件
     * @param parentNode 父节点，如果父节点为根，则穿null
     * @param removeNode 要删除的节点名
     * @throws Exception
     */
    public static void removeElement(String pomPath,String parentNode,String removeNode) throws Exception{
        handlePom(root -> {
            Element parentElement = root;
            if(parentNode != null){
                parentElement = root.element(parentNode);
            }
            parentElement.remove(parentElement.element(removeNode));
        },pomPath);
    }

    /**
     * 追加POM元素的Dependencies
     * @param pomPath pom文件
     * @param dependencies
     */
    public static void appendPomDependencies(String pomPath,List<String> dependencies) throws Exception{
        handlePom(root -> {
            Element dependenciesRoot = root.element("dependencies");
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
        },pomPath);
    }


    public static void handlePom(Option option,String pomSrc) throws Exception{

        XMLWriter writer = null;

        try {
            File pomFile = new File(pomSrc);
            Document doc = new SAXReader().read(pomFile);
            Element root = doc.getRootElement();

            option.option(root);
            OutputFormat opf=new OutputFormat("\t",true,"UTF-8");
            opf.setTrimText(true);
            writer=new XMLWriter(new FileOutputStream(pomSrc),opf);
            writer.write(doc);
        } finally {
            writer.close();
        }
    }

    public interface Option{
        void option(Element root);
    }
}
