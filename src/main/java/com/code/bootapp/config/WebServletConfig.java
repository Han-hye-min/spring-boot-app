package com.code.bootapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.File;

@Configuration
public class WebServletConfig implements WebMvcConfigurer {

        @Value("${server.stored.file.path}")
        private String filePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String galleryPath = filePath + File.separator + "gallery" + File.separator;
        //물리적 경로를 springboot에서 읽을 수 있는 경로로 등록해준다
        registry.addResourceHandler("/gall/images/**")
                .addResourceLocations("file:///" + galleryPath)
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
