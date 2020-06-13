package com.wang.resources;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 配置关联类
 */
@Component
@ConfigurationProperties(prefix = "file")//配置前缀名
@PropertySource("classpath:file-upload-dev.properties")
public class FileUpload {
    private String imageUserFaceLocation;
    private String imageServiceUrl;

    public String getImageServiceUrl() {
        return imageServiceUrl;
    }

    public void setImageServiceUrl(String imageServiceUrl) {
        this.imageServiceUrl = imageServiceUrl;
    }

    public String getImageUserFaceLocation() {
        return imageUserFaceLocation;
    }

    public void setImageUserFaceLocation(String imageUserFaceLocation) {
        this.imageUserFaceLocation = imageUserFaceLocation;
    }
}
