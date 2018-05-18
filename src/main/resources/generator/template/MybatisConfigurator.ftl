package ${basePackage}.core;

import org.springframework.context.annotation.Configuration;
import org.mybatis.spring.annotation.MapperScan;

/**
 * Created by ${author} on ${date}
 * @date 2018/5/18
 */
@Configuration
@MapperScan("${basePackage}.dao")
public class MybatisConfigurator{
}
