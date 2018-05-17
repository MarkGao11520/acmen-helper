package com.acmen.acmenhelper.generate;

import com.acmen.acmenhelper.model.CodeDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.acmen.acmenhelper.model.CodeDefinitionDetail.PROJECT_PATH;
import static com.acmen.acmenhelper.model.CodeDefinitionDetail.RESOURCES_PATH;

/**
 * 默认项目结构生成器,使用java.io生成
 * @author gaowenfeng
 * @date 2018/5/16
 */
@Service("defaultProjectGenerator")
@Slf4j
public class DefaultProjectGenerator implements IProjectGenerator {
    private final static String LOG_PRE = "生成项目基本骨架>";

    @Value("${acmen.project.generate_path}")
    private String generatePath;

    @Override
    public String generateProjectStructure(CodeDefinition codeDefinition) {

        //1.生成springboot项目骨架
        String buildInProjectName = doGenerateProjectStructure(codeDefinition);

        //2.将核心文件放入项目骨架


        return buildInProjectName;
    }


    /**
     * 执行具体的创建项目骨架的逻辑
     * @param codeDefinition
     */
    private String doGenerateProjectStructure(CodeDefinition codeDefinition){
        String buildInProjectName = codeDefinition.getProjectName()+"_"+System.currentTimeMillis();

        Process pro = null;
        try{
            //1.1构建shell命令
            String[] cmds = buildCmd(codeDefinition,buildInProjectName);

            //1.2执行命令并阻塞至执行完成
            pro = Runtime.getRuntime().exec(cmds);
            pro.waitFor();

            //1.3输出结果
            List<String> lines = IOUtils.readLines(pro.getInputStream());
            lines.forEach(line->log.info(LOG_PRE+"参数=【"+codeDefinition+"】,输出=【"+line+"】"));
        } catch (Exception e) {
            log.error(LOG_PRE+"错误:"+e.getMessage());
            //TODO 自定义异常
            throw new RuntimeException(LOG_PRE+"错误");
        }finally {
            pro.destroy();
        }
        return buildInProjectName;
    }
    /**
     * 构建创建项目的命令
     * @param codeDefinition
     * @return
     */
    private String[] buildCmd(CodeDefinition codeDefinition,String buildInProjectName){
        String shellPath = PROJECT_PATH+RESOURCES_PATH+"/build_springboot.sh";

        String[] cmds = new String[]{shellPath,
                codeDefinition.getGroupId(),
                codeDefinition.getArtifactId(),
                generatePath+buildInProjectName,
                codeDefinition.getDescription(),
                codeDefinition.getVersion(),
                };
        return cmds;
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        CodeDefinition codeDefinition = new CodeDefinition();
        codeDefinition.setArtifactId("demo");
        codeDefinition.setGroupId("com.example");

        codeDefinition.setProjectName("test-demo");
        codeDefinition.setDescription("测试");
        codeDefinition.setVersion("vt2.1");

        new DefaultProjectGenerator().generateProjectStructure(codeDefinition);
    }



}
