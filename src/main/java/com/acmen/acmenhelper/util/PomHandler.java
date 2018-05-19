package com.acmen.acmenhelper.util;

import com.acmen.acmenhelper.exception.GlobalException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;

/**
 * @author gaowenfeng
 * @date 2018/5/19
 */
@Data
@Slf4j
public class PomHandler {

    private static final String LOG_PRE = "修改POM文件>";
    private String pomPath;

    private XMLWriter writer;

    private Document doc;

    private Element root;

    private OutputFormat opf;

    public PomHandler(String pomPath) throws FileNotFoundException, UnsupportedEncodingException, DocumentException {
        this.pomPath = pomPath;
        File file = new File(pomPath);
        this.doc = new SAXReader().read(file);
        this.root = doc.getRootElement();
    }

    public void removeElement(String name){
        root.remove(root.element(name));
    }

    public void removeElement(String ...name){
        Element prev = root;
        Element cur = prev.element(name[0]);
        for (int i = 1; i < name.length; i++) {
            prev = cur;
            cur = prev.element(name[i]);
        }
        prev.remove(cur);
    }



    /**
     * 追加POM元素的依赖encies
     * @param dependencies
     */
    public void appendPomDependencies(List<String> dependencies){
        try {
            //1.打开文件，并构造Element树
            Element dependenciesRoot = root.element("dependencies");

            //2.添加新的节点
            for (String line : dependencies) {
                String[] data = StringUtils.split(line,",");
                Element dependency = dependenciesRoot.addElement("dependency");
                Element groupId = dependency.addElement("groupId");
                Element artifactId = dependency.addElement("artifactId");
                Element version = dependency.addElement("version");

                groupId.setText(data[0]);
                artifactId.setText(data[1]);
                if(data.length==3) {
                    version.setText(data[2]);
                }
            }

            //3.写入新的文件
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

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, DocumentException {
        PomHandler pomHandler = new PomHandler("pom1.xml");
        pomHandler.removeElement("dependencies","dependency");
        pomHandler.removeElement("groupId");
        pomHandler.removeElement("version");
    }
}
