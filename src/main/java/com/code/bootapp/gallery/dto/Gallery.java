package com.code.bootapp.gallery.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class Gallery {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{

        private int seq;
        private String title;
        private String writer;
        private String fileName;
        private String fileStoredName;
        private String filePath;
        private String thumbFileName;
        private String thumbFilePath;
        private String createDate;
        private String updateDate;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static  class GalleryData{
        List<Response> galleryList;
        int nowPage;
        int totalSize;
        String pageHTML;
    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create{
        private int seq;
        private String title;
        private String writer;
        private MultipartFile file;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        private int seq;
        private String title;
        private String writer;
        private String fileName;
        private String fileStoredName;
        private String filePath;
        private String thumbFileName;
        private String thumbFilePath;
    }
}
