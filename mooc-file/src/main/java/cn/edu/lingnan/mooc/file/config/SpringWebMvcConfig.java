package cn.edu.lingnan.mooc.file.config;

import cn.edu.lingnan.mooc.file.constant.FileConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xmz
 * @date: 2020/12/20
 */
@Configuration
public class SpringWebMvcConfig implements WebMvcConfigurer {

    @Value("${mooc.file.path}")
    private String BASE_FILE_PATH;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(FileConstant.MAPPING_PATH + "**").addResourceLocations("file:" + BASE_FILE_PATH);
    }
}
