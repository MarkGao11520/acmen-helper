package com.acmen.acmenhelper.web;

import com.acmen.acmenhelper.common.ApiResponse;
import com.acmen.acmenhelper.common.ServiceMultiResult;
import com.acmen.acmenhelper.common.ServiceResult;
import com.acmen.acmenhelper.exception.GlobalException;
import com.acmen.acmenhelper.model.CodeDefinition;
import com.acmen.acmenhelper.model.MysqlDBDefinition;
import com.acmen.acmenhelper.service.ICodeGeneratorService;
import com.acmen.acmenhelper.util.JsonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 自动
 * @author gaowenfeng
 * @date 2018/5/16
 */
@RestController
@Slf4j
public class CodeGeneratorController {

    @Autowired
    private ICodeGeneratorService codeGenerator;

    @PostMapping("/db/connect")
    public ApiResponse getTableList(MysqlDBDefinition mysqlDBDefinition) throws GlobalException {
        ServiceMultiResult<String> res = codeGenerator.getTableList(mysqlDBDefinition);
        return ApiResponse.ofSuccess(res.getResult());
    }

    @PostMapping("/code/mysql/generator")
    public void codeGenerator(String dataJSON,
                              HttpServletResponse response) throws GlobalException {
        System.out.println(dataJSON);

        CodeDefinition codeDefinition = JsonUtils.fromJSON(dataJSON,CodeDefinition.class);
        ServiceResult<String> res = codeGenerator.genCode(codeDefinition);
        if(!res.isSuccess()){
            throw new GlobalException(1 , "未知异常" , null);
        }
        String path = res.getResult();
        // 设置response
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;filename="+codeDefinition.getProjectName()+".zip");

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
            try {
                // TODO: 2018/5/18 给用户一个更友好的提示 
                response.getWriter().print("下载失败");
            } catch (IOException e) {
                log.error("下载文件失败",e);
            }
        }finally{
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                log.error("删除下载文件失败",e);
            }
        }
    }

    @ExceptionHandler(value = GlobalException.class)
    public String resolveException(HttpServletRequest request , HttpServletResponse response , Exception e) {
        GlobalException ex = (GlobalException)e;
        log.error("Exception:{}" , ex.getE());
        ApiResponse apiResponse = new ApiResponse(ex.getCode() , ex.getMessage());
        return JSON.toJSONString(apiResponse);
    }
}
