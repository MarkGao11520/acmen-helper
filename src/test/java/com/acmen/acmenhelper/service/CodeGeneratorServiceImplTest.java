package com.acmen.acmenhelper.service;

import com.acmen.acmenhelper.AcmenHelperApplicationTests;
import com.acmen.acmenhelper.model.CodeDefinition;
import com.acmen.acmenhelper.util.ApplicationContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.Assert.*;

public class CodeGeneratorServiceImplTest extends AcmenHelperApplicationTests {

    @Autowired
    private ICodeGeneratorService iCodeGeneratorService;

    @Test
    public void genCode() {
        CodeDefinition codeDefinition = new CodeDefinition();
        codeDefinition.setArtifactId("demo");
        codeDefinition.setGroupId("com.example");

        codeDefinition.setProjectName("test-demo");
        codeDefinition.setDescription("测试");
        codeDefinition.setVersion("vt2.1");
        codeDefinition.setTableList(Arrays.asList(new String[]{"blog"}));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println(objectMapper.writeValueAsString(codeDefinition));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

//        iCodeGeneratorService.genCode(codeDefinition);
    }
}