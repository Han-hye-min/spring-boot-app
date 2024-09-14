package com.code.bootapp.gallery.service;

import com.code.bootapp.gallery.dto.Gallery;
import com.code.bootapp.gallery.mapper.GalleryMapper;
import com.code.bootapp.common.utils.ImageResizeUtil;
import com.code.bootapp.common.utils.PagingVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class GalleryService {

    @Value("${server.stored.file.path}")
    private String filePath;

    private final GalleryMapper mapper;

    public Gallery.GalleryData getGalleryList(Map<String, Object> param) throws Exception{
        int nowPage = Integer.parseInt(param.get("nowPage").toString());
        int totalSize = mapper.getGalleryTotal(param);

        List<Gallery.Response> galleryList = new ArrayList<>();
        //페이징 객체 생성
        PagingVO page = new PagingVO();
        page.setPageData(nowPage, totalSize);

        //출력할 데이터가 있을 경우에만 SQL 실행
        if(totalSize != 0) {
            param.put("start", page.getStart());
            param.put("end", page.getEnd());
            galleryList = mapper.getGalleryList(param);
        }

        return Gallery.GalleryData
                .builder()
                .galleryList(galleryList)
                .nowPage(nowPage)
                .totalSize(totalSize)
                .pageHTML(page.pageHTML())
                .build();

    }

    public Map<String, Object> addGallery(Gallery.Create createDTO) throws  Exception{
        Map<String, Object> resultMap = new HashMap<>();
        //저장할 요청 객체만들기
        Gallery.Request request = this.makeGalleryFiles("add", createDTO);
        //디비에 저장
        int result = mapper.insertGallery(request);
        //결과 확인
        if(result < 0){
            throw new Exception(" 파일 저장 오류");
        }
        //아무 문제 없이 성공하면 200
        resultMap.put("resultCode", 200);

        return resultMap;
    }

    public Map<String, Object> updateGallery(Gallery.Create createDTO) throws  Exception{
        Map<String, Object> resultMap = new HashMap<>();
        //기존 정보 가져오기
        Gallery.Response response = mapper.getGallery(createDTO.getSeq());
        //기존 정보를 가지고 만듬
        Gallery.Request  request = Gallery.Request
                .builder()
                .title(createDTO.getTitle())
                .seq(response.getSeq())
                .filePath(response.getFilePath())
                .fileName(response.getFileName())
                .fileStoredName(response.getFileStoredName())
                .thumbFileName(response.getThumbFileName())
                .thumbFilePath(response.getThumbFilePath())
                .build();

        //새로 추가할 파일이 있다면 기존 파일 삭제
        if(createDTO.getFile() != null && !createDTO.getFile().isEmpty()){
            //업데이트를 위해서 기존 정보를 저장
            createDTO.setSeq(response.getSeq());
            //업데이트를 위한 신규 파일 생성 및 결과 정보 받기
            request = this.makeGalleryFiles("update", createDTO);
            //기존 파일 삭제
            this.deleteImage(response);
        }

        int result = mapper.updateGallery(request);
        //성공여부 판단
        if(result < 0){
            throw new Exception(" 갤러리 업데이트 실패 ");
        }
        //아무 문제 없이 성공하면 200
        resultMap.put("resultCode", 200);

        return resultMap;
    }

    public Map<String, Object> deleteGallery(List<Integer> targets) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        if(targets!= null && !targets.isEmpty()) {
            //파일을 먼저 삭제
            for(int target : targets) {
                Gallery.Response response = mapper.getGallery(target);
                //기존 파일 삭제
                this.deleteImage(response);
            }

            //파일 삭제 후 실제 디비 삭제
            Map<String, List<Integer>> param = new HashMap<>();
            param.put("deleteTargets",  targets);
            int result = mapper.deleteGallery(param);

            if(result < 0){
                throw new Exception(" 갤러리 삭제 실패 ");
            }
        }

        resultMap.put("resultCode", 200);
        return resultMap;
    }


    public Gallery.Request makeGalleryFiles (String type, Gallery.Create createDTO) throws  Exception{
        //파일을 업로드
        Map<String, Object> uploadInfo =
                this.uploadFiles(createDTO.getFile(), "gallery");

        if(uploadInfo == null) {
            throw new Exception("파일 생성 실패");
        }

        File newFile = (File)uploadInfo.get("newFile");
        String newFilePath = uploadInfo.get("filePath").toString();
        String thumbPath = newFilePath + "thumb" + File.separator;

        String thumbFileName
                = ImageResizeUtil.makeThumbnailFile(200, 150, newFile, thumbPath);
        //디비에 저장할 객체를 만듭니다.
        Gallery.Request  request =
                Gallery.Request
                        .builder()
                        .seq(type.equals("update") ? createDTO.getSeq() : 0)
                        .title(createDTO.getTitle())
                        .writer(type.equals("add") ? "관리자" : createDTO.getWriter())
                        .filePath(newFilePath)
                        .fileName(createDTO.getFile().getOriginalFilename())
                        .fileStoredName(newFile.getName())
                        .thumbFileName(thumbFileName)
                        .thumbFilePath(thumbPath)
                        .build();
        return request;
    }

    //파일 업로드 기능
    public  Map<String, Object> uploadFiles(MultipartFile file, String fileType){

        Map<String, Object> result = null;
        try{
            //파일 객체 없다면  에러 처리
            if(file == null) {
                throw new FileNotFoundException("파일이 없음");
            }


            String originFileName = file.getOriginalFilename(); // 원본 파일 이름
            String ext = originFileName.substring(originFileName.lastIndexOf(".") +1);
            //저장되는 파일이름이 겹치지 않도록 중복이 어려운 UUID 이용하여 만든다
            //uuid 는 하이픈(-) 이 들어가기 때문에 제거한다.
            String uuId = UUID.randomUUID().toString().replaceAll("-", "");
            uuId = uuId.substring(0, 16); //길이가 길어서 16자로 자른다.
            String newFileName = uuId + "." + ext;

            String fullPath = filePath + File.separator + fileType + File.separator + newFileName;
            //새로 생성할 파일 객체를 만든다
            File newFile = new File(fullPath);

            //저장할 경로가 있는지 확인해서 없으면 만들자
            if(!newFile.getParentFile().exists()){
                newFile.getParentFile().mkdirs();
            }
            //빈파일 만들기
            newFile.createNewFile();
            //빈파일에 내용 옮기기
            file.transferTo(newFile);

            result = new HashMap<>();
            result.put("newFile", newFile);
            result.put("filePath",  filePath + File.separator + fileType + File.separator);

        }catch (Exception e) {
            //에러 로그 남기기
            log.error("======File Upload Error ===== : {}", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private void deleteImage( Gallery.Response response) throws Exception{

        String path = response.getFilePath() + response.getFileStoredName();
        String thumbPath = response.getThumbFilePath() + response.getThumbFileName();

        //원본파일을 경로를 통해 파일 객체로 만든다.
        File file = new File(path);
        //썸네일을 경로를 통해 파일 객체로 만든다
        File thumbFile = new File(thumbPath);
        if(file.exists()){
            file.delete();
        }

        if(thumbFile.exists()){
            thumbFile.delete();
        }
    }

}
