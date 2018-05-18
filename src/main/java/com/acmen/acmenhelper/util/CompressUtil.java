package com.acmen.acmenhelper.util;

import com.acmen.acmenhelper.exception.GlobalException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.File;

/**
 * @author gaowenfeng
 * @date 2018/5/16
 */
public class CompressUtil {
    private CompressUtil() {
    }


    /**
     * 执行压缩操作
     * @param srcPathName 需要被压缩的文件/文件夹
     * @param dest 生成的zip包的位置
     */
    public static void doZipCompress(String srcPathName, String dest) {
        File srcdir = new File(srcPathName);
        if (!srcdir.exists()){
            throw new GlobalException(1 , srcPathName + "不存在" , null);
        }
        File zipFile = new File(dest);
        Project prj = new Project();
        Zip zip = new Zip();
        zip.setProject(prj);
        zip.setDestFile(zipFile);
        FileSet fileSet = new FileSet();
        fileSet.setProject(prj);
        fileSet.setDir(srcdir);
        //排除哪些文件或文件夹
        fileSet.setExcludes(".DS_Store");
        zip.addFileset(fileSet);
        zip.execute();
    }


}
