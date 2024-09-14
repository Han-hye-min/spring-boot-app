package com.code.bootapp.notice.entity;

import com.code.bootapp.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notice_file")
public class NoticeFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fileId;
    private String fileName;
    private String filePath;
    private String fileStoredName;

    @OneToOne
    @JoinColumn(name = "no_id")
    private NoticeEntity notice;
}
