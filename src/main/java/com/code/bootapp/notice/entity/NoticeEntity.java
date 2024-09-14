package com.code.bootapp.notice.entity;

import com.code.bootapp.common.entity.BaseEntity;
import lombok.*;
import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notice")
public class NoticeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int noId;

    private String title;

    @Column(columnDefinition = "text")
    private String contents;

    private String writer;

    private int readCnt;

    @OneToOne(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private NoticeFileEntity noticeFile;
}
