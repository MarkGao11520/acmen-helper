package com.acmen.acmenhelper.generate.spiltmodule;

import com.acmen.acmenhelper.AcmenHelperApplicationTests;
import com.acmen.acmenhelper.model.CodeDefinition;
import com.acmen.acmenhelper.model.CodeDefinitionDetail;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SpiltModuleProjectGeneratorTest extends AcmenHelperApplicationTests {

    @Autowired
    SpiltModuleProjectGenerator spiltModuleProjectGenerator;

    @Test
    public void generateProjectStructure() {
        CodeDefinition codeDefinition = new CodeDefinition();
        codeDefinition.setArtifactId("demo");
        codeDefinition.setGroupId("com.example");

        codeDefinition.setProjectName("test-demo");
        codeDefinition.setDescription("测试");
        codeDefinition.setVersion("vt2.1");

        CodeDefinitionDetail codeDefinitionDetail = new CodeDefinitionDetail(codeDefinition);
        spiltModuleProjectGenerator.generateProjectStructure(codeDefinitionDetail);
    }
}