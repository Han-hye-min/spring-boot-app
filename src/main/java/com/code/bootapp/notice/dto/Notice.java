package com.code.bootapp.notice.dto;

import com.code.bootapp.notice.entity.NoticeEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Notice {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private List<NoticeDTO> noticeList;
        private int nowPage;
        private long totalRows;

    }


    //게시글 저장 클래스
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NoticeDTO{
        private int noId;
        private String title;
        private String contents;
        private String writer;
        private int readCnt;
        private String createDate;
        private String updateDate;
        private NoticeFileDTO file;

        public static NoticeDTO of(NoticeEntity entity) {
            NoticeFileDTO file = new NoticeFileDTO();
            if(entity.getNoticeFile() != null) {
                file = NoticeFileDTO
                        .builder()
                        .fileId(entity.getNoticeFile().getFileId())
                        .noId(entity.getNoId())
                        .fileName(entity.getNoticeFile().getFileName())
                        .fileStoredName(entity.getNoticeFile().getFileStoredName())
                        .filePath(entity.getNoticeFile().getFilePath())
                        .build();
            }

            String createTime = entity.getCreateDate()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String updateTime = entity.getUpdateDate() != null
                    ? entity.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    : "";

            return NoticeDTO
                    .builder()
                    .noId(entity.getNoId())
                    .title(entity.getTitle())
                    .writer(entity.getWriter())
                    .contents(entity.getContents())
                    .readCnt(entity.getReadCnt())
                    .file(file)
                    .createDate(createTime)
                    .updateDate(updateTime)
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NoticeFileDTO{
        private int fileId;
        private int noId;
        private String fileName;
        private String fileStoredName;
        private String filePath;
    }

}
