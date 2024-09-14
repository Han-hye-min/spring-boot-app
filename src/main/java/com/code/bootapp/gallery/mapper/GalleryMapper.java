package com.code.bootapp.gallery.mapper;


import com.code.bootapp.gallery.dto.Gallery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GalleryMapper {
    int insertGallery(Gallery.Request request) throws  Exception;
    int getGalleryTotal(Map<String, Object> param) throws  Exception;
    List<Gallery.Response> getGalleryList(Map<String, Object> param) throws  Exception;

    Gallery.Response getGallery(@Param("seq") int seq) throws  Exception;
    int updateGallery(Gallery.Request request) throws  Exception;
    int deleteGallery(Map<String, List<Integer>> param) throws Exception;

}
