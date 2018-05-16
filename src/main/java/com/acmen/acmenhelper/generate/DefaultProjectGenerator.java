package com.acmen.acmenhelper.generate;

import com.acmen.acmenhelper.model.CodeDefinition;
import org.springframework.stereotype.Service;

/**
 * 默认项目结构生成器,使用java.io生成
 * @author gaowenfeng
 * @date 2018/5/16
 */
@Service("defaultProjectGenerator")
public class DefaultProjectGenerator implements IProjectGenerator {
    @Override
    public String generateProjectStructure(CodeDefinition codeDefinition) {
        return null;
    }
}
