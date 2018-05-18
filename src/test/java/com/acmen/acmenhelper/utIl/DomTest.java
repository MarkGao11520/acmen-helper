package com.acmen.acmenhelper.utIl;

import java.io.*;
import java.util.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.dom4j.*;
import org.dom4j.io.*;

/**
 * @author gaowenfeng
 * @date 2018/5/17
 */
public class DomTest {
    private static final List<Map<String,String>> dependencies = Lists.newArrayList();

    static {

        Map<String,String> map = Maps.newHashMap();
        map.put("groupId","tk.mybatis");
        map.put("artifactId","mapper");
        map.put("version","3.4.2");
        dependencies.add(map);

        Map<String,String> map1 = Maps.newHashMap();
        map1.put("groupId","com.github.pagehelper");
        map1.put("artifactId","pagehelper");
        map1.put("version","4.2.1");
        dependencies.add(map1);
    }

    public static void main(String[] args) {
        XMLWriter writer= null;
        try {
            //1.打开文件，并构造Element树
            File file = new File("pom.xml");
            Document doc = new SAXReader().read(file);
            Element dependenciesRoot = doc.getRootElement().element("dependencies");

            //2.添加新的节点
            for (Map<String, String> data : dependencies) {
                Element dependency = dependenciesRoot.addElement("dependency");
                Element groupId = dependency.addElement("groupId");
                Element artifactId = dependency.addElement("artifactId");
                Element version = dependency.addElement("version");

                groupId.setText(data.get("groupId"));
                artifactId.setText(data.get("artifactId"));
                version.setText(data.get("version"));
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


//    public static void main(String arge[]) {
//    　　long lasting = System.currentTimeMillis();
//    　　try {
//    　　　File f = new File("data_10k.xml");
//    　　　SAXReader reader = new SAXReader();
//    　　　Document doc = reader.read(f);
//    　　　Element root = doc.getRootElement();
//    　　　Element foo;
//    　　　for (Iterator i = root.elementIterator("VALUE"); i.hasNext();) {
//    　　　　foo = (Element) i.next();
//    　　　　System.out.print("车牌号码:" + foo.elementText("NO"));
//    　　　　System.out.println("车主地址:" + foo.elementText("ADDR"));
//    　　　}
//    　　}catch (Exception e) {
//    　　　e.printStackTrace();
//       }
//    }
}
