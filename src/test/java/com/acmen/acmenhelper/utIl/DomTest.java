package com.acmen.acmenhelper.utIl;

import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.*;

/**
 * @author gaowenfeng
 * @date 2018/5/17
 */
public class DomTest {

    public static void main(String[] args) {
        try {
            File file = new File("pom.xml");
            SAXReader reader = new SAXReader();
            Document doc = reader.read(file);
            Element root = doc.getRootElement().element("dependencies");
            Element dependency;
            for(Iterator i = root.elementIterator("dependency");i.hasNext();){
                dependency = (Element) i.next();
                System.out.println(dependency.elementText("groupId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
