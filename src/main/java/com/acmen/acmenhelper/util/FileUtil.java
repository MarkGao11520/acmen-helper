package com.acmen.acmenhelper.util;

import com.acmen.acmenhelper.exception.GlobalException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author gaowenfeng
 * @date 2018/5/19
 */
public class FileUtil {

    private static final String LOG_PRE = "文件工具类>";

    /**
     * 移动文件夹
     * @param sourcePath
     * @param targetPath
     */
    public static void moveDir(String sourcePath,String targetPath){
        copyDir(sourcePath,targetPath);
        new File(sourcePath).delete();
    }


    /**
     * 删除文件夹
     * @param sourcePath
     * @param targetPath
     */
    public static void copyDir(String sourcePath,String targetPath){
        try {
            File sourceDir = new File(sourcePath);
            File targetDir = new File(targetPath);
            targetDir.mkdirs();
            FileUtils.copyDirectory(sourceDir,targetDir);
        } catch (IOException e) {
            throw new GlobalException(1 , LOG_PRE+"拷贝文件异常,sourcePath=["+sourcePath+"],targetPath=["+targetPath+"]",e);
        }
    }

    /**
     * 修改核心文件包名
     * @param targetPath
     * @param basePackage
     */
    public static void modifyCorePackage(String targetPath,String basePackage){
        //6.修改核心文件的包名
        File configurerDir = new File(targetPath+"/core");
        for (File configurerFile : configurerDir.listFiles()) {
            appendFileContent(configurerFile,"package "+basePackage+".core;\n");
        }
    }

    /**
     * 追加文件内容
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
