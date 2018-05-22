package com.acmen.acmenhelper.util;

import com.acmen.acmenhelper.exception.GlobalException;
import com.acmen.acmenhelper.model.CodeDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.util.List;

import static com.acmen.acmenhelper.model.CodeDefinitionDetail.PROJECT_PATH;
import static com.acmen.acmenhelper.model.CodeDefinitionDetail.RESOURCES_PATH;

/**
 * @author gaowenfeng
 * @date 2018/5/19
 */
@Slf4j
public class ExecCommandUtil {

    private static final String LOG_PRE = "生成项目结构";

    public static void doGenerateProjectStructure(CodeDefinition codeDefinition, String[] cmds){

        Process pro = null;
        try{
            //1.2执行命令并阻塞至执行完成
            Runtime.getRuntime().exec("chmod +x"+cmds[0]);
            pro = Runtime.getRuntime().exec(cmds);
            pro.waitFor();

            //1.3输出结果
            List<String> lines = IOUtils.readLines(pro.getInputStream());
            lines.forEach(line->log.info(LOG_PRE+"参数=【"+codeDefinition+"】,输出=【"+line+"】"));
        } catch (Exception e) {
            log.error(LOG_PRE+"错误:"+e.getMessage());
            throw new GlobalException(1 , LOG_PRE + "错误" , e);
        }finally {
            pro.destroy();
        }
    }

    /**
     * 构建创建项目的命令
     * @param codeDefinition
     * @return
     */
    public static String[] buildBootCmd(CodeDefinition codeDefinition,String generatePath,String buildInProjectName){
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

    public static String[] buildMavenCmd(CodeDefinition codeDefinition,String generatePath,String buildInProjectName,String module){
        String shellPath = PROJECT_PATH+RESOURCES_PATH+"/build_maven.sh";

        String[] cmds = new String[]{shellPath,
                generatePath+"/"+buildInProjectName,
                codeDefinition.getGroupId(),
                codeDefinition.getArtifactId()+"-"+module,
                codeDefinition.getVersion(),
        };
        return cmds;
    }

}
