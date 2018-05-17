package com.acmen.acmenhelper.web;

import com.acmen.acmenhelper.common.ApiResponse;
import com.acmen.acmenhelper.common.ServiceMultiResult;
import com.acmen.acmenhelper.common.ServiceResult;
import com.acmen.acmenhelper.model.CodeDefinition;
import com.acmen.acmenhelper.model.MysqlDBDefinition;
import com.acmen.acmenhelper.service.ICodeGeneratorService;
import com.sun.deploy.net.HttpResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 自动
 * @author gaowenfeng
 * @date 2018/5/16
 */
@RestController
public class CodeGeneratorController {

    @Autowired
    private ICodeGeneratorService codeGenerator;

    @PostMapping("/db/connect")
    public ApiResponse getTableList(MysqlDBDefinition mysqlDBDefinition){
        ServiceMultiResult<String> res = codeGenerator.getTableList(mysqlDBDefinition);
        return ApiResponse.ofSuccess(res.getResult());
    }

    @PostMapping("/code/mysql/generator")
    public void codeGenerator(CodeDefinition codeDefinition,
                              HttpServletResponse response) {
        ServiceResult<String> res = codeGenerator.genCode(codeDefinition);
        if(!res.isSuccess()){
            //TODO 改成自定义异常
            throw new RuntimeException("未知异常");
        }
        String path = res.getResult();
        // 设置response
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;filename=cyCode.zip");

        doOutPut(path,response);
    }

    /**
     * 将zip用response输出
     * @param path
     * @param response
     */
    private void doOutPut(String path,HttpServletResponse response){
        File file = new File(path);

        try(InputStream input = FileUtils.openInputStream(file);
            OutputStream output = response.getOutputStream()){
            //缓冲区
            byte[] buffer = new byte[1024];
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while(input.read(buffer) != -1) {
                IOUtils.write(buffer,output);
            }
        }catch(Exception ex){
            //TODO 改成自定义异常
            throw new RuntimeException("下载失败!");
        }finally{
            file.deleteOnExit();
        }
    }
}
